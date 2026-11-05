import { useEffect, useState } from 'react'
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  SafeAreaView,
  StatusBar,
  ActivityIndicator,
} from 'react-native'
import { useNavigation, useRoute } from '@react-navigation/native'
import { Ionicons } from '@expo/vector-icons'
import { useTheme } from '../../../../context/ThemeContext.jsx'
import { useTranslation } from 'react-i18next'
import { useProvisioningVM } from '../../viewmodels/useProvisioningVM.js'
import sensorService from '../../../environment/services/sensorService.js'
import environmentService from '../../../environment/services/environmentService.js'
import Input from '../../../../shared/components/Input/Input.jsx'
import Button from '../../../../shared/components/Button/Button.jsx'
import { useToast } from '../../../../shared/components/Toast/Toast.jsx'
import { styles } from './ProvisioningScreen.styles'

const PHASE_STATUS = {
  idle: 'provisioning.status.idle',
  scanning: 'provisioning.status.scanning',
  connecting: 'provisioning.status.connecting',
  ready: 'provisioning.status.ready',
  sending: 'provisioning.status.sending',
  waiting: 'provisioning.status.waiting',
  success: 'provisioning.status.success',
  error: 'provisioning.status.error',
}

export default function ProvisioningScreen() {
  const navigation = useNavigation()
  const route = useRoute()
  const { currentColors: c } = useTheme()
  const { t } = useTranslation()
  const toast = useToast()
  const vm = useProvisioningVM()

  const environmentId = route.params?.environmentId || null
  const [mac, setMac] = useState(route.params?.macAddress || null)
  const [saving, setSaving] = useState(false)
  const [deviceConfig, setDeviceConfig] = useState(null)

  // Catalogos de ms-sensor-management: el alta exige un modelo y un estado reales.
  const [models, setModels] = useState([])
  const [statuses, setStatuses] = useState([])
  const [environments, setEnvironments] = useState([])
  const [modelId, setModelId] = useState('')
  const [statusId, setStatusId] = useState('')
  const [envId, setEnvId] = useState(environmentId || '')

  useEffect(() => {
    let cancelled = false
    Promise.all([
      sensorService.listModels().catch(() => []),
      sensorService.listStatuses().catch(() => []),
      environmentService.getAll().catch(() => []),
    ])
      .then(([modelList, statusList, envList]) => {
        if (cancelled) return
        setModels(modelList)
        setStatuses(statusList)
        setEnvironments(envList)
        // El modelo completo es el que lleva los tres modulos.
        const full = modelList.find((m) => m.code === 'ESP32_FULL') || modelList[0]
        if (full) setModelId(full.sensorModelId || full.id)
        const active = statusList.find((s) => s.code === 'ACTIVE') || statusList[0]
        if (active) setStatusId(active.sensorStatusId || active.id)
        if (!envId && envList.length === 1) setEnvId(envList[0].id)
      })
      .catch(() => {})
    return () => {
      cancelled = true
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  const handleScan = async () => {
    const device = await vm.startScan()
    if (device) setMac((prev) => prev || device.id?.toUpperCase() || null)
  }

  /**
   * Da de alta el sensor, lo instala en el ambiente y pide su credencial.
   * Todo esto tiene que existir ANTES de enviar el payload por BLE: el ESP32
   * guarda el token y el installationId y luego envia con ellos.
   */
  const registerDevice = async () => {
    if (!mac) {
      toast.error(t('devices.errors.mac'))
      return null
    }
    if (!modelId || !statusId) {
      toast.error(t('provisioning.errors.model'))
      return null
    }
    if (!envId) {
      toast.error(t('provisioning.errors.environment'))
      return null
    }

    setSaving(true)
    try {
      const sensor = await sensorService.create({
        sensorId: mac.toUpperCase(),
        sensorModelId: modelId,
        sensorStatusId: statusId,
      })
      const installation = await sensorService.install(sensor.id, envId)
      const installationId = installation?.sensorInstallationId || installation?.id
      const tokenResponse = await sensorService.requestDeviceToken(sensor.id)

      const config = {
        sensorId: sensor.id,
        installationId,
        token: tokenResponse?.accessToken,
      }
      setDeviceConfig(config)
      toast.success(t('provisioning.toasts.saved'))
      return config
    } catch (e) {
      toast.error(e.message)
      return null
    } finally {
      setSaving(false)
    }
  }

  const handleSend = async () => {
    const config = deviceConfig || (await registerDevice())
    if (!config) return
    const result = await vm.submit(config)
    if (result === 'connected') toast.success(t('provisioning.toasts.connected'))
    else if (result === 'fail') toast.error(t('devices.errors.wifiFail'))
  }

  const statusKey = vm.phase === 'error' && vm.errorKey ? vm.errorKey : PHASE_STATUS[vm.phase]
  const success = vm.phase === 'success'
  const canSend = vm.phase === 'ready'

  return (
    <SafeAreaView style={[styles.safe, { backgroundColor: c.bgBody }]}>
      <StatusBar barStyle="dark-content" backgroundColor={c.bgBody} />

      <View style={[styles.header, { borderBottomColor: c.borderColor }]}>
        <TouchableOpacity onPress={() => { vm.cleanup(); navigation.goBack() }} hitSlop={8}>
          <Ionicons name="arrow-back" size={22} color={c.textPrimary} />
        </TouchableOpacity>
        <Text style={[styles.headerTitle, { color: c.textPrimary }]}>{t('provisioning.title')}</Text>
        <View style={{ width: 22 }} />
      </View>

      <ScrollView contentContainerStyle={styles.content} showsVerticalScrollIndicator={false} keyboardShouldPersistTaps="handled">
        <Text style={[styles.intro, { color: c.textSecondary }]}>{t('provisioning.subtitle')}</Text>

        <View style={[styles.card, { backgroundColor: c.bgCard, borderColor: c.borderColor }]}>
          <Text style={[styles.cardLabel, { color: c.textMuted }]}>{t('provisioning.deviceLabel')}</Text>
          <View style={styles.deviceRow}>
            <View style={[styles.deviceIcon, { backgroundColor: c.accentDim }]}>
              <Ionicons name="hardware-chip-outline" size={18} color={c.accent} />
            </View>
            <View style={{ flex: 1 }}>
              <Text style={[styles.deviceMac, { color: c.textPrimary }]}>
                {mac || '—'}
              </Text>
              <Text style={[styles.deviceName, { color: c.textMuted }]} numberOfLines={1}>
                {t('devices.noName')}
                {vm.ssid ? ` · ${vm.ssid}` : ''}
              </Text>
            </View>
          </View>
        </View>

        {/* Modelo, estado y ambiente: el alta del sensor exige UUIDs reales. */}
        <View style={[styles.card, { backgroundColor: c.bgCard, borderColor: c.borderColor }]}>
          <Text style={[styles.cardLabel, { color: c.textMuted }]}>{t('provisioning.configLabel')}</Text>

          <Text style={[styles.hint, { color: c.textMuted }]}>{t('provisioning.modelLabel')}</Text>
          <View style={styles.profilesRow}>
            {models.map((m) => {
              const id = m.sensorModelId || m.id
              const active = id === modelId
              return (
                <TouchableOpacity
                  key={id}
                  style={[styles.profileBtn, active && styles.profileBtnActive]}
                  onPress={() => setModelId(id)}
                >
                  <Text style={[styles.profileBtnTxt, active && styles.profileBtnTxtActive]}>
                    {m.name}
                  </Text>
                </TouchableOpacity>
              )
            })}
          </View>

          <Text style={[styles.hint, { color: c.textMuted }]}>{t('provisioning.statusLabel')}</Text>
          <View style={styles.profilesRow}>
            {statuses.map((s) => {
              const id = s.sensorStatusId || s.id
              const active = id === statusId
              return (
                <TouchableOpacity
                  key={id}
                  style={[styles.profileBtn, active && styles.profileBtnActive]}
                  onPress={() => setStatusId(id)}
                >
                  <Text style={[styles.profileBtnTxt, active && styles.profileBtnTxtActive]}>
                    {s.name}
                  </Text>
                </TouchableOpacity>
              )
            })}
          </View>

          <Text style={[styles.hint, { color: c.textMuted }]}>{t('provisioning.environmentLabel')}</Text>
          <View style={styles.profilesRow}>
            {environments.map((env) => {
              const active = env.id === envId
              return (
                <TouchableOpacity
                  key={env.id}
                  style={[styles.profileBtn, active && styles.profileBtnActive]}
                  onPress={() => setEnvId(env.id)}
                >
                  <Text style={[styles.profileBtnTxt, active && styles.profileBtnTxtActive]}>
                    {env.name}
                  </Text>
                </TouchableOpacity>
              )
            })}
            {environments.length === 0 && (
              <Text style={[styles.hint, { color: c.textMuted }]}>{t('provisioning.noEnvironments')}</Text>
            )}
          </View>

          <Text style={[styles.hint, { color: c.textMuted }]}>
            {deviceConfig
              ? t('provisioning.registered', { id: deviceConfig.sensorId })
              : t('provisioning.notRegistered')}
          </Text>
        </View>

        <View style={[styles.card, { backgroundColor: c.bgCard, borderColor: vm.phase === 'error' ? c.error : c.borderCard }]}>
          <View style={styles.statusRow}>
            <View
              style={[
                styles.statusDot,
                { backgroundColor: success ? c.success : vm.phase === 'error' ? c.error : c.accent },
              ]}
            />
            <Text
              style={[
                styles.statusText,
                { color: success ? c.success : vm.phase === 'error' ? c.error : c.textPrimary },
              ]}
            >
              {t(statusKey, { defaultValue: statusKey })}
            </Text>
            {vm.isBusy && <ActivityIndicator size="small" color={c.accent} style={{ marginLeft: 'auto' }} />}
          </View>

          {/* Un fallo de BLE sin explicacion se parece demasiado a "esta cargando":
              aqui se dice que se puede volver a intentar. */}
          {vm.phase === 'error' && (
            <Text style={[styles.hint, { color: c.error }]}>
              {t('provisioning.errors.retryHint')}
            </Text>
          )}

          <Button
            onPress={handleScan}
            disabled={vm.isBusy || vm.phase === 'ready' || success}
            loading={vm.phase === 'scanning' || vm.phase === 'connecting'}
            icon={<Ionicons name="bluetooth-outline" size={16} color="#fff" />}
            iconPosition="left"
          >
            {t('provisioning.scanBtn')}
          </Button>

          <View style={{ height: 10 }} />

          <Input
            label={t('provisioning.ssidLabel')}
            value={vm.ssid}
            onChangeText={vm.setSsid}
            placeholder={t('provisioning.ssidPlaceholder')}
            autoCapitalize="none"
            editable={!success}
          />
          <Input
            label={t('provisioning.passwordLabel')}
            value={vm.password}
            onChangeText={vm.setPassword}
            placeholder={t('provisioning.passwordPlaceholder')}
            secureTextEntry
            autoCapitalize="none"
            editable={!success}
          />

          <Button
            variant={canSend ? 'primary' : 'outline'}
            onPress={handleSend}
            disabled={!canSend}
            loading={saving || vm.phase === 'sending' || vm.phase === 'waiting'}
            icon={<Ionicons name="send-outline" size={16} color={canSend ? '#fff' : c.textMuted} />}
            iconPosition="left"
          >
            {t('provisioning.sendBtn')}
          </Button>

          <Text style={[styles.hint, { color: c.textMuted }]}>{t('provisioning.hint')}</Text>
        </View>

        {success && (
          <View style={[styles.card, { backgroundColor: `${c.success}14`, borderColor: c.success }]}>
            <View style={styles.statusRow}>
              <Ionicons name="checkmark-circle" size={20} color={c.success} />
              <Text style={[styles.statusText, { color: c.success, marginLeft: 8 }]}>
                {t('provisioning.successTitle')}
              </Text>
            </View>
            <Text style={[styles.successText, { color: c.textSecondary }]}>{t('provisioning.successBody')}</Text>
            <TouchableOpacity style={styles.linkBtn} onPress={() => navigation.navigate('ManagementHome')}>
              <Text style={[styles.linkTxt, { color: c.accent }]}>{t('provisioning.backToDevices')}</Text>
              <Ionicons name="chevron-forward" size={14} color={c.accent} />
            </TouchableOpacity>
          </View>
        )}

        <View style={[styles.logCard, { backgroundColor: c.bgCardAlt || c.bgCard, borderColor: c.borderColor }]}>
          <View style={styles.logHeader}>
            <Ionicons name="terminal-outline" size={14} color={c.textMuted} />
            <Text style={[styles.logTitle, { color: c.textMuted }]}>{t('provisioning.logTitle')}</Text>
            {vm.log.length > 0 && (
              <TouchableOpacity onPress={vm.reset}>
                <Text style={[styles.logClear, { color: c.accent }]}>{t('provisioning.clear')}</Text>
              </TouchableOpacity>
            )}
          </View>
          {vm.log.length === 0 ? (
            <Text style={[styles.logEmpty, { color: c.textMuted }]}>{t('provisioning.logEmpty')}</Text>
          ) : (
            vm.log.map((entry, index) => (
              <View key={`${entry.at}-${index}`} style={styles.logLine}>
                <Text style={[styles.logIndex, { color: c.textMuted }]}>{String(index + 1).padStart(2, '0')}</Text>
                <Text style={[styles.logMsg, { color: entry.raw ? c.accent : c.textSecondary }]}>
                  {entry.raw || t(entry.key)}
                </Text>
              </View>
            ))
          )}
        </View>

        <View style={{ height: 24 }} />
      </ScrollView>
    </SafeAreaView>
  )
}
