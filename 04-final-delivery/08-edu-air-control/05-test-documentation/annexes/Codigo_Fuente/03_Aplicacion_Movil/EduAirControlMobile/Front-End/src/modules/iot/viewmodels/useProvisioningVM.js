/**
 * ViewModel: useProvisioningVM
 * Aprovisionamiento Wi-Fi del ESP32 por Bluetooth LE (ver
 * guia_configuracion_esp32_react_native_ble.md): escaneo filtrado por
 * SERVICE_UUID, escritura de {ssid, pass} en Base64 y escucha de
 * STATUS:CONNECTED | STATUS:FAIL | STATUS:ERR_JSON.
 */

import { useCallback, useEffect, useRef, useState } from 'react'
import {
  ensureBlePermissions,
  scanAndConnect,
  subscribeStatus,
  sendCredentials,
  disconnect,
  destroyBleManager,
  toBleErrorKey,
} from '../ble/bleProvisioning'

const WIFI_TIMEOUT_MS = 45000

export function useProvisioningVM() {
  const [phase, setPhase] = useState('idle')
  const [log, setLog] = useState([])
  const [errorKey, setErrorKey] = useState(null)
  const [ssid, setSsid] = useState('')
  const [password, setPassword] = useState('')
  const [bleDevice, setBleDevice] = useState(null)

  const subscriptionRef = useRef(null)
  const statusResolverRef = useRef(null)
  const deviceRef = useRef(null)

  const pushLog = useCallback((key, raw) => {
    setLog((current) => [...current, { key, raw, at: Date.now() }])
  }, [])

  const cleanup = useCallback(async () => {
    if (subscriptionRef.current) {
      subscriptionRef.current.remove?.()
      subscriptionRef.current = null
    }
    if (deviceRef.current) {
      await disconnect(deviceRef.current)
      deviceRef.current = null
    }
    setBleDevice(null)
  }, [])

  useEffect(() => {
    return () => {
      if (subscriptionRef.current) subscriptionRef.current.remove?.()
      if (deviceRef.current) disconnect(deviceRef.current)
      destroyBleManager()
    }
  }, [])

  const startScan = useCallback(async () => {
    setErrorKey(null)
    setPhase('scanning')
    pushLog('provisioning.log.permissions')

    try {
      await ensureBlePermissions()
    } catch (e) {
      setPhase('error')
      setErrorKey(toBleErrorKey(e))
      pushLog('provisioning.log.error', e?.message)
      return null
    }

    pushLog('provisioning.log.scanning')
    try {
      const device = await scanAndConnect({
        onStatus: (status) => {
          if (status === 'connecting') {
            setPhase('connecting')
            pushLog('provisioning.log.connecting')
          }
        },
      })

      deviceRef.current = device
      setBleDevice(device)

      subscriptionRef.current = subscribeStatus(
        device,
        (message) => {
          pushLog('provisioning.log.status', message)
          const resolver = statusResolverRef.current
          if (resolver) {
            statusResolverRef.current = null
            resolver(message)
          }
        },
        () => {}
      )

      setPhase('ready')
      pushLog('provisioning.log.ready')
      return device
    } catch (e) {
      // El codigo del error se deja en el log: sin el, un fallo de BLE es un
      // "algo falló" imposible de diagnosticar desde el movil.
      setPhase('error')
      setErrorKey(toBleErrorKey(e))
      pushLog('provisioning.log.error', e?.message)
      return null
    }
  }, [pushLog])

  /**
   * Envía las credenciales al nodo y espera su respuesta.
   *
   * @param {{token: string, installationId: string}} deviceConfig credencial del
   *   dispositivo e id de instalación; van en el mismo payload BLE que el Wi-Fi.
   */
  const submit = useCallback(async (deviceConfig = {}) => {
    const device = deviceRef.current
    if (!device) {
      setErrorKey('devices.errors.notConnected')
      setPhase('error')
      return null
    }
    if (!ssid.trim()) {
      setErrorKey('provisioning.errors.ssid')
      return null
    }

    setErrorKey(null)
    setPhase('sending')
    pushLog('provisioning.log.sending')

    const resultPromise = new Promise((resolve) => {
      statusResolverRef.current = resolve
    })

    try {
      await sendCredentials(
        device,
        ssid.trim(),
        password,
        deviceConfig.token,
        deviceConfig.installationId
      )
    } catch {
      statusResolverRef.current = null
      setPhase('error')
      setErrorKey('devices.errors.generic')
      pushLog('provisioning.log.error')
      return null
    }

    setPhase('waiting')
    pushLog('provisioning.log.waiting')

    const timeout = new Promise((resolve) =>
      setTimeout(() => resolve('STATUS:TIMEOUT'), WIFI_TIMEOUT_MS)
    )

    const status = await Promise.race([resultPromise, timeout])

    if (status === 'STATUS:CONNECTED') {
      setPhase('success')
      pushLog('provisioning.log.connected')
      await cleanup()
      return 'connected'
    }

    if (status === 'STATUS:FAIL') {
      setPhase('error')
      setErrorKey('devices.errors.wifiFail')
      pushLog('provisioning.log.fail')
      return 'fail'
    }

    if (status === 'STATUS:ERR_JSON') {
      setPhase('error')
      setErrorKey('devices.errors.badPayload')
      pushLog('provisioning.log.errJson')
      return 'error'
    }

    setPhase('error')
    setErrorKey('devices.errors.timeout')
    pushLog('provisioning.log.error')
    return 'timeout'
  }, [ssid, password, pushLog, cleanup])

  const reset = useCallback(async () => {
    await cleanup()
    setPhase('idle')
    setErrorKey(null)
    setLog([])
    setPassword('')
  }, [cleanup])

  return {
    // estado
    phase,
    log,
    errorKey,
    ssid,
    password,
    bleDevice,
    isBusy: phase === 'scanning' || phase === 'connecting' || phase === 'sending' || phase === 'waiting',
    // acciones
    setSsid,
    setPassword,
    startScan,
    submit,
    reset,
    cleanup,
  }
}

export default useProvisioningVM
