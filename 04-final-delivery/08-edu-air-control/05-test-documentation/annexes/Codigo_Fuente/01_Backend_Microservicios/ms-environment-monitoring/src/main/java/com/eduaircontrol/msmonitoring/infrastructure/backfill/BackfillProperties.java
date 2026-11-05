package com.eduaircontrol.msmonitoring.infrastructure.backfill;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuracion del backfill historico.
 *
 * <p>El backfill lee de la base de datos del monolito. Es acoplamiento deliberado
 * y con fecha de caducidad: existe unicamente para el corte inicial, porque las
 * mediciones sembradas en desarrollo (y todo el historico anterior al corte)
 * nunca pasaron por el outbox. Una vez agotado, la unica fuente de verdad es el
 * broker.
 */
@ConfigurationProperties(prefix = "app.backfill")
public class BackfillProperties {

    /** Desactivado por defecto: solo debe correr si se configura la fuente. */
    private boolean enabled = false;

    /**
     * Copia todo el historico en el arranque. A proposito separado de
     * {@code enabled}: la resincronizacion periodica es barata y puede estar
     * siempre activa, pero releer 400 dias en cada reinicio no lo es.
     */
    private boolean initialRun = false;


    /** Ventana que se resincroniza en cada pasada, para cerrar huecos del broker. */
    private Duration resyncWindow = Duration.ofHours(24);

    /** Cada cuanto se ejecuta la resincronizacion. */
    private Duration resyncInterval = Duration.ofHours(6);

    /** Filas por lote al copiar. */
    private int batchSize = 1000;

    /** Cuanto historico se copia en el corte inicial (dias hacia atras). */
    private int initialDays = 400;

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


    public Duration getResyncWindow() {
        return resyncWindow;
    }

    public void setResyncWindow(Duration resyncWindow) {
        this.resyncWindow = resyncWindow;
    }

    public Duration getResyncInterval() {
        return resyncInterval;
    }

    public void setResyncInterval(Duration resyncInterval) {
        this.resyncInterval = resyncInterval;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getInitialDays() {
        return initialDays;
    }

    public void setInitialDays(int initialDays) {
        this.initialDays = initialDays;
    }

}