import React, { useState } from 'react'
import {
  View,
  Text,
  ScrollView,
  TextInput,
  Pressable,
  KeyboardAvoidingView,
  Platform,
  SafeAreaView,
  StyleSheet,
} from 'react-native'
import { useNavigation, useRoute } from '@react-navigation/native'
import { useTranslation } from 'react-i18next'
import { Ionicons } from '@expo/vector-icons'
import Button from '../../../../shared/components/Button/Button.jsx'
import { useTheme } from '../../../../context/ThemeContext.jsx'
import { useToast } from '../../../../shared/components/Toast/Toast.jsx'
import authService from '../../services/authService'
import { styles } from './ChangePasswordScreen.styles'

export default function ChangePasswordScreen() {
  const navigation = useNavigation()
  const route = useRoute()
  const email = route.params?.email
  const code = route.params?.code
  const { t } = useTranslation()
  const { currentColors: c } = useTheme()
  const toast = useToast()

  const [newPassword, setNewPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [show, setShow] = useState({ new: false, confirm: false })
  const [saving, setSaving] = useState(false)

  const matches = newPassword === confirmPassword && newPassword.length > 0
  const policyOk = newPassword.length >= 8 && /[A-Z]/.test(newPassword)

  const onSubmit = async () => {
    if (newPassword !== confirmPassword || !newPassword) {
      toast.error(t('changePassword.mismatch'))
      return
    }
    if (!policyOk) {
      toast.error(t('changePassword.minHint'))
      return
    }
    if (!email || !code) {
      toast.error(t('changePassword.error'))
      return
    }
    setSaving(true)
    try {
      await authService.resetPassword(email, code, newPassword)
      toast.success(t('changePassword.success', 'Contraseña actualizada'))
      navigation.reset({ index: 0, routes: [{ name: 'Login' }] })
    } catch (e) {
      toast.error(e.message)
    } finally {
      setSaving(false)
    }
  }

  const renderField = (field, key) => (
    <View>
      <View style={[styles.field, { backgroundColor: c.bgInput, borderColor: c.borderColor }]}>
        <Ionicons name="lock-closed-outline" size={18} color={c.textMuted} style={styles.fieldIcon} />
        <TextInput
          value={field === 'new' ? newPassword : confirmPassword}
          onChangeText={field === 'new' ? setNewPassword : setConfirmPassword}
          placeholder={field === 'new' ? t('changePassword.newPassword') : t('changePassword.confirmPassword')}
          placeholderTextColor={c.textMuted}
          secureTextEntry={!show[field]}
          style={[styles.fieldInput, { color: c.textPrimary }]}
        />
        <Pressable onPress={() => setShow((s) => ({ ...s, [key]: !s[key] }))} hitSlop={8}>
          <Ionicons name={show[key] ? 'eye-off-outline' : 'eye-outline'} size={20} color={c.textMuted} />
        </Pressable>
      </View>
      <Text style={[styles.hint, { color: c.textMuted }]}>
        {t('changePassword.minHint', 'Mínimo 6 caracteres')}
      </Text>
    </View>
  )

  return (
    <SafeAreaView style={[styles.safe, { backgroundColor: c.bgBody }]}>
      <KeyboardAvoidingView style={styles.flex} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
        <ScrollView contentContainerStyle={styles.container} keyboardShouldPersistTaps="handled">
          <Pressable onPress={() => navigation.goBack()} style={styles.back} hitSlop={8}>
            <Ionicons name="arrow-back" size={22} color={c.textPrimary} />
          </Pressable>

          <View style={styles.iconWrap}>
            <View style={[styles.icon, { backgroundColor: c.accentDim }]}>
              <Ionicons name="key-outline" size={30} color={c.accent} />
            </View>
          </View>
          <Text style={[styles.title, { color: c.textPrimary }]}>{t('changePassword.title')}</Text>
          <Text style={[styles.subtitle, { color: c.textMuted }]}>
            {t('changePassword.description')}
          </Text>

          {renderField('new', 'new')}
          {renderField('confirm', 'confirm')}

          <View style={[styles.matchRow, { borderColor: matches ? c.success : c.borderColor }]}>
            <Ionicons
              name={matches ? 'checkmark-circle' : 'ellipse-outline'}
              size={18}
              color={matches ? c.success : c.textMuted}
            />
            <Text style={[styles.matchText, { color: matches ? c.success : c.textMuted }]}>
              {t('changePassword.matchStatus', 'Las contraseñas coinciden')}
            </Text>
          </View>

          <Button onPress={onSubmit} loading={saving} size="lg" style={styles.submit}>
            {t('changePassword.confirmBtn')}
          </Button>
        </ScrollView>
      </KeyboardAvoidingView>
    </SafeAreaView>
  )
}

