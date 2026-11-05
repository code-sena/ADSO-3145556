import { useTranslation } from 'react-i18next'
import AuthLayout from "../../components/AuthLayout/AuthLayout";
import ForgotPasswordForm from '../../components/ForgotPasswordForm/ForgotPasswordForm'
import "./ForgotPassword.css";

function ForgotPasswordScreen() {
  const { t } = useTranslation()

  return (
    <AuthLayout className="auth-login-background">
      <div className="forgot-password-content">
        <h1>{t('forgotPassword.title')}</h1>
        <p className="forgot-description">{t('forgotPassword.description')}</p>
        <ForgotPasswordForm />
      </div>
    </AuthLayout>
  )
}

export default ForgotPasswordScreen
