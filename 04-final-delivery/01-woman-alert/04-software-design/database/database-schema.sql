-- ============================================================================
--  ALERTA MUJER — Script de creación de la base de datos (solo estructura)
--  Motor: PostgreSQL 16
--  Generado el 28/10/2026 a partir de una base PostgreSQL 16 temporal con todas las
--  migraciones Liquibase de la rama main aplicadas (v1.0.0 a v1.6.0, changeset 25).
--  35 tablas en cinco esquemas. No contiene datos ni credenciales.
--
--  Esquemas:
--    identity      -> personas, cuentas, roles, perfiles, sesiones y recuperación
--    alert         -> alertas SOS, contactos, ubicaciones, evidencias, notificaciones,
--                     chat, trayectos y protección automática
--    admin         -> configuración del sistema, auditoría, moderación, informes y
--                     solicitudes de eliminación de cuenta
--    resource      -> zonas y puntos de ayuda
--    notification  -> enlaces públicos de seguimiento y sus visitas
--
--
-- PostgreSQL database dump
--


-- Dumped from database version 16.15 (Debian 16.15-1.pgdg13+2)
-- Dumped by pg_dump version 16.15 (Debian 16.15-1.pgdg13+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: admin; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA admin;


--
-- Name: alert; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA alert;


--
-- Name: identity; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA identity;


--
-- Name: notification; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA notification;


--
-- Name: resource; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA resource;


--
-- Name: forbid_audit_log_change(); Type: FUNCTION; Schema: admin; Owner: -
--

CREATE FUNCTION admin.forbid_audit_log_change() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    RAISE EXCEPTION 'admin.audit_log es de solo inserción (% bloqueado)', TG_OP
        USING ERRCODE = 'insufficient_privilege';
END;
$$;


--
-- Name: log_config_change(); Type: FUNCTION; Schema: admin; Owner: -
--

CREATE FUNCTION admin.log_config_change() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_changes TEXT;
BEGIN
    SELECT string_agg(n.key || ': ' || COALESCE(to_jsonb(OLD) ->> n.key, '(vacío)')
                      || ' → ' || COALESCE(n.value #>> '{}', '(vacío)'), '; ' ORDER BY n.key)
      INTO v_changes
    FROM jsonb_each(to_jsonb(NEW)) n
    WHERE n.key NOT IN ('updated_at', 'updated_by_admin_id', 'created_at')
      AND n.value IS DISTINCT FROM to_jsonb(OLD) -> n.key;
    IF v_changes IS NULL THEN
        RETURN NEW;
    END IF;

    INSERT INTO admin.audit_log (account_id, action, entity_type, entity_id, description, ip_address, source, created_at)
    VALUES (
        NULLIF(current_setting('app.current_account_id', true), '')::BIGINT,
        'UPDATE',
        'SYSTEM_CONFIGURATION',
        NEW.id::INTEGER,
        left('Valores: ' || v_changes, 500),
        NULLIF(current_setting('app.client_ip', true), ''),
        NULLIF(current_setting('app.source', true), ''),
        now()
    );
    RETURN NEW;
END;
$$;


--
-- Name: log_row_change(); Type: FUNCTION; Schema: admin; Owner: -
--

CREATE FUNCTION admin.log_row_change() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_row     JSONB;
    v_changed TEXT;
BEGIN
    IF TG_OP = 'UPDATE' THEN
        SELECT string_agg(n.key, ', ' ORDER BY n.key) INTO v_changed
        FROM jsonb_each(to_jsonb(NEW)) n
        WHERE n.key NOT IN ('updated_at', 'last_access')
          AND n.value IS DISTINCT FROM to_jsonb(OLD) -> n.key;
        IF v_changed IS NULL THEN
            RETURN NEW;  
        END IF;
        v_row := to_jsonb(NEW);
    ELSIF TG_OP = 'DELETE' THEN
        v_row := to_jsonb(OLD);
    ELSE
        v_row := to_jsonb(NEW);
    END IF;

    INSERT INTO admin.audit_log (account_id, action, entity_type, entity_id, description, ip_address, source, created_at)
    VALUES (
        NULLIF(current_setting('app.current_account_id', true), '')::BIGINT,
        TG_OP,
        TG_ARGV[0],
        (v_row ->> 'id')::INTEGER,
        left(CASE WHEN TG_OP = 'UPDATE' THEN 'Columnas modificadas: ' || v_changed
                  ELSE TG_TABLE_SCHEMA || '.' || TG_TABLE_NAME END, 500),
        NULLIF(current_setting('app.client_ip', true), ''),
        NULLIF(current_setting('app.source', true), ''),
        now()
    );

    RETURN COALESCE(NEW, OLD);
END;
$$;


--
-- Name: enforce_saved_route_limits(); Type: FUNCTION; Schema: alert; Owner: -
--

CREATE FUNCTION alert.enforce_saved_route_limits() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    
    
    PERFORM pg_advisory_xact_lock(hashtext('saved_route'), NEW.user_profile_id::INTEGER);

    IF TG_OP = 'INSERT' AND
       (SELECT count(*) FROM alert.saved_route WHERE user_profile_id = NEW.user_profile_id) >= 4 THEN
        RAISE EXCEPTION 'Máximo 4 recorridos por usuaria' USING ERRCODE = 'check_violation';
    END IF;

    IF NEW.is_starred AND (TG_OP = 'INSERT' OR NOT OLD.is_starred) AND
       (SELECT count(*) FROM alert.saved_route
         WHERE user_profile_id = NEW.user_profile_id AND is_starred AND id <> NEW.id) >= 2 THEN
        RAISE EXCEPTION 'Máximo 2 recorridos estrella por usuaria' USING ERRCODE = 'check_violation';
    END IF;

    RETURN NEW;
END;
$$;


--
-- Name: set_updated_at(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.set_updated_at() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: account_deletion_request; Type: TABLE; Schema: admin; Owner: -
--

CREATE TABLE admin.account_deletion_request (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    requested_by bigint NOT NULL,
    reason character varying(500) NOT NULL,
    status character varying(10) DEFAULT 'pending'::character varying NOT NULL,
    decided_by bigint,
    decision_note character varying(500),
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    decided_at timestamp without time zone,
    CONSTRAINT chk_account_deletion_status CHECK (((status)::text = ANY ((ARRAY['pending'::character varying, 'approved'::character varying, 'rejected'::character varying])::text[])))
);


--
-- Name: account_deletion_request_id_seq; Type: SEQUENCE; Schema: admin; Owner: -
--

ALTER TABLE admin.account_deletion_request ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME admin.account_deletion_request_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: alert_report; Type: TABLE; Schema: admin; Owner: -
--

CREATE TABLE admin.alert_report (
    id bigint NOT NULL,
    name character varying(200) NOT NULL,
    city character varying(100),
    status character varying(20) DEFAULT 'pending'::character varying NOT NULL,
    user_label character varying(200),
    event character varying(200),
    description text,
    format character varying(10) DEFAULT 'PDF'::character varying NOT NULL,
    alert_ids text,
    from_date timestamp without time zone,
    to_date timestamp without time zone,
    alert_status character varying(20),
    export_count integer DEFAULT 0 NOT NULL,
    created_by_user_id bigint,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp without time zone,
    CONSTRAINT chk_alert_report_export_count CHECK ((export_count >= 0)),
    CONSTRAINT chk_alert_report_format CHECK (((format)::text = ANY ((ARRAY['PDF'::character varying, 'EXCEL'::character varying])::text[]))),
    CONSTRAINT chk_alert_report_status CHECK (((status)::text = ANY ((ARRAY['pending'::character varying, 'attended'::character varying, 'closed'::character varying])::text[])))
);


--
-- Name: alert_report_id_seq; Type: SEQUENCE; Schema: admin; Owner: -
--

CREATE SEQUENCE admin.alert_report_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: alert_report_id_seq; Type: SEQUENCE OWNED BY; Schema: admin; Owner: -
--

ALTER SEQUENCE admin.alert_report_id_seq OWNED BY admin.alert_report.id;


--
-- Name: audit_log; Type: TABLE; Schema: admin; Owner: -
--

CREATE TABLE admin.audit_log (
    id bigint NOT NULL,
    account_id bigint,
    action character varying(100) NOT NULL,
    entity_type character varying(50) NOT NULL,
    entity_id integer,
    description character varying(500),
    ip_address character varying(45),
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    source character varying(120)
);


--
-- Name: audit_log_id_seq; Type: SEQUENCE; Schema: admin; Owner: -
--

ALTER TABLE admin.audit_log ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME admin.audit_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: moderation_action; Type: TABLE; Schema: admin; Owner: -
--

CREATE TABLE admin.moderation_action (
    id bigint NOT NULL,
    user_report_id bigint NOT NULL,
    admin_perfil_id integer NOT NULL,
    action_type character varying(30) NOT NULL,
    notes character varying(1000),
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now()
);


--
-- Name: moderation_action_id_seq; Type: SEQUENCE; Schema: admin; Owner: -
--

ALTER TABLE admin.moderation_action ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME admin.moderation_action_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: system_configuration; Type: TABLE; Schema: admin; Owner: -
--

CREATE TABLE admin.system_configuration (
    id bigint NOT NULL,
    singleton_key character varying(10) DEFAULT 'SYSTEM'::character varying NOT NULL,
    sos_max_duration_minutes integer NOT NULL,
    emergency_phone_number character varying(20) NOT NULL,
    admin_email character varying(150) NOT NULL,
    notifications_enabled boolean DEFAULT true NOT NULL,
    default_notification_priority character varying(20) DEFAULT 'normal'::character varying NOT NULL,
    default_theme character varying(20) DEFAULT 'light'::character varying NOT NULL,
    updated_by_admin_id integer,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    max_emergency_contacts integer DEFAULT 5 NOT NULL,
    min_app_version character varying(20),
    CONSTRAINT chk_system_configuration_max_contacts CHECK (((max_emergency_contacts >= 1) AND (max_emergency_contacts <= 20))),
    CONSTRAINT chk_system_configuration_min_app_version CHECK (((min_app_version IS NULL) OR ((min_app_version)::text ~ '^\d+(\.\d+){0,2}$'::text)))
);


--
-- Name: system_configuration_id_seq; Type: SEQUENCE; Schema: admin; Owner: -
--

ALTER TABLE admin.system_configuration ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME admin.system_configuration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: user_report; Type: TABLE; Schema: admin; Owner: -
--

CREATE TABLE admin.user_report (
    id bigint NOT NULL,
    reported_user_id bigint NOT NULL,
    reporter_user_id integer,
    reason character varying(100) NOT NULL,
    description character varying(1000) NOT NULL,
    status character varying(20) DEFAULT 'pending'::character varying NOT NULL,
    reviewed_by_admin_id integer,
    reviewed_at timestamp without time zone,
    resolution_notes character varying(1000),
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    severity character varying(10) DEFAULT 'medium'::character varying NOT NULL,
    evidence text,
    CONSTRAINT chk_user_report_severity CHECK (((severity)::text = ANY ((ARRAY['low'::character varying, 'medium'::character varying, 'high'::character varying, 'critical'::character varying])::text[]))),
    CONSTRAINT chk_user_report_status CHECK (((status)::text = ANY ((ARRAY['pending'::character varying, 'in_review'::character varying, 'reviewed'::character varying, 'resolved'::character varying, 'approved'::character varying, 'rejected'::character varying])::text[])))
);


--
-- Name: user_report_id_seq; Type: SEQUENCE; Schema: admin; Owner: -
--

ALTER TABLE admin.user_report ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME admin.user_report_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: alert; Type: TABLE; Schema: alert; Owner: -
--

CREATE TABLE alert.alert (
    id bigint NOT NULL,
    user_profile_id bigint NOT NULL,
    device_id bigint NOT NULL,
    alert_type character varying(20) DEFAULT 'main'::character varying NOT NULL,
    activation_method character varying(30) NOT NULL,
    status character varying(20) DEFAULT 'active'::character varying NOT NULL,
    message character varying(500),
    latitude numeric(9,6),
    longitude numeric(9,6),
    started_at timestamp without time zone DEFAULT now() NOT NULL,
    ended_at timestamp without time zone,
    cancelled_at timestamp without time zone,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    keep_evidence boolean DEFAULT false NOT NULL,
    escalated_at timestamp without time zone,
    extended_until timestamp without time zone,
    unanswered_at timestamp without time zone,
    duress_at timestamp without time zone,
    last_signal_at timestamp without time zone,
    signal_lost_at timestamp without time zone,
    CONSTRAINT chk_alert_activation_method CHECK (((activation_method)::text = ANY ((ARRAY['panic_button'::character varying, 'gesture'::character varying, 'voice'::character varying, 'timer'::character varying, 'manual'::character varying, 'widget'::character varying, 'suspicious_movement'::character varying, 'contact'::character varying, 'power_button'::character varying, 'signal_lost'::character varying, 'auto'::character varying])::text[]))),
    CONSTRAINT chk_alert_coordinates CHECK ((((latitude IS NULL) OR ((latitude >= ('-90'::integer)::numeric) AND (latitude <= (90)::numeric))) AND ((longitude IS NULL) OR ((longitude >= ('-180'::integer)::numeric) AND (longitude <= (180)::numeric))))),
    CONSTRAINT chk_alert_status CHECK (((status)::text = ANY ((ARRAY['active'::character varying, 'resolved'::character varying, 'cancelled'::character varying, 'expired'::character varying])::text[]))),
    CONSTRAINT chk_alert_type CHECK (((alert_type)::text = ANY ((ARRAY['main'::character varying, 'medical'::character varying, 'police'::character varying, 'fire'::character varying, 'custom'::character varying])::text[])))
);


--
-- Name: alert_contact; Type: TABLE; Schema: alert; Owner: -
--

CREATE TABLE alert.alert_contact (
    id bigint NOT NULL,
    alert_id bigint NOT NULL,
    emergency_contact_id bigint,
    channel character varying(20) NOT NULL,
    destination character varying(150) NOT NULL,
    status character varying(20) DEFAULT 'pending'::character varying NOT NULL,
    sent_at timestamp without time zone,
    delivered_at timestamp without time zone,
    attempts bigint DEFAULT 0 NOT NULL,
    error_message character varying(500),
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    seen_at timestamp without time zone
);


--
-- Name: alert_contact_id_seq; Type: SEQUENCE; Schema: alert; Owner: -
--

ALTER TABLE alert.alert_contact ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME alert.alert_contact_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: alert_id_seq; Type: SEQUENCE; Schema: alert; Owner: -
--

ALTER TABLE alert.alert ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME alert.alert_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: alert_message; Type: TABLE; Schema: alert; Owner: -
--

CREATE TABLE alert.alert_message (
    id bigint NOT NULL,
    alert_id bigint NOT NULL,
    sender_type character varying(10) NOT NULL,
    sender_name character varying(60) NOT NULL,
    body character varying(500) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT alert_message_sender_type_check CHECK (((sender_type)::text = ANY ((ARRAY['owner'::character varying, 'contact'::character varying])::text[])))
);


--
-- Name: alert_message_id_seq; Type: SEQUENCE; Schema: alert; Owner: -
--

ALTER TABLE alert.alert_message ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME alert.alert_message_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: alert_reminder; Type: TABLE; Schema: alert; Owner: -
--

CREATE TABLE alert.alert_reminder (
    id bigint NOT NULL,
    alert_id bigint NOT NULL,
    reminder_type character varying(20) NOT NULL,
    sequence_number integer NOT NULL,
    scheduled_at timestamp without time zone NOT NULL,
    sent_at timestamp without time zone,
    status character varying(20) DEFAULT 'scheduled'::character varying NOT NULL,
    error_message character varying(500),
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now()
);


--
-- Name: alert_reminder_id_seq; Type: SEQUENCE; Schema: alert; Owner: -
--

ALTER TABLE alert.alert_reminder ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME alert.alert_reminder_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: emergency_contact; Type: TABLE; Schema: alert; Owner: -
--

CREATE TABLE alert.emergency_contact (
    id bigint NOT NULL,
    user_profile_id bigint NOT NULL,
    contact_name character varying(100) NOT NULL,
    telephone character varying(20) NOT NULL,
    email character varying(150),
    relationship character varying(30),
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    photo text,
    CONSTRAINT chk_emergency_contact_telephone_format CHECK (((telephone)::text ~ '^\+?[0-9][0-9 -]{5,18}[0-9]$'::text))
);


--
-- Name: emergency_contact_id_seq; Type: SEQUENCE; Schema: alert; Owner: -
--

ALTER TABLE alert.emergency_contact ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME alert.emergency_contact_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: evidence; Type: TABLE; Schema: alert; Owner: -
--

CREATE TABLE alert.evidence (
    id bigint NOT NULL,
    alert_id bigint NOT NULL,
    media_type character varying(20) NOT NULL,
    file_url character varying(255) NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    file_name character varying(255),
    content_type character varying(100),
    size_bytes bigint,
    sha256 character(64),
    storage_key character varying(100),
    review_status character varying(20) DEFAULT 'pending'::character varying NOT NULL,
    CONSTRAINT chk_evidence_media_type CHECK (((media_type)::text = ANY ((ARRAY['photo'::character varying, 'video'::character varying, 'audio'::character varying, 'document'::character varying])::text[]))),
    CONSTRAINT chk_evidence_review_status CHECK (((review_status)::text = ANY ((ARRAY['pending'::character varying, 'verified'::character varying, 'rejected'::character varying])::text[]))),
    CONSTRAINT chk_evidence_size CHECK (((size_bytes IS NULL) OR (size_bytes >= 0)))
);


--
-- Name: evidence_id_seq; Type: SEQUENCE; Schema: alert; Owner: -
--

ALTER TABLE alert.evidence ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME alert.evidence_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: frequent_location; Type: TABLE; Schema: alert; Owner: -
--

CREATE TABLE alert.frequent_location (
    id bigint NOT NULL,
    user_profile_id bigint NOT NULL,
    name character varying(100) NOT NULL,
    address character varying(255),
    city character varying(100),
    latitude numeric(9,6) NOT NULL,
    longitude numeric(9,6) NOT NULL,
    notes character varying(255),
    risk_level character varying(20) DEFAULT 'moderada'::character varying NOT NULL,
    is_active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    CONSTRAINT chk_frequent_location_coordinates CHECK ((((latitude >= ('-90'::integer)::numeric) AND (latitude <= (90)::numeric)) AND ((longitude >= ('-180'::integer)::numeric) AND (longitude <= (180)::numeric)))),
    CONSTRAINT chk_frequent_location_risk_level CHECK (((risk_level)::text = ANY ((ARRAY['muy_segura'::character varying, 'moderada'::character varying, 'muy_insegura'::character varying])::text[])))
);


--
-- Name: frequent_location_id_seq; Type: SEQUENCE; Schema: alert; Owner: -
--

ALTER TABLE alert.frequent_location ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME alert.frequent_location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: location_log; Type: TABLE; Schema: alert; Owner: -
--

CREATE TABLE alert.location_log (
    id bigint NOT NULL,
    user_profile_id bigint NOT NULL,
    alert_id integer,
    latitude numeric(9,6) NOT NULL,
    longitude numeric(9,6) NOT NULL,
    accuracy numeric(6,2),
    recorded_at timestamp without time zone DEFAULT now() NOT NULL,
    CONSTRAINT chk_location_log_accuracy CHECK (((accuracy IS NULL) OR (accuracy >= (0)::numeric))),
    CONSTRAINT chk_location_log_coordinates CHECK ((((latitude >= ('-90'::integer)::numeric) AND (latitude <= (90)::numeric)) AND ((longitude >= ('-180'::integer)::numeric) AND (longitude <= (180)::numeric))))
);


--
-- Name: location_log_id_seq; Type: SEQUENCE; Schema: alert; Owner: -
--

ALTER TABLE alert.location_log ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME alert.location_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: notification; Type: TABLE; Schema: alert; Owner: -
--

CREATE TABLE alert.notification (
    id bigint NOT NULL,
    user_profile_id bigint NOT NULL,
    alert_id integer,
    type character varying(30) NOT NULL,
    priority character varying(20) DEFAULT 'normal'::character varying NOT NULL,
    title character varying(100) NOT NULL,
    message character varying(500) NOT NULL,
    is_read boolean DEFAULT false NOT NULL,
    is_persistent boolean DEFAULT false NOT NULL,
    expires_at timestamp without time zone,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    CONSTRAINT chk_notification_priority CHECK (((priority)::text = ANY ((ARRAY['low'::character varying, 'normal'::character varying, 'high'::character varying, 'urgent'::character varying])::text[])))
);


--
-- Name: notification_id_seq; Type: SEQUENCE; Schema: alert; Owner: -
--

ALTER TABLE alert.notification ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME alert.notification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: protection_device; Type: TABLE; Schema: alert; Owner: -
--

CREATE TABLE alert.protection_device (
    id bigint NOT NULL,
    user_profile_id bigint NOT NULL,
    user_id bigint NOT NULL,
    device_id bigint NOT NULL,
    key_hash character varying(64) NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: protection_device_id_seq; Type: SEQUENCE; Schema: alert; Owner: -
--

CREATE SEQUENCE alert.protection_device_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: protection_device_id_seq; Type: SEQUENCE OWNED BY; Schema: alert; Owner: -
--

ALTER SEQUENCE alert.protection_device_id_seq OWNED BY alert.protection_device.id;


--
-- Name: safe_trip; Type: TABLE; Schema: alert; Owner: -
--

CREATE TABLE alert.safe_trip (
    id bigint NOT NULL,
    user_profile_id bigint NOT NULL,
    user_id bigint NOT NULL,
    device_id bigint NOT NULL,
    destination character varying(120) NOT NULL,
    destination_latitude numeric(9,6),
    destination_longitude numeric(9,6),
    started_at timestamp without time zone NOT NULL,
    expected_arrival timestamp without time zone NOT NULL,
    status character varying(12) DEFAULT 'active'::character varying NOT NULL,
    reminder_sent boolean DEFAULT false NOT NULL,
    alert_id bigint,
    ended_at timestamp without time zone,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    last_seen_at timestamp without time zone,
    last_latitude numeric(9,6),
    last_longitude numeric(9,6),
    alert_reason character varying(20),
    mode character varying(10) DEFAULT 'trip'::character varying NOT NULL,
    low_battery_at timestamp without time zone,
    CONSTRAINT chk_safe_trip_alert_reason CHECK (((alert_reason)::text = ANY ((ARRAY['not_confirmed'::character varying, 'signal_lost'::character varying, 'no_answer'::character varying, 'unconfirmed'::character varying, 'duress'::character varying])::text[]))),
    CONSTRAINT chk_safe_trip_status CHECK (((status)::text = ANY ((ARRAY['active'::character varying, 'arrived'::character varying, 'cancelled'::character varying, 'alerted'::character varying, 'ended'::character varying])::text[])))
);


--
-- Name: safe_trip_id_seq; Type: SEQUENCE; Schema: alert; Owner: -
--

ALTER TABLE alert.safe_trip ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME alert.safe_trip_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: saved_route; Type: TABLE; Schema: alert; Owner: -
--

CREATE TABLE alert.saved_route (
    id bigint NOT NULL,
    user_profile_id bigint NOT NULL,
    name character varying(150) NOT NULL,
    start_label character varying(150),
    end_label character varying(150),
    city character varying(100),
    department character varying(100),
    country character varying(100),
    distance_km numeric(8,2),
    estimated_minutes integer,
    points jsonb NOT NULL,
    is_starred boolean DEFAULT false NOT NULL,
    is_manual boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    CONSTRAINT chk_saved_route_distance CHECK (((distance_km IS NULL) OR (distance_km >= (0)::numeric))),
    CONSTRAINT chk_saved_route_minutes CHECK (((estimated_minutes IS NULL) OR (estimated_minutes >= 0))),
    CONSTRAINT chk_saved_route_points CHECK (((jsonb_typeof(points) = 'array'::text) AND ((jsonb_array_length(points) >= 2) AND (jsonb_array_length(points) <= 5000))))
);


--
-- Name: saved_route_id_seq; Type: SEQUENCE; Schema: alert; Owner: -
--

CREATE SEQUENCE alert.saved_route_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: saved_route_id_seq; Type: SEQUENCE OWNED BY; Schema: alert; Owner: -
--

ALTER SEQUENCE alert.saved_route_id_seq OWNED BY alert.saved_route.id;


--
-- Name: account; Type: TABLE; Schema: identity; Owner: -
--

CREATE TABLE identity.account (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    password_hash character varying(255) NOT NULL,
    status character varying(20) DEFAULT 'active'::character varying NOT NULL,
    last_access timestamp without time zone,
    updated_at timestamp without time zone DEFAULT now(),
    CONSTRAINT chk_account_status CHECK (((status)::text = ANY ((ARRAY['active'::character varying, 'inactive'::character varying, 'blocked'::character varying, 'pending'::character varying])::text[])))
);


--
-- Name: account_id_seq; Type: SEQUENCE; Schema: identity; Owner: -
--

ALTER TABLE identity.account ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME identity.account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: admin_profile; Type: TABLE; Schema: identity; Owner: -
--

CREATE TABLE identity.admin_profile (
    id bigint NOT NULL,
    account_id bigint NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now()
);


--
-- Name: admin_profile_id_seq; Type: SEQUENCE; Schema: identity; Owner: -
--

ALTER TABLE identity.admin_profile ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME identity.admin_profile_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: alert_activation_setting; Type: TABLE; Schema: identity; Owner: -
--

CREATE TABLE identity.alert_activation_setting (
    id bigint NOT NULL,
    device_id bigint NOT NULL,
    activation_method character varying(30) NOT NULL,
    is_enabled boolean DEFAULT true NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: alert_activation_setting_id_seq; Type: SEQUENCE; Schema: identity; Owner: -
--

ALTER TABLE identity.alert_activation_setting ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME identity.alert_activation_setting_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: device; Type: TABLE; Schema: identity; Owner: -
--

CREATE TABLE identity.device (
    id bigint NOT NULL,
    account_id bigint NOT NULL,
    device_uuid character varying(255) NOT NULL,
    fcm_token character varying(255),
    brand character varying(50),
    model character varying(100),
    os_name character varying(30) NOT NULL,
    os_version character varying(30),
    app_version character varying(30),
    gps_status character varying(20) DEFAULT 'unknown'::character varying NOT NULL,
    status character varying(20) DEFAULT 'active'::character varying NOT NULL,
    last_access timestamp without time zone,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    device_name character varying(100),
    permissions character varying(400),
    permissions_updated_at timestamp without time zone,
    CONSTRAINT chk_device_gps_status CHECK (((gps_status)::text = ANY ((ARRAY['active'::character varying, 'inactive'::character varying, 'unknown'::character varying, 'denied'::character varying])::text[]))),
    CONSTRAINT chk_device_status CHECK (((status)::text = ANY ((ARRAY['active'::character varying, 'inactive'::character varying, 'blocked'::character varying, 'lost'::character varying])::text[])))
);


--
-- Name: device_id_seq; Type: SEQUENCE; Schema: identity; Owner: -
--

ALTER TABLE identity.device ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME identity.device_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: device_permission; Type: TABLE; Schema: identity; Owner: -
--

CREATE TABLE identity.device_permission (
    id bigint NOT NULL,
    device_id bigint NOT NULL,
    permission_type character varying(30) NOT NULL,
    status character varying(20) NOT NULL,
    updated_at timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: device_permission_id_seq; Type: SEQUENCE; Schema: identity; Owner: -
--

ALTER TABLE identity.device_permission ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME identity.device_permission_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: device_session; Type: TABLE; Schema: identity; Owner: -
--

CREATE TABLE identity.device_session (
    id bigint NOT NULL,
    account_id bigint NOT NULL,
    device_id bigint NOT NULL,
    refresh_token_hash character varying(255) NOT NULL,
    status character varying(20) DEFAULT 'active'::character varying NOT NULL,
    expires_at timestamp without time zone NOT NULL,
    last_used_at timestamp without time zone,
    revoked_at timestamp without time zone,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    CONSTRAINT chk_device_session_status CHECK (((status)::text = ANY ((ARRAY['active'::character varying, 'expired'::character varying, 'revoked'::character varying])::text[])))
);


--
-- Name: device_session_id_seq; Type: SEQUENCE; Schema: identity; Owner: -
--

ALTER TABLE identity.device_session ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME identity.device_session_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: recovery_request; Type: TABLE; Schema: identity; Owner: -
--

CREATE TABLE identity.recovery_request (
    id bigint NOT NULL,
    account_id bigint NOT NULL,
    method character varying(20) NOT NULL,
    token_hash character varying(255) NOT NULL,
    expires_at timestamp without time zone NOT NULL,
    used_at timestamp without time zone,
    status character varying(20) DEFAULT 'pending'::character varying NOT NULL,
    attempts bigint DEFAULT 0 NOT NULL,
    created_at timestamp without time zone DEFAULT now(),
    requested_ip character varying(45),
    updated_at timestamp without time zone DEFAULT now()
);


--
-- Name: recovery_request_id_seq; Type: SEQUENCE; Schema: identity; Owner: -
--

ALTER TABLE identity.recovery_request ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME identity.recovery_request_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: role; Type: TABLE; Schema: identity; Owner: -
--

CREATE TABLE identity.role (
    id bigint NOT NULL,
    name character varying(30) NOT NULL,
    description character varying(255),
    updated_at timestamp without time zone DEFAULT now()
);


--
-- Name: role_id_seq; Type: SEQUENCE; Schema: identity; Owner: -
--

ALTER TABLE identity.role ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME identity.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: user_preference; Type: TABLE; Schema: identity; Owner: -
--

CREATE TABLE identity.user_preference (
    id bigint NOT NULL,
    user_profile_id bigint NOT NULL,
    language character varying(10) DEFAULT 'es'::character varying NOT NULL,
    theme character varying(20) DEFAULT 'light'::character varying NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now()
);


--
-- Name: user_preference_id_seq; Type: SEQUENCE; Schema: identity; Owner: -
--

ALTER TABLE identity.user_preference ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME identity.user_preference_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: user_profile; Type: TABLE; Schema: identity; Owner: -
--

CREATE TABLE identity.user_profile (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    profile_photo_url text,
    tutorial_completed boolean DEFAULT false NOT NULL,
    tutorial_seen_at timestamp without time zone,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now()
);


--
-- Name: user_profile_id_seq; Type: SEQUENCE; Schema: identity; Owner: -
--

ALTER TABLE identity.user_profile ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME identity.user_profile_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: users; Type: TABLE; Schema: identity; Owner: -
--

CREATE TABLE identity.users (
    id bigint NOT NULL,
    role_id bigint DEFAULT 1 NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    telephone character varying(20),
    email character varying(150) NOT NULL,
    document_number character varying(20),
    document_type character varying(20),
    birthdate date,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    terms_accepted_at timestamp without time zone,
    terms_version character varying(20),
    CONSTRAINT chk_users_email_format CHECK (((email)::text ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'::text)),
    CONSTRAINT chk_users_telephone_format CHECK (((telephone IS NULL) OR ((telephone)::text ~ '^\+?[0-9][0-9 -]{5,18}[0-9]$'::text)))
);


--
-- Name: users_id_seq; Type: SEQUENCE; Schema: identity; Owner: -
--

ALTER TABLE identity.users ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME identity.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: tracking_link; Type: TABLE; Schema: notification; Owner: -
--

CREATE TABLE notification.tracking_link (
    id bigint NOT NULL,
    alert_id bigint NOT NULL,
    token_hash character(64) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    revoked_at timestamp without time zone
);


--
-- Name: tracking_link_id_seq; Type: SEQUENCE; Schema: notification; Owner: -
--

ALTER TABLE notification.tracking_link ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME notification.tracking_link_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: tracking_view; Type: TABLE; Schema: notification; Owner: -
--

CREATE TABLE notification.tracking_view (
    alert_id bigint NOT NULL,
    visitor character varying(64) NOT NULL,
    first_seen timestamp without time zone DEFAULT now() NOT NULL,
    last_seen timestamp without time zone DEFAULT now() NOT NULL
);


--
-- Name: emergency_resource; Type: TABLE; Schema: resource; Owner: -
--

CREATE TABLE resource.emergency_resource (
    id bigint NOT NULL,
    name character varying(150) NOT NULL,
    resource_type character varying(40) NOT NULL,
    telephone character varying(30),
    secondary_telephone character varying(30),
    email character varying(150),
    address character varying(255),
    city character varying(100) NOT NULL,
    latitude numeric(9,6),
    longitude numeric(9,6),
    description character varying(500),
    is_active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    CONSTRAINT chk_emergency_resource_type CHECK (((resource_type)::text = ANY ((ARRAY['emergency_line'::character varying, 'police_station'::character varying, 'police_post'::character varying, 'health_center'::character varying, 'hospital'::character varying, 'support_organization'::character varying, 'fire_station'::character varying, 'ambulance'::character varying])::text[])))
);


--
-- Name: emergency_resource_id_seq; Type: SEQUENCE; Schema: resource; Owner: -
--

ALTER TABLE resource.emergency_resource ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME resource.emergency_resource_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: resource_call; Type: TABLE; Schema: resource; Owner: -
--

CREATE TABLE resource.resource_call (
    id bigint NOT NULL,
    emergency_resource_id bigint NOT NULL,
    user_profile_id bigint NOT NULL,
    alert_id integer,
    device_id integer,
    telephone_dialed character varying(30) NOT NULL,
    status character varying(20) NOT NULL,
    started_at timestamp without time zone DEFAULT now() NOT NULL,
    ended_at timestamp without time zone,
    duration_seconds integer,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now()
);


--
-- Name: resource_call_id_seq; Type: SEQUENCE; Schema: resource; Owner: -
--

ALTER TABLE resource.resource_call ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME resource.resource_call_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: zone; Type: TABLE; Schema: resource; Owner: -
--

CREATE TABLE resource.zone (
    id bigint NOT NULL,
    created_by_admin_id integer,
    name character varying(150) NOT NULL,
    zone_type character varying(20) NOT NULL,
    risk_level character varying(20) DEFAULT 'medium'::character varying NOT NULL,
    description character varying(500),
    address character varying(255),
    city character varying(100) NOT NULL,
    latitude numeric(9,6) NOT NULL,
    longitude numeric(9,6) NOT NULL,
    radius_meters numeric(8,2) NOT NULL,
    is_active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    CONSTRAINT chk_zone_coordinates CHECK ((((latitude >= ('-90'::integer)::numeric) AND (latitude <= (90)::numeric)) AND ((longitude >= ('-180'::integer)::numeric) AND (longitude <= (180)::numeric)))),
    CONSTRAINT chk_zone_radius_positive CHECK ((radius_meters > (0)::numeric)),
    CONSTRAINT chk_zone_risk_level CHECK (((risk_level)::text = ANY ((ARRAY['low'::character varying, 'medium'::character varying, 'high'::character varying])::text[]))),
    CONSTRAINT chk_zone_type CHECK (((zone_type)::text = ANY ((ARRAY['risk'::character varying, 'safe'::character varying, 'help'::character varying, 'policia'::character varying, 'asistencia'::character varying])::text[])))
);


--
-- Name: zone_id_seq; Type: SEQUENCE; Schema: resource; Owner: -
--

ALTER TABLE resource.zone ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME resource.zone_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: zone_report; Type: TABLE; Schema: resource; Owner: -
--

CREATE TABLE resource.zone_report (
    id bigint NOT NULL,
    zone_id bigint NOT NULL,
    user_profile_id bigint NOT NULL,
    classification character varying(20) NOT NULL,
    comment character varying(500),
    status character varying(20) DEFAULT 'pending'::character varying NOT NULL,
    reviewed_by_admin_id integer,
    reviewed_at timestamp without time zone,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    updated_at timestamp without time zone DEFAULT now(),
    CONSTRAINT chk_zone_report_status CHECK (((status)::text = ANY ((ARRAY['pending'::character varying, 'approved'::character varying, 'rejected'::character varying])::text[])))
);


--
-- Name: zone_report_id_seq; Type: SEQUENCE; Schema: resource; Owner: -
--

ALTER TABLE resource.zone_report ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME resource.zone_report_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: alert_report id; Type: DEFAULT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.alert_report ALTER COLUMN id SET DEFAULT nextval('admin.alert_report_id_seq'::regclass);


--
-- Name: protection_device id; Type: DEFAULT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.protection_device ALTER COLUMN id SET DEFAULT nextval('alert.protection_device_id_seq'::regclass);


--
-- Name: saved_route id; Type: DEFAULT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.saved_route ALTER COLUMN id SET DEFAULT nextval('alert.saved_route_id_seq'::regclass);


--
-- Name: account_deletion_request account_deletion_request_pkey; Type: CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.account_deletion_request
    ADD CONSTRAINT account_deletion_request_pkey PRIMARY KEY (id);


--
-- Name: alert_report alert_report_pkey; Type: CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.alert_report
    ADD CONSTRAINT alert_report_pkey PRIMARY KEY (id);


--
-- Name: audit_log audit_log_pkey; Type: CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.audit_log
    ADD CONSTRAINT audit_log_pkey PRIMARY KEY (id);


--
-- Name: moderation_action moderation_action_pkey; Type: CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.moderation_action
    ADD CONSTRAINT moderation_action_pkey PRIMARY KEY (id);


--
-- Name: system_configuration system_configuration_pkey; Type: CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.system_configuration
    ADD CONSTRAINT system_configuration_pkey PRIMARY KEY (id);


--
-- Name: system_configuration system_configuration_singleton_key_key; Type: CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.system_configuration
    ADD CONSTRAINT system_configuration_singleton_key_key UNIQUE (singleton_key);


--
-- Name: user_report user_report_pkey; Type: CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.user_report
    ADD CONSTRAINT user_report_pkey PRIMARY KEY (id);


--
-- Name: alert_contact alert_contact_pkey; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.alert_contact
    ADD CONSTRAINT alert_contact_pkey PRIMARY KEY (id);


--
-- Name: alert_message alert_message_pkey; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.alert_message
    ADD CONSTRAINT alert_message_pkey PRIMARY KEY (id);


--
-- Name: alert alert_pkey; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.alert
    ADD CONSTRAINT alert_pkey PRIMARY KEY (id);


--
-- Name: alert_reminder alert_reminder_pkey; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.alert_reminder
    ADD CONSTRAINT alert_reminder_pkey PRIMARY KEY (id);


--
-- Name: emergency_contact emergency_contact_pkey; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.emergency_contact
    ADD CONSTRAINT emergency_contact_pkey PRIMARY KEY (id);


--
-- Name: evidence evidence_pkey; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.evidence
    ADD CONSTRAINT evidence_pkey PRIMARY KEY (id);


--
-- Name: frequent_location frequent_location_pkey; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.frequent_location
    ADD CONSTRAINT frequent_location_pkey PRIMARY KEY (id);


--
-- Name: location_log location_log_pkey; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.location_log
    ADD CONSTRAINT location_log_pkey PRIMARY KEY (id);


--
-- Name: notification notification_pkey; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- Name: protection_device protection_device_key_hash_key; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.protection_device
    ADD CONSTRAINT protection_device_key_hash_key UNIQUE (key_hash);


--
-- Name: protection_device protection_device_pkey; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.protection_device
    ADD CONSTRAINT protection_device_pkey PRIMARY KEY (id);


--
-- Name: safe_trip safe_trip_pkey; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.safe_trip
    ADD CONSTRAINT safe_trip_pkey PRIMARY KEY (id);


--
-- Name: saved_route saved_route_pkey; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.saved_route
    ADD CONSTRAINT saved_route_pkey PRIMARY KEY (id);


--
-- Name: alert_contact uk_alert_contact; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.alert_contact
    ADD CONSTRAINT uk_alert_contact UNIQUE (alert_id, emergency_contact_id, channel);


--
-- Name: evidence uk_evidence_storage_key; Type: CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.evidence
    ADD CONSTRAINT uk_evidence_storage_key UNIQUE (storage_key);


--
-- Name: account account_pkey; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


--
-- Name: account account_user_id_key; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.account
    ADD CONSTRAINT account_user_id_key UNIQUE (user_id);


--
-- Name: admin_profile admin_profile_account_id_key; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.admin_profile
    ADD CONSTRAINT admin_profile_account_id_key UNIQUE (account_id);


--
-- Name: admin_profile admin_profile_pkey; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.admin_profile
    ADD CONSTRAINT admin_profile_pkey PRIMARY KEY (id);


--
-- Name: alert_activation_setting alert_activation_setting_pkey; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.alert_activation_setting
    ADD CONSTRAINT alert_activation_setting_pkey PRIMARY KEY (id);


--
-- Name: device device_device_uuid_key; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.device
    ADD CONSTRAINT device_device_uuid_key UNIQUE (device_uuid);


--
-- Name: device_permission device_permission_pkey; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.device_permission
    ADD CONSTRAINT device_permission_pkey PRIMARY KEY (id);


--
-- Name: device device_pkey; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.device
    ADD CONSTRAINT device_pkey PRIMARY KEY (id);


--
-- Name: device_session device_session_pkey; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.device_session
    ADD CONSTRAINT device_session_pkey PRIMARY KEY (id);


--
-- Name: device_session device_session_refresh_token_hash_key; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.device_session
    ADD CONSTRAINT device_session_refresh_token_hash_key UNIQUE (refresh_token_hash);


--
-- Name: recovery_request recovery_request_pkey; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.recovery_request
    ADD CONSTRAINT recovery_request_pkey PRIMARY KEY (id);


--
-- Name: recovery_request recovery_request_token_hash_key; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.recovery_request
    ADD CONSTRAINT recovery_request_token_hash_key UNIQUE (token_hash);


--
-- Name: role role_name_key; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.role
    ADD CONSTRAINT role_name_key UNIQUE (name);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: alert_activation_setting uk_alert_activation_setting; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.alert_activation_setting
    ADD CONSTRAINT uk_alert_activation_setting UNIQUE (device_id, activation_method);


--
-- Name: device_permission uk_device_permission; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.device_permission
    ADD CONSTRAINT uk_device_permission UNIQUE (device_id, permission_type);


--
-- Name: user_preference user_preference_pkey; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.user_preference
    ADD CONSTRAINT user_preference_pkey PRIMARY KEY (id);


--
-- Name: user_preference user_preference_user_profile_id_key; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.user_preference
    ADD CONSTRAINT user_preference_user_profile_id_key UNIQUE (user_profile_id);


--
-- Name: user_profile user_profile_pkey; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.user_profile
    ADD CONSTRAINT user_profile_pkey PRIMARY KEY (id);


--
-- Name: user_profile user_profile_user_id_key; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.user_profile
    ADD CONSTRAINT user_profile_user_id_key UNIQUE (user_id);


--
-- Name: users users_document_number_key; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.users
    ADD CONSTRAINT users_document_number_key UNIQUE (document_number);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_telephone_key; Type: CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.users
    ADD CONSTRAINT users_telephone_key UNIQUE (telephone);


--
-- Name: tracking_link tracking_link_pkey; Type: CONSTRAINT; Schema: notification; Owner: -
--

ALTER TABLE ONLY notification.tracking_link
    ADD CONSTRAINT tracking_link_pkey PRIMARY KEY (id);


--
-- Name: tracking_link tracking_link_token_hash_key; Type: CONSTRAINT; Schema: notification; Owner: -
--

ALTER TABLE ONLY notification.tracking_link
    ADD CONSTRAINT tracking_link_token_hash_key UNIQUE (token_hash);


--
-- Name: tracking_view tracking_view_pkey; Type: CONSTRAINT; Schema: notification; Owner: -
--

ALTER TABLE ONLY notification.tracking_view
    ADD CONSTRAINT tracking_view_pkey PRIMARY KEY (alert_id, visitor);


--
-- Name: emergency_resource emergency_resource_pkey; Type: CONSTRAINT; Schema: resource; Owner: -
--

ALTER TABLE ONLY resource.emergency_resource
    ADD CONSTRAINT emergency_resource_pkey PRIMARY KEY (id);


--
-- Name: resource_call resource_call_pkey; Type: CONSTRAINT; Schema: resource; Owner: -
--

ALTER TABLE ONLY resource.resource_call
    ADD CONSTRAINT resource_call_pkey PRIMARY KEY (id);


--
-- Name: zone_report uk_zone_report; Type: CONSTRAINT; Schema: resource; Owner: -
--

ALTER TABLE ONLY resource.zone_report
    ADD CONSTRAINT uk_zone_report UNIQUE (zone_id, user_profile_id);


--
-- Name: zone zone_pkey; Type: CONSTRAINT; Schema: resource; Owner: -
--

ALTER TABLE ONLY resource.zone
    ADD CONSTRAINT zone_pkey PRIMARY KEY (id);


--
-- Name: zone_report zone_report_pkey; Type: CONSTRAINT; Schema: resource; Owner: -
--

ALTER TABLE ONLY resource.zone_report
    ADD CONSTRAINT zone_report_pkey PRIMARY KEY (id);


--
-- Name: idx_account_deletion_status; Type: INDEX; Schema: admin; Owner: -
--

CREATE INDEX idx_account_deletion_status ON admin.account_deletion_request USING btree (status);


--
-- Name: idx_alert_report_created_at; Type: INDEX; Schema: admin; Owner: -
--

CREATE INDEX idx_alert_report_created_at ON admin.alert_report USING btree (created_at DESC);


--
-- Name: idx_audit_log_account_id; Type: INDEX; Schema: admin; Owner: -
--

CREATE INDEX idx_audit_log_account_id ON admin.audit_log USING btree (account_id);


--
-- Name: idx_audit_log_created_at; Type: INDEX; Schema: admin; Owner: -
--

CREATE INDEX idx_audit_log_created_at ON admin.audit_log USING btree (created_at DESC);


--
-- Name: idx_audit_log_entity; Type: INDEX; Schema: admin; Owner: -
--

CREATE INDEX idx_audit_log_entity ON admin.audit_log USING btree (entity_type, entity_id);


--
-- Name: idx_moderation_action_user_report_id; Type: INDEX; Schema: admin; Owner: -
--

CREATE INDEX idx_moderation_action_user_report_id ON admin.moderation_action USING btree (user_report_id);


--
-- Name: idx_user_report_reported_user_id; Type: INDEX; Schema: admin; Owner: -
--

CREATE INDEX idx_user_report_reported_user_id ON admin.user_report USING btree (reported_user_id);


--
-- Name: idx_user_report_reporter_user_id; Type: INDEX; Schema: admin; Owner: -
--

CREATE INDEX idx_user_report_reporter_user_id ON admin.user_report USING btree (reporter_user_id);


--
-- Name: idx_user_report_status_created; Type: INDEX; Schema: admin; Owner: -
--

CREATE INDEX idx_user_report_status_created ON admin.user_report USING btree (status, created_at DESC);


--
-- Name: idx_alert_active; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_alert_active ON alert.alert USING btree (started_at DESC) WHERE ((status)::text = 'active'::text);


--
-- Name: idx_alert_contact_emergency_contact_id; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_alert_contact_emergency_contact_id ON alert.alert_contact USING btree (emergency_contact_id);


--
-- Name: idx_alert_device_id; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_alert_device_id ON alert.alert USING btree (device_id);


--
-- Name: idx_alert_message_alert; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_alert_message_alert ON alert.alert_message USING btree (alert_id, id);


--
-- Name: idx_alert_reminder_alert_id; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_alert_reminder_alert_id ON alert.alert_reminder USING btree (alert_id);


--
-- Name: idx_alert_reminder_pending; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_alert_reminder_pending ON alert.alert_reminder USING btree (scheduled_at) WHERE ((status)::text = 'scheduled'::text);


--
-- Name: idx_alert_user_profile_created; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_alert_user_profile_created ON alert.alert USING btree (user_profile_id, created_at DESC);


--
-- Name: idx_emergency_contact_user_profile_id; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_emergency_contact_user_profile_id ON alert.emergency_contact USING btree (user_profile_id);


--
-- Name: idx_evidence_alert_id; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_evidence_alert_id ON alert.evidence USING btree (alert_id);


--
-- Name: idx_evidence_created_at; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_evidence_created_at ON alert.evidence USING btree (created_at DESC);


--
-- Name: idx_frequent_location_user_profile_id; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_frequent_location_user_profile_id ON alert.frequent_location USING btree (user_profile_id);


--
-- Name: idx_location_log_alert_id; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_location_log_alert_id ON alert.location_log USING btree (alert_id);


--
-- Name: idx_location_log_user_profile_recorded; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_location_log_user_profile_recorded ON alert.location_log USING btree (user_profile_id, recorded_at DESC);


--
-- Name: idx_notification_alert_id; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_notification_alert_id ON alert.notification USING btree (alert_id);


--
-- Name: idx_notification_unread; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_notification_unread ON alert.notification USING btree (user_profile_id) WHERE (is_read = false);


--
-- Name: idx_notification_user_profile_created; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_notification_user_profile_created ON alert.notification USING btree (user_profile_id, created_at DESC);


--
-- Name: idx_protection_device_profile; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_protection_device_profile ON alert.protection_device USING btree (user_profile_id, device_id);


--
-- Name: idx_safe_trip_active_arrival; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_safe_trip_active_arrival ON alert.safe_trip USING btree (expected_arrival) WHERE ((status)::text = 'active'::text);


--
-- Name: idx_saved_route_user_profile_id; Type: INDEX; Schema: alert; Owner: -
--

CREATE INDEX idx_saved_route_user_profile_id ON alert.saved_route USING btree (user_profile_id, created_at DESC);


--
-- Name: uq_safe_trip_one_active; Type: INDEX; Schema: alert; Owner: -
--

CREATE UNIQUE INDEX uq_safe_trip_one_active ON alert.safe_trip USING btree (user_profile_id) WHERE ((status)::text = 'active'::text);


--
-- Name: idx_device_account_id; Type: INDEX; Schema: identity; Owner: -
--

CREATE INDEX idx_device_account_id ON identity.device USING btree (account_id);


--
-- Name: idx_device_session_account_id; Type: INDEX; Schema: identity; Owner: -
--

CREATE INDEX idx_device_session_account_id ON identity.device_session USING btree (account_id);


--
-- Name: idx_device_session_device_id; Type: INDEX; Schema: identity; Owner: -
--

CREATE INDEX idx_device_session_device_id ON identity.device_session USING btree (device_id);


--
-- Name: idx_device_session_expires_at; Type: INDEX; Schema: identity; Owner: -
--

CREATE INDEX idx_device_session_expires_at ON identity.device_session USING btree (expires_at);


--
-- Name: idx_recovery_request_account_id; Type: INDEX; Schema: identity; Owner: -
--

CREATE INDEX idx_recovery_request_account_id ON identity.recovery_request USING btree (account_id);


--
-- Name: idx_users_role_id; Type: INDEX; Schema: identity; Owner: -
--

CREATE INDEX idx_users_role_id ON identity.users USING btree (role_id);


--
-- Name: idx_tracking_link_alert; Type: INDEX; Schema: notification; Owner: -
--

CREATE INDEX idx_tracking_link_alert ON notification.tracking_link USING btree (alert_id);


--
-- Name: idx_emergency_resource_city_active; Type: INDEX; Schema: resource; Owner: -
--

CREATE INDEX idx_emergency_resource_city_active ON resource.emergency_resource USING btree (city) WHERE (is_active = true);


--
-- Name: idx_resource_call_alert_id; Type: INDEX; Schema: resource; Owner: -
--

CREATE INDEX idx_resource_call_alert_id ON resource.resource_call USING btree (alert_id);


--
-- Name: idx_resource_call_device_id; Type: INDEX; Schema: resource; Owner: -
--

CREATE INDEX idx_resource_call_device_id ON resource.resource_call USING btree (device_id);


--
-- Name: idx_resource_call_emergency_resource_id; Type: INDEX; Schema: resource; Owner: -
--

CREATE INDEX idx_resource_call_emergency_resource_id ON resource.resource_call USING btree (emergency_resource_id);


--
-- Name: idx_resource_call_user_profile_id; Type: INDEX; Schema: resource; Owner: -
--

CREATE INDEX idx_resource_call_user_profile_id ON resource.resource_call USING btree (user_profile_id);


--
-- Name: idx_zone_city_active; Type: INDEX; Schema: resource; Owner: -
--

CREATE INDEX idx_zone_city_active ON resource.zone USING btree (city) WHERE (is_active = true);


--
-- Name: idx_zone_report_status; Type: INDEX; Schema: resource; Owner: -
--

CREATE INDEX idx_zone_report_status ON resource.zone_report USING btree (status);


--
-- Name: idx_zone_report_user_profile_id; Type: INDEX; Schema: resource; Owner: -
--

CREATE INDEX idx_zone_report_user_profile_id ON resource.zone_report USING btree (user_profile_id);


--
-- Name: audit_log trg_audit_log_no_truncate; Type: TRIGGER; Schema: admin; Owner: -
--

CREATE TRIGGER trg_audit_log_no_truncate BEFORE TRUNCATE ON admin.audit_log FOR EACH STATEMENT EXECUTE FUNCTION admin.forbid_audit_log_change();


--
-- Name: audit_log trg_audit_log_no_update_delete; Type: TRIGGER; Schema: admin; Owner: -
--

CREATE TRIGGER trg_audit_log_no_update_delete BEFORE DELETE OR UPDATE ON admin.audit_log FOR EACH ROW EXECUTE FUNCTION admin.forbid_audit_log_change();


--
-- Name: system_configuration trg_audit_system_configuration; Type: TRIGGER; Schema: admin; Owner: -
--

CREATE TRIGGER trg_audit_system_configuration AFTER UPDATE ON admin.system_configuration FOR EACH ROW EXECUTE FUNCTION admin.log_config_change();


--
-- Name: moderation_action trg_set_updated_at; Type: TRIGGER; Schema: admin; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON admin.moderation_action FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: system_configuration trg_set_updated_at; Type: TRIGGER; Schema: admin; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON admin.system_configuration FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: user_report trg_set_updated_at; Type: TRIGGER; Schema: admin; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON admin.user_report FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: alert trg_audit_alert; Type: TRIGGER; Schema: alert; Owner: -
--

CREATE TRIGGER trg_audit_alert AFTER DELETE OR UPDATE ON alert.alert FOR EACH ROW EXECUTE FUNCTION admin.log_row_change('ALERT');


--
-- Name: emergency_contact trg_audit_emergency_contact; Type: TRIGGER; Schema: alert; Owner: -
--

CREATE TRIGGER trg_audit_emergency_contact AFTER DELETE OR UPDATE ON alert.emergency_contact FOR EACH ROW EXECUTE FUNCTION admin.log_row_change('EMERGENCY_CONTACT');


--
-- Name: evidence trg_audit_evidence; Type: TRIGGER; Schema: alert; Owner: -
--

CREATE TRIGGER trg_audit_evidence AFTER INSERT OR DELETE OR UPDATE ON alert.evidence FOR EACH ROW EXECUTE FUNCTION admin.log_row_change('EVIDENCE');


--
-- Name: saved_route trg_saved_route_limits; Type: TRIGGER; Schema: alert; Owner: -
--

CREATE TRIGGER trg_saved_route_limits BEFORE INSERT OR UPDATE OF is_starred, user_profile_id ON alert.saved_route FOR EACH ROW EXECUTE FUNCTION alert.enforce_saved_route_limits();


--
-- Name: alert trg_set_updated_at; Type: TRIGGER; Schema: alert; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON alert.alert FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: alert_contact trg_set_updated_at; Type: TRIGGER; Schema: alert; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON alert.alert_contact FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: alert_reminder trg_set_updated_at; Type: TRIGGER; Schema: alert; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON alert.alert_reminder FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: emergency_contact trg_set_updated_at; Type: TRIGGER; Schema: alert; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON alert.emergency_contact FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: evidence trg_set_updated_at; Type: TRIGGER; Schema: alert; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON alert.evidence FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: frequent_location trg_set_updated_at; Type: TRIGGER; Schema: alert; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON alert.frequent_location FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: notification trg_set_updated_at; Type: TRIGGER; Schema: alert; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON alert.notification FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: saved_route trg_set_updated_at; Type: TRIGGER; Schema: alert; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON alert.saved_route FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: account trg_audit_account; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_audit_account AFTER DELETE OR UPDATE ON identity.account FOR EACH ROW EXECUTE FUNCTION admin.log_row_change('ACCOUNT');


--
-- Name: admin_profile trg_audit_admin_profile; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_audit_admin_profile AFTER INSERT OR DELETE OR UPDATE ON identity.admin_profile FOR EACH ROW EXECUTE FUNCTION admin.log_row_change('ADMIN_PROFILE');


--
-- Name: role trg_audit_role; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_audit_role AFTER INSERT OR DELETE OR UPDATE ON identity.role FOR EACH ROW EXECUTE FUNCTION admin.log_row_change('ROLE');


--
-- Name: users trg_audit_users; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_audit_users AFTER DELETE OR UPDATE ON identity.users FOR EACH ROW EXECUTE FUNCTION admin.log_row_change('USER');


--
-- Name: account trg_set_updated_at; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON identity.account FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: admin_profile trg_set_updated_at; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON identity.admin_profile FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: alert_activation_setting trg_set_updated_at; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON identity.alert_activation_setting FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: device trg_set_updated_at; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON identity.device FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: device_permission trg_set_updated_at; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON identity.device_permission FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: device_session trg_set_updated_at; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON identity.device_session FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: recovery_request trg_set_updated_at; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON identity.recovery_request FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: role trg_set_updated_at; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON identity.role FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: user_preference trg_set_updated_at; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON identity.user_preference FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: user_profile trg_set_updated_at; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON identity.user_profile FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: users trg_set_updated_at; Type: TRIGGER; Schema: identity; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON identity.users FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: emergency_resource trg_set_updated_at; Type: TRIGGER; Schema: resource; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON resource.emergency_resource FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: resource_call trg_set_updated_at; Type: TRIGGER; Schema: resource; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON resource.resource_call FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: zone trg_set_updated_at; Type: TRIGGER; Schema: resource; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON resource.zone FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: zone_report trg_set_updated_at; Type: TRIGGER; Schema: resource; Owner: -
--

CREATE TRIGGER trg_set_updated_at BEFORE UPDATE ON resource.zone_report FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();


--
-- Name: account_deletion_request account_deletion_request_decided_by_fkey; Type: FK CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.account_deletion_request
    ADD CONSTRAINT account_deletion_request_decided_by_fkey FOREIGN KEY (decided_by) REFERENCES identity.users(id);


--
-- Name: account_deletion_request account_deletion_request_requested_by_fkey; Type: FK CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.account_deletion_request
    ADD CONSTRAINT account_deletion_request_requested_by_fkey FOREIGN KEY (requested_by) REFERENCES identity.users(id);


--
-- Name: account_deletion_request account_deletion_request_user_id_fkey; Type: FK CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.account_deletion_request
    ADD CONSTRAINT account_deletion_request_user_id_fkey FOREIGN KEY (user_id) REFERENCES identity.users(id);


--
-- Name: alert_report alert_report_created_by_user_id_fkey; Type: FK CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.alert_report
    ADD CONSTRAINT alert_report_created_by_user_id_fkey FOREIGN KEY (created_by_user_id) REFERENCES identity.users(id) ON DELETE SET NULL;


--
-- Name: audit_log fk_audit_log_account; Type: FK CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.audit_log
    ADD CONSTRAINT fk_audit_log_account FOREIGN KEY (account_id) REFERENCES identity.account(id);


--
-- Name: moderation_action fk_moderation_action_user_report; Type: FK CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.moderation_action
    ADD CONSTRAINT fk_moderation_action_user_report FOREIGN KEY (user_report_id) REFERENCES admin.user_report(id);


--
-- Name: user_report fk_user_report_reported_user; Type: FK CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.user_report
    ADD CONSTRAINT fk_user_report_reported_user FOREIGN KEY (reported_user_id) REFERENCES identity.users(id);


--
-- Name: user_report fk_user_report_reporter_user; Type: FK CONSTRAINT; Schema: admin; Owner: -
--

ALTER TABLE ONLY admin.user_report
    ADD CONSTRAINT fk_user_report_reporter_user FOREIGN KEY (reporter_user_id) REFERENCES identity.users(id);


--
-- Name: alert_message alert_message_alert_id_fkey; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.alert_message
    ADD CONSTRAINT alert_message_alert_id_fkey FOREIGN KEY (alert_id) REFERENCES alert.alert(id) ON DELETE CASCADE;


--
-- Name: alert_contact fk_alert_contact_alert; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.alert_contact
    ADD CONSTRAINT fk_alert_contact_alert FOREIGN KEY (alert_id) REFERENCES alert.alert(id);


--
-- Name: alert_contact fk_alert_contact_emergency_contact; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.alert_contact
    ADD CONSTRAINT fk_alert_contact_emergency_contact FOREIGN KEY (emergency_contact_id) REFERENCES alert.emergency_contact(id) ON DELETE SET NULL;


--
-- Name: alert fk_alert_device; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.alert
    ADD CONSTRAINT fk_alert_device FOREIGN KEY (device_id) REFERENCES identity.device(id);


--
-- Name: alert_reminder fk_alert_reminder_alert; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.alert_reminder
    ADD CONSTRAINT fk_alert_reminder_alert FOREIGN KEY (alert_id) REFERENCES alert.alert(id);


--
-- Name: alert fk_alert_user_profile; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.alert
    ADD CONSTRAINT fk_alert_user_profile FOREIGN KEY (user_profile_id) REFERENCES identity.user_profile(id);


--
-- Name: emergency_contact fk_emergency_contact_user_profile; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.emergency_contact
    ADD CONSTRAINT fk_emergency_contact_user_profile FOREIGN KEY (user_profile_id) REFERENCES identity.user_profile(id);


--
-- Name: evidence fk_evidence_alert; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.evidence
    ADD CONSTRAINT fk_evidence_alert FOREIGN KEY (alert_id) REFERENCES alert.alert(id);


--
-- Name: frequent_location fk_frequent_location_user_profile; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.frequent_location
    ADD CONSTRAINT fk_frequent_location_user_profile FOREIGN KEY (user_profile_id) REFERENCES identity.user_profile(id);


--
-- Name: location_log fk_location_log_alert; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.location_log
    ADD CONSTRAINT fk_location_log_alert FOREIGN KEY (alert_id) REFERENCES alert.alert(id);


--
-- Name: location_log fk_location_log_user_profile; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.location_log
    ADD CONSTRAINT fk_location_log_user_profile FOREIGN KEY (user_profile_id) REFERENCES identity.user_profile(id);


--
-- Name: notification fk_notification_alert; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.notification
    ADD CONSTRAINT fk_notification_alert FOREIGN KEY (alert_id) REFERENCES alert.alert(id);


--
-- Name: notification fk_notification_user_profile; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.notification
    ADD CONSTRAINT fk_notification_user_profile FOREIGN KEY (user_profile_id) REFERENCES identity.user_profile(id);


--
-- Name: saved_route fk_saved_route_user_profile; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.saved_route
    ADD CONSTRAINT fk_saved_route_user_profile FOREIGN KEY (user_profile_id) REFERENCES identity.user_profile(id);


--
-- Name: safe_trip safe_trip_alert_id_fkey; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.safe_trip
    ADD CONSTRAINT safe_trip_alert_id_fkey FOREIGN KEY (alert_id) REFERENCES alert.alert(id);


--
-- Name: safe_trip safe_trip_user_profile_id_fkey; Type: FK CONSTRAINT; Schema: alert; Owner: -
--

ALTER TABLE ONLY alert.safe_trip
    ADD CONSTRAINT safe_trip_user_profile_id_fkey FOREIGN KEY (user_profile_id) REFERENCES identity.user_profile(id);


--
-- Name: account fk_account_user; Type: FK CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.account
    ADD CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES identity.users(id);


--
-- Name: admin_profile fk_admin_profile_account; Type: FK CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.admin_profile
    ADD CONSTRAINT fk_admin_profile_account FOREIGN KEY (account_id) REFERENCES identity.account(id);


--
-- Name: alert_activation_setting fk_alert_activation_setting_device; Type: FK CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.alert_activation_setting
    ADD CONSTRAINT fk_alert_activation_setting_device FOREIGN KEY (device_id) REFERENCES identity.device(id);


--
-- Name: device fk_device_account; Type: FK CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.device
    ADD CONSTRAINT fk_device_account FOREIGN KEY (account_id) REFERENCES identity.account(id);


--
-- Name: device_permission fk_device_permission_device; Type: FK CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.device_permission
    ADD CONSTRAINT fk_device_permission_device FOREIGN KEY (device_id) REFERENCES identity.device(id);


--
-- Name: device_session fk_device_session_account; Type: FK CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.device_session
    ADD CONSTRAINT fk_device_session_account FOREIGN KEY (account_id) REFERENCES identity.account(id);


--
-- Name: device_session fk_device_session_device; Type: FK CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.device_session
    ADD CONSTRAINT fk_device_session_device FOREIGN KEY (device_id) REFERENCES identity.device(id);


--
-- Name: recovery_request fk_recovery_request_account; Type: FK CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.recovery_request
    ADD CONSTRAINT fk_recovery_request_account FOREIGN KEY (account_id) REFERENCES identity.account(id);


--
-- Name: user_preference fk_user_preference_user_profile; Type: FK CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.user_preference
    ADD CONSTRAINT fk_user_preference_user_profile FOREIGN KEY (user_profile_id) REFERENCES identity.user_profile(id);


--
-- Name: user_profile fk_user_profile_user; Type: FK CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.user_profile
    ADD CONSTRAINT fk_user_profile_user FOREIGN KEY (user_id) REFERENCES identity.users(id);


--
-- Name: users fk_users_role; Type: FK CONSTRAINT; Schema: identity; Owner: -
--

ALTER TABLE ONLY identity.users
    ADD CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES identity.role(id);


--
-- Name: tracking_view tracking_view_alert_id_fkey; Type: FK CONSTRAINT; Schema: notification; Owner: -
--

ALTER TABLE ONLY notification.tracking_view
    ADD CONSTRAINT tracking_view_alert_id_fkey FOREIGN KEY (alert_id) REFERENCES alert.alert(id);


--
-- Name: resource_call fk_resource_call_alert; Type: FK CONSTRAINT; Schema: resource; Owner: -
--

ALTER TABLE ONLY resource.resource_call
    ADD CONSTRAINT fk_resource_call_alert FOREIGN KEY (alert_id) REFERENCES alert.alert(id);


--
-- Name: resource_call fk_resource_call_device; Type: FK CONSTRAINT; Schema: resource; Owner: -
--

ALTER TABLE ONLY resource.resource_call
    ADD CONSTRAINT fk_resource_call_device FOREIGN KEY (device_id) REFERENCES identity.device(id);


--
-- Name: resource_call fk_resource_call_emergency_resource; Type: FK CONSTRAINT; Schema: resource; Owner: -
--

ALTER TABLE ONLY resource.resource_call
    ADD CONSTRAINT fk_resource_call_emergency_resource FOREIGN KEY (emergency_resource_id) REFERENCES resource.emergency_resource(id);


--
-- Name: resource_call fk_resource_call_user_profile; Type: FK CONSTRAINT; Schema: resource; Owner: -
--

ALTER TABLE ONLY resource.resource_call
    ADD CONSTRAINT fk_resource_call_user_profile FOREIGN KEY (user_profile_id) REFERENCES identity.user_profile(id);


--
-- Name: zone_report fk_zone_report_user_profile; Type: FK CONSTRAINT; Schema: resource; Owner: -
--

ALTER TABLE ONLY resource.zone_report
    ADD CONSTRAINT fk_zone_report_user_profile FOREIGN KEY (user_profile_id) REFERENCES identity.user_profile(id);


--
-- Name: zone_report fk_zone_report_zone; Type: FK CONSTRAINT; Schema: resource; Owner: -
--

ALTER TABLE ONLY resource.zone_report
    ADD CONSTRAINT fk_zone_report_zone FOREIGN KEY (zone_id) REFERENCES resource.zone(id);


--
-- PostgreSQL database dump complete
--


