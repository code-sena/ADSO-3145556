import { useNavigate } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import AuthLayout from "../../components/AuthLayout/AuthLayout";
import VerifyCodeForm from '../../components/VerifyCodeForm/VerifyCodeForm'
import { BackButton } from "../../../../shared/components";
import "./VerifyCode.css";

function VerifyCodeScreen() {
  const navigate = useNavigate()
  const { t } = useTranslation()

  return (
    <AuthLayout>
      <BackButton onClick={() => navigate('/forgot-password')} />
      <div className="verify-code-content">
        <div className="verify-icon">📱</div>
        <h1>{t('verifyCode.title')}</h1>
        <p className="verify-description">{t('verifyCode.description')}</p>
        <VerifyCodeForm />
      </div>
    </AuthLayout>
  )
}

export default VerifyCodeScreen
