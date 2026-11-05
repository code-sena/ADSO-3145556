import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { loginSchema } from '../../schemas/loginSchema';
import authService from '../../services/authService';
import SocialLoginButtons from '../SocialLoginButtons/SocialLoginButtons';
import { FaEnvelope, FaLock } from 'react-icons/fa';
import '../../pages/login/Login.css';

function LoginForm() {
  const navigate = useNavigate();
  const { t } = useTranslation();

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm({
    resolver: zodResolver(loginSchema),
  });

  const [apiError, setApiError] = useState('');

  const onSubmit = async (data) => {
    setApiError('');
    try {
      await authService.loginWithCredentials(data.email, data.password);
      navigate('/dashboard');
    } catch (err) {
      setApiError(err.message || 'Error al iniciar sesión');
    }
  };

  const handleOAuth2Login = () => {
    authService.loginWithOAuth2();
  };

  return (
    <form className="login-form-modern" onSubmit={handleSubmit(onSubmit)}>
      <div className="input-group-modern">
        <label htmlFor="email">{t('login.email')}</label>
        <div className="input-wrapper">
          <FaEnvelope className="input-icon" />
          <input
            {...register('email')}
            type="email"
            id="email"
            placeholder={t('login.placeholderEmail')}
            className={errors.email ? 'input-error shake' : ''}
          />
        </div>
        {errors.email && <p className="error-text">⚠ {t(errors.email.message)}</p>}
      </div>

      <div className="input-group-modern">
        <label htmlFor="password">{t('login.password')}</label>
        <div className="input-wrapper">
          <FaLock className="input-icon" />
          <input
            {...register('password')}
            type="password"
            id="password"
            placeholder={t('login.placeholderPassword')}
            className={errors.password ? 'input-error shake' : ''}
          />
        </div>
        {errors.password && <p className="error-text">⚠ {t(errors.password.message)}</p>}
      </div>

      <div className="login-options-modern">
        <button
          type="button"
          className="forgot-password-link"
          onClick={() => navigate('/forgot-password')}
        >
          {t('login.forgotPassword')}
        </button>
      </div>

      {apiError && <p className="error-text">⚠ {apiError}</p>}

      <button type="submit" className="btn-login-premium" disabled={isSubmitting}>
        {isSubmitting ? '...' : t('login.title')}
      </button>

      {isSubmitting && <p className="loading-text">Validando credenciales...</p>}

      <div className="social-divider">
        <span>{t('login.socialDivider', 'o continúa con')}</span>
      </div>

      <button
        type="button"
        className="btn-oauth2"
        onClick={handleOAuth2Login}
      >
        Continuar con OAuth2
      </button>

      <SocialLoginButtons />
    </form>
  );
}

export default LoginForm;
