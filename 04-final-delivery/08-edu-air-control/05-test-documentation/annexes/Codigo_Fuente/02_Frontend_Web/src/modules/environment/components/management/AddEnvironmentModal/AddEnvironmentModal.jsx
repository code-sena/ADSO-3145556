import { useEffect, useState } from 'react'
import catalogService from '../../../services/catalogService'
import './AddEnvironmentModal.css'

/**
 * Alta de ambiente educativo (ms-classroom-management).
 *
 * <p>El backend exige {@code code}, {@code name}, {@code campusId} y
 * {@code environmentTypeId} (UUID). Antes el formulario enviaba etiquetas de texto
 * ("Aula", "Área Técnica") y omitía campus y código: la creación devolvía 400.
 *
 * <p>Los umbrales de temperatura se quitaron de aquí a propósito: viven en
 * ms-environment-monitoring (ADR-014) y este formulario los guardaba en ninguna
 * parte.
 */
function AddEnvironmentModal({ onClose, onAdd }) {
  const [name,     setName]     = useState('')
  const [code,     setCode]     = useState('')
  const [capacity, setCapacity] = useState('')
  const [campusId, setCampusId] = useState('')
  const [floor,    setFloor]    = useState('')
  const [envType,  setEnvType]  = useState('')
  const [campuses, setCampuses] = useState([])
  const [types,    setTypes]    = useState([])
  const [loading,  setLoading]  = useState(true)

  useEffect(() => {
    let cancelled = false
    Promise.all([
      catalogService.getCampuses().catch(() => []),
      catalogService.getEnvironmentTypes().catch(() => []),
    ])
      .then(([campusList, typeList]) => {
        if (cancelled) return
        setCampuses(campusList)
        setTypes(typeList)
        if (campusList.length === 1) setCampusId(campusList[0].id)
        if (typeList.length === 1) setEnvType(typeList[0].id)
      })
      .finally(() => {
        if (!cancelled) setLoading(false)
      })
    return () => {
      cancelled = true
    }
  }, [])

  const canSubmit = name.trim() && code.trim() && campusId && envType

  const handleSubmit = () => {
    if (!canSubmit) return
    onAdd({
      name: name.trim(),
      code: code.trim(),
      campusId,
      environmentTypeId: envType,
      floor: floor.trim() === '' ? undefined : Number(floor),
      capacity: Number(capacity) || 0,
    })
  }

  return (
    <div className="add-env-overlay" onClick={onClose}>
      <div className="add-env-modal add-env-modal--wide" onClick={(e) => e.stopPropagation()}>

        <h2 className="add-env-modal__title">Agregar Ambiente</h2>

        {/* Fila 1: Código + Nombre */}
        <div className="add-env-modal__row">
          <div className="add-env-modal__field">
            <label className="add-env-modal__label">
              Código <span className="add-env-modal__required">*</span>
            </label>
            <input
              className="add-env-modal__input"
              placeholder="Ej. 209-1 (máx. 30)"
              maxLength={30}
              value={code}
              onChange={(e) => setCode(e.target.value)}
            />
          </div>
          <div className="add-env-modal__field">
            <label className="add-env-modal__label">
              Nombre <span className="add-env-modal__required">*</span>
            </label>
            <input
              className="add-env-modal__input"
              placeholder="Ej. Aula 209-1"
              maxLength={120}
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </div>
        </div>

        {/* Fila 2: Campus + Capacidad */}
        <div className="add-env-modal__row">
          <div className="add-env-modal__field">
            <label className="add-env-modal__label">
              Campus <span className="add-env-modal__required">*</span>
            </label>
            <select
              className="add-env-modal__input add-env-modal__select"
              value={campusId}
              onChange={(e) => setCampusId(e.target.value)}
              disabled={loading}
            >
              <option value="" disabled>
                {loading ? 'Cargando campus…' : 'Seleccionar campus…'}
              </option>
              {campuses.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.name}
                </option>
              ))}
            </select>
            {!loading && campuses.length === 0 && (
              <span className="add-env-modal__required">
                No hay campuses cargados: cree uno antes de añadir ambientes.
              </span>
            )}
          </div>
          <div className="add-env-modal__field">
            <label className="add-env-modal__label">Capacidad</label>
            <input
              className="add-env-modal__input"
              type="number"
              min={0}
              placeholder="Ej. 30"
              value={capacity}
              onChange={(e) => setCapacity(e.target.value)}
            />
          </div>
        </div>

        {/* Fila 3: Tipo + Piso */}
        <div className="add-env-modal__row">
          <div className="add-env-modal__field">
            <label className="add-env-modal__label">
              Tipo de ambiente <span className="add-env-modal__required">*</span>
            </label>
            <select
              className="add-env-modal__input add-env-modal__select"
              value={envType}
              onChange={(e) => setEnvType(e.target.value)}
              disabled={loading}
            >
              <option value="" disabled>
                {loading ? 'Cargando tipos…' : 'Seleccionar tipo…'}
              </option>
              {types.map((t) => (
                <option key={t.id} value={t.id}>
                  {t.name}
                </option>
              ))}
            </select>
            {!loading && types.length === 0 && (
              <span className="add-env-modal__required">
                No hay tipos de ambiente cargados: cree uno antes de añadir ambientes.
              </span>
            )}
          </div>
          <div className="add-env-modal__field">
            <label className="add-env-modal__label">Piso / Bloque</label>
            <input
              className="add-env-modal__input"
              type="number"
              placeholder="Ej. 2"
              value={floor}
              onChange={(e) => setFloor(e.target.value)}
            />
          </div>
        </div>

        <div className="add-env-modal__actions">
          <button className="add-env-modal__btn-cancel" onClick={onClose}>
            Cancelar
          </button>
          <button
            className="add-env-modal__btn-save"
            onClick={handleSubmit}
            disabled={!canSubmit}
          >
            Agregar Ambiente
          </button>
        </div>

      </div>
    </div>
  )
}

export default AddEnvironmentModal
