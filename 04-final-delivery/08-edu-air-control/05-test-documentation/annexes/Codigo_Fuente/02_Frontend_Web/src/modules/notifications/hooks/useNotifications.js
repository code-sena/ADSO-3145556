import { useMemo, useState, useEffect, useCallback } from "react";
import { useEnvironments } from "../../../context/useEnvironment";
import alertService from "../services/alertService";

const SEVERITY_TO_TYPE = {
  CRITICAL: "danger",
  WARNING: "warning",
  INFO: "info",
};

const SEVERITY_TO_LABEL = {
  CRITICAL: "notifications.thresholdAlert",
  WARNING: "notifications.thresholdWarning",
  INFO: "notifications.thresholdInfo",
};

/**
 * Alertas reales del backend (ms-environment-monitoring).
 *
 * <p>Antes se sintetizaban en el navegador a partir de {@code env.co2}/{@code env.temp},
 * que nunca se cargaban: el panel solo mostraba el resumen diario y estaba vacío.
 * Ahora se leen de {@code GET /api/v1/alerts} y el reconocimiento es
 * {@code POST /api/v1/alerts/{id}/acknowledge}, que es lo que el servicio guarda.
 */
export const useNotifications = () => {
  const { environments } = useEnvironments();
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  const nameOf = useCallback(
    (environmentId) => {
      const env = environments.find((e) => e.id === environmentId);
      return env?.name || env?.code || environmentId;
    },
    [environments]
  );

  const load = useCallback(async () => {
    setLoading(true);
    try {
      const alerts = await alertService.listActive(50);
      setNotifications(
        alerts.map((a) => ({
          id: a.id,
          type: SEVERITY_TO_TYPE[a.severityCode] || "info",
          title: "notifications.thresholdExceeded",
          message: "notifications.thresholdExceededMessage",
          data: {
            name: nameOf(a.environmentId),
            variable: a.variableCode || "",
            value: a.triggeringValue ?? "",
            severity: SEVERITY_TO_LABEL[a.severityCode] || "notifications.thresholdInfo",
          },
          time: a.raisedAt ? new Date(a.raisedAt) : new Date(),
          read: Boolean(a.acknowledgedAt),
          alertId: a.id,
        }))
      );
    } catch {
      // Sin alertas no hay nada que pintar: no romper el panel por un fallo de red.
      setNotifications([]);
    } finally {
      setLoading(false);
    }
  }, [nameOf]);

  useEffect(() => {
    load();
  }, [load]);

  const unreadCount = useMemo(
    () => notifications.filter((n) => !n.read).length,
    [notifications]
  );

  /**
   * Marca como leída en la UI y reconoce en el backend. Optimista: si el
   * reconocimiento falla, el estado local se revierte.
   */
  const markAsRead = (id) => {
    const snapshot = notifications;
    setNotifications((prev) =>
      prev.map((n) => (n.id === id ? { ...n, read: true } : n))
    );
    alertService.acknowledge(id).catch(() => setNotifications(snapshot));
  };

  /** Reconoce todas. No hay bulk en el backend: se hace una por una. */
  const markAllRead = () => {
    const snapshot = notifications;
    setNotifications((prev) => prev.map((n) => ({ ...n, read: true })));
    Promise.all(
      snapshot.filter((n) => !n.read).map((n) => alertService.acknowledge(n.id))
    ).catch(() => setNotifications(snapshot));
  };

  return {
    notifications,
    unreadCount,
    loading,
    refresh: load,
    markAsRead,
    markAllRead,
  };
};
