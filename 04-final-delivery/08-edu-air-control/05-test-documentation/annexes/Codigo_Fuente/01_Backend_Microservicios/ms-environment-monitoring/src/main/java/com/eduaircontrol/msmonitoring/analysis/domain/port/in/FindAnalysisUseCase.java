package com.eduaircontrol.msmonitoring.analysis.domain.port.in;

import com.eduaircontrol.msmonitoring.analysis.domain.model.AnalysisPeriod;
import com.eduaircontrol.msmonitoring.analysis.domain.model.AnalysisStatusCode;
import com.eduaircontrol.msmonitoring.analysis.domain.model.EnvironmentalAnalysis;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Caso de uso de entrada: consulta de analisis historicos materializados.
 */
public interface FindAnalysisUseCase {

    Optional<EnvironmentalAnalysis> byId(UUID id);

    Page<EnvironmentalAnalysis> search(Query query);

    /**
     * Ultimo analisis completado de un ambiente para el periodo que contiene el
     * instante de referencia. Es la lectura que consume el dashboard.
     */
    Optional<EnvironmentalAnalysis> latestCompleted(UUID environmentId,
                                                    AnalysisPeriod period,
                                                    java.time.Instant referenceDate);

    record Query(UUID environmentId,
                 AnalysisStatusCode status,
                 Pageable pageable) {
    }
}