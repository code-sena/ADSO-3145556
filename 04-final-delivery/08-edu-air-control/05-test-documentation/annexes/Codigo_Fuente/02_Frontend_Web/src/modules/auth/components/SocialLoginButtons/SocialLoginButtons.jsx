import { useTranslation } from 'react-i18next';
import { FcGoogle } from 'react-icons/fc';
import { FaFacebook } from 'react-icons/fa';
import './SocialLoginButtons.css';

const API_BASE = import.meta.env.VITE_API_URL || `${window.location.protocol}//${window.location.hostname}:8080`;

function SocialLoginButtons() {
  const { t } = useTranslation();

  const handleGoogleClick = () => {
    // Redirect al backend que maneja el OAuth2 flow con Google
    window.location.assign(`${API_BASE}/api/v1/auth/oauth2/google`);
  };

  const handleFacebookClick = () => {
    window.location.assign(`${API_BASE}/api/v1/auth/oauth2/facebook`);
  };

  return (
    <>
      <div className="social-divider">
        <span>{t('login.socialDivider', 'o continúa con')}</span>
      </div>
      <div className="social-btn-group">
        <button type="button" className="social-btn social-btn-google" onClick={handleGoogleClick}>
          <FcGoogle size={20} />
          <span>{t('login.googleBtn', 'Google')}</span>
        </button>
        <button type="button" className="social-btn social-btn-facebook" onClick={handleFacebookClick}>
          <FaFacebook size={20} />
          <span>{t('login.facebookBtn', 'Facebook')}</span>
        </button>
      </div>
    </>
  );
}

export default SocialLoginButtons;
