package com.eduaircontrol.msmonitoring.analysis.application;

import com.eduaircontrol.msmonitoring.analysis.domain.event.EnvironmentalDataAnalyzed;
import com.eduaircontrol.msmonitoring.analysis.domain.model.AnalysisPeriod;
import com.eduaircontrol.msmonitoring.analysis.domain.model.AnalysisResult;
import com.eduaircontrol.msmonitoring.analysis.domain.model.AnalysisStatusCode;
import com.eduaircontrol.msmonitoring.analysis.domain.model.EnvironmentalAnalysis;
import com.eduaircontrol.msmonitoring.analysis.domain.port.in.RequestAnalysisUseCase;
import com.eduaircontrol.msmonitoring.analysis.domain.port.out.AnalysisRepository;
import com.eduaircontrol.msmonitoring.analysis.domain.port.out.AnalysisStatusRepository;
import com.eduaircontrol.msmonitoring.analysis.domain.port.out.DomainEventPublisher;
import com.eduaircontrol.msmonitoring.analysis.domain.service.PeriodResolver;
import com.eduaircontrol.msmonitoring.shared.exception.ConflictException;
import com.eduaircontrol.msmonitoring.shared.exception.NotFoundException;
import com.eduaircontrol.msmonitoring.shared.contract.EnvironmentLookupPort;
import com.eduaircontrol.msmonitoring.shared.contract.MeasurementAggregationPort;
import com.eduaircontrol.msmonitoring.shared.contract.VariableCatalogPort;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Solicita y calcula el analisis de un ambiente para un periodo.
 *
 * <p>Orquesta el agregado y los puertos; no contiene reglas de negocio, que viven en
 * el dominio. El ciclo de estados se persiste para trazabilidad y el calculo es
 * sincrono, por lo que la respuesta ya incluye los resultados.
 */
@Service
@RequiredArgsConstructor
public class RequestAnalysisService implements RequestAnalysisUseCase {

    private final AnalysisRepository analysisRepository;
    private final AnalysisStatusRepository statusRepository;
    private final MeasurementAggregationPort aggregationPort;
    private final VariableCatalogPort variableCatalogPort;
    private final EnvironmentLookupPort environmentLookupPort;
    private final DomainEventPublisher eventPublisher;
    private final PeriodResolver periodResolver;
    private final Clock clock;

    @Override
    @Transactional
    public EnvironmentalAnalysis execute(Command command) {
        validate(command);

        // El contrato declara referenceDate opcional, asi que si no viene se ancla al
        // momento actual. El default se aplica aqui, no en el dominio, para que
        // PeriodResolver siga siendo puro y determinista.
        Instant reference = command.referenceDate() != null
                ? command.referenceDate()
                : clock.instant();
        AnalysisPeriod.Window window = periodResolver.resolve(command.period(), reference);
        if (analysisRepository.existsByEnvironmentAndWindow(command.environmentId(), window)) {
            throw new ConflictException("Ya existe un analisis para el mismo ambiente y periodo");
        }

        EnvironmentalAnalysis analysis = EnvironmentalAnalysis.request(
                command.environmentId(), window,
                statusRepository.get(AnalysisStatusCode.PENDING),
                command.requestedBy());

        analysisRepository.save(analysis);
        analysis.start(statusRepository.get(AnalysisStatusCode.RUNNING));

        List<AnalysisResult> aggregated = aggregate(command.environmentId(), window);
        Instant computedAt = clock.instant();
        analysis.complete(statusRepository.get(AnalysisStatusCode.COMPLETED), aggregated, computedAt);
        analysisRepository.save(analysis);

        eventPublisher.publish(toEvent(analysis, aggregated, command.period(), computedAt));
        return analysis;
    }

    private List<AnalysisResult> aggregate(UUID environmentId, AnalysisPeriod.Window window) {
        return aggregationPort.aggregate(environmentId, window.start(), window.end()).stream()
                .map(item -> AnalysisResult.builder()
                        .variableId(item.variableId())
                        .minValue(item.minValue())
                        .maxValue(item.maxValue())
                        .avgValue(item.avgValue())
                        .sampleCount((int) item.sampleCount())
                        .exceedanceCount((int) item.exceedanceCount())
                        .build())
                .toList();
    }

    private EnvironmentalDataAnalyzed toEvent(EnvironmentalAnalysis analysis,
                                             List<AnalysisResult> results,
                                             AnalysisPeriod period,
                                             Instant occurredAt) {
        Map<UUID, String> codesByVariableId = variableCatalogPort.findAll().stream()
                .collect(Collectors.toMap(VariableCatalogPort.VariableRef::id,
                        VariableCatalogPort.VariableRef::code,
                        (first, second) -> first));

        List<EnvironmentalDataAnalyzed.VariableSummary> summaries = results.stream()
                .map(result -> new EnvironmentalDataAnalyzed.VariableSummary(
                        codesByVariableId.getOrDefault(result.getVariableId(), result.getVariableId().toString()),
                        result.getMinValue(),
                        result.getMaxValue(),
                        result.getAvgValue(),
                        result.getSampleCount(),
                        result.getExceedanceCount()))
                .toList();

        return new EnvironmentalDataAnalyzed(
                UUID.randomUUID(),
                analysis.getId(),
                analysis.getEducationalEnvironmentId(),
                period.name(),
                analysis.getPeriodStart(),
                analysis.getPeriodEnd(),
                occurredAt,
                summaries);
    }

    private void validate(Command command) {
        if (command.environmentId() == null) {
            throw new IllegalArgumentException("environmentId is required");
        }
        if (command.period() == null) {
            throw new IllegalArgumentException("period is required");
        }
        if (environmentLookupPort.findById(command.environmentId()).isEmpty()) {
            throw new NotFoundException("Ambiente no encontrado: " + command.environmentId());
        }
    }
}