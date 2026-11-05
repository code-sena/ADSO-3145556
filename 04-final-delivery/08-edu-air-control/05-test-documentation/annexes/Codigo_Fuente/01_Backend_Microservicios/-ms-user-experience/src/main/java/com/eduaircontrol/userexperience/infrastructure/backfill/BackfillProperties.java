package com.eduaircontrol.userexperience.infrastructure.backfill;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuracion del backfill de datos UX desde el monolito.
 *
 * <p>ADR-003 deja origen y destino en la misma instancia PostgreSQL con un esquema
 * por servicio, asi que no hace falta un segundo datasource: basta con calificar
 * las tablas por esquema. El acoplamiento es temporal y con fecha de caducidad.
 */
@ConfigurationProperties(prefix = "app.backfill")
public class BackfillProperties {

    /** Desactivado por defecto: solo debe correr si se habilita a mano. */
    private boolean enabled = false;

    /**
     * Copia todo el historico en el arranque. Separado de {@code enabled} para no
     * releer todo en cada reinicio.
     */
    private boolean initialRun = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isInitialRun() {
        return initialRun;
    }

    public void setInitialRun(boolean initialRun) {
        this.initialRun = initialRun;
    }

    /** No usado en UX (sin ventana temporal), pero se mantiene por coherencia. */
    @SuppressWarnings("unused")
    private Duration resyncWindow = Duration.ofHours(24);

    public Duration getResyncWindow() {
        return resyncWindow;
    }

    public void setResyncWindow(Duration resyncWindow) {
        this.resyncWindow = resyncWindow;
    }
}
