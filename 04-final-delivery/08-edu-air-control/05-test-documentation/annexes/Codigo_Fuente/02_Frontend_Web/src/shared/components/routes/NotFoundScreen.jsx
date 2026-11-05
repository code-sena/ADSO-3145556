import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { IoHomeOutline, IoSearchOutline, IoArrowBackOutline } from 'react-icons/io5';
import './NotFoundScreen.css';

function NotFoundScreen() {
  const { t } = useTranslation();

  return (
    <main className="notfound" id="main-content">
      <div className="notfound__visual">
        <p className="notfound__code">404</p>
        <div className="notfound__illustration">
          <IoSearchOutline className="notfound__icon" aria-hidden="true" />
        </div>
      </div>

      <div className="notfound__content">
        <h1 className="notfound__title">{t('notFound.title', 'Página no encontrada')}</h1>
        <p className="notfound__description">
          {t('notFound.description', 'La ruta que buscas no existe o fue movida.')}
        </p>

        <div className="notfound__actions">
          <Link className="notfound__link" to="/landing">
            <IoHomeOutline aria-hidden="true" />
            {t('notFound.backHome', 'Volver al inicio')}
          </Link>
          <button className="notfound__back" onClick={() => window.history.back()}>
            <IoArrowBackOutline aria-hidden="true" />
            {t('notFound.goBack', 'Regresar')}
          </button>
        </div>

        <div className="notfound__links">
          <Link to="/dashboard" className="notfound__quick-link">
            {t('notFound.dashboard', 'Dashboard')}
          </Link>
          <Link to="/all-environments" className="notfound__quick-link">
            {t('notFound.environments', 'Ambientes')}
          </Link>
          <Link to="/profile" className="notfound__quick-link">
            {t('notFound.profile', 'Mi perfil')}
          </Link>
        </div>
      </div>
    </main>
  );
}

export default NotFoundScreen;
