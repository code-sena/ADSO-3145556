import { useState, useEffect } from 'react'
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  SafeAreaView,
  StatusBar,
  StyleSheet,
  TextInput,
  Alert,
} from 'react-native'
import { Ionicons } from '@expo/vector-icons'
import { useTheme } from '../../../../context/ThemeContext.jsx'
import { useTranslation } from 'react-i18next'
import { setAppLanguage } from '../../../../shared/i18n/i18n.js'
import storage from '../../../../shared/storage/storage.js'
import {
  getAccessibilitySettings,
  saveAccessibilitySettings,
} from '../../../../shared/accessibility/accessibilitySettings.js'
import Modal from '../../../../shared/components/Modal/Modal.jsx'
import Button from '../../../../shared/components/Button/Button.jsx'
import { styles } from './SettingsScreen.styles'

const TIMEZONES = [
  { value: 'America/Bogota', labelKey: 'settings.timezoneBogota' },
  { value: 'America/Lima', labelKey: 'settings.timezoneLima' },
  { value: 'America/Mexico_City', labelKey: 'settings.timezoneMexicoCity' },
  { value: 'America/New_York', labelKey: 'settings.timezoneNewYork' },
  { value: 'America/Los_Angeles', labelKey: 'settings.timezoneLosAngeles' },
  { value: 'Europe/London', labelKey: 'settings.timezoneLondon' },
  { value: 'Europe/Madrid', labelKey: 'settings.timezoneMadrid' },
  { value: 'Europe/Paris', labelKey: 'settings.timezoneParis' },
  { value: 'Asia/Tokyo', labelKey: 'settings.timezoneTokyo' },
  { value: 'Australia/Sydney', labelKey: 'settings.timezoneSydney' },
]

const LANGUAGES = [
  { code: 'es', name: 'Español', flag: '🇪🇸' },
  { code: 'en', name: 'English', flag: '🇺🇸' },
  { code: 'fr', name: 'Français', flag: '🇫🇷' },
  { code: 'pt', name: 'Português', flag: '🇵🇹' },
]

const COLOR_THEMES = [
  { key: '', label: 'settings.themeNormal' },
  { key: 'theme-protanopia', label: 'settings.themeProtanopia' },
  { key: 'theme-deuteranopia', label: 'settings.themeDeuteranopia' },
  { key: 'theme-tritanopia', label: 'settings.themeTritanopia' },
]

const DATE_FORMATS = [
  { value: 'DD/MM/YYYY', label: 'DD/MM/YYYY', sample: '06/10/2026' },
  { value: 'MM/DD/YYYY', label: 'MM/DD/YYYY', sample: '10/06/2026' },
  { value: 'YYYY-MM-DD', label: 'YYYY-MM-DD', sample: '2026-10-06' },
  { value: 'DD-MM-YYYY', label: 'DD-MM-YYYY', sample: '06-10-2026' },
  { value: 'DD.MM.YYYY', label: 'DD.MM.YYYY', sample: '06.10.2026' },
  { value: 'YYYY/MM/DD', label: 'YYYY/MM/DD', sample: '2026/10/06' },
]

function Toggle({ value, onValueChange, currentColors }) {
  return (
    <TouchableOpacity
      style={[styles.toggle, { backgroundColor: value ? currentColors.accent : currentColors.borderColor }]}
      onPress={() => onValueChange(!value)}
      activeOpacity={0.8}
    >
      <View style={[styles.toggleCircle, value && { transform: [{ translateX: 20 }] }]} />
    </TouchableOpacity>
  )
}

function SectionCard({ icon, title, children, currentColors }) {
  return (
    <View style={[styles.card, { backgroundColor: currentColors.bgCard, borderColor: currentColors.borderColor }]}>
      <View style={styles.cardHeader}>
        <Ionicons name={icon} size={18} color={currentColors.accent} />
        <Text style={[styles.cardTitle, { color: currentColors.textPrimary }]}>{title}</Text>
      </View>
      {children}
    </View>
  )
}

function Row({ icon, color, label, value, onPress, right, currentColors }) {
  return (
    <TouchableOpacity style={styles.row} onPress={onPress} disabled={!onPress} activeOpacity={onPress ? 0.7 : 1}>
      <View style={styles.rowLeft}>
        <Ionicons name={icon} size={16} color={color || currentColors.textMuted} />
        <Text style={[styles.rowLabel, { color: color ? currentColors[color] || color : currentColors.textPrimary }]}>
          {label}
        </Text>
      </View>
      {value ? <Text style={[styles.rowValue, { color: currentColors.textMuted }]} numberOfLines={1}>{value}</Text> : null}
      {right || (onPress ? <Ionicons name="chevron-forward" size={16} color={currentColors.textMuted} /> : null)}
    </TouchableOpacity>
  )
}

export default function SettingsScreen({ navigation }) {
  const { darkMode, toggleDarkMode, currentColors } = useTheme()
  const { t, i18n } = useTranslation()

  const [colorTheme, setColorTheme] = useState('')
  const [autoTimezone, setAutoTimezone] = useState(true)
  const [manualTimezone, setManualTimezone] = useState('America/Bogota')
  const [dateFormat, setDateFormat] = useState('DD-MM-YYYY')
  const [dateFormatDraft, setDateFormatDraft] = useState('DD-MM-YYYY')
  const [reminders, setReminders] = useState({ alerts: true, warnings: true, daily: false, sound: true })
  // El valor se persiste y se restaura pero la UI no lo lee todavia.
  const [, setPrivacy] = useState({ visible: false })
  const [showLangModal, setShowLangModal] = useState(false)
  const [showDateFormatModal, setShowDateFormatModal] = useState(false)
  const [showPasswordModal, setShowPasswordModal] = useState(false)
  const [showDeleteModal, setShowDeleteModal] = useState(false)
  const [deleteSent, setDeleteSent] = useState(false)
  const [deletePassword, setDeletePassword] = useState('')
  const [showHelpModal, setShowHelpModal] = useState({ open: false, type: null })
  const [passwordData, setPasswordData] = useState({ current: '', new: '', confirm: '' })
  const [showPassword, setShowPassword] = useState({ new: false, confirm: false })

  useEffect(() => {
    setColorTheme(getAccessibilitySettings().colorTheme || '')
    ;['autoTimezone', 'manualTimezone', 'settings', 'reminders', 'privacy'].forEach((key) => {
      const raw = storage.getItem(key)
      if (raw != null) {
        try {
          if (key === 'autoTimezone') setAutoTimezone(JSON.parse(raw))
          else if (key === 'manualTimezone') setManualTimezone(raw)
          else if (key === 'settings') setDateFormat(JSON.parse(raw).dateFormat || 'DD-MM-YYYY')
          else if (key === 'reminders') setReminders(JSON.parse(raw))
          else if (key === 'privacy') setPrivacy(JSON.parse(raw))
        } catch (e) {
          console.warn('Error loading', key, e)
        }
      }
    })
  }, [])

  const persist = async (key, value) => {
    try {
      await storage.setItem(key, typeof value === 'string' ? value : JSON.stringify(value))
    } catch (e) {
      console.warn('Error saving', key, e)
    }
  }

  const handleThemeChange = async (key) => {
    setColorTheme(key)
    const a11y = getAccessibilitySettings()
    await saveAccessibilitySettings({ ...a11y, colorTheme: key })
  }

  const handleChangeLanguage = async (code) => {
    await setAppLanguage(code)
    setShowLangModal(false)
  }

  const handleOpenDateFormatModal = () => {
    setDateFormatDraft(dateFormat)
    setShowDateFormatModal(true)
  }

  const handleCloseDateFormatModal = () => {
    setDateFormatDraft(dateFormat)
    setShowDateFormatModal(false)
  }

  const handleDateFormatSave = () => {
    setDateFormat(dateFormatDraft)
    persist('settings', { dateFormat: dateFormatDraft })
    setShowDateFormatModal(false)
  }

  const handleSavePassword = () => {
    const pm = t('settings.passwordModal', { returnObjects: true })
    if (!passwordData.current || !passwordData.new || !passwordData.confirm) {
      return Alert.alert('', pm.errorEmpty)
    }
    if (passwordData.new !== passwordData.confirm) {
      return Alert.alert('', pm.errorMatch)
    }
    if (passwordData.new.length < 6) {
      return Alert.alert('', pm.errorLength)
    }
    Alert.alert('', pm.success)
    setShowPasswordModal(false)
    setPasswordData({ current: '', new: '', confirm: '' })
  }

  const handleDeleteAccountConfirm = () => {
    if (!deletePassword.trim()) {
      return Alert.alert('', 'Ingresa tu contraseña para confirmar la eliminación de la cuenta.')
    }

    setDeleteSent(true)
    setDeletePassword('')
  }

  const handleCloseDeleteModal = () => {
    setDeleteSent(false)
    setDeletePassword('')
    setShowDeleteModal(false)
  }

  const themeLabel = (key) => {
    const entry = COLOR_THEMES.find((c) => c.key === key)
    return entry ? t(entry.label) : t('settings.themeNormal')
  }

  const renderHelpContent = () => {
    switch (showHelpModal.type) {
      case 'faq':
        return (
          <View style={styles.helpFaqList}>
            {[
              { q: t('settings.faq.q1'), a: t('settings.faq.a1') },
              { q: t('settings.faq.q2'), a: t('settings.faq.a2') },
              { q: t('settings.faq.q3'), a: t('settings.faq.a3') },
              { q: t('settings.faq.q4'), a: t('settings.faq.a4') },
            ].map((item, idx) => (
              <View
                key={idx}
                style={[
                  styles.helpFaqCard,
                  {
                    backgroundColor: currentColors.bgBody,
                    borderColor: currentColors.borderColor,
                  },
                ]}
              >
                <Text style={[styles.helpQuestion, { color: currentColors.textPrimary }]}>{item.q}</Text>
                <Text style={[styles.helpAnswer, { color: currentColors.textSecondary }]}>{item.a}</Text>
              </View>
            ))}
          </View>
        )
      case 'contact':
        return (
          <View style={styles.helpContactList}>
            {[
              { icon: '✉️', label: t('settings.contact.emailTitle'), value: t('settings.contact.emailDesc') },
              { icon: '🕐', label: t('settings.contact.scheduleTitle'), value: t('settings.contact.scheduleDesc') },
              { icon: '⏱️', label: t('settings.contact.responseTitle'), value: t('settings.contact.responseDesc') },
            ].map((row, idx) => (
              <View
                key={idx}
                style={[
                  styles.helpContactCard,
                  {
                    backgroundColor: currentColors.bgBody,
                    borderColor: currentColors.borderColor,
                  },
                ]}
              >
                <View style={[styles.helpContactIcon, { backgroundColor: currentColors.accentDim, borderColor: currentColors.accent }]}>
                  <Text style={styles.helpContactIconText}>{row.icon}</Text>
                </View>
                <View style={styles.helpContactText}>
                  <Text style={[styles.helpLabel, { color: currentColors.textPrimary }]}>{row.label}</Text>
                  <Text style={[styles.helpValue, { color: currentColors.textSecondary }]}>{row.value}</Text>
                </View>
              </View>
            ))}
          </View>
        )
      case 'terms':
        return (
          <>
            <Text style={[styles.helpText, { color: currentColors.textSecondary }]}>{t('settings.terms.intro')}</Text>
            {t('settings.terms.sections', { returnObjects: true }).map((sec, idx) => (
              <View key={idx} style={styles.helpSection}>
                <Text style={[styles.helpSectionTitle, { color: currentColors.textPrimary }]}>{sec.title}</Text>
                {sec.items.map((item, i) => (
                  <Text key={i} style={[styles.helpListItem, { color: currentColors.textSecondary }]}>- {item}</Text>
                ))}
              </View>
            ))}
            <Text style={[styles.helpText, { color: currentColors.textSecondary }]}>{t('settings.terms.footer')}</Text>
          </>
        )
      case 'privacy':
        return (
          <>
            <Text style={[styles.helpText, { color: currentColors.textSecondary }]}>
              En EduAirControl nos tomamos en serio la protección de tu información personal. Esta política explica qué datos recopilamos y cómo los cuidamos:
            </Text>

            <View style={styles.helpSection}>
              <Text style={[styles.helpSectionTitle, { color: currentColors.textPrimary }]}>Qué información recopilamos</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Datos de cuenta: nombre, correo electrónico y credenciales de acceso.</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Datos ambientales generados por los sensores de tu institución (calidad del aire, temperatura, humedad, etc.).</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Datos de uso: preferencias, idioma, zona horaria y configuración de la aplicación.</Text>
            </View>

            <View style={styles.helpSection}>
              <Text style={[styles.helpSectionTitle, { color: currentColors.textPrimary }]}>Cómo usamos tu información</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Para brindarte acceso al panel, los rankings y las notificaciones de la plataforma.</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Para generar reportes e informes ambientales de tu institución.</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Para mejorar el servicio y comunicarnos contigo sobre actualizaciones importantes.</Text>
            </View>

            <View style={styles.helpSection}>
              <Text style={[styles.helpSectionTitle, { color: currentColors.textPrimary }]}>Cómo protegemos tus datos</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Tus datos se transmiten y almacenan de forma cifrada.</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Las contraseñas nunca se guardan en texto plano; se protegen mediante algoritmos de cifrado seguros.</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• El acceso a la información está restringido solo al personal autorizado que lo necesita para operar la plataforma.</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Revisamos periódicamente nuestras prácticas de seguridad para prevenir accesos no autorizados.</Text>
            </View>

            <View style={styles.helpSection}>
              <Text style={[styles.helpSectionTitle, { color: currentColors.textPrimary }]}>Con quién compartimos tu información</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• No vendemos ni alquilamos tus datos personales a terceros.</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Solo compartimos información con los administradores de tu propia institución, o con proveedores estrictamente necesarios para operar el servicio (por ejemplo, hosting).</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Podemos divulgar información si así lo exige la ley.</Text>
            </View>

            <View style={styles.helpSection}>
              <Text style={[styles.helpSectionTitle, { color: currentColors.textPrimary }]}>Tus derechos</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Puedes acceder, corregir o actualizar tus datos personales desde tu perfil.</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Puedes solicitar la eliminación de tu cuenta y de tus datos en cualquier momento desde esta sección de Configuración.</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Puedes solicitar una copia de la información que tenemos sobre ti escribiéndonos a soporte.</Text>
            </View>

            <View style={styles.helpSection}>
              <Text style={[styles.helpSectionTitle, { color: currentColors.textPrimary }]}>Retención de datos</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Conservamos tu información mientras tu cuenta esté activa.</Text>
              <Text style={[styles.helpListItem, { color: currentColors.textSecondary }]}>• Al solicitar la eliminación de tu cuenta, tus datos personales se eliminan o anonimizan dentro de un plazo razonable, salvo que la ley exija conservarlos.</Text>
            </View>

            <Text style={[styles.helpText, { color: currentColors.textSecondary }]}>
              ¿Tienes preguntas sobre cómo tratamos tus datos? Escríbenos a soporte@eduaircontrol.com.
            </Text>
          </>
        )
      case 'version':
        return (
          <View style={styles.versionModalWrap}>
            <View style={[styles.versionBadge, { backgroundColor: currentColors.accentDim, borderColor: currentColors.accent }]}>
              <Text style={[styles.versionBadgeText, { color: currentColors.accent }]}>v1.0</Text>
            </View>

            <Text style={[styles.versionAppName, { color: currentColors.textPrimary }]}>
              Sistema de Monitoreo de Calidad del Aire
            </Text>

            <Text style={[styles.versionDate, { color: currentColors.textSecondary }]}>
              Última actualización: Abril 2026
            </Text>

            <View style={styles.versionChipsRow}>
              {[
                'React 18',
                'i18n',
                darkMode ? 'Modo oscuro' : 'Modo claro',
              ].map((chip) => (
                <View key={chip} style={[styles.versionChip, { backgroundColor: currentColors.accentDim, borderColor: currentColors.accent }]}>
                  <Text style={[styles.versionChipText, { color: currentColors.accent }]}>{chip}</Text>
                </View>
              ))}
            </View>
          </View>
        )
      default:
        return null
    }
  }

  const currentLanguage = LANGUAGES.find((l) => l.code === i18n.language) || LANGUAGES[0]

  return (
    <SafeAreaView style={[styles.safe, { backgroundColor: currentColors.bgBody }]}>
      <StatusBar barStyle={darkMode ? 'light-content' : 'dark-content'} backgroundColor={currentColors.bgBody} />

      <View style={[styles.header, { backgroundColor: currentColors.bgCard, borderBottomColor: currentColors.borderColor }]}>
        <Ionicons name="settings-outline" size={30} color={currentColors.accent} />
        <Text style={[styles.headerTitle, { color: currentColors.textPrimary }]}>{t('settings.title')}</Text>
      </View>

      <ScrollView style={styles.scroll} contentContainerStyle={styles.scrollContent} showsVerticalScrollIndicator={false}>
        <SectionCard icon="color-palette-outline" title={t('settings.appearance')} currentColors={currentColors}>
          <Row
            icon={darkMode ? 'moon' : 'moon-outline'}
            label={t('settings.darkMode')}
            currentColors={currentColors}
            right={<Toggle value={darkMode} onValueChange={toggleDarkMode} currentColors={currentColors} />}
          />
          <Row icon="eye-outline" label={t('settings.accessibleThemes')} value={themeLabel(colorTheme)} currentColors={currentColors} color={colorTheme === '' ? null : currentColors.accent} />
          <View style={styles.chipWrap}>
            {COLOR_THEMES.map((c) => (
              <TouchableOpacity
                key={c.key}
                style={[
                  styles.chip,
                  { backgroundColor: currentColors.bgBody, borderColor: colorTheme === c.key ? currentColors.accent : currentColors.borderColor },
                  colorTheme === c.key && { backgroundColor: currentColors.accentDim },
                ]}
                onPress={() => handleThemeChange(c.key)}
              >
                <Text style={[styles.chipLabel, { color: colorTheme === c.key ? currentColors.accent : currentColors.textSecondary }]}>
                  {t(c.label)}
                </Text>
              </TouchableOpacity>
            ))}
          </View>
        </SectionCard>

        <SectionCard icon="globe-outline" title={t('settings.langAndDates')} currentColors={currentColors}>
          <Text style={[styles.cardDesc, { color: currentColors.textMuted }]}>{t('settings.langDescription')}</Text>
          <Row
            icon="language-outline"
            label={t('settings.language')}
            value={currentLanguage.name}
            currentColors={currentColors}
            onPress={() => setShowLangModal(true)}
          />
          <Row
            icon="calendar-outline"
            label={t('settings.dateFormat')}
            value={dateFormat}
            currentColors={currentColors}
            onPress={handleOpenDateFormatModal}
          />
          <Row
            icon="time-outline"
            label={t('settings.autoTimezone')}
            currentColors={currentColors}
            right={<Toggle value={autoTimezone} onValueChange={(v) => { setAutoTimezone(v); persist('autoTimezone', v) }} currentColors={currentColors} />}
          />
          {!autoTimezone && (
            <View style={styles.timezoneWrap}>
              <Text style={[styles.timezoneLabel, { color: currentColors.textSecondary }]}>{t('settings.selectTimezone')}</Text>
              <View style={styles.chipWrap}>
                {TIMEZONES.map((tz) => (
                  <TouchableOpacity
                    key={tz.value}
                    style={[styles.chip, { backgroundColor: currentColors.bgBody, borderColor: manualTimezone === tz.value ? currentColors.accent : currentColors.borderColor }]}
                    onPress={() => { setManualTimezone(tz.value); persist('manualTimezone', tz.value) }}
                  >
                    <Text style={[styles.chipLabel, { color: manualTimezone === tz.value ? currentColors.accent : currentColors.textSecondary }]}>
                      {t(tz.labelKey)}
                    </Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>
          )}
        </SectionCard>

        <SectionCard icon="notifications-outline" title={t('settings.reminders')} currentColors={currentColors}>
          <Text style={[styles.cardDesc, { color: currentColors.textMuted }]}>{t('settings.remindersDescription')}</Text>
          {[
            { key: 'alerts', icon: 'alert-circle-outline', label: t('settings.reminderAlerts'), value: reminders.alerts },
            { key: 'warnings', icon: 'warning-outline', label: t('settings.reminderWarnings'), value: reminders.warnings },
            { key: 'daily', icon: 'calendar-outline', label: t('settings.reminderDaily'), value: reminders.daily },
            { key: 'sound', icon: 'volume-high-outline', label: t('settings.reminderSound'), value: reminders.sound },
          ].map((item) => (
            <Row
              key={item.key}
              icon={item.icon}
              label={item.label}
              currentColors={currentColors}
              right={<Toggle value={item.value} onValueChange={(v) => { setReminders((prev) => { const next = { ...prev, [item.key]: v }; persist('reminders', next); return next }) }} currentColors={currentColors} />}
            />
          ))}
        </SectionCard>

        <SectionCard icon="shield-checkmark-outline" title={t('settings.privacy')} currentColors={currentColors}>
          <Text style={[styles.cardDesc, { color: currentColors.textMuted }]}>{t('settings.privacyDescription')}</Text>
          <View style={styles.row}>
            <View style={styles.rowLeft}>
              <Ionicons name="shield-outline" size={16} color={currentColors.textMuted} />
              <View style={{ flex: 1 }}>
                <Text style={[styles.rowLabel, { color: currentColors.textPrimary }]}>{t('settings.privacyInfoLabel')}</Text>
                <Text style={[styles.helpValue, { color: currentColors.textMuted }]}>{t('settings.privacyInfo')}</Text>
              </View>
            </View>
          </View>
          <Row icon="lock-closed-outline" label={t('settings.changePassword')} currentColors={currentColors} onPress={() => setShowPasswordModal(true)} />
          <Row icon="book-outline" label={t('settings.viewPrivacyPolicy')} currentColors={currentColors} onPress={() => setShowHelpModal({ open: true, type: 'privacy' })} />
          <Row icon="trash-outline" label={t('settings.deleteAccount')} currentColors={currentColors} color={currentColors.error} onPress={() => { setDeleteSent(false); setDeletePassword(''); setShowDeleteModal(true) }} />
        </SectionCard>

        <SectionCard icon="help-circle-outline" title={t('settings.help')} currentColors={currentColors}>
          <Text style={[styles.cardDesc, { color: currentColors.textMuted }]}>{t('settings.helpDescription')}</Text>
          {[
            { type: 'guide', icon: 'compass-outline', label: t('settings.helpGuide', 'Guía de producto') },
            { type: 'faq', icon: 'help-circle-outline', label: t('settings.helpFaq') },
            { type: 'contact', icon: 'mail-outline', label: t('settings.helpContact') },
            { type: 'terms', icon: 'document-text-outline', label: t('settings.helpTerms') },
            { type: 'version', icon: 'information-circle-outline', label: t('settings.helpVersion') },
          ].map((item) => (
            <Row
              key={item.type}
              icon={item.icon}
              label={item.label}
              currentColors={currentColors}
              onPress={() => {
                if (item.type === 'guide') navigation.navigate('Guide')
                else setShowHelpModal({ open: true, type: item.type })
              }}
            />
          ))}
        </SectionCard>

        <View style={{ height: 30 }} />
      </ScrollView>

      <Modal isOpen={showLangModal} onClose={() => setShowLangModal(false)} title={t('settings.language')} size="sm">
        {LANGUAGES.map((lang) => {
          const active = lang.code === i18n.language
          return (
            <TouchableOpacity
              key={lang.code}
              style={[styles.langOption, active && { backgroundColor: currentColors.accentDim }]}
              onPress={() => handleChangeLanguage(lang.code)}
            >
              <View style={styles.langOptionRow}>
                <Text style={styles.langFlag}>{lang.flag}</Text>
                <Text style={[styles.langOptionText, { color: active ? currentColors.accent : currentColors.textPrimary }]}>
                  {lang.name}
                </Text>
              </View>
              {active && <Ionicons name="checkmark" size={18} color={currentColors.accent} />}
            </TouchableOpacity>
          )
        })}
      </Modal>

      <Modal
        isOpen={showDateFormatModal}
        onClose={handleCloseDateFormatModal}
        title="Formato de fecha"
        size="lg"
        contentStyle={styles.dateFormatModalContent}
      >
        <Text style={[styles.dateFormatSubtitle, { color: currentColors.textSecondary }]}>
          Elige cómo se muestran las fechas en la app
        </Text>

        <View style={styles.dateFormatGrid}>
          {DATE_FORMATS.map((option) => {
            const active = option.value === dateFormatDraft
            return (
              <TouchableOpacity
                key={option.value}
                activeOpacity={0.9}
                style={[
                  styles.dateFormatOption,
                  {
                    backgroundColor: active ? currentColors.accentDim : currentColors.bgBody,
                    borderColor: active ? currentColors.accent : currentColors.borderColor,
                  },
                  active && { borderWidth: 1.5 },
                ]}
                onPress={() => setDateFormatDraft(option.value)}
              >
                <Text style={[styles.dateFormatOptionLabel, { color: active ? currentColors.accent : currentColors.textPrimary }]}>
                  {option.label}
                </Text>
                <Text style={[styles.dateFormatOptionSample, { color: active ? currentColors.accent : currentColors.textMuted }]}>
                  {option.sample}
                </Text>
              </TouchableOpacity>
            )
          })}
        </View>

        <View style={styles.dateFormatPreviewBox}>
          <Text style={[styles.dateFormatPreviewLabel, { color: currentColors.textMuted }]}>VISTA PREVIA</Text>
          <Text style={[styles.dateFormatPreviewValue, { color: currentColors.accent }]}>
            {DATE_FORMATS.find((option) => option.value === dateFormatDraft)?.sample || '06-10-2026'}
          </Text>
        </View>

        <View style={styles.modalActions}>
          <Button variant="secondary" onPress={handleCloseDateFormatModal} style={styles.dateFormatCancelButton} textStyle={styles.dateFormatCancelText}>
            Cancelar
          </Button>
          <Button onPress={handleDateFormatSave} style={styles.dateFormatSaveButton} textStyle={styles.dateFormatSaveText}>
            Guardar
          </Button>
        </View>
      </Modal>

      <Modal
        isOpen={showPasswordModal}
        onClose={() => setShowPasswordModal(false)}
        title={t('settings.passwordModal.title')}
        contentStyle={styles.passwordModalContent}
      >
        <View style={[styles.passwordHeader, { backgroundColor: currentColors.bgBody, borderColor: currentColors.borderColor }]}>
          <View style={[styles.passwordHeaderIcon, { backgroundColor: currentColors.accentDim, borderColor: currentColors.accent }]}>
            <Ionicons name="lock-closed-outline" size={24} color={currentColors.accent} />
          </View>
          <Text
            style={[styles.passwordHeaderTitle, { color: currentColors.textPrimary }]}
            numberOfLines={2}
            adjustsFontSizeToFit={false}
          >
            Actualiza tu contraseña
          </Text>
        </View>

        <View style={styles.passwordFieldGroup}>
          <Text style={[styles.passwordFieldLabel, { color: currentColors.textSecondary }]}>{t('settings.passwordModal.current')}</Text>
          <View style={[styles.passwordField, { backgroundColor: currentColors.bgInput, borderColor: currentColors.borderColor }]}>
            <Ionicons name="key-outline" size={18} color={currentColors.textMuted} />
            <TextInput
              style={[styles.passwordInput, { color: currentColors.textPrimary }]}
              placeholder={t('settings.passwordModal.current')}
              placeholderTextColor={currentColors.textMuted}
              value={passwordData.current}
              onChangeText={(v) => setPasswordData((p) => ({ ...p, current: v }))}
              secureTextEntry
              autoCapitalize="none"
            />
          </View>
        </View>

        <View style={styles.passwordFieldGroup}>
          <Text style={[styles.passwordFieldLabel, { color: currentColors.textSecondary }]}>{t('settings.passwordModal.new')}</Text>
          <View style={[styles.passwordField, { backgroundColor: currentColors.bgInput, borderColor: currentColors.borderColor }]}>
            <Ionicons name="shield-checkmark-outline" size={18} color={currentColors.textMuted} />
            <TextInput
              style={[styles.passwordInput, { color: currentColors.textPrimary }]}
              placeholder={t('settings.passwordModal.new')}
              placeholderTextColor={currentColors.textMuted}
              value={passwordData.new}
              onChangeText={(v) => setPasswordData((p) => ({ ...p, new: v }))}
              secureTextEntry={!showPassword.new}
              autoCapitalize="none"
            />
            <TouchableOpacity onPress={() => setShowPassword((p) => ({ ...p, new: !p.new }))} hitSlop={8}>
              <Ionicons name={showPassword.new ? 'eye-outline' : 'eye-off-outline'} size={20} color={currentColors.textMuted} />
            </TouchableOpacity>
          </View>
        </View>

        <View style={styles.passwordFieldGroup}>
          <Text style={[styles.passwordFieldLabel, { color: currentColors.textSecondary }]}>{t('settings.passwordModal.confirm')}</Text>
          <View style={[styles.passwordField, { backgroundColor: currentColors.bgInput, borderColor: currentColors.borderColor }]}>
            <Ionicons name="shield-outline" size={18} color={currentColors.textMuted} />
            <TextInput
              style={[styles.passwordInput, { color: currentColors.textPrimary }]}
              placeholder={t('settings.passwordModal.confirm')}
              placeholderTextColor={currentColors.textMuted}
              value={passwordData.confirm}
              onChangeText={(v) => setPasswordData((p) => ({ ...p, confirm: v }))}
              secureTextEntry={!showPassword.confirm}
              autoCapitalize="none"
            />
            <TouchableOpacity onPress={() => setShowPassword((p) => ({ ...p, confirm: !p.confirm }))} hitSlop={8}>
              <Ionicons name={showPassword.confirm ? 'eye-outline' : 'eye-off-outline'} size={20} color={currentColors.textMuted} />
            </TouchableOpacity>
          </View>
        </View>

        <View style={styles.modalActions}>
          <Button variant="secondary" onPress={() => setShowPasswordModal(false)} style={styles.passwordActionButton}>
            {t('settings.passwordModal.cancel')}
          </Button>
          <Button onPress={handleSavePassword} style={styles.passwordActionButton}>
            {t('settings.passwordModal.save')}
          </Button>
        </View>
      </Modal>

      <Modal isOpen={showDeleteModal} onClose={handleCloseDeleteModal} title="Confirmación de seguridad" contentStyle={styles.deleteModalContent}>
        {deleteSent ? (
          <View style={[styles.deleteSuccessCard, { backgroundColor: currentColors.bgBody, borderColor: currentColors.borderColor }]}>
            <Ionicons name="checkmark-circle-outline" size={32} color={currentColors.accent} />
            <Text style={[styles.modalText, { color: currentColors.textPrimary }]}>Solicitud enviada</Text>
            <Text style={[styles.helpValue, { color: currentColors.textSecondary }]}>
              Tu cuenta queda pendiente de eliminación segura. Verificaremos la confirmación y te avisaremos si necesitamos más información.
            </Text>
            <Button variant="secondary" onPress={handleCloseDeleteModal} style={styles.deleteSuccessButton}>
              Cerrar
            </Button>
          </View>
        ) : (
          <>
            <View style={[styles.deleteHeader, { backgroundColor: currentColors.bgBody, borderColor: currentColors.borderColor }]}>
              <View style={[styles.deleteHeaderIcon, { backgroundColor: currentColors.error + '15', borderColor: currentColors.error }]}>
                <Ionicons name="trash-outline" size={22} color={currentColors.error} />
              </View>
              <Text style={[styles.deleteHeaderTitle, { color: currentColors.textPrimary }]}>
                Eliminar cuenta
              </Text>
            </View>

            <Text style={[styles.modalText, { color: currentColors.textSecondary }]}>
              Esta acción elimina permanentemente tu cuenta y todos los datos asociados a tu perfil.
            </Text>
            <Text style={[styles.deleteDetail, { color: currentColors.textMuted }]}>
              Para continuar, introduce tu contraseña actual para confirmar la eliminación.
            </Text>

            <View style={styles.deleteFieldGroup}>
              <Text style={[styles.deleteFieldLabel, { color: currentColors.textSecondary }]}>Contraseña actual</Text>
              <View style={[styles.passwordField, { backgroundColor: currentColors.bgInput, borderColor: currentColors.borderColor }]}>
                <Ionicons name="key-outline" size={18} color={currentColors.textMuted} />
                <TextInput
                  style={[styles.passwordInput, { color: currentColors.textPrimary }]}
                  placeholder="Confirma tu contraseña"
                  placeholderTextColor={currentColors.textMuted}
                  value={deletePassword}
                  onChangeText={setDeletePassword}
                  secureTextEntry
                  autoCapitalize="none"
                />
              </View>
            </View>

            <View style={styles.modalActions}>
              <Button variant="secondary" onPress={handleCloseDeleteModal} style={styles.deleteActionButton}>
                Cancelar
              </Button>
              <Button variant="danger" onPress={handleDeleteAccountConfirm} style={styles.deleteActionButton}>
                Eliminar cuenta
              </Button>
            </View>
          </>
        )}
      </Modal>

      <Modal isOpen={showHelpModal.open} onClose={() => setShowHelpModal({ open: false, type: null })} size="lg">
        <View style={[styles.helpModalHeader, { backgroundColor: currentColors.bgBody, borderColor: currentColors.borderColor }]}>
          <View style={[styles.helpModalIcon, { backgroundColor: currentColors.accentDim, borderColor: currentColors.accent }]}>
            <Ionicons name={showHelpModal.type === 'version' ? 'information-circle-outline' : 'shield-checkmark-outline'} size={20} color={currentColors.accent} />
          </View>
          <View style={styles.helpModalTextWrap}>
            <Text style={[styles.helpModalTitle, { color: currentColors.textPrimary }]}>
              {showHelpModal.type === 'version' ? 'Versión de la app' : 'Políticas y seguridad'}
            </Text>
            {!showHelpModal.type || showHelpModal.type === 'version' ? null : (
              <Text style={[styles.helpModalSubtitle, { color: currentColors.textSecondary }]}>
                {showHelpModal.type === 'privacy' ? 'Información sobre privacidad, protección de datos y seguridad.' : t('settings.helpDescription')}
              </Text>
            )}
          </View>
        </View>

        <ScrollView
          style={styles.helpModalScroll}
          contentContainerStyle={styles.helpModalContent}
          showsVerticalScrollIndicator={false}
          bounces={true}
        >
          {renderHelpContent()}
        </ScrollView>
      </Modal>
    </SafeAreaView>
  )
}

