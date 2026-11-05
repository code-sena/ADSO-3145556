import { useEffect, useState } from 'react'
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  SafeAreaView,
  StatusBar,
  TextInput,
  Alert,
  ActivityIndicator,
} from 'react-native'
import { Ionicons } from '@expo/vector-icons'
import { useTheme } from '../../../../context/ThemeContext.jsx'
import { useTranslation } from 'react-i18next'
import institutionService from '../../services/institutionService.js'
import { styles } from './SuperAdminScreen.styles.js'

const EMPTY_FORM = { code: '', name: '', type: '' }

export default function SuperAdminScreen({ navigation }) {
  const { darkMode, currentColors } = useTheme()
  const { t } = useTranslation()

  const [institutions, setInstitutions] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [form, setForm] = useState(EMPTY_FORM)
  const [saving, setSaving] = useState(false)

  const load = async () => {
    setLoading(true)
    try {
      const data = await institutionService.getAll()
      setInstitutions(data)
      setError('')
    } catch (err) {
      setError(err.message || t('superadmin.error', 'Error al cargar instituciones'))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
  }, [])

  const handleChange = (field, value) => setForm((prev) => ({ ...prev, [field]: value }))

  const handleCreate = async () => {
    if (!form.code.trim() || !form.name.trim()) {
      Alert.alert(
        t('superadmin.title', 'Instituciones'),
        t('superadmin.requiredFields', 'El código y nombre son obligatorios')
      )
      return
    }
    setSaving(true)
    setError('')
    try {
      await institutionService.create({
        code: form.code.trim().toUpperCase(),
        name: form.name.trim(),
        type: form.type.trim(),
      })
      setForm(EMPTY_FORM)
      await load()
    } catch (err) {
      setError(err.message || t('superadmin.createError', 'Error al crear institución'))
    } finally {
      setSaving(false)
    }
  }

  const handleToggleStatus = async (institution) => {
    try {
      await institutionService.update(institution.id, {
        status: institution.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE',
      })
      await load()
    } catch (err) {
      setError(err.message || t('superadmin.updateError', 'Error al actualizar institución'))
    }
  }

  return (
    <SafeAreaView style={[styles.safe, { backgroundColor: currentColors.bgBody }]}>
      <StatusBar barStyle={darkMode ? 'light-content' : 'dark-content'} backgroundColor={currentColors.bgBody} />

      <View
        style={[
          styles.header,
          {
            borderBottomColor: currentColors.borderColor,
            paddingTop: (StatusBar.currentHeight || 0) + 10,
          },
        ]}
      >
        <View style={styles.headerLeft}>
          <Ionicons name="business-outline" size={22} color={currentColors.accent} />
          <Text style={[styles.headerTitle, { color: currentColors.textPrimary }]}>
            {t('superadmin.title', 'Instituciones')}
          </Text>
        </View>
        <TouchableOpacity
          style={[styles.refreshBtn, { backgroundColor: currentColors.accent }]}
          onPress={load}
          activeOpacity={0.85}
        >
          <Ionicons name="refresh" size={16} color="#fff" />
        </TouchableOpacity>
      </View>

      <ScrollView style={styles.scroll} contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
        {error ? (
          <View style={[styles.alert, { backgroundColor: currentColors.errorDim, borderColor: currentColors.error }]}>
            <Ionicons name="alert-circle-outline" size={16} color={currentColors.error} />
            <Text style={[styles.alertText, { color: currentColors.error }]}>{error}</Text>
          </View>
        ) : null}

        <View style={[styles.card, { backgroundColor: currentColors.bgCard, borderColor: currentColors.borderColor }]}>
          <Text style={[styles.cardTitle, { color: currentColors.textPrimary }]}>
            {t('superadmin.createTitle', 'Nueva institución')}
          </Text>

          <View style={styles.formGroup}>
            <Text style={[styles.label, { color: currentColors.textSecondary }]}>
              {t('superadmin.code', 'Código')}
            </Text>
            <TextInput
              style={[styles.input, { color: currentColors.textPrimary, borderColor: currentColors.borderColor, backgroundColor: currentColors.bgInput }]}
              placeholder={t('superadmin.codePlaceholder', 'ej. SEN-444')}
              placeholderTextColor={currentColors.textMuted}
              value={form.code}
              onChangeText={(v) => handleChange('code', v.toUpperCase())}
              maxLength={20}
              autoCapitalize="characters"
            />
          </View>

          <View style={styles.formGroup}>
            <Text style={[styles.label, { color: currentColors.textSecondary }]}>
              {t('superadmin.name', 'Nombre')}
            </Text>
            <TextInput
              style={[styles.input, { color: currentColors.textPrimary, borderColor: currentColors.borderColor, backgroundColor: currentColors.bgInput }]}
              placeholder={t('superadmin.namePlaceholder', 'Nombre de la institución')}
              placeholderTextColor={currentColors.textMuted}
              value={form.name}
              onChangeText={(v) => handleChange('name', v)}
              maxLength={150}
            />
          </View>

          <View style={styles.formGroup}>
            <Text style={[styles.label, { color: currentColors.textSecondary }]}>
              {t('superadmin.type', 'Tipo')}
            </Text>
            <TextInput
              style={[styles.input, { color: currentColors.textPrimary, borderColor: currentColors.borderColor, backgroundColor: currentColors.bgInput }]}
              placeholder={t('superadmin.typePlaceholder', 'Tipo (opcional)')}
              placeholderTextColor={currentColors.textMuted}
              value={form.type}
              onChangeText={(v) => handleChange('type', v)}
              maxLength={50}
            />
          </View>

          <TouchableOpacity
            style={[styles.createBtn, { backgroundColor: currentColors.accent }]}
            onPress={handleCreate}
            disabled={saving}
            activeOpacity={0.85}
          >
            {saving ? (
              <ActivityIndicator size="small" color="#fff" />
            ) : (
              <>
                <Ionicons name="add" size={18} color="#fff" />
                <Text style={styles.createBtnText}>{t('superadmin.create', 'Crear')}</Text>
              </>
            )}
          </TouchableOpacity>
        </View>

        <View style={[styles.card, { backgroundColor: currentColors.bgCard, borderColor: currentColors.borderColor }]}>
          <Text style={[styles.cardTitle, { color: currentColors.textPrimary }]}>
            {t('superadmin.listTitle', 'Registradas')} ({institutions.length})
          </Text>

          {loading ? (
            <View style={styles.loadingContainer}>
              <ActivityIndicator size="large" color={currentColors.accent} />
              <Text style={[styles.mutedText, { color: currentColors.textMuted }]}>
                {t('common.loading', 'Cargando...')}
              </Text>
            </View>
          ) : institutions.length === 0 ? (
            <View style={styles.emptyContainer}>
              <Ionicons name="business-outline" size={40} color={currentColors.textMuted} />
              <Text style={[styles.mutedText, { color: currentColors.textMuted }]}>
                {t('superadmin.empty', 'Aún no hay instituciones.')}
              </Text>
            </View>
          ) : (
            institutions.map((inst) => (
              <View
                key={inst.id}
                style={[styles.institutionRow, { borderColor: currentColors.borderColor }]}
              >
                <View style={styles.institutionInfo}>
                  <Text style={[styles.institutionCode, { color: currentColors.textPrimary }]}>
                    {inst.code}
                  </Text>
                  <Text style={[styles.institutionName, { color: currentColors.textSecondary }]} numberOfLines={1}>
                    {inst.name}
                  </Text>
                  {inst.type ? (
                    <Text style={[styles.institutionType, { color: currentColors.textMuted }]} numberOfLines={1}>
                      {inst.type}
                    </Text>
                  ) : null}
                </View>
                <View style={styles.institutionActions}>
                  <View
                    style={[
                      styles.statusBadge,
                      {
                        backgroundColor: inst.status === 'ACTIVE' ? currentColors.successDim : currentColors.errorDim,
                        borderColor: inst.status === 'ACTIVE' ? currentColors.success : currentColors.error,
                      },
                    ]}
                  >
                    <Text
                      style={[
                        styles.statusText,
                        { color: inst.status === 'ACTIVE' ? currentColors.success : currentColors.error },
                      ]}
                    >
                      {inst.status}
                    </Text>
                  </View>
                  <TouchableOpacity
                    style={[
                      styles.toggleBtn,
                      { borderColor: currentColors.borderColor },
                    ]}
                    onPress={() => handleToggleStatus(inst)}
                    activeOpacity={0.7}
                  >
                    <Ionicons
                      name={inst.status === 'ACTIVE' ? 'pause-outline' : 'play-outline'}
                      size={14}
                      color={inst.status === 'ACTIVE' ? currentColors.error : currentColors.success}
                    />
                    <Text
                      style={[
                        styles.toggleBtnText,
                        { color: inst.status === 'ACTIVE' ? currentColors.error : currentColors.success },
                      ]}
                    >
                      {inst.status === 'ACTIVE'
                        ? t('superadmin.deactivate', 'Desactivar')
                        : t('superadmin.activate', 'Activar')}
                    </Text>
                  </TouchableOpacity>
                </View>
              </View>
            ))
          )}
        </View>

        <View style={{ height: 24 }} />
      </ScrollView>
    </SafeAreaView>
  )
}
