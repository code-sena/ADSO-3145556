/**
 * Métricas ambientales: mapeo del contrato del backend y derivación del estado.
 *
 * <p>Lógica pura — sin hooks, sin estado React.
 *
 * <p>El backend ({@code ms-environment-monitoring}) devuelve la última medición por
 * (ambiente, variable) con {@code variableCode} ({@code temperature}, {@code humidity},
 * {@code co2}, {@code noise}). La UI usa cortos ({@code temp}, {@code co2}, …) y un
 * {@code statusKey} que alimenta el ranking, los filtros y los contadores.
 */

import { STATUS } from '../constants/environments';

/** Códigos del catálogo del backend → campos que consume la UI. */
const CODE_TO_FIELD = {
  temperature: 'temp',
  humidity: 'humidity',
  co2: 'co2',
  noise: 'noise',
};

/**
 * Agrupa las mediciones por ambiente.
 *
 * @param {Array<{environmentId:string, variableCode:string, value:number}>} rows
 * @returns {Record<string, {temp?:number, humidity?:number, co2?:number, noise?:number}>}
 */
export function mapMetricsByEnvironment(rows) {
  const byEnvironment = {};
  for (const row of rows || []) {
    const field = CODE_TO_FIELD[row.variableCode];
    if (!field || row.environmentId == null) continue;
    const entry = byEnvironment[row.environmentId] || (byEnvironment[row.environmentId] = {});
    entry[field] = Number(row.value);
    if (!entry.measuredAt || row.measuredAt > entry.measuredAt) entry.measuredAt = row.measuredAt;
  }
  return byEnvironment;
}

const SEVERITY = { OK: 0, WARNING: 1, ALERT: 2 };

/** Severidad de una métrica concreta; usa los mismos umbrales que getMetricStatus. */
function severityOf(field, value) {
  if (value === undefined || value === null || Number.isNaN(value)) return SEVERITY.OK;
  switch (field) {
    case 'temp':
      return value >= 20 && value <= 26 ? SEVERITY.OK : SEVERITY.WARNING;
    case 'humidity':
      return value >= 40 && value <= 60 ? SEVERITY.OK : SEVERITY.WARNING;
    case 'co2':
      if (value <= 800) return SEVERITY.OK;
      return value <= 1000 ? SEVERITY.WARNING : SEVERITY.ALERT;
    case 'noise':
      if (value <= 50) return SEVERITY.OK;
      return value <= 65 ? SEVERITY.WARNING : SEVERITY.ALERT;
    default:
      return SEVERITY.OK;
  }
}

/**
 * Estado global de un ambiente: la peor severidad entre sus métricas.
 *
 * <p>Sin métricas el estado queda sin definir y la UI muestra «desconocido»: es
 * mejor que pintar un verde inventado (antes el score siempre devolvía 100).
 */
export function deriveStatusKey(metrics) {
  if (!metrics) return undefined;
  const worst = Object.values(CODE_TO_FIELD).reduce((acc, field) => {
    const severity = severityOf(field, metrics[field]);
    return severity > acc ? severity : acc;
  }, SEVERITY.OK);

  if (worst >= SEVERITY.ALERT) return STATUS.ALERT;
  if (worst >= SEVERITY.WARNING) return STATUS.WARNING;

  const hasAnyMetric = Object.values(CODE_TO_FIELD).some((f) => metrics[f] !== undefined);
  return hasAnyMetric ? STATUS.NORMAL : undefined;
}
