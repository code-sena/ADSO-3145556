import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { FaUsers, FaCheckCircle, FaTimesCircle, FaUserShield } from 'react-icons/fa';
import Navbar from '../../dashboard/components/Navbar/Navbar';
import adminService from '../services/adminService';
import './UserManagementScreen.css';

function UserManagementScreen() {
  const { t } = useTranslation();
  const [users, setUsers] = useState([]);
  const [roles, setRoles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const loadData = async () => {
    setLoading(true);
    try {
      const [usersData, rolesData] = await Promise.all([
        adminService.listUsers(),
        adminService.listRoles(),
      ]);
      setUsers(usersData);
      setRoles(rolesData);
    } catch (err) {
      setError(err.message || 'Error loading data');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleAssignRole = async (userId, roleName) => {
    setError('');
    setSuccess('');
    try {
      const user = users.find((u) => u.userId === userId);
      const currentRoles = user?.roles || [];
      const newRoles = currentRoles.includes(roleName)
        ? currentRoles.filter((r) => r !== roleName)
        : [...currentRoles, roleName];
      await adminService.assignRoles(userId, newRoles);
      setSuccess(t('admin.roleUpdated', 'Roles updated successfully'));
      loadData();
    } catch (err) {
      setError(err.message || 'Error updating roles');
    }
  };

  return (
    <div className="usermgmt-page">
      <Navbar />
      <main className="usermgmt-main">
        <header className="usermgmt-header">
          <div className="usermgmt-header__title">
            <FaUserShield size={28} />
            <div>
              <h1>{t('admin.title', 'Gestión de Usuarios')}</h1>
              <p>{t('admin.subtitle', 'Asigna roles a los administradores de tus compañías')}</p>
            </div>
          </div>
        </header>

        {error && <div className="usermgmt-alert usermgmt-alert-error">⚠ {error}</div>}
        {success && <div className="usermgmt-alert usermgmt-alert-success">✓ {success}</div>}

        <div className="usermgmt-hint">
          <FaUsers /> {t('admin.usersHint', 'Asigna el rol de Administrador a los usuarios que serán administradores de sus compañías')}
        </div>

        {loading ? (
          <p className="usermgmt-loading">...</p>
        ) : (
          <div className="usermgmt-table-wrapper">
            <table className="usermgmt-table">
              <thead>
                <tr>
                  <th>{t('admin.colEmail', 'Correo')}</th>
                  <th>{t('admin.colUsername', 'Usuario')}</th>
                  <th>{t('admin.colRoles', 'Roles')}</th>
                  <th>{t('admin.colActions', 'Acciones')}</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.userId}>
                    <td>{user.email}</td>
                    <td>{user.username}</td>
                    <td>
                      <div className="usermgmt-roles">
                        {(user.roles || []).map((role) => (
                          <span key={role} className={`usermgmt-role-badge usermgmt-role-${role.toLowerCase()}`}>
                            {role}
                          </span>
                        ))}
                      </div>
                    </td>
                    <td>
                      <div className="usermgmt-actions">
                        {roles
                          .filter((r) => r.name !== 'SUPER_ADMIN')
                          .map((role) => (
                            <button
                              key={role.name}
                              className={`usermgmt-role-btn ${
                                (user.roles || []).includes(role.name) ? 'active' : ''
                              }`}
                              onClick={() => handleAssignRole(user.userId, role.name)}
                              title={role.description}
                            >
                              {(user.roles || []).includes(role.name) ? (
                                <FaCheckCircle />
                              ) : (
                                <FaTimesCircle />
                              )}
                              {role.name}
                            </button>
                          ))}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </main>
    </div>
  );
}

export default UserManagementScreen;
