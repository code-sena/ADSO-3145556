import { useMemo } from 'react'
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  SafeAreaView,
  StatusBar,
  ActivityIndicator,
  Dimensions,
} from 'react-native'
import { Ionicons } from '@expo/vector-icons'
import { LineChart, BarChart, PieChart } from 'react-native-gifted-charts'
import { useTheme } from '../../../../context/ThemeContext.jsx'
import { useTranslation } from 'react-i18next'
import { useEnvironment } from '../../../../context/EnvironmentContext.jsx'
import { useDashboardAnalysisVM } from '../../viewmodels/useDashboardAnalysisVM.js'
import {
  ENVIRONMENT_COLORS,
  METRICS,
  normalizeStatus,
} from '../../utils/historicalSeries.js'
import { styles } from './DashboardScreen.style'

const GRID_COLOR = 'rgba(167,188,208,.28)'
const AXIS_TEXT_COLOR = '#8394A8'

const STATUS_COLORS = {
  normal: '#25E77C',
  warning: '#FFB11A',
  alert: '#FF4D5B',
}

function MetricIcon({ name, size = 14, color }) {
  return <Ionicons name={name} size={size} color={color} />
}

function KpiCard({
  label,
  value,
  note,
  icon,
  iconColor,
  valueColor,
  noteColor,
  cardColor,
  surface,
}) {
  return (
    <View
      style={[
        styles.kpiCard,
        {
          backgroundColor: surface,
          borderColor: cardColor,
        },
      ]}
    >
      <View style={styles.kpiTop}>
        <Text
          style={[styles.kpiLabel, { color: noteColor }]}
          numberOfLines={1}
        >
          {label}
        </Text>

        <MetricIcon name={icon} color={iconColor} />
      </View>

      <Text style={[styles.kpiValue, { color: valueColor }]}>
        {value}
      </Text>

      <View style={styles.kpiNote}>
        <Text
          style={{
            color: noteColor,
            fontSize: 10.5,
          }}
          numberOfLines={2}
        >
          {note}
        </Text>
      </View>
    </View>
  )
}

export default function DashboardScreen({ navigation }) {
  const { darkMode, currentColors } = useTheme()
  const { t } = useTranslation()
  const { loading } = useEnvironment()
  const vm = useDashboardAnalysisVM()

  const screenWidth = Dimensions.get('window').width

  const chartWidth = Math.max(
    240,
    screenWidth - 32 - 28 - 44
  )

  const pointCount = vm.periodLabels?.[vm.period]?.length || 0

  const initialSpacing = 10
  const endSpacing = 10

  const spacing =
    pointCount > 1
      ? Math.max(
          16,
          (chartWidth - initialSpacing - endSpacing) /
            (pointCount - 1)
        )
      : 40

  const chartColors = vm.series.map(
    (_, index) =>
      ENVIRONMENT_COLORS[
        index % ENVIRONMENT_COLORS.length
      ]
  )

  /*
   * Datos para el gráfico de barras.
   *
   * Se toma el último valor disponible de cada serie.
   */
  const barData = useMemo(() => {
    if (!Array.isArray(vm.series)) {
      return []
    }

    return vm.series
      .map((item, index) => {
        const points = Array.isArray(item.points)
          ? item.points
          : []

        const lastPoint =
          points.length > 0
            ? points[points.length - 1]
            : null

        const value =
          typeof lastPoint?.value === 'number'
            ? lastPoint.value
            : 0

        return {
          value,
          label: item.name || '',
          frontColor:
            ENVIRONMENT_COLORS[
              index % ENVIRONMENT_COLORS.length
            ],
          topLabelComponent: () => (
            <Text
              style={{
                color: currentColors.textMuted,
                fontSize: 9,
                marginBottom: 4,
              }}
            >
              {value !== 0 ? vm.formatMetric(value) : ''}
            </Text>
          ),
        }
      })
      .filter((item) => item.value >= 0)
  }, [
    vm.series,
    vm.formatMetric,
    currentColors.textMuted,
  ])

  const barWidth = Math.max(
    18,
    Math.min(
      34,
      (chartWidth - 32) /
        Math.max(barData.length * 2, 1)
    )
  )

  /*
   * Datos para el gráfico circular de estado.
   */
  const donutData = useMemo(() => {
    const normal = Number(vm.statusCounts?.normal || 0)
    const warning = Number(vm.statusCounts?.warning || 0)
    const alert = Number(vm.statusCounts?.alert || 0)

    const total = normal + warning + alert

    if (total === 0) {
      return [
        {
          value: 1,
          color: currentColors.borderColor,
        },
      ]
    }

    return [
      {
        value: normal,
        color: STATUS_COLORS.normal,
      },
      {
        value: warning,
        color: STATUS_COLORS.warning,
      },
      {
        value: alert,
        color: STATUS_COLORS.alert,
      },
    ].filter((item) => item.value > 0)
  }, [
    vm.statusCounts,
    currentColors.borderColor,
  ])

  /*
   * Propiedades de las líneas del gráfico histórico.
   */
  const lineProps = useMemo(() => {
    const props = {
      data: vm.series?.[0]?.points || [],
    }

    vm.series.forEach((item, index) => {
      const number = index + 1

      const color =
        ENVIRONMENT_COLORS[
          index % ENVIRONMENT_COLORS.length
        ]

      if (number > 1) {
        props[`data${number}`] = item.points || []
      }

      props[`color${number}`] = color
      props[`startFillColor${number}`] = color
      props[`endFillColor${number}`] = color
      props[`startOpacity${number}`] = 0.3
      props[`endOpacity${number}`] = 0.02
      props[`thickness${number}`] = 2.5
      props[`hideDataPoints${number}`] = true
    })

    if (!vm.series?.length) {
      props.data = []
    }

    return props
  }, [vm.series])

  /*
   * Tooltip del gráfico histórico.
   */
  const pointerLabel = (items) => {
    if (!Array.isArray(items) || !items.length) {
      return null
    }

    const rows = items.filter(
      (item) =>
        item &&
        typeof item.value === 'number'
    )

    if (!rows.length) {
      return null
    }

    return (
      <View
        style={[
          styles.pointer,
          {
            backgroundColor: currentColors.bgCard,
            borderColor: currentColors.borderColor,
          },
        ]}
      >
        <Text
          style={[
            styles.pointerLabel,
            {
              color: currentColors.textPrimary,
            },
          ]}
        >
          {rows[0].label}
        </Text>

        {rows.map((item, index) => (
          <View
            key={index}
            style={styles.pointerRow}
          >
            <View
              style={[
                styles.pointerDot,
                {
                  backgroundColor:
                    chartColors[index] ||
                    currentColors.accent,
                },
              ]}
            />

            <Text
              style={[
                styles.pointerName,
                {
                  color: currentColors.textMuted,
                },
              ]}
              numberOfLines={1}
            >
              {vm.series[index]?.name || ''}
            </Text>

            <Text
              style={[
                styles.pointerValue,
                {
                  color: currentColors.textPrimary,
                },
              ]}
            >
              {vm.formatMetric(item.value)}
            </Text>
          </View>
        ))}
      </View>
    )
  }

  /*
   * Porcentaje de ambientes dentro del rango.
   */
  const healthPercent = Number(
    vm.healthPercent || 0
  )


  return (
    <SafeAreaView
      style={[
        styles.safe,
        {
          backgroundColor: currentColors.bgBody,
        },
      ]}
    >
      <StatusBar
        barStyle={
          darkMode
            ? 'light-content'
            : 'dark-content'
        }
        backgroundColor={currentColors.bgBody}
      />

      {/* HEADER */}
      <View
        style={[
          styles.header,
          {
            backgroundColor: currentColors.bgCard,
            borderBottomColor:
              currentColors.borderColor,
          },
        ]}
      >
        <Ionicons
          name="analytics"
          size={24}
          color={currentColors.accent}
        />

        <View style={{ flex: 1 }}>
          <Text
            style={[
              styles.headerTitle,
              {
                color: currentColors.textPrimary,
              },
            ]}
          >
            {t('dashboardAnalysis.title')}
          </Text>

          <Text
            style={[
              styles.headerSub,
              {
                color: currentColors.textMuted,
              },
            ]}
          >
            {t('dashboardAnalysis.subtitle')}
          </Text>
        </View>

        <TouchableOpacity
          onPress={() =>
            navigation.navigate(
              'NotificationsPanel'
            )
          }
          style={[
            styles.iconBtn,
            {
              backgroundColor:
                currentColors.bgBody,
              borderColor:
                currentColors.borderColor,
            },
          ]}
          hitSlop={6}
        >
          <Ionicons
            name="notifications-outline"
            size={20}
            color={currentColors.textSecondary}
          />
        </TouchableOpacity>
      </View>

      {/* LOADING */}
      {loading ? (
        <View style={styles.center}>
          <ActivityIndicator
            size="large"
            color={currentColors.accent}
          />

          <Text
            style={[
              styles.centerTxt,
              {
                color: currentColors.textMuted,
              },
            ]}
          >
            {t(
              'common.loading',
              'Cargando…'
            )}
          </Text>
        </View>
      ) : (
        <ScrollView
          style={styles.scroll}
          contentContainerStyle={
            styles.scrollContent
          }
          showsVerticalScrollIndicator={false}
        >
          {/* HERO */}
          <View style={styles.hero}>
            <View style={styles.eyebrow}>
              <View
                style={[
                  styles.eyebrowDot,
                  {
                    backgroundColor:
                      currentColors.accent,
                  },
                ]}
              />

              <Text
                style={[
                  styles.eyebrowTxt,
                  {
                    color:
                      currentColors.accent,
                  },
                ]}
              >
                {t('dashboardAnalysis.title')}
              </Text>
            </View>

            <Text
              style={[
                styles.heroTitle,
                {
                  color:
                    currentColors.textPrimary,
                },
              ]}
            >
              {t(
                'dashboardAnalysis.heroLine1',
                'La calidad ambiental'
              )}
              {'\n'}

              <Text style={styles.heroTitleEm}>
                {vm.metricLabel}
              </Text>
            </Text>

            <Text
              style={[
                styles.heroDesc,
                {
                  color:
                    currentColors.textSecondary,
                },
              ]}
            >
              {t(
                'dashboardAnalysis.description'
              )}
            </Text>
          </View>

          {/* PERIODOS */}
          <View style={styles.periodRow}>
            {vm.periods.map((item) => {
              const active =
                vm.period === item.id

              return (
                <TouchableOpacity
                  key={item.id}
                  style={[
                    styles.periodChip,
                    {
                      backgroundColor:
                        currentColors.bgCard,
                      borderColor:
                        currentColors.borderColor,
                    },
                    active && {
                      backgroundColor:
                        currentColors.accent,
                      borderColor:
                        currentColors.accent,
                    },
                  ]}
                  onPress={() =>
                    vm.setPeriod(item.id)
                  }
                  activeOpacity={0.85}
                >
                  <Text
                    style={[
                      styles.periodChipTxt,
                      {
                        color: active
                          ? '#fff'
                          : currentColors.textSecondary,
                      },
                    ]}
                  >
                    {item.label}
                  </Text>
                </TouchableOpacity>
              )
            })}
          </View>

          <View style={styles.periodContext}>
            <Ionicons
              name="calendar-outline"
              size={14}
              color={currentColors.textMuted}
            />

            <Text
              style={[
                styles.periodContextTxt,
                {
                  color:
                    currentColors.textMuted,
                },
              ]}
            >
              {vm.periodInfo.context}
            </Text>
          </View>

          {/* MÉTRICAS */}
          <View style={styles.chipRow}>
            {Object.keys(METRICS).map((key) => {
              const active =
                vm.metric === key

              const info = METRICS[key]

              return (
                <TouchableOpacity
                  key={key}
                  style={[
                    styles.chip,
                    {
                      backgroundColor:
                        currentColors.bgCard,
                      borderColor:
                        currentColors.borderColor,
                    },
                    active && {
                      backgroundColor: `${info.color}18`,
                      borderColor:
                        info.color,
                    },
                  ]}
                  onPress={() =>
                    vm.setMetric(key)
                  }
                  activeOpacity={0.85}
                >
                  <Ionicons
                    name={info.icon}
                    size={14}
                    color={
                      active
                        ? info.color
                        : currentColors.textMuted
                    }
                  />

                  <Text
                    style={[
                      styles.chipTxt,
                      {
                        color: active
                          ? info.color
                          : currentColors.textSecondary,
                      },
                    ]}
                    numberOfLines={1}
                    adjustsFontSizeToFit
                  >
                    {vm.metricLabels[key]}
                  </Text>
                </TouchableOpacity>
              )
            })}
          </View>

          {/* SELECCIÓN DE AMBIENTES */}
          <View
            style={[
              styles.card,
              {
                backgroundColor:
                  currentColors.bgCard,
                borderColor:
                  currentColors.borderColor,
              },
            ]}
          >
            <View style={styles.cardHead}>
              <View style={{ flex: 1 }}>
                <Text
                  style={[
                    styles.sectionLabel,
                    {
                      color:
                        currentColors.textMuted,
                    },
                  ]}
                >
                  {t(
                    'dashboardAnalysis.contextLabel',
                    'CONTEXTO'
                  )}
                </Text>

                <Text
                  style={[
                    styles.cardTitle,
                    {
                      color:
                        currentColors.textPrimary,
                    },
                  ]}
                >
                  {t(
                    'dashboardAnalysis.context.environments'
                  )}
                </Text>

                <Text
                  style={[
                    styles.cardSub,
                    {
                      color:
                        currentColors.textMuted,
                    },
                  ]}
                >
                  {t(
                    'dashboardAnalysis.context.selectEnvironment'
                  )}
                </Text>
              </View>

              <View
                style={[
                  styles.countBadge,
                  {
                    backgroundColor: `${currentColors.accent}18`,
                  },
                ]}
              >
                <Text
                  style={[
                    styles.countBadgeTxt,
                    {
                      color:
                        currentColors.accent,
                    },
                  ]}
                >
                  {vm.environments.length}
                </Text>
              </View>
            </View>

            <View style={styles.envList}>
              {/* TODOS */}
              <TouchableOpacity
                style={[
                  styles.envRow,
                  {
                    backgroundColor:
                      vm.environmentId ===
                      'all'
                        ? `${currentColors.accent}12`
                        : currentColors.bgCard,
                    borderColor:
                      vm.environmentId ===
                      'all'
                        ? currentColors.accent
                        : currentColors.borderColor,
                  },
                ]}
                onPress={() =>
                  vm.setEnvironmentId('all')
                }
                activeOpacity={0.8}
              >
                <View
                  style={[
                    styles.envIcon,
                    {
                      backgroundColor: `${currentColors.accent}18`,
                    },
                  ]}
                >
                  <Ionicons
                    name="leaf-outline"
                    size={16}
                    color={
                      currentColors.accent
                    }
                  />
                </View>

                <View style={styles.envInfo}>
                  <Text
                    style={[
                      styles.envName,
                      {
                        color:
                          currentColors.textPrimary,
                      },
                    ]}
                  >
                    {t(
                      'dashboardAnalysis.context.allEnvironments'
                    )}
                  </Text>

                  <Text
                    style={[
                      styles.envMeta,
                      {
                        color:
                          currentColors.textMuted,
                      },
                    ]}
                  >
                    {t(
                      'dashboardAnalysis.context.consolidatedView'
                    )}
                  </Text>
                </View>
              </TouchableOpacity>

              {/* AMBIENTES */}
              {vm.environments.map(
                (environment) => {
                  const isSelected =
                    String(
                      vm.environmentId
                    ) ===
                    String(environment.id)

                  const normalizedStatus =
                    normalizeStatus(
                      environment.statusKey
                    )

                  const statusColor =
                    STATUS_COLORS[
                      normalizedStatus.key
                    ] ||
                    currentColors.accent

                  return (
                    <TouchableOpacity
                      key={environment.id}
                      style={[
                        styles.envRow,
                        {
                          backgroundColor:
                            isSelected
                              ? `${currentColors.accent}12`
                              : currentColors.bgCard,
                          borderColor:
                            isSelected
                              ? currentColors.accent
                              : currentColors.borderColor,
                        },
                      ]}
                      onPress={() =>
                        vm.setEnvironmentId(
                          String(
                            environment.id
                          )
                        )
                      }
                      activeOpacity={0.8}
                    >
                      <View
                        style={[
                          styles.envIcon,
                          {
                            backgroundColor: `${statusColor}18`,
                          },
                        ]}
                      >
                        <Ionicons
                          name={
                            normalizedStatus.key ===
                            'alert'
                              ? 'alert-circle-outline'
                              : normalizedStatus.key ===
                                  'warning'
                                ? 'warning-outline'
                                : 'checkmark-circle-outline'
                          }
                          size={16}
                          color={statusColor}
                        />
                      </View>

                      <View
                        style={
                          styles.envInfo
                        }
                      >
                        <Text
                          style={[
                            styles.envName,
                            {
                              color:
                                currentColors.textPrimary,
                            },
                          ]}
                          numberOfLines={1}
                        >
                          {environment.name}
                        </Text>

                        <Text
                          style={[
                            styles.envMeta,
                            {
                              color:
                                currentColors.textMuted,
                            },
                          ]}
                          numberOfLines={1}
                        >
                          {environment.location ||
                            t(
                              'dashboardAnalysis.context.environment'
                            )}
                        </Text>
                      </View>

                      <View
                        style={[
                          styles.countBadge,
                          {
                            backgroundColor: `${statusColor}18`,
                          },
                        ]}
                      >
                        <Text
                          style={[
                            styles.countBadgeTxt,
                            {
                              color: statusColor,
                            },
                          ]}
                        >
                          {environment.statusKey
                            ? t(
                                environment.statusKey
                              )
                            : ''}
                        </Text>
                      </View>
                    </TouchableOpacity>
                  )
                }
              )}
            </View>

            {/* ÚLTIMA ACTUALIZACIÓN */}
            <View
              style={[
                styles.updatedRow,
                {
                  borderTopColor:
                    currentColors.borderColor,
                },
              ]}
            >
              <Ionicons
                name="time-outline"
                size={14}
                color={currentColors.textMuted}
              />

              <Text
                style={[
                  styles.updatedTxt,
                  {
                    color:
                      currentColors.textMuted,
                  },
                ]}
              >
                {t(
                  'dashboardAnalysis.context.lastReading'
                )}
                {' · '}
                <Text
                  style={{
                    fontWeight: '800',
                    color:
                      currentColors.textSecondary,
                  }}
                >
                  {vm.lastUpdatedLabel}
                </Text>
              </Text>

              <TouchableOpacity
                style={[
                  styles.refreshBtn,
                  {
                    borderColor:
                      currentColors.borderColor,
                  },
                ]}
                onPress={vm.refresh}
                hitSlop={8}
                accessibilityLabel={t(
                  'dashboardAnalysis.ariaLabels.refreshData'
                )}
              >
                <Ionicons
                  name="refresh"
                  size={14}
                  color={currentColors.accent}
                />
              </TouchableOpacity>
            </View>
          </View>

          {/* KPIS */}
          <View style={styles.kpiGrid}>
            <KpiCard
              label={`${t(
                'dashboardAnalysis.kpi.averagePrefix'
              )}${vm.metricLabel}`}
              value={vm.formatMetric(
                vm.average
              )}
              note={`↓ ${t(
                'dashboardAnalysis.kpi.vsPreviousPeriod'
              )}`}
              icon={vm.metricInfo.icon}
              iconColor={currentColors.accent}
              valueColor={currentColors.accent}
              noteColor={currentColors.textMuted}
              cardColor={`${currentColors.accent}55`}
              surface={`${currentColors.accent}10`}
            />

            <KpiCard
              label={vm.statusLabels.normal}
              value={String(
                vm.statusCounts.normal
              )}
              note={t(
                'dashboardAnalysis.kpi.environmentsInRange'
              )}
              icon="checkmark-circle-outline"
              iconColor={STATUS_COLORS.normal}
              valueColor={STATUS_COLORS.normal}
              noteColor={currentColors.textMuted}
              cardColor={
                currentColors.borderColor
              }
              surface={currentColors.bgCard}
            />

            <KpiCard
              label={vm.statusLabels.warning}
              value={String(
                vm.statusCounts.warning
              )}
              note={t(
                'dashboardAnalysis.kpi.requireFollowUp'
              )}
              icon="stats-chart-outline"
              iconColor={STATUS_COLORS.warning}
              valueColor={STATUS_COLORS.warning}
              noteColor={currentColors.textMuted}
              cardColor={
                currentColors.borderColor
              }
              surface={currentColors.bgCard}
            />

            <KpiCard
              label={vm.statusLabels.alert}
              value={String(
                vm.statusCounts.alert
              )}
              note={t(
                'dashboardAnalysis.kpi.requireAttention'
              )}
              icon="speedometer-outline"
              iconColor={STATUS_COLORS.alert}
              valueColor={STATUS_COLORS.alert}
              noteColor={currentColors.textMuted}
              cardColor={
                currentColors.borderColor
              }
              surface={currentColors.bgCard}
            />
          </View>

          {/* GRÁFICO HISTÓRICO */}
          <View
            style={[
              styles.card,
              {
                backgroundColor:
                  currentColors.bgCard,
                borderColor:
                  currentColors.borderColor,
              },
            ]}
          >
            <View style={styles.cardHead}>
              <View style={{ flex: 1 }}>
                <Text
                  style={[
                    styles.sectionLabel,
                    {
                      color:
                        currentColors.textMuted,
                    },
                  ]}
                >
                  {t(
                    'dashboardAnalysis.title'
                  )}
                </Text>

                <Text
                  style={[
                    styles.cardTitle,
                    {
                      color:
                        currentColors.textPrimary,
                    },
                  ]}
                >
                  {vm.metricLabel}{' '}
                  {t(
                    'dashboardAnalysis.chart.throughTime'
                  )}
                </Text>

                <Text
                  style={[
                    styles.cardSub,
                    {
                      color:
                        currentColors.textMuted,
                    },
                  ]}
                >
                  {vm.selectedLabel} ·{' '}
                  {vm.periodInfo.context}
                </Text>
              </View>

              <View
                style={[
                  styles.livePill,
                  {
                    backgroundColor: `${currentColors.accent}14`,
                  },
                ]}
              >
                <View
                  style={[
                    styles.liveDot,
                    {
                      backgroundColor:
                        currentColors.accent,
                    },
                  ]}
                />

                <Text
                  style={[
                    styles.liveTxt,
                    {
                      color:
                        currentColors.accent,
                    },
                  ]}
                >
                  {t(
                    'dashboardAnalysis.chart.syncedData'
                  )}
                </Text>
              </View>
            </View>

            {/* LEYENDA */}
            <View style={styles.legendRow}>
              {vm.series.map(
                (item, index) => (
                  <View
                    key={item.id}
                    style={styles.legendItem}
                  >
                    <View
                      style={[
                        styles.legendDot,
                        {
                          backgroundColor:
                            ENVIRONMENT_COLORS[
                              index %
                                ENVIRONMENT_COLORS.length
                            ],
                        },
                      ]}
                    />

                    <Text
                      style={[
                        styles.legendTxt,
                        {
                          color:
                            currentColors.textSecondary,
                        },
                      ]}
                      numberOfLines={1}
                    >
                      {item.name}
                    </Text>
                  </View>
                )
              )}

              <View
                style={styles.legendComfort}
              >
                <View
                  style={[
                    styles.legendDash,
                    {
                      borderColor:
                        currentColors.textMuted,
                    },
                  ]}
                />

                <Text
                  style={[
                    styles.legendTxt,
                    {
                      color:
                        currentColors.textMuted,
                    },
                  ]}
                >
                  {t(
                    'dashboardAnalysis.chart.comfortThreshold'
                  )}
                </Text>
              </View>
            </View>

            <LineChart
              {...lineProps}
              areaChart
              width={chartWidth}
              height={210}
              spacing={spacing}
              initialSpacing={initialSpacing}
              endSpacing={endSpacing}
              maxValue={Math.max(
                1,
                vm.chartRange.max -
                  vm.chartRange.min
              )}
              yAxisOffset={vm.chartRange.min}
              noOfSections={4}
              thickness={2.5}
              isAnimated={false}
              rulesColor={GRID_COLOR}
              dashWidth={3}
              dashGap={5}
              rulesType="dashed"
              xAxisColor="transparent"
              yAxisColor="transparent"
              yAxisThickness={0}
              yAxisLabelWidth={44}
              xAxisLabelTextStyle={{
                color: AXIS_TEXT_COLOR,
                fontSize: 10,
              }}
              yAxisTextStyle={{
                color: AXIS_TEXT_COLOR,
                fontSize: 10,
              }}
              showReferenceLine1
              referenceLine1Position={
                vm.chartRange.comfort
              }
              referenceLine1Config={{
                color: 'rgba(185,203,197,.85)',
                type: 'dashed',
                dashWidth: 5,
                dashGap: 5,
                thickness: 1.2,
                labelText: '',
              }}
              disableScroll
              pointerConfig={{
                activatePointersOnLongPress:
                  true,
                persistPointer: true,
                pointerColor:
                  currentColors.accent,
                pointerWidth: 10,
                pointerHeight: 10,
                pointerRadius: 5,
                pointerLabelComponent:
                  pointerLabel,
              }}
            />

            <View style={styles.captionRow}>
              <Text
                style={[
                  styles.captionTxt,
                  {
                    color:
                      currentColors.textMuted,
                  },
                ]}
              >
                {t(
                  'dashboardAnalysis.chart.recommendedAverage'
                )}
                {vm.formatMetric(
                  vm.chartRange.comfort
                )}
              </Text>

              <Text
                style={[
                  styles.captionTxt,
                  {
                    color:
                      currentColors.textMuted,
                    textAlign: 'right',
                  },
                ]}
              >
                {vm.metric === 'co2'
                  ? t(
                      'dashboardAnalysis.chart.co2Range'
                    )
                  : t(
                      'dashboardAnalysis.chart.referenceRange'
                    )}
              </Text>
            </View>
          </View>

          {/* COMPARACIÓN POR AMBIENTE */}
          <View
            style={[
              styles.card,
              {
                backgroundColor:
                  currentColors.bgCard,
                borderColor:
                  currentColors.borderColor,
              },
            ]}
          >
            <View style={styles.cardHead}>
              <View style={{ flex: 1 }}>
                <Text
                  style={[
                    styles.sectionLabel,
                    {
                      color:
                        currentColors.textMuted,
                    },
                  ]}
                >
                  {t(
                    'dashboardAnalysis.comparison.title'
                  )}
                </Text>

                <Text
                  style={[
                    styles.cardTitle,
                    {
                      color:
                        currentColors.textPrimary,
                    },
                  ]}
                >
                  {t(
                    'dashboardAnalysis.comparison.byEnvironment'
                  )}
                </Text>

                <Text
                  style={[
                    styles.cardSub,
                    {
                      color:
                        currentColors.textMuted,
                    },
                  ]}
                >
                  {t(
                    'dashboardAnalysis.comparison.descriptionPrefix'
                  )}{' '}
                  {vm.metricLabel.toLowerCase()}{' '}
                  {t(
                    'dashboardAnalysis.comparison.descriptionSuffix'
                  )}
                </Text>
              </View>

              <Ionicons
                name="bar-chart-outline"
                size={18}
                color={currentColors.textMuted}
              />
            </View>

            {barData.length ? (
              <BarChart
                data={barData}
                width={chartWidth}
                height={190}
                barWidth={barWidth}
                roundedTop
                barBorderRadius={5}
                noOfSections={4}
                maxValue={Math.max(
                  1,
                  ...barData.map(
                    (item) => item.value
                  )
                ) * 1.15}
                spacing={Math.max(
                  8,
                  (chartWidth - 46) /
                    Math.max(
                      barData.length,
                      1
                    ) -
                    barWidth
                )}
                initialSpacing={8}
                endSpacing={8}
                isAnimated={false}
                rulesColor={GRID_COLOR}
                dashWidth={3}
                dashGap={5}
                rulesType="dashed"
                xAxisColor="transparent"
                yAxisColor="transparent"
                yAxisThickness={0}
                yAxisLabelWidth={44}
                xAxisLabelTextStyle={{
                  color: AXIS_TEXT_COLOR,
                  fontSize: 9,
                }}
                yAxisTextStyle={{
                  color: AXIS_TEXT_COLOR,
                  fontSize: 10,
                }}
                disableScroll
              />
            ) : (
              <Text
                style={[
                  styles.cardSub,
                  {
                    color:
                      currentColors.textMuted,
                  },
                ]}
              >
                {t(
                  'dashboardAnalysis.comparison.descriptionSuffix',
                  'Sin datos'
                )}
              </Text>
            )}
          </View>

          {/* ESTADO DE LA RED */}
          <View
            style={[
              styles.card,
              {
                backgroundColor:
                  currentColors.bgCard,
                borderColor:
                  currentColors.borderColor,
              },
            ]}
          >
            <View style={styles.cardHead}>
              <View style={{ flex: 1 }}>
                <Text
                  style={[
                    styles.sectionLabel,
                    {
                      color:
                        currentColors.textMuted,
                    },
                  ]}
                >
                  {t(
                    'dashboardAnalysis.network.title'
                  )}
                </Text>

                <Text
                  style={[
                    styles.cardTitle,
                    {
                      color:
                        currentColors.textPrimary,
                    },
                  ]}
                >
                  {t(
                    'dashboardAnalysis.network.health'
                  )}
                </Text>
              </View>

              <View
                style={[
                  styles.livePill,
                  {
                    backgroundColor: `${currentColors.accent}14`,
                  },
                ]}
              >
                <View
                  style={[
                    styles.liveDot,
                    {
                      backgroundColor:
                        currentColors.accent,
                    },
                  ]}
                />

                <Text
                  style={[
                    styles.liveTxt,
                    {
                      color:
                        currentColors.accent,
                    },
                  ]}
                >
                  {t(
                    'dashboardAnalysis.network.monitoring'
                  )}
                </Text>
              </View>
            </View>

            <View style={styles.networkBody}>
              <View style={styles.donutWrap}>
                <PieChart
                  donut
                  data={donutData}
                  radius={62}
                  innerRadius={42}
                  strokeWidth={0}
                  backgroundColor="transparent"
                  isAnimated={false}
                  centerLabelComponent={() => (
                    <View
                      style={styles.donutCenter}
                    >
                      <Text
                        style={[
                          styles.donutValue,
                          {
                            color:
                              currentColors.textPrimary,
                          },
                        ]}
                      >
                        {healthPercent}%
                      </Text>

                      <Text
                        style={[
                          styles.donutLabel,
                          {
                            color:
                              currentColors.textMuted,
                          },
                        ]}
                      >
                        {t(
                          'dashboardAnalysis.network.inRange'
                        )}
                      </Text>
                    </View>
                  )}
                />
              </View>

              <View
                style={styles.networkLegend}
              >
                {[
                  {
                    key: 'normal',
                    label:
                      vm.statusLabels.normal,
                    value:
                      vm.statusCounts.normal,
                  },
                  {
                    key: 'warning',
                    label:
                      vm.statusLabels.warning,
                    value:
                      vm.statusCounts.warning,
                  },
                  {
                    key: 'alert',
                    label:
                      vm.statusLabels.alert,
                    value:
                      vm.statusCounts.alert,
                  },
                ].map((item) => (
                  <View
                    key={item.key}
                    style={styles.networkRow}
                  >
                    <View
                      style={[
                        styles.networkDot,
                        {
                          backgroundColor:
                            STATUS_COLORS[
                              item.key
                            ],
                        },
                      ]}
                    />

                    <Text
                      style={[
                        styles.networkName,
                        {
                          color:
                            currentColors.textSecondary,
                        },
                      ]}
                    >
                      {item.label}
                    </Text>

                    <Text
                      style={[
                        styles.networkCount,
                        {
                          color:
                            currentColors.textPrimary,
                        },
                      ]}
                    >
                      {item.value}
                    </Text>
                  </View>
                ))}

                <View style={styles.networkFoot}>
                  <Ionicons
                    name="checkmark-circle"
                    size={13}
                    color={
                      currentColors.accent
                    }
                  />

                  <Text
                    style={[
                      styles.footerTxt,
                      {
                        color:
                          currentColors.textMuted,
                      },
                    ]}
                  >
                    {t(
                      'dashboardAnalysis.network.dataUpdated'
                    )}
                  </Text>
                </View>
              </View>
            </View>
          </View>

          {/* LECTURA RÁPIDA */}
          <View
            style={[
              styles.card,
              {
                backgroundColor:
                  currentColors.bgCard,
                borderColor:
                  currentColors.borderColor,
              },
            ]}
          >
            <Text
              style={[
                styles.sectionLabel,
                {
                  color:
                    currentColors.textMuted,
                },
              ]}
            >
              {t(
                'dashboardAnalysis.quickReading.title'
              )}
            </Text>

            <Text
              style={[
                styles.readingTitle,
                {
                  color:
                    currentColors.textPrimary,
                },
              ]}
            >
              {vm.statusCounts.alert > 0
                ? t(
                    'dashboardAnalysis.quickReading.alertMessage'
                  )
                : t(
                    'dashboardAnalysis.quickReading.normalMessage'
                  )}
            </Text>

            <Text
              style={[
                styles.readingDesc,
                {
                  color:
                    currentColors.textSecondary,
                },
              ]}
            >
              {vm.statusCounts.alert > 0
                ? t(
                    'dashboardAnalysis.quickReading.alertDescription'
                  )
                : t(
                    'dashboardAnalysis.quickReading.normalDescription'
                  )}
            </Text>

            <TouchableOpacity
              style={styles.readingBtn}
              onPress={() => {
                if (
                  vm.statusCounts.alert > 0
                ) {
                  const alertEnvironment =
                    vm.environments.find(
                      (environment) =>
                        normalizeStatus(
                          environment.statusKey
                        ).key === 'alert'
                    )

                  if (
                    alertEnvironment?.id !=
                    null
                  ) {
                    vm.setEnvironmentId(
                      String(
                        alertEnvironment.id
                      )
                    )
                    return
                  }
                }

                vm.setEnvironmentId('all')
              }}
            >
              <Text
                style={[
                  styles.readingBtnTxt,
                  {
                    color:
                      currentColors.accent,
                  },
                ]}
              >
                {t(
                  'dashboardAnalysis.quickReading.viewDetail'
                )}
              </Text>

              <Ionicons
                name="arrow-up"
                size={14}
                color={currentColors.accent}
              />
            </TouchableOpacity>
          </View>

          {/* FOOTER */}
          <View style={styles.footer}>
            <Text
              style={[
                styles.footerTxt,
                {
                  color:
                    currentColors.textMuted,
                },
              ]}
            >
              {t(
                'dashboardAnalysis.footer.brand'
              )}
            </Text>

            <Text
              style={[
                styles.footerTxt,
                {
                  color:
                    currentColors.textMuted,
                },
              ]}
            >
              {vm.environments.length}{' '}
              {t(
                'dashboardAnalysis.footer.autoUpdate'
              )}
            </Text>
          </View>
        </ScrollView>
      )}
    </SafeAreaView>
  )
}