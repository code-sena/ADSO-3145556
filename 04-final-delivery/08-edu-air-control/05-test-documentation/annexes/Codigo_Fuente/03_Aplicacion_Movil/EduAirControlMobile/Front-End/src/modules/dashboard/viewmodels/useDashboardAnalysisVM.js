/**
 * ViewModel: useDashboardAnalysisVM
 * Lógica del análisis del dashboard (período, métrica, series históricas,
 * KPIs y salud de la red) — equivalente móvil del DashboardScreen del web.
 */

import { useMemo, useState, useCallback } from 'react'
import { useTranslation } from 'react-i18next'
import { useEnvironments } from '../../../context/EnvironmentContext'
import {
  CURVES,
  METRICS,
  PERIOD_IDS,
  MAX_SERIES,
  formatMetric,
  getHistoricalValue,
  getPeriodLabels,
  getPeriodValue,
  normalizeStatus,
} from '../utils/historicalSeries'

export function useDashboardAnalysisVM() {
  const { t } = useTranslation()
  const { environments = [], refreshEnvironments } = useEnvironments()

  const [period, setPeriod] = useState('day')
  const [metric, setMetric] = useState('co2')
  const [environmentId, setEnvironmentId] = useState('all')
  const [isFresh, setIsFresh] = useState(false)

  const periods = useMemo(
    () =>
      PERIOD_IDS.map((id) => ({
        id,
        label: t(`dashboardAnalysis.periods.${id}`),
        context: t(`dashboardAnalysis.periods.${id}Context`),
      })),
    [t]
  )

  const periodLabels = useMemo(() => getPeriodLabels(t), [t])

  const metricLabels = useMemo(
    () => ({
      co2: 'CO₂',
      temperature: t('dashboardAnalysis.labels.temperature'),
      humidity: t('dashboardAnalysis.labels.humidity'),
      noise: t('dashboardAnalysis.labels.noise'),
    }),
    [t]
  )

  const statusLabels = useMemo(
    () => ({
      normal: t('dashboardAnalysis.labels.normal'),
      warning: t('dashboardAnalysis.labels.warning'),
      alert: t('dashboardAnalysis.labels.alert'),
    }),
    [t]
  )

  const selectedEnvironments = useMemo(() => {
    if (environmentId === 'all') return environments
    return environments.filter((env) => String(env.id) === String(environmentId))
  }, [environmentId, environments])

  const visibleEnvironments = selectedEnvironments.length ? selectedEnvironments : environments

  const periodInfo = periods.find((item) => item.id === period) || periods[0]

  const selectedLabel =
    environmentId === 'all'
      ? t('dashboardAnalysis.context.allEnvironments')
      : visibleEnvironments[0]?.name || t('dashboardAnalysis.context.noEnvironment', 'Sin ambiente seleccionado')

  const labels = periodLabels[period] || []

  const series = useMemo(
    () =>
      visibleEnvironments.slice(0, MAX_SERIES).map((environment, environmentIndex) => ({
        id: environment.id,
        name: environment.name,
        points: labels.map((label, index) => ({
          label,
          value: getHistoricalValue(
            environment,
            metric,
            CURVES[period][index] ?? 1,
            index + environmentIndex
          ),
        })),
      })),
    [visibleEnvironments, labels, metric, period]
  )

  const comparisonData = useMemo(
    () =>
      visibleEnvironments.map((environment, index) => ({
        id: environment.id,
        name: environment.name.replace('Ambiente ', ''),
        value: getPeriodValue(environment, metric, period),
        color: index,
      })),
    [visibleEnvironments, metric, period]
  )

  const average = useMemo(() => {
    if (!visibleEnvironments.length) return 0
    return (
      visibleEnvironments.reduce(
        (total, environment) => total + getPeriodValue(environment, metric, period),
        0
      ) / visibleEnvironments.length
    )
  }, [visibleEnvironments, metric, period])

  const statusCounts = useMemo(
    () =>
      visibleEnvironments.reduce(
        (counts, environment) => {
          counts[normalizeStatus(environment.statusKey).key] += 1
          return counts
        },
        { normal: 0, warning: 0, alert: 0 }
      ),
    [visibleEnvironments]
  )

  const metricInfo = METRICS[metric]

  // Rango real del gráfico: domínio teórico ampliado si alguna lectura se sale
  const chartRange = useMemo(() => {
    const values = series.flatMap((item) => item.points.map((point) => point.value))
    const min = Math.min(metricInfo.domain[0], metricInfo.comfort, ...(values.length ? values : [0]))
    const max = Math.max(metricInfo.domain[1], metricInfo.comfort, ...(values.length ? values : [1]))
    return { min, max, comfort: metricInfo.comfort }
  }, [series, metricInfo])

  const refresh = useCallback(() => {
    setIsFresh(true)
    if (refreshEnvironments) refreshEnvironments()
    setTimeout(() => setIsFresh(false), 2600)
  }, [refreshEnvironments])

  const healthPercent = visibleEnvironments.length
    ? Math.round((statusCounts.normal / visibleEnvironments.length) * 100)
    : 0

  const lastUpdatedLabel = isFresh
    ? t('dashboardAnalysis.updatedNow', 'actualizado ahora')
    : t('dashboardAnalysis.updatedAgo', 'hace 3 min')

  return {
    environments,
    period,
    setPeriod,
    metric,
    setMetric,
    environmentId,
    setEnvironmentId,
    periods,
    periodInfo,
    periodLabels,
    metricLabels,
    statusLabels,
    metricInfo,
    metricLabel: metricLabels[metric],
    selectedLabel,
    selectedEnvironments,
    visibleEnvironments,
    series,
    comparisonData,
    average,
    statusCounts,
    chartRange,
    healthPercent,
    lastUpdatedLabel,
    formatMetric: (value) => formatMetric(value, metric),
    refresh,
  }
}

export default useDashboardAnalysisVM
