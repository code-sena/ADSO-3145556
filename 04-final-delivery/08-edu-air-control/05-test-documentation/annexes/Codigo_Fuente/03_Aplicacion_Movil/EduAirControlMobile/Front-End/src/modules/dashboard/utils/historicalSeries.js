export const CURVES = {
  day: [0.94, 0.9, 0.82, 1.03, 1.2, 1.09, 0.98, 0.86],
  week: [1.02, 0.96, 1.07, 0.91, 1.12, 0.82, 0.88],
  month: [1.08, 1.03, 0.98, 0.93, 0.88],
  year: [1.16, 1.09, 1.04, 1.02, 0.98, 0.93, 0.9, 0.86],
}

export const METRICS = {
  co2: {
    label: 'CO₂',
    unit: 'ppm',
    color: '#01805b',
    comfort: 800,
    domain: [350, 1300],
    icon: 'cloud-outline',
  },
  temperature: {
    label: 'temperature',
    unit: '°C',
    color: '#FF6873',
    comfort: 22,
    domain: [18, 34],
    icon: 'thermometer-outline',
  },
  humidity: {
    label: 'humidity',
    unit: '%',
    color: '#58AFFF',
    comfort: 50,
    domain: [25, 80],
    icon: 'water-outline',
  },
  noise: {
    label: 'noise',
    unit: 'dB',
    color: '#D98AFF',
    comfort: 45,
    domain: [20, 85],
    icon: 'volume-high-outline',
  },
}

export const STATUS_KEYS = {
  normal: { key: 'normal', color: '#25E77C' },
  warning: { key: 'warning', color: '#FFB11A' },
  alert: { key: 'alert', color: '#FF4D5B' },
}

export const ENVIRONMENT_COLORS = ['#01805b', '#FFB11A', '#FF4D5B', '#9D73FF', '#49D17D', '#58AFFF']

// gifted-charts admite máximo 5 series por gráfica (data..data5)
export const MAX_SERIES = 5

export const PERIOD_IDS = ['day', 'week', 'month', 'year']

export function normalizeStatus(statusKey) {
  const value = String(statusKey || '').toLowerCase()
  if (value.includes('alert')) return STATUS_KEYS.alert
  if (value.includes('warning')) return STATUS_KEYS.warning
  return STATUS_KEYS.normal
}

export function formatMetric(value, metric) {
  if (value == null || Number.isNaN(Number(value))) return '—'
  if (metric === 'temperature') return `${Number(value).toFixed(1)}°C`
  return `${Math.round(Number(value))} ${METRICS[metric].unit}`
}

export function getBaseValue(environment, metric) {
  return {
    co2: Number(environment.co2) || 0,
    temperature: Number(environment.temp) || 0,
    humidity: Number(environment.humidity) || 0,
    noise: Number(environment.noise) || 0,
  }[metric]
}

export function getHistoricalValue(environment, metric, curveValue, index) {
  const base = getBaseValue(environment, metric)
  const variation =
    Math.sin((index + String(environment.id).length) * 1.37) *
    (metric === 'co2' ? 58 : metric === 'humidity' ? 3.8 : metric === 'noise' ? 4.5 : 0.55)
  const factor =
    metric === 'co2'
      ? curveValue
      : metric === 'temperature'
        ? 0.68 + curveValue * 0.25
        : metric === 'humidity'
          ? 0.76 + curveValue * 0.18
          : 0.8 + curveValue * 0.16

  return metric === 'temperature'
    ? Number((base + variation * factor).toFixed(1))
    : Math.round(base * factor + variation)
}

export function getPeriodValue(environment, metric, period) {
  const factor = period === 'day' ? 1 : period === 'week' ? 0.98 : period === 'month' ? 0.95 : 0.91
  return getHistoricalValue(environment, metric, factor, 4)
}

export function getPeriodLabels(t) {
  return {
    day: t('dashboardAnalysis.chart.hours', { returnObjects: true }),
    week: t('dashboardAnalysis.chart.weekdays', { returnObjects: true }),
    month: t('dashboardAnalysis.chart.weeks', { returnObjects: true }),
    year: t('dashboardAnalysis.chart.months', { returnObjects: true }),
  }
}

export function getSeriesColors(count) {
  return Array.from({ length: count }, (_, index) => ENVIRONMENT_COLORS[index % ENVIRONMENT_COLORS.length])
}
