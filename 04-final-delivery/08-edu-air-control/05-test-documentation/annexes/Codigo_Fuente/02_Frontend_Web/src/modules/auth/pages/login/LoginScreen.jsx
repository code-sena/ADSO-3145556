import { useEffect, useRef, useState } from 'react';
import { useLocation, useSearchParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import AuthLayout from '../../components/AuthLayout/AuthLayout';
import AuthSlider from '../../components/AuthSlider/AuthSlider';
import authService from '../../services/authService';

function LoginScreen() {
  const [searchParams] = useSearchParams();
  const location = useLocation();
  const { t } = useTranslation();
  const initialRegister = searchParams.get('panel') === 'register';
  const passwordReset = location.state?.passwordReset;
  const processedRef = useRef(false);
  const [onboardingUserId, setOnboardingUserId] = useState(null);
  const [companyCode, setCompanyCode] = useState('');
  const [onboardingError, setOnboardingError] = useState('');
  const [onboardingLoading, setOnboardingLoading] = useState(false);

  useEffect(() => {
    if (processedRef.current) return;
    const hash = window.location.hash;
    if (!hash || !hash.includes('access_token')) return;

    processedRef.current = true;
    const params = new URLSearchParams(hash.substring(1));
    const accessToken = params.get('access_token');
    const userId = params.get('userId');
    const needsOnboarding = params.get('needsOnboarding');

    if (accessToken) {
      localStorage.setItem('token', accessToken);
      window.history.replaceState(null, '', window.location.pathname);
      if (needsOnboarding === 'true' && userId) {
        setOnboardingUserId(userId);
      } else {
        window.location.href = '/dashboard';
      }
    }
  }, []);

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const error = params.get('error');
    if (error) {
      window.history.replaceState(null, '', window.location.pathname);
    }
  }, []);

  const handleOnboardingSubmit = async (e) => {
    e.preventDefault();
    setOnboardingError('');
    setOnboardingLoading(true);
    try {
      await authService.completeSocialOnboarding(onboardingUserId, companyCode);
      window.location.href = '/dashboard';
    } catch (err) {
      setOnboardingError(err.message || 'Error al completar el registro');
    } finally {
      setOnboardingLoading(false);
    }
  };

  if (onboardingUserId) {
    return (
      <AuthLayout className="auth-login-background">
        <form className="login-form-modern" onSubmit={handleOnboardingSubmit}>
          <h2>Completa tu registro</h2>
          <p>Ingresa el código de tu institución para continuar</p>
          <div className="input-group-modern">
            <label htmlFor="companyCode">Código de institución</label>
            <input
              type="text"
              id="companyCode"
              value={companyCode}
              onChange={(e) => setCompanyCode(e.target.value)}
              placeholder="SEN-444"
              required
            />
          </div>
          {onboardingError && <p className="error-text">⚠ {onboardingError}</p>}
          <button type="submit" className="btn-login-premium" disabled={onboardingLoading}>
            {onboardingLoading ? '...' : 'Continuar'}
          </button>
        </form>
      </AuthLayout>
    );
  }

  return (
    <AuthLayout className="auth-login-background">
      {passwordReset && (
        <p className="error-text" role="status" style={{ textAlign: 'center', marginBottom: 12 }}>
          ✓ {t('changePassword.success', 'Contraseña actualizada. Inicia sesión.')}
        </p>
      )}
      <AuthSlider initialRegister={initialRegister} />
    </AuthLayout>
  );
}

export default LoginScreen;
