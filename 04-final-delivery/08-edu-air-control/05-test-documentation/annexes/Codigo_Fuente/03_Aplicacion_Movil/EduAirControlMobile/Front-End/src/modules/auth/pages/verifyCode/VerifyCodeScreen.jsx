import React, { useRef, useState } from 'react'
import {
  View,
  Text,
  ScrollView,
  TextInput,
  Pressable,
  SafeAreaView,
  StyleSheet,
} from 'react-native'
import { useNavigation, useRoute } from '@react-navigation/native'
import { useTranslation } from 'react-i18next'
import { Ionicons } from '@expo/vector-icons'
import Button from '../../../../shared/components/Button/Button.jsx'
import { useToast } from '../../../../shared/components/Toast/Toast.jsx'
import { useTheme } from '../../../../context/ThemeContext.jsx'
import authService from '../../services/authService'
import { styles } from './VerifyCodeScreen.styles'

const CODE_LENGTH = 6

export default function VerifyCodeScreen() {
  const navigation = useNavigation()
  const route = useRoute()
  const { t } = useTranslation()
  const { currentColors: c } = useTheme()
  const toast = useToast()
  const email = route.params?.email || ''
  const [code, setCode] = useState(Array(CODE_LENGTH).fill(''))
  const [verifying, setVerifying] = useState(false)
  const inputsRef = useRef([])
  const fullCode = code.join('')

  const handleResend = async () => {
    if (!email) {
      toast.error(t('verifyCode.resendError'))
      return
    }
    try {
      await authService.resendCode(email)
      setCode(Array(CODE_LENGTH).fill(''))
      inputsRef.current[0]?.focus()
      toast.success(t('verifyCode.resent'))
    } catch (e) {
      toast.error(e.message)
    }
  }

  const handleVerify = async () => {
    if (!email || fullCode.length !== CODE_LENGTH || verifying) return
    setVerifying(true)
    try {
      await authService.verifyCode(email, fullCode)
      navigation.navigate('ChangePassword', { email, code: fullCode })
    } catch (e) {
      toast.error(e.message)
    } finally {
      setVerifying(false)
    }
  }

  const handleChange = (index, value) => {
    if (value.length > 1) return
    const next = [...code]
    next[index] = value
    setCode(next)
    if (value && index < CODE_LENGTH - 1) inputsRef.current[index + 1]?.focus()
    if (!value && index > 0) inputsRef.current[index - 1]?.focus()
  }

  return (
    <SafeAreaView style={[styles.safe, { backgroundColor: c.bgBody }]}>
      <ScrollView contentContainerStyle={styles.container}>
        <Pressable onPress={() => navigation.goBack()} style={styles.back} hitSlop={8}>
          <Ionicons name="arrow-back" size={22} color={c.textPrimary} />
        </Pressable>

        <View style={styles.iconWrap}>
          <View style={[styles.icon, { backgroundColor: c.accentDim }]}>
            <Ionicons name="shield-checkmark-outline" size={30} color={c.accent} />
          </View>
        </View>
        <Text style={[styles.title, { color: c.textPrimary }]}>{t('verifyCode.title')}</Text>
        <Text style={[styles.subtitle, { color: c.textMuted }]}>
          {t('verifyCode.description')}
        </Text>

        <View style={styles.codeRow}>
          {code.map((digit, index) => (
            <TextInput
              key={index}
              ref={(el) => (inputsRef.current[index] = el)}
              value={digit}
              onChangeText={(v) => handleChange(index, v)}
              keyboardType="number-pad"
              maxLength={1}
              style={[
                styles.codeInput,
                {
                  color: c.textPrimary,
                  backgroundColor: c.bgInput,
                  borderColor: digit ? c.accent : c.borderColor,
                },
              ]}
              accessibilityLabel={t('verifyCode.digitLabel', 'Código dígito {{n}}', { n: index + 1 })}
            />
          ))}
        </View>

        <Pressable style={styles.resend} onPress={handleResend}>
          <Text style={[styles.resendText, { color: c.accent }]}>{t('verifyCode.resend')}</Text>
        </Pressable>

        <Button
          onPress={handleVerify}
          loading={verifying}
          size="lg"
          style={styles.submit}
          disabled={fullCode.length !== CODE_LENGTH}
        >
          {t('verifyCode.verifyBtn')}
        </Button>
      </ScrollView>
    </SafeAreaView>
  )
}

