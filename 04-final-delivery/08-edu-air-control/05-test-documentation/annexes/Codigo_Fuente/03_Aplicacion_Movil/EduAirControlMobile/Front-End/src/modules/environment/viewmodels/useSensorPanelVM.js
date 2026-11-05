/**
 * ViewModel: useSensorPanelVM
 * Sensores y variables (pestaña "Sensores y variables" del panel admin).
 * CRUD sobre /api/v1/sensors (backend real) + filtros de variable y estado.
 */

import { useState, useMemo, useEffect, useCallback } from 'react'
import { useTranslation } from 'react-i18next'
import { useEnvironments } from '../../../context/EnvironmentContext'
import sensorService, { ENV_FIELD_BY_VARIABLE, VARIABLE_META } from '../services/sensorService'

const EMPTY_FORM = { sensorId: '', environmentId: '', type: 'temperature', min: '', max: '' }

export function useSensorPanelVM() {
  const { t } = useTranslation()
  const { environments } = useEnvironments()

  const [sensors, setSensors] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [search, setSearch] = useState('')
  const [statusFilter, setStatusFilter] = useState('all')
  const [variableFilter, setVariableFilter] = useState('all')
  const [showAdd, setShowAdd] = useState(false)
  const [editing, setEditing] = useState(null)
  const [form, setForm] = useState(EMPTY_FORM)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await sensorService.getAll()
      setSensors(Array.isArray(data) ? data : [])
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    load()
  }, [load])

  const getEnvironmentName = useCallback(
    (id) => {
      const env = environments.find((item) => String(item.id) === String(id))
      if (!env) return t('sensors.noEnvironment')
      return env.nameKey ? t(env.nameKey) : env.name || t('sensors.noEnvironment')
    },
    [environments, t]
  )

  const getReading = useCallback(
    (sensor) => {
      const env = environments.find((item) => String(item.id) === String(sensor.environmentId))
      if (!env) return null
      const field = ENV_FIELD_BY_VARIABLE[sensor.type] || sensor.type
      const value = env[field]
      return value ?? null
    },
    [environments]
  )

  const filtered = useMemo(() => {
    const q = search.trim().toLowerCase()
    return sensors.filter((sensor) => {
      const meta = VARIABLE_META[sensor.type]
      const haystack = `${sensor.sensorId} ${getEnvironmentName(sensor.environmentId)} ${t(meta?.labelKey || '')}`.toLowerCase()
      const matchesSearch = !q || haystack.includes(q)
      return (
        matchesSearch &&
        (statusFilter === 'all' || sensor.status === statusFilter) &&
        (variableFilter === 'all' || sensor.type === variableFilter)
      )
    })
  }, [sensors, search, statusFilter, variableFilter, getEnvironmentName, t])

  const summary = useMemo(
    () => ({
      total: sensors.length,
      active: sensors.filter((s) => s.status === 'active').length,
      warning: sensors.filter((s) => s.status === 'warning').length,
      offline: sensors.filter((s) => s.status === 'offline').length,
    }),
    [sensors]
  )

  const addSensor = async (payload) => {
    const meta = VARIABLE_META[payload.type] || {}
    await sensorService.create({
      sensorId: payload.sensorId.trim(),
      environmentId: payload.environmentId,
      type: payload.type,
      min: payload.min !== '' && payload.min != null ? Number(payload.min) : meta.defaultMin,
      max: payload.max !== '' && payload.max != null ? Number(payload.max) : meta.defaultMax,
    })
    setShowAdd(false)
    setForm(EMPTY_FORM)
    await load()
  }

  const saveSensor = async (payload) => {
    await sensorService.update({
      ...payload,
      type: payload.type,
      min: Number(payload.min),
      max: Number(payload.max),
    })
    setEditing(null)
    await load()
  }

  const toggleSensor = async (sensor) => {
    await sensorService.toggleStatus(sensor)
    await load()
  }

  const resetFilters = () => {
    setSearch('')
    setStatusFilter('all')
    setVariableFilter('all')
  }

  return {
    // data
    sensors,
    filtered,
    summary,
    environments,
    loading,
    error,
    // filters
    search,
    statusFilter,
    variableFilter,
    setSearch,
    setStatusFilter,
    setVariableFilter,
    resetFilters,
    // modals
    showAdd,
    setShowAdd,
    editing,
    setEditing,
    form,
    setForm,
    // actions
    addSensor,
    saveSensor,
    toggleSensor,
    reload: load,
    getEnvironmentName,
    getReading,
  }
}

export default useSensorPanelVM
