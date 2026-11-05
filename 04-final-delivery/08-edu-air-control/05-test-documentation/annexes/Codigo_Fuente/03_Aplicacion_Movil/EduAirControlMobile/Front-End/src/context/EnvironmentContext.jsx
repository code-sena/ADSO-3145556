import { createContext, useContext, useState, useEffect, useCallback } from 'react'
import { useToast } from '../shared/components/Toast/Toast.jsx'
import environmentService from '../modules/environment/services/environmentService'

const EnvironmentContext = createContext()

export function EnvironmentProvider({ children }) {
  const toast = useToast()
  const [environments, setEnvironments] = useState([])
  const [loading, setLoading] = useState(true)

  const refreshEnvironments = useCallback(() => {
    return environmentService
      .getAll()
      .then(setEnvironments)
      .catch((e) => toast.error(e.message))
      .finally(() => setLoading(false))
  }, [toast])

  useEffect(() => {
    refreshEnvironments()
  }, [refreshEnvironments])

  const toggleFavorite = useCallback((id) => {
    return environmentService
      .toggleFavorite(id)
      .then((isFavorite) => {
        setEnvironments((prev) => prev.map((env) => (env.id === id ? { ...env, isFavorite } : env)))
        return isFavorite
      })
      .catch((e) => {
        toast.error(e.message)
        return null
      })
  }, [toast])

  const addEnvironment = useCallback((data) => {
    environmentService
      .create(data)
      .then((newEnv) => {
        setEnvironments((prev) => [...prev, newEnv])
      })
      .catch((e) => toast.error(e.message))
  }, [toast])

  const editEnvironment = useCallback((id, data) => {
    environmentService
      .update(id, data)
      .then((updated) => {
        setEnvironments((prev) => prev.map((env) => (env.id === id ? updated : env)))
      })
      .catch((e) => toast.error(e.message))
  }, [toast])

  const deleteEnvironment = useCallback((id) => {
    environmentService
      .delete(id)
      .then(() => {
        setEnvironments((prev) => prev.filter((env) => env.id !== id))
      })
      .catch((e) => toast.error(e.message))
  }, [toast])

  return (
    <EnvironmentContext.Provider
      value={{
        environments,
        loading,
        toggleFavorite,
        addEnvironment,
        editEnvironment,
        deleteEnvironment,
        refreshEnvironments,
      }}
    >
      {children}
    </EnvironmentContext.Provider>
  )
}

export function useEnvironment() {
  return useContext(EnvironmentContext)
}

export const useEnvironments = useEnvironment
