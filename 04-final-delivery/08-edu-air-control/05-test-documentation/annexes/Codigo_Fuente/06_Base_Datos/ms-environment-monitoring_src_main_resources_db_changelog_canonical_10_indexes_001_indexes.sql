-- Indices segun especificacion §4.2.
--
-- environment_measurement es la tabla de alto volumen: el indice compuesto
-- (sensor_installation_id, variable_id, measured_at) cubre la consulta principal
-- de agregacion y los unicos por variable o por tiempo ayudan al dashboard.
CREATE INDEX ix_environment_measurement_inst_var_time
    ON environment_monitoring.environment_measurement
    (sensor_installation_id, variable_id, measured_at);

CREATE INDEX ix_environment_measurement_variable
    ON environment_monitoring.environment_measurement (variable_id);

CREATE INDEX ix_environment_measurement_time
    ON environment_monitoring.environment_measurement (measured_at);

-- Umbrales vigentes por (variable, tipo, severidad). Sin solape por periodo.
CREATE INDEX ix_variable_threshold_var_type_sev
    ON environment_monitoring.variable_threshold
    (variable_id, environment_type_id, severity_id, valid_to);

CREATE INDEX ix_variable_threshold_type
    ON environment_monitoring.variable_threshold (environment_type_id);

-- Analisis: listado por ambiente y busqueda por ventana.
CREATE INDEX ix_environmental_analysis_environment
    ON environment_monitoring.environmental_analysis (educational_environment_id);

CREATE INDEX ix_environmental_analysis_period_start
    ON environment_monitoring.environmental_analysis (period_start);

CREATE INDEX ix_environmental_analysis_period_end
    ON environment_monitoring.environmental_analysis (period_end);

CREATE INDEX ix_environmental_analysis_status
    ON environment_monitoring.environmental_analysis (analysis_status_id);

-- Resultados: siempre junto a su analisis y por variable.
CREATE INDEX ix_analysis_result_analysis
    ON environment_monitoring.analysis_result (environmental_analysis_id);

CREATE INDEX ix_analysis_result_variable
    ON environment_monitoring.analysis_result (variable_id);

-- Alertas: listado por ambiente y por fecha de generacion.
CREATE INDEX ix_environment_alert_environment
    ON environment_monitoring.environment_alert (educational_environment_id);

CREATE INDEX ix_environment_alert_raised
    ON environment_monitoring.environment_alert (raised_at DESC);

-- Proyeccion: resolucion instalacion -> ambiente.
CREATE INDEX ix_installation_projection_environment
    ON environment_monitoring.installation_projection (educational_environment_id);
