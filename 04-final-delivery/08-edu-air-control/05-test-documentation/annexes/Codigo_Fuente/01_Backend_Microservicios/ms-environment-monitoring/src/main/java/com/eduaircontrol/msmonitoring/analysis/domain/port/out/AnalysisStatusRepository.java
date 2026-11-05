package com.eduaircontrol.msmonitoring.analysis.domain.port.out;

import com.eduaircontrol.msmonitoring.analysis.domain.model.AnalysisStatus;
import com.eduaircontrol.msmonitoring.analysis.domain.model.AnalysisStatusCode;
import java.util.List;

/**
 * Acceso al catalogo de estados del analisis. Las filas estan sembradas por
 * Liquibase, por lo que solo se leen.
 */
public interface AnalysisStatusRepository {

    AnalysisStatus get(AnalysisStatusCode code);

    List<AnalysisStatus> findAll();
}