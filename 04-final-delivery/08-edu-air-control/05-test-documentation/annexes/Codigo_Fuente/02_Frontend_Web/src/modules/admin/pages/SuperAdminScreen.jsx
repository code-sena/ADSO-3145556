import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { IoBusinessOutline, IoAddOutline, IoRefreshOutline } from 'react-icons/io5';
import Navbar from '../../dashboard/components/Navbar/Navbar';
import institutionService from '../services/institutionService';
import './SuperAdminScreen.css';

const EMPTY_FORM = { code: '', name: '', type: '' };

function SuperAdminScreen() {
  const { t } = useTranslation();
  const [institutions, setInstitutions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [form, setForm] = useState(EMPTY_FORM);
  const [saving, setSaving] = useState(false);

  const load = async () => {
    setLoading(true);
    try {
      const data = await institutionService.getAll();
      setInstitutions(data);
      setError('');
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleChange = (field, value) => setForm((prev) => ({ ...prev, [field]: value }));

  const handleCreate = async (event) => {
    event.preventDefault();
    setSaving(true);
    setError('');
    try {
      await institutionService.create(form);
      setForm(EMPTY_FORM);
      await load();
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  const handleToggleStatus = async (institution) => {
    try {
      await institutionService.update(institution.id, {
        status: institution.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE',
      });
      await load();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="superadmin-page">
      <Navbar />
      <main className="superadmin-main">
        <header className="superadmin-header">
          <div className="superadmin-header__title">
            <IoBusinessOutline size={28} />
            <div>
              <h1>{t('superadmin.title', 'Instituciones')}</h1>
              <p>{t('superadmin.subtitle', 'Alta y gestión de instituciones (multi-tenant)')}</p>
            </div>
          </div>
          <button type="button" className="superadmin-btn ghost" onClick={load}>
            <IoRefreshOutline size={18} /> {t('superadmin.refresh', 'Recargar')}
          </button>
        </header>

        {error && <div className="superadmin-alert">{error}</div>}

        <section className="superadmin-card">
          <h2>{t('superadmin.createTitle', 'Nueva institución')}</h2>
          <form className="superadmin-form" onSubmit={handleCreate}>
            <input
              type="text"
              placeholder={t('superadmin.code', 'Código (ej. SEN-444)')}
              value={form.code}
              onChange={(e) => handleChange('code', e.target.value.toUpperCase())}
              maxLength={20}
              required
            />
            <input
              type="text"
              placeholder={t('superadmin.name', 'Nombre')}
              value={form.name}
              onChange={(e) => handleChange('name', e.target.value)}
              maxLength={150}
              required
            />
            <input
              type="text"
              placeholder={t('superadmin.type', 'Tipo (opcional)')}
              value={form.type}
              onChange={(e) => handleChange('type', e.target.value)}
              maxLength={50}
            />
            <button type="submit" className="superadmin-btn primary" disabled={saving}>
              <IoAddOutline size={18} /> {saving ? '...' : t('superadmin.create', 'Crear')}
            </button>
          </form>
        </section>

        <section className="superadmin-card">
          <h2>{t('superadmin.listTitle', 'Registradas')}</h2>
          {loading ? (
            <p className="superadmin-muted">{t('common.loading', 'Cargando...')}</p>
          ) : institutions.length === 0 ? (
            <p className="superadmin-muted">
              {t('superadmin.empty', 'Aún no hay instituciones.')}
            </p>
          ) : (
            <table className="superadmin-table">
              <thead>
                <tr>
                  <th>{t('superadmin.code', 'Código')}</th>
                  <th>{t('superadmin.name', 'Nombre')}</th>
                  <th>{t('superadmin.type', 'Tipo')}</th>
                  <th>{t('superadmin.status', 'Estado')}</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {institutions.map((inst) => (
                  <tr key={inst.id}>
                    <td className="mono">{inst.code}</td>
                    <td>{inst.name}</td>
                    <td>{inst.type || '—'}</td>
                    <td>
                      <span className={`superadmin-badge ${inst.status === 'ACTIVE' ? 'on' : 'off'}`}>
                        {inst.status}
                      </span>
                    </td>
                    <td className="right">
                      <button
                        type="button"
                        className="superadmin-btn small"
                        onClick={() => handleToggleStatus(inst)}
                      >
                        {inst.status === 'ACTIVE'
                          ? t('superadmin.deactivate', 'Desactivar')
                          : t('superadmin.activate', 'Activar')}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </section>
      </main>
    </div>
  );
}

export default SuperAdminScreen;
