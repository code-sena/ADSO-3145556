package com.eduaircontrol.userexperience.infrastructure.backfill;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Corte inicial: copia todo el historico UX y termina.
 *
 * <p>Arranca solo con {@code app.backfill.initial-run=true}, que hay que pasar a
 * mano. Es idempotente: repetirlo no duplica.
 *
 * <pre>
 *   ./mvnw.cmd spring-boot:run \
 *     -Dspring-boot.run.arguments=--app.backfill.initial-run=true
 * </pre>
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.backfill", name = "initial-run", havingValue = "true")
public class UxBackfillRunner implements ApplicationRunner {

    private final UxBackfillService backfill;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Backfill UX inicial: copiando historico del monolito");
        int total = backfill.copyAll();
        log.info("Backfill UX completado: {} fila(s)", total);
    }
}
