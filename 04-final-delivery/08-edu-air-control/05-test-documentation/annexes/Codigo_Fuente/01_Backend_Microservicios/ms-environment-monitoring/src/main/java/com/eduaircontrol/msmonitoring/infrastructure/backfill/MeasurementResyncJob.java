package com.eduaircontrol.msmonitoring.infrastructure.backfill;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Re-sincroniza periodicamente la ventana reciente desde el monolito.
 *
 * <p>Cubre lo que el broker puede perder: si ms-environment-monitoring estuvo caido, los
 * eventos quedan en el outbox del monolito y se reenvian solos, pero el read model
 * tendria un hueco si un evento se perdio en el broker. Como la resincronizacion
 * es idempotente, repetirla es barato y no duplica nada.
 *
 * <p>Se solapa a proposito con la ventana anterior en vez de pegarlas: un minuto
 * de solapamiento absorbe el desfase de reloj entre los dos servicios.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.backfill", name = "enabled", havingValue = "true")
public class MeasurementResyncJob {

    private static final int OVERLAP_MINUTES = 2;

    private final HistoricalBackfillService backfill;
    private final BackfillProperties properties;

    @Scheduled(fixedDelayString = "${app.backfill.resync-interval:PT6H}")
    public void resyncRecentWindow() {
        Instant to = Instant.now();
        Instant from = to.minus(properties.getResyncWindow()).minus(OVERLAP_MINUTES, ChronoUnit.MINUTES);
        try {
            backfill.copyWindow(from, to);
        } catch (RuntimeException e) {
            // Un fallo aqui no debe tumbar el servicio: el broker sigue siendo la
            // via principal y esta solo es la red de seguridad.
            log.error("No se pudo resincronizar la ventana [{} a {}): {}",
                    from, to, e.getMessage(), e);
        }
    }
}