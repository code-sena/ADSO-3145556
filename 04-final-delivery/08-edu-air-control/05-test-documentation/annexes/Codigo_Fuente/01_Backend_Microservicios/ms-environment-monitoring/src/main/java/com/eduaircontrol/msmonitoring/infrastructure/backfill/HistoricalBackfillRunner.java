package com.eduaircontrol.msmonitoring.infrastructure.backfill;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Corte inicial: copia todo el historico disponible y termina.
 *
 * <p>Arranca solo con {@code app.backfill.initial-run=true}, que hay que pasar a
 * mano. Deliberadamente <b>no</b> se activa con {@code app.backfill.enabled}: ese
 * flag habilita la resincronizacion periodica, que es barata, pero copiar 400 dias
 * en cada reinicio del servicio seria un desastre.
 *
 * <p>Se ejecuta por ventanas de 30 dias para no cargar en memoria todo el
 * historico de golpe: con el seed de desarrollo son ~93k filas, que en una sola
 * ventana serian cientos de MB de objetos Java.
 *
 * <p>Como cada ventana es idempotente, cortarlo a mitad no deja nada que no se
 * pueda reanudar: basta relanzarlo.
 *
 * <pre>
 *   ./mvnw.cmd spring-boot:run \
 *     -Dspring-boot.run.profiles=backfill \
 *     -Dspring-boot.run.arguments=--app.backfill.initial-run=true
 * </pre>
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.backfill", name = "initial-run", havingValue = "true")
public class HistoricalBackfillRunner implements ApplicationRunner {

    private static final Duration WINDOW = Duration.ofDays(30);

    private final HistoricalBackfillService backfill;
    private final BackfillProperties properties;

    @Override
    public void run(ApplicationArguments args) {
        Instant to = Instant.now();
        Instant from = to.minus(properties.getInitialDays(), ChronoUnit.DAYS);

        log.info("Backfill inicial: copiando historico de {} hasta {}", from, to);

        int total = 0;
        int windows = 0;
        for (Instant cursor = from; cursor.isBefore(to); cursor = cursor.plus(WINDOW)) {
            Instant windowEnd = cursor.plus(WINDOW).isAfter(to) ? to : cursor.plus(WINDOW);
            total += backfill.copyWindow(cursor, windowEnd);
            windows++;
            log.info("Backfill inicial: ventana {}/{} completada ({} filas hasta {})",
                    windows, to, total, windowEnd);
        }

        log.info("Backfill inicial completado: {} medicion(es) copiadas en {} ventana(s)",
                total, windows);
    }
}