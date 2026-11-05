package com.eduaircontrol.mssensor.domain.port.out;

import com.eduaircontrol.mssensor.domain.model.SensorInstallation;

/**
 * Publica el ciclo de vida de las instalaciones hacia otros servicios.
 *
 * <p>Es un puerto de salida: el dominio no sabe si esto va a un outbox, a un
 * broker o a ninguno. Lo consume ms-environment-monitoring para mantener su
 * proyeccion {@code installation_projection}, de la que depende la atribucion de
 * las mediciones a un ambiente.
 *
 * <p>Debe invocarse dentro de la misma transaccion que el cambio de negocio (patron
 * outbox, ADR-007): o se guarda el cambio con su evento, o no se guarda ninguno.
 */
public interface InstallationEventPublisher {

    void installed(SensorInstallation installation);

    void removed(SensorInstallation installation);
}
