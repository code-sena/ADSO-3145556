package com.eduaircontrol.msmonitoring.analysis.infrastructure.persistence;

import com.eduaircontrol.msmonitoring.analysis.domain.model.AnalysisResult;
import com.eduaircontrol.msmonitoring.analysis.domain.model.EnvironmentalAnalysis;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Carga explicita de lo que la API necesita de un analisis.
 *
 * <p>Las relaciones son lazy por diseno, para no penalizar las consultas de
 * listado. Pero {@code open-in-view=false} significa que la sesion se cierra al
 * salir del servicio, asi que si no se cargan aqui, el controller falla con
 * {@code LazyInitializationException} al montar la respuesta.
 *
 * <p>Se cargan las dos cosas que la respuesta siempre incluye: los resultados y el
 * estado.
 */
@Component
@RequiredArgsConstructor
class AnalysisResultLoader {

    private final AnalysisResultJpaRepository results;
    private final AnalysisStatusJpaRepository statuses;

    @Transactional(readOnly = true)
    EnvironmentalAnalysis withResults(EnvironmentalAnalysis analysis) {
        attachStatus(analysis);
        attachResults(analysis);
        return analysis;
    }

    /**
     * El estado es un proxy perezoso. Se materializa con una consulta por id en vez de
     * tocar la relacion: son cuatro filas en todo el catalogo.
     */
    private void attachStatus(EnvironmentalAnalysis analysis) {
        statuses.findAll().stream()
                .filter(status -> sameStatus(analysis, status))
                .findFirst()
                .ifPresent(analysis::setStatus);
    }

    private boolean sameStatus(EnvironmentalAnalysis analysis,
                               com.eduaircontrol.msmonitoring.analysis.domain.model.AnalysisStatus status) {
        // El proxy expone el identificador sin inicializar; comparar por id evita
        // disparar una carga para descartarla despues.
        return analysis.getStatus() != null
                && analysis.getStatus().getId() != null
                && analysis.getStatus().getId().equals(status.getId());
    }

    private void attachResults(EnvironmentalAnalysis analysis) {
        List<AnalysisResult> loaded = results.findByEnvironmentalAnalysisId(analysis.getId());
        analysis.getResults().clear();
        loaded.forEach(result -> {
            result.setEnvironmentalAnalysis(analysis);
            analysis.getResults().add(result);
        });
    }
}