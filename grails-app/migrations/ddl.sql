--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.25
-- Dumped by pg_dump version 9.5.5

-- Started on 2019-06-28 10:30:53

--
-- TOC entry 8 (class 2615 OID 109773207)
-- Name: quartz; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA quartz;


ALTER SCHEMA quartz OWNER TO postgres;

--
-- TOC entry 9 (class 2615 OID 109773208)
-- Name: smarthome; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA smarthome;


ALTER SCHEMA smarthome OWNER TO postgres;

--
-- TOC entry 2727 (class 0 OID 0)
-- Dependencies: 9
-- Name: SCHEMA smarthome; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA smarthome IS 'standard public schema';


--
-- TOC entry 1 (class 3079 OID 11750)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2729 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 173 (class 1259 OID 109773209)
-- Name: act_evt_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_evt_log (
    log_nr_ integer NOT NULL,
    type_ character varying(64),
    proc_def_id_ character varying(64),
    proc_inst_id_ character varying(64),
    execution_id_ character varying(64),
    task_id_ character varying(64),
    time_stamp_ timestamp without time zone NOT NULL,
    user_id_ character varying(255),
    data_ bytea,
    lock_owner_ character varying(255),
    lock_time_ timestamp without time zone,
    is_processed_ smallint DEFAULT 0
);


ALTER TABLE act_evt_log OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 109773216)
-- Name: act_evt_log_log_nr__seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE act_evt_log_log_nr__seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE act_evt_log_log_nr__seq OWNER TO postgres;

--
-- TOC entry 2730 (class 0 OID 0)
-- Dependencies: 174
-- Name: act_evt_log_log_nr__seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE act_evt_log_log_nr__seq OWNED BY act_evt_log.log_nr_;


--
-- TOC entry 175 (class 1259 OID 109773218)
-- Name: act_ge_bytearray; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_ge_bytearray (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    name_ character varying(255),
    deployment_id_ character varying(64),
    bytes_ bytea,
    generated_ boolean
);


ALTER TABLE act_ge_bytearray OWNER TO postgres;

--
-- TOC entry 176 (class 1259 OID 109773224)
-- Name: act_ge_property; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_ge_property (
    name_ character varying(64) NOT NULL,
    value_ character varying(300),
    rev_ integer
);


ALTER TABLE act_ge_property OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 109773227)
-- Name: act_id_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_id_group (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    name_ character varying(255),
    type_ character varying(255)
);


ALTER TABLE act_id_group OWNER TO postgres;

--
-- TOC entry 178 (class 1259 OID 109773233)
-- Name: act_id_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_id_info (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    user_id_ character varying(64),
    type_ character varying(64),
    key_ character varying(255),
    value_ character varying(255),
    password_ bytea,
    parent_id_ character varying(255)
);


ALTER TABLE act_id_info OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 109773239)
-- Name: act_id_membership; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_id_membership (
    user_id_ character varying(64) NOT NULL,
    group_id_ character varying(64) NOT NULL
);


ALTER TABLE act_id_membership OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 109773242)
-- Name: act_id_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_id_user (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    first_ character varying(255),
    last_ character varying(255),
    email_ character varying(255),
    pwd_ character varying(255),
    picture_id_ character varying(64)
);


ALTER TABLE act_id_user OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 109773248)
-- Name: act_re_deployment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_re_deployment (
    id_ character varying(64) NOT NULL,
    name_ character varying(255),
    category_ character varying(255),
    tenant_id_ character varying(255) DEFAULT ''::character varying,
    deploy_time_ timestamp without time zone
);


ALTER TABLE act_re_deployment OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 109773255)
-- Name: act_re_model; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_re_model (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    name_ character varying(255),
    key_ character varying(255),
    category_ character varying(255),
    create_time_ timestamp without time zone,
    last_update_time_ timestamp without time zone,
    version_ integer,
    meta_info_ character varying(4000),
    deployment_id_ character varying(64),
    editor_source_value_id_ character varying(64),
    editor_source_extra_value_id_ character varying(64),
    tenant_id_ character varying(255) DEFAULT ''::character varying
);


ALTER TABLE act_re_model OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 109773262)
-- Name: act_re_procdef; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_re_procdef (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    category_ character varying(255),
    name_ character varying(255),
    key_ character varying(255) NOT NULL,
    version_ integer NOT NULL,
    deployment_id_ character varying(64),
    resource_name_ character varying(4000),
    dgrm_resource_name_ character varying(4000),
    description_ character varying(4000),
    has_start_form_key_ boolean,
    has_graphical_notation_ boolean,
    suspension_state_ integer,
    tenant_id_ character varying(255) DEFAULT ''::character varying
);


ALTER TABLE act_re_procdef OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 109773269)
-- Name: act_ru_event_subscr; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_ru_event_subscr (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    event_type_ character varying(255) NOT NULL,
    event_name_ character varying(255),
    execution_id_ character varying(64),
    proc_inst_id_ character varying(64),
    activity_id_ character varying(64),
    configuration_ character varying(255),
    created_ timestamp without time zone NOT NULL,
    proc_def_id_ character varying(64),
    tenant_id_ character varying(255) DEFAULT ''::character varying
);


ALTER TABLE act_ru_event_subscr OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 109773276)
-- Name: act_ru_execution; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_ru_execution (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    proc_inst_id_ character varying(64),
    business_key_ character varying(255),
    parent_id_ character varying(64),
    proc_def_id_ character varying(64),
    super_exec_ character varying(64),
    act_id_ character varying(255),
    is_active_ boolean,
    is_concurrent_ boolean,
    is_scope_ boolean,
    is_event_scope_ boolean,
    suspension_state_ integer,
    cached_ent_state_ integer,
    tenant_id_ character varying(255) DEFAULT ''::character varying,
    name_ character varying(255),
    lock_time_ timestamp without time zone
);


ALTER TABLE act_ru_execution OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 109773283)
-- Name: act_ru_identitylink; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_ru_identitylink (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    group_id_ character varying(255),
    type_ character varying(255),
    user_id_ character varying(255),
    task_id_ character varying(64),
    proc_inst_id_ character varying(64),
    proc_def_id_ character varying(64)
);


ALTER TABLE act_ru_identitylink OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 109773289)
-- Name: act_ru_job; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_ru_job (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    type_ character varying(255) NOT NULL,
    lock_exp_time_ timestamp without time zone,
    lock_owner_ character varying(255),
    exclusive_ boolean,
    execution_id_ character varying(64),
    process_instance_id_ character varying(64),
    proc_def_id_ character varying(64),
    retries_ integer,
    exception_stack_id_ character varying(64),
    exception_msg_ character varying(4000),
    duedate_ timestamp without time zone,
    repeat_ character varying(255),
    handler_type_ character varying(255),
    handler_cfg_ character varying(4000),
    tenant_id_ character varying(255) DEFAULT ''::character varying
);


ALTER TABLE act_ru_job OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 109773296)
-- Name: act_ru_task; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_ru_task (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    execution_id_ character varying(64),
    proc_inst_id_ character varying(64),
    proc_def_id_ character varying(64),
    name_ character varying(255),
    parent_task_id_ character varying(64),
    description_ character varying(4000),
    task_def_key_ character varying(255),
    owner_ character varying(255),
    assignee_ character varying(255),
    delegation_ character varying(64),
    priority_ integer,
    create_time_ timestamp without time zone,
    due_date_ timestamp without time zone,
    category_ character varying(255),
    suspension_state_ integer,
    tenant_id_ character varying(255) DEFAULT ''::character varying,
    form_key_ character varying(255)
);


ALTER TABLE act_ru_task OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 109773303)
-- Name: act_ru_variable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE act_ru_variable (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    type_ character varying(255) NOT NULL,
    name_ character varying(255) NOT NULL,
    execution_id_ character varying(64),
    proc_inst_id_ character varying(64),
    task_id_ character varying(64),
    bytearray_id_ character varying(64),
    double_ double precision,
    long_ bigint,
    text_ character varying(4000),
    text2_ character varying(4000)
);


ALTER TABLE act_ru_variable OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 109773309)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO postgres;

SET search_path = quartz, pg_catalog;

--
-- TOC entry 191 (class 1259 OID 109773311)
-- Name: qrtz_blob_triggers; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE qrtz_blob_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    blob_data bytea
);


ALTER TABLE qrtz_blob_triggers OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 109773317)
-- Name: qrtz_calendars; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE qrtz_calendars (
    sched_name character varying(120) NOT NULL,
    calendar_name character varying(200) NOT NULL,
    calendar bytea NOT NULL
);


ALTER TABLE qrtz_calendars OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 109773323)
-- Name: qrtz_cron_triggers; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE qrtz_cron_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    cron_expression character varying(120) NOT NULL,
    time_zone_id character varying(80)
);


ALTER TABLE qrtz_cron_triggers OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 109773329)
-- Name: qrtz_fired_triggers; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE qrtz_fired_triggers (
    sched_name character varying(120) NOT NULL,
    entry_id character varying(95) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    instance_name character varying(200) NOT NULL,
    fired_time bigint NOT NULL,
    sched_time bigint NOT NULL,
    priority integer NOT NULL,
    state character varying(16) NOT NULL,
    job_name character varying(200),
    job_group character varying(200),
    is_nonconcurrent boolean,
    requests_recovery boolean
);


ALTER TABLE qrtz_fired_triggers OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 109773335)
-- Name: qrtz_job_details; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE qrtz_job_details (
    sched_name character varying(120) NOT NULL,
    job_name character varying(200) NOT NULL,
    job_group character varying(200) NOT NULL,
    description character varying(250),
    job_class_name character varying(250) NOT NULL,
    is_durable boolean NOT NULL,
    is_nonconcurrent boolean NOT NULL,
    is_update_data boolean NOT NULL,
    requests_recovery boolean NOT NULL,
    job_data bytea
);


ALTER TABLE qrtz_job_details OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 109773341)
-- Name: qrtz_locks; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE qrtz_locks (
    sched_name character varying(120) NOT NULL,
    lock_name character varying(40) NOT NULL
);


ALTER TABLE qrtz_locks OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 109773344)
-- Name: qrtz_paused_trigger_grps; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE qrtz_paused_trigger_grps (
    sched_name character varying(120) NOT NULL,
    trigger_group character varying(200) NOT NULL
);


ALTER TABLE qrtz_paused_trigger_grps OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 109773347)
-- Name: qrtz_scheduler_state; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE qrtz_scheduler_state (
    sched_name character varying(120) NOT NULL,
    instance_name character varying(200) NOT NULL,
    last_checkin_time bigint NOT NULL,
    checkin_interval bigint NOT NULL
);


ALTER TABLE qrtz_scheduler_state OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 109773350)
-- Name: qrtz_simple_triggers; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE qrtz_simple_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    repeat_count bigint NOT NULL,
    repeat_interval bigint NOT NULL,
    times_triggered bigint NOT NULL
);


ALTER TABLE qrtz_simple_triggers OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 109773356)
-- Name: qrtz_simprop_triggers; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE qrtz_simprop_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    str_prop_1 character varying(512),
    str_prop_2 character varying(512),
    str_prop_3 character varying(512),
    int_prop_1 integer,
    int_prop_2 integer,
    long_prop_1 bigint,
    long_prop_2 bigint,
    dec_prop_1 numeric(13,4),
    dec_prop_2 numeric(13,4),
    bool_prop_1 boolean,
    bool_prop_2 boolean
);


ALTER TABLE qrtz_simprop_triggers OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 109773362)
-- Name: qrtz_triggers; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE qrtz_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    job_name character varying(200) NOT NULL,
    job_group character varying(200) NOT NULL,
    description character varying(250),
    next_fire_time bigint,
    prev_fire_time bigint,
    priority integer,
    trigger_state character varying(16) NOT NULL,
    trigger_type character varying(8) NOT NULL,
    start_time bigint NOT NULL,
    end_time bigint,
    calendar_name character varying(200),
    misfire_instr smallint,
    job_data bytea
);


ALTER TABLE qrtz_triggers OWNER TO postgres;

SET search_path = smarthome, pg_catalog;

--
-- TOC entry 202 (class 1259 OID 109773368)
-- Name: acl_class; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE acl_class (
    id bigint NOT NULL,
    class character varying(255) NOT NULL
);


ALTER TABLE acl_class OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 109773371)
-- Name: acl_entry; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE acl_entry (
    id bigint NOT NULL,
    ace_order integer NOT NULL,
    acl_object_identity bigint NOT NULL,
    audit_failure boolean NOT NULL,
    audit_success boolean NOT NULL,
    granting boolean NOT NULL,
    mask integer NOT NULL,
    sid bigint NOT NULL
);


ALTER TABLE acl_entry OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 109773374)
-- Name: acl_object_identity; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE acl_object_identity (
    id bigint NOT NULL,
    object_id_class bigint NOT NULL,
    entries_inheriting boolean NOT NULL,
    object_id_identity bigint NOT NULL,
    owner_sid bigint,
    parent_object bigint
);


ALTER TABLE acl_object_identity OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 109773377)
-- Name: acl_sid; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE acl_sid (
    id bigint NOT NULL,
    principal boolean NOT NULL,
    sid character varying(255) NOT NULL
);


ALTER TABLE acl_sid OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 109773380)
-- Name: agent; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE agent (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    last_connexion timestamp without time zone NOT NULL,
    locked boolean NOT NULL,
    mac character varying(255) NOT NULL,
    private_ip character varying(255),
    public_ip character varying(255),
    user_id bigint NOT NULL,
    agent_model character varying(255) NOT NULL,
    online boolean DEFAULT false NOT NULL,
    libelle character varying(255)
);


ALTER TABLE agent OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 109774498)
-- Name: agent_config; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE agent_config (
    id bigint NOT NULL,
    version bigint NOT NULL,
    agent_id bigint NOT NULL,
    data character varying(255) NOT NULL,
    last_sync timestamp without time zone NOT NULL
);


ALTER TABLE agent_config OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 109773388)
-- Name: agent_token; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE agent_token (
    id bigint NOT NULL,
    version bigint NOT NULL,
    agent_id bigint NOT NULL,
    date_expiration timestamp without time zone NOT NULL,
    token character varying(255) NOT NULL,
    websocket_key character varying(255),
    server_id character varying(255)
);


ALTER TABLE agent_token OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 109773394)
-- Name: chart; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE chart (
    id bigint NOT NULL,
    version bigint NOT NULL,
    chart_type character varying(255) NOT NULL,
    groupe character varying(255) NOT NULL,
    label character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    ylegend character varying(255)
);


ALTER TABLE chart OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 109773400)
-- Name: chart_device; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE chart_device (
    id bigint NOT NULL,
    version bigint NOT NULL,
    chart_id bigint NOT NULL,
    chart_type character varying(255) NOT NULL,
    device_id bigint NOT NULL,
    function character varying(255) NOT NULL,
    metavalue character varying(255),
    "position" integer NOT NULL,
    legend character varying(255),
    transformer text,
    color character varying(16)
);


ALTER TABLE chart_device OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 109773406)
-- Name: chauffage; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE chauffage (
    id bigint NOT NULL,
    version bigint NOT NULL,
    libelle character varying(255) NOT NULL
);


ALTER TABLE chauffage OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 109774503)
-- Name: composant_vue; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE composant_vue (
    id bigint NOT NULL,
    version bigint NOT NULL,
    data character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    page character varying(255) NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE composant_vue OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 109773415)
-- Name: databasechangelog; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255)
);


ALTER TABLE databasechangelog OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 109773421)
-- Name: databasechangeloglock; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE databasechangeloglock OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 109773424)
-- Name: device; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    agent_id bigint,
    date_value timestamp without time zone,
    groupe character varying(255),
    label character varying(255) NOT NULL,
    mac character varying(255) NOT NULL,
    value character varying(255),
    device_type_id bigint NOT NULL,
    user_id bigint NOT NULL,
    command character varying(255),
    formula text,
    favori boolean DEFAULT false NOT NULL,
    tableau_bord character varying(255),
    extras text,
    unite character varying(16)
);


ALTER TABLE device OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 109773432)
-- Name: device_alert; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device_alert (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_debut timestamp without time zone NOT NULL,
    date_fin timestamp without time zone,
    device_id bigint NOT NULL,
    level character varying(16) NOT NULL,
    relance integer NOT NULL,
    status character varying(16) NOT NULL
);


ALTER TABLE device_alert OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 109773435)
-- Name: device_level_alert; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device_level_alert (
    id bigint NOT NULL,
    version bigint NOT NULL,
    device_id bigint NOT NULL,
    event_id bigint,
    level character varying(255) NOT NULL,
    mode character varying(255) NOT NULL,
    tempo integer NOT NULL,
    value double precision NOT NULL
);


ALTER TABLE device_level_alert OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 109773441)
-- Name: device_metadata; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device_metadata (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    device_id bigint NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255),
    label character varying(512),
    type character varying(255),
    "values" text
);


ALTER TABLE device_metadata OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 109773448)
-- Name: device_metavalue; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device_metavalue (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    device_id bigint NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255),
    label character varying(255),
    type character varying(255),
    main boolean DEFAULT false NOT NULL,
    trace boolean DEFAULT false NOT NULL,
    virtual_device boolean DEFAULT false NOT NULL,
    unite character varying(32)
);


ALTER TABLE device_metavalue OWNER TO postgres;

--
-- TOC entry 250 (class 1259 OID 109774511)
-- Name: device_planning; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device_planning (
    id bigint NOT NULL,
    device_id bigint NOT NULL,
    planning_id bigint NOT NULL
);


ALTER TABLE device_planning OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 109773458)
-- Name: device_share; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device_share (
    id bigint NOT NULL,
    version bigint NOT NULL,
    device_id bigint NOT NULL,
    shared_user_id bigint NOT NULL
);


ALTER TABLE device_share OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 109773461)
-- Name: device_type; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    libelle character varying(255) NOT NULL,
    impl_class character varying(255) NOT NULL,
    qualitatif boolean DEFAULT false NOT NULL,
    planning boolean DEFAULT false NOT NULL
);


ALTER TABLE device_type OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 109773468)
-- Name: device_type_config; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device_type_config (
    device_type_id bigint NOT NULL,
    version bigint NOT NULL,
    data text NOT NULL
);


ALTER TABLE device_type_config OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 109773474)
-- Name: device_type_provider; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device_type_provider (
    id bigint NOT NULL,
    version bigint NOT NULL,
    device_type_id bigint NOT NULL,
    libelle character varying(255) NOT NULL
);


ALTER TABLE device_type_provider OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 109773477)
-- Name: device_type_provider_prix; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device_type_provider_prix (
    id bigint NOT NULL,
    version bigint NOT NULL,
    annee integer NOT NULL,
    contrat character varying(16) NOT NULL,
    device_type_provider_id bigint NOT NULL,
    period character varying(16) NOT NULL,
    prix_unitaire double precision NOT NULL
);


ALTER TABLE device_type_provider_prix OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 109773480)
-- Name: device_value; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device_value (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_value timestamp without time zone NOT NULL,
    device_id bigint NOT NULL,
    name character varying(64),
    value numeric NOT NULL,
    alert_level character varying(16)
);


ALTER TABLE device_value OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 109773486)
-- Name: device_value_day; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device_value_day (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    date_value timestamp without time zone NOT NULL,
    device_id bigint NOT NULL,
    name character varying(64) NOT NULL,
    value double precision NOT NULL
);


ALTER TABLE device_value_day OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 109773490)
-- Name: device_value_month; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE device_value_month (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    date_value timestamp without time zone NOT NULL,
    device_id bigint NOT NULL,
    name character varying(64) NOT NULL,
    value double precision NOT NULL
);


ALTER TABLE device_value_month OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 109773494)
-- Name: event; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE event (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    actif boolean NOT NULL,
    condition text,
    cron character varying(128),
    heure_decalage character varying(8),
    heure_ete boolean NOT NULL,
    last_event timestamp without time zone,
    last_heure_decalage character varying(8),
    libelle character varying(255) NOT NULL,
    solstice character varying(8),
    synchro_soleil boolean NOT NULL,
    user_id bigint NOT NULL,
    inverse_mode boolean DEFAULT false NOT NULL
);


ALTER TABLE event OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 109773502)
-- Name: event_device; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE event_device (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    device_id bigint NOT NULL,
    event_id bigint NOT NULL
);


ALTER TABLE event_device OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 109773506)
-- Name: event_mode; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE event_mode (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    event_id bigint NOT NULL,
    mode_id bigint NOT NULL
);


ALTER TABLE event_mode OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 109773510)
-- Name: event_trigger; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE event_trigger (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    action_name character varying(255) NOT NULL,
    domain_class_name character varying(255) NOT NULL,
    domain_id bigint NOT NULL,
    event_id bigint NOT NULL,
    parameters text
);


ALTER TABLE event_trigger OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 109773517)
-- Name: house; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE house (
    id bigint NOT NULL,
    version bigint NOT NULL,
    compteur_id bigint,
    defaut boolean NOT NULL,
    name character varying(255) NOT NULL,
    surface double precision,
    user_id bigint NOT NULL,
    humidite_id bigint,
    temperature_id bigint,
    chauffage_id bigint,
    latitude character varying(32),
    location character varying(255),
    longitude character varying(32),
    compteur_gaz_id bigint
);


ALTER TABLE house OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 109773523)
-- Name: house_conso; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE house_conso (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_conso timestamp without time zone NOT NULL,
    house_id bigint NOT NULL,
    kwhc double precision NOT NULL,
    kwhp double precision NOT NULL,
    kwbase double precision DEFAULT (0)::double precision NOT NULL,
    kwgaz double precision DEFAULT (0)::double precision NOT NULL
);


ALTER TABLE house_conso OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 109773528)
-- Name: house_mode; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE house_mode (
    house_id bigint NOT NULL,
    mode_id bigint NOT NULL
);


ALTER TABLE house_mode OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 109773531)
-- Name: house_weather; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE house_weather (
    house_id bigint NOT NULL,
    data text NOT NULL,
    date_weather timestamp without time zone NOT NULL,
    provider_class character varying(255) NOT NULL
);


ALTER TABLE house_weather OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 109773537)
-- Name: mode; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE mode (
    id bigint NOT NULL,
    version bigint NOT NULL,
    name character varying(32) NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE mode OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 109773540)
-- Name: notification; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE notification (
    id bigint NOT NULL,
    version bigint NOT NULL,
    description character varying(255) NOT NULL,
    message text NOT NULL,
    notification_account_id bigint NOT NULL,
    parameters text,
    user_id bigint NOT NULL
);


ALTER TABLE notification OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 109773546)
-- Name: notification_account; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE notification_account (
    id bigint NOT NULL,
    version bigint NOT NULL,
    config text,
    notification_account_sender_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE notification_account OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 109773549)
-- Name: notification_account_sender; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE notification_account_sender (
    id bigint NOT NULL,
    version bigint NOT NULL,
    impl_class character varying(255) NOT NULL,
    libelle character varying(255) NOT NULL,
    role character varying(255),
    cron character varying(255)
);


ALTER TABLE notification_account_sender OWNER TO postgres;

--
-- TOC entry 251 (class 1259 OID 109774516)
-- Name: planning; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE planning (
    id bigint NOT NULL,
    version bigint NOT NULL,
    data text NOT NULL,
    label character varying(255) NOT NULL,
    rule text,
    user_id bigint NOT NULL
);


ALTER TABLE planning OWNER TO postgres;

--
-- TOC entry 254 (class 1259 OID 109782137)
-- Name: producteur_energie; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE producteur_energie (
    id bigint NOT NULL,
    version bigint NOT NULL,
    investissement double precision NOT NULL,
    libelle character varying(255) NOT NULL,
    surface double precision NOT NULL,
    nbaction integer NOT NULL
);


ALTER TABLE producteur_energie OWNER TO postgres;

--
-- TOC entry 255 (class 1259 OID 109782142)
-- Name: producteur_energie_action; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE producteur_energie_action (
    id bigint NOT NULL,
    version bigint NOT NULL,
    nbaction integer NOT NULL,
    producteur_id bigint NOT NULL,
    user_id bigint NOT NULL,
    device_id bigint
);


ALTER TABLE producteur_energie_action OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 109773555)
-- Name: registration_code; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE registration_code (
    id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    server_url character varying(255) NOT NULL,
    token character varying(255) NOT NULL,
    username character varying(255) NOT NULL
);


ALTER TABLE registration_code OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 109773561)
-- Name: role; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE role (
    id bigint NOT NULL,
    version bigint NOT NULL,
    authority character varying(255) NOT NULL
);


ALTER TABLE role OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 109773564)
-- Name: scenario; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE scenario (
    id bigint NOT NULL,
    version bigint NOT NULL,
    description character varying(255),
    label character varying(255) NOT NULL,
    script text NOT NULL,
    user_id bigint NOT NULL,
    last_execution timestamp(6) without time zone
);


ALTER TABLE scenario OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 109773570)
-- Name: script_rule; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE script_rule (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone,
    description character varying(255) NOT NULL,
    last_updated timestamp without time zone,
    rule_name character varying(255) NOT NULL,
    script text NOT NULL
);


ALTER TABLE script_rule OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 109773576)
-- Name: user_admin; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE user_admin (
    user_id bigint NOT NULL,
    admin_id bigint NOT NULL
);


ALTER TABLE user_admin OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 109773579)
-- Name: user_application; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE user_application (
    id bigint NOT NULL,
    version bigint NOT NULL,
    application_id character varying(255) NOT NULL,
    token character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    date_auth timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE user_application OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 109773585)
-- Name: user_friend; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE user_friend (
    id bigint NOT NULL,
    version bigint NOT NULL,
    friend_id bigint NOT NULL,
    user_id bigint NOT NULL,
    confirm boolean NOT NULL
);


ALTER TABLE user_friend OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 109773588)
-- Name: user_role; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE user_role (
    role_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE user_role OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 109773591)
-- Name: utilisateur; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE utilisateur (
    id bigint NOT NULL,
    version bigint NOT NULL,
    account_expired boolean NOT NULL,
    account_locked boolean NOT NULL,
    enabled boolean NOT NULL,
    last_activation timestamp without time zone NOT NULL,
    nom character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    password_expired boolean NOT NULL,
    prenom character varying(255) NOT NULL,
    username character varying(255) NOT NULL,
    application_key character varying(255) NOT NULL,
    telephone_mobile character varying(255),
    profil_public boolean DEFAULT false NOT NULL,
    last_connexion timestamp without time zone
);


ALTER TABLE utilisateur OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 109774577)
-- Name: widget; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE widget (
    id bigint NOT NULL,
    version bigint NOT NULL,
    description character varying(255) NOT NULL,
    libelle character varying(255) NOT NULL,
    action_name character varying(255) NOT NULL,
    controller_name character varying(255) NOT NULL,
    refresh_period integer,
    config_name character varying(255)
);


ALTER TABLE widget OWNER TO postgres;

--
-- TOC entry 253 (class 1259 OID 109774585)
-- Name: widget_user; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE widget_user (
    id bigint NOT NULL,
    version bigint NOT NULL,
    col integer NOT NULL,
    data character varying(255),
    param_id bigint,
    "row" integer NOT NULL,
    user_id bigint NOT NULL,
    widget_id bigint NOT NULL
);


ALTER TABLE widget_user OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 109773604)
-- Name: workflow; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE workflow (
    id bigint NOT NULL,
    version bigint NOT NULL,
    libelle character varying(255) NOT NULL,
    data bytea,
    description character varying(255)
);


ALTER TABLE workflow OWNER TO postgres;

SET search_path = public, pg_catalog;

--
-- TOC entry 2198 (class 2604 OID 109773610)
-- Name: log_nr_; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_evt_log ALTER COLUMN log_nr_ SET DEFAULT nextval('act_evt_log_log_nr__seq'::regclass);


--
-- TOC entry 2228 (class 2606 OID 109773812)
-- Name: act_evt_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_evt_log
    ADD CONSTRAINT act_evt_log_pkey PRIMARY KEY (log_nr_);


--
-- TOC entry 2230 (class 2606 OID 109773809)
-- Name: act_ge_bytearray_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ge_bytearray
    ADD CONSTRAINT act_ge_bytearray_pkey PRIMARY KEY (id_);


--
-- TOC entry 2233 (class 2606 OID 109773807)
-- Name: act_ge_property_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ge_property
    ADD CONSTRAINT act_ge_property_pkey PRIMARY KEY (name_);


--
-- TOC entry 2235 (class 2606 OID 109773816)
-- Name: act_id_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_id_group
    ADD CONSTRAINT act_id_group_pkey PRIMARY KEY (id_);


--
-- TOC entry 2237 (class 2606 OID 109773820)
-- Name: act_id_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_id_info
    ADD CONSTRAINT act_id_info_pkey PRIMARY KEY (id_);


--
-- TOC entry 2239 (class 2606 OID 109773815)
-- Name: act_id_membership_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_id_membership
    ADD CONSTRAINT act_id_membership_pkey PRIMARY KEY (user_id_, group_id_);


--
-- TOC entry 2243 (class 2606 OID 109773833)
-- Name: act_id_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_id_user
    ADD CONSTRAINT act_id_user_pkey PRIMARY KEY (id_);


--
-- TOC entry 2245 (class 2606 OID 109773822)
-- Name: act_re_deployment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_re_deployment
    ADD CONSTRAINT act_re_deployment_pkey PRIMARY KEY (id_);


--
-- TOC entry 2250 (class 2606 OID 109773824)
-- Name: act_re_model_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_re_model
    ADD CONSTRAINT act_re_model_pkey PRIMARY KEY (id_);


--
-- TOC entry 2252 (class 2606 OID 109773826)
-- Name: act_re_procdef_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_re_procdef
    ADD CONSTRAINT act_re_procdef_pkey PRIMARY KEY (id_);


--
-- TOC entry 2258 (class 2606 OID 109773849)
-- Name: act_ru_event_subscr_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_event_subscr
    ADD CONSTRAINT act_ru_event_subscr_pkey PRIMARY KEY (id_);


--
-- TOC entry 2265 (class 2606 OID 109773837)
-- Name: act_ru_execution_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_execution
    ADD CONSTRAINT act_ru_execution_pkey PRIMARY KEY (id_);


--
-- TOC entry 2272 (class 2606 OID 109773836)
-- Name: act_ru_identitylink_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_identitylink
    ADD CONSTRAINT act_ru_identitylink_pkey PRIMARY KEY (id_);


--
-- TOC entry 2275 (class 2606 OID 109773851)
-- Name: act_ru_job_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_job
    ADD CONSTRAINT act_ru_job_pkey PRIMARY KEY (id_);


--
-- TOC entry 2281 (class 2606 OID 109773856)
-- Name: act_ru_task_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_task
    ADD CONSTRAINT act_ru_task_pkey PRIMARY KEY (id_);


--
-- TOC entry 2287 (class 2606 OID 109773858)
-- Name: act_ru_variable_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_variable
    ADD CONSTRAINT act_ru_variable_pkey PRIMARY KEY (id_);


--
-- TOC entry 2254 (class 2606 OID 109773831)
-- Name: act_uniq_procdef; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_re_procdef
    ADD CONSTRAINT act_uniq_procdef UNIQUE (key_, version_, tenant_id_);


SET search_path = quartz, pg_catalog;

--
-- TOC entry 2289 (class 2606 OID 109773868)
-- Name: qrtz_blob_triggers_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2291 (class 2606 OID 109773870)
-- Name: qrtz_calendars_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_calendars
    ADD CONSTRAINT qrtz_calendars_pkey PRIMARY KEY (sched_name, calendar_name);


--
-- TOC entry 2293 (class 2606 OID 109773874)
-- Name: qrtz_cron_triggers_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2301 (class 2606 OID 109773876)
-- Name: qrtz_fired_triggers_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_fired_triggers
    ADD CONSTRAINT qrtz_fired_triggers_pkey PRIMARY KEY (sched_name, entry_id);


--
-- TOC entry 2305 (class 2606 OID 109773886)
-- Name: qrtz_job_details_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_job_details
    ADD CONSTRAINT qrtz_job_details_pkey PRIMARY KEY (sched_name, job_name, job_group);


--
-- TOC entry 2307 (class 2606 OID 109773872)
-- Name: qrtz_locks_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_locks
    ADD CONSTRAINT qrtz_locks_pkey PRIMARY KEY (sched_name, lock_name);


--
-- TOC entry 2309 (class 2606 OID 109773878)
-- Name: qrtz_paused_trigger_grps_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_paused_trigger_grps
    ADD CONSTRAINT qrtz_paused_trigger_grps_pkey PRIMARY KEY (sched_name, trigger_group);


--
-- TOC entry 2311 (class 2606 OID 109773888)
-- Name: qrtz_scheduler_state_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_scheduler_state
    ADD CONSTRAINT qrtz_scheduler_state_pkey PRIMARY KEY (sched_name, instance_name);


--
-- TOC entry 2313 (class 2606 OID 109773892)
-- Name: qrtz_simple_triggers_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2315 (class 2606 OID 109773897)
-- Name: qrtz_simprop_triggers_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_simprop_triggers
    ADD CONSTRAINT qrtz_simprop_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2329 (class 2606 OID 109773910)
-- Name: qrtz_triggers_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_triggers
    ADD CONSTRAINT qrtz_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


SET search_path = smarthome, pg_catalog;

--
-- TOC entry 2331 (class 2606 OID 109773894)
-- Name: acl_class_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY acl_class
    ADD CONSTRAINT acl_class_pkey PRIMARY KEY (id);


--
-- TOC entry 2335 (class 2606 OID 109773900)
-- Name: acl_entry_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY acl_entry
    ADD CONSTRAINT acl_entry_pkey PRIMARY KEY (id);


--
-- TOC entry 2339 (class 2606 OID 109773902)
-- Name: acl_object_identity_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT acl_object_identity_pkey PRIMARY KEY (id);


--
-- TOC entry 2343 (class 2606 OID 109773908)
-- Name: acl_sid_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY acl_sid
    ADD CONSTRAINT acl_sid_pkey PRIMARY KEY (id);


--
-- TOC entry 2501 (class 2606 OID 109774502)
-- Name: agent_config_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY agent_config
    ADD CONSTRAINT agent_config_pkey PRIMARY KEY (id);


--
-- TOC entry 2347 (class 2606 OID 109773926)
-- Name: agent_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY agent
    ADD CONSTRAINT agent_pkey PRIMARY KEY (id);


--
-- TOC entry 2352 (class 2606 OID 109773928)
-- Name: agent_token_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY agent_token
    ADD CONSTRAINT agent_token_pkey PRIMARY KEY (id);


--
-- TOC entry 2360 (class 2606 OID 109773938)
-- Name: chart_device_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY chart_device
    ADD CONSTRAINT chart_device_pkey PRIMARY KEY (id);


--
-- TOC entry 2357 (class 2606 OID 109773936)
-- Name: chart_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY chart
    ADD CONSTRAINT chart_pkey PRIMARY KEY (id);


--
-- TOC entry 2363 (class 2606 OID 109773942)
-- Name: chauffage_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY chauffage
    ADD CONSTRAINT chauffage_pkey PRIMARY KEY (id);


--
-- TOC entry 2504 (class 2606 OID 109774510)
-- Name: composant_vue_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY composant_vue
    ADD CONSTRAINT composant_vue_pkey PRIMARY KEY (id);


--
-- TOC entry 2374 (class 2606 OID 109773952)
-- Name: device_alert_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_alert
    ADD CONSTRAINT device_alert_pkey PRIMARY KEY (id);


--
-- TOC entry 2377 (class 2606 OID 109773958)
-- Name: device_level_alert_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_level_alert
    ADD CONSTRAINT device_level_alert_pkey PRIMARY KEY (id);


--
-- TOC entry 2380 (class 2606 OID 109773975)
-- Name: device_metadata_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_metadata
    ADD CONSTRAINT device_metadata_pkey PRIMARY KEY (id);


--
-- TOC entry 2385 (class 2606 OID 109773970)
-- Name: device_metavalue_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_metavalue
    ADD CONSTRAINT device_metavalue_pkey PRIMARY KEY (id);


--
-- TOC entry 2371 (class 2606 OID 109773956)
-- Name: device_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device
    ADD CONSTRAINT device_pkey PRIMARY KEY (id);


--
-- TOC entry 2509 (class 2606 OID 109774515)
-- Name: device_planning_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_planning
    ADD CONSTRAINT device_planning_pkey PRIMARY KEY (id);


--
-- TOC entry 2390 (class 2606 OID 109773964)
-- Name: device_share_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_share
    ADD CONSTRAINT device_share_pkey PRIMARY KEY (id);


--
-- TOC entry 2399 (class 2606 OID 109773966)
-- Name: device_type_config_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_type_config
    ADD CONSTRAINT device_type_config_pkey PRIMARY KEY (device_type_id);


--
-- TOC entry 2395 (class 2606 OID 109773985)
-- Name: device_type_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_type
    ADD CONSTRAINT device_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2401 (class 2606 OID 109773980)
-- Name: device_type_provider_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_type_provider
    ADD CONSTRAINT device_type_provider_pkey PRIMARY KEY (id);


--
-- TOC entry 2405 (class 2606 OID 109773987)
-- Name: device_type_provider_prix_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_type_provider_prix
    ADD CONSTRAINT device_type_provider_prix_pkey PRIMARY KEY (id);


--
-- TOC entry 2412 (class 2606 OID 109774473)
-- Name: device_value_day_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_value_day
    ADD CONSTRAINT device_value_day_pkey PRIMARY KEY (id);


--
-- TOC entry 2415 (class 2606 OID 109774015)
-- Name: device_value_month_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_value_month
    ADD CONSTRAINT device_value_month_pkey PRIMARY KEY (id);


--
-- TOC entry 2408 (class 2606 OID 109774487)
-- Name: device_value_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_value
    ADD CONSTRAINT device_value_pkey PRIMARY KEY (id);


--
-- TOC entry 2383 (class 2606 OID 109773978)
-- Name: devicemetadata_uniq; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_metadata
    ADD CONSTRAINT devicemetadata_uniq UNIQUE (device_id, name);


--
-- TOC entry 2388 (class 2606 OID 109773973)
-- Name: devicemetavalue_uniq; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_metavalue
    ADD CONSTRAINT devicemetavalue_uniq UNIQUE (device_id, name);


--
-- TOC entry 2421 (class 2606 OID 109773994)
-- Name: event_device_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY event_device
    ADD CONSTRAINT event_device_pkey PRIMARY KEY (id);


--
-- TOC entry 2425 (class 2606 OID 109773999)
-- Name: event_mode_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY event_mode
    ADD CONSTRAINT event_mode_pkey PRIMARY KEY (id);


--
-- TOC entry 2418 (class 2606 OID 109773992)
-- Name: event_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY event
    ADD CONSTRAINT event_pkey PRIMARY KEY (id);


--
-- TOC entry 2428 (class 2606 OID 109774001)
-- Name: event_trigger_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY event_trigger
    ADD CONSTRAINT event_trigger_pkey PRIMARY KEY (id);


--
-- TOC entry 2434 (class 2606 OID 109774007)
-- Name: house_conso_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house_conso
    ADD CONSTRAINT house_conso_pkey PRIMARY KEY (id);


--
-- TOC entry 2439 (class 2606 OID 109774012)
-- Name: house_mode_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house_mode
    ADD CONSTRAINT house_mode_pkey PRIMARY KEY (house_id, mode_id);


--
-- TOC entry 2431 (class 2606 OID 109774005)
-- Name: house_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house
    ADD CONSTRAINT house_pkey PRIMARY KEY (id);


--
-- TOC entry 2441 (class 2606 OID 109774024)
-- Name: house_weather_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house_weather
    ADD CONSTRAINT house_weather_pkey PRIMARY KEY (house_id);


--
-- TOC entry 2443 (class 2606 OID 109774017)
-- Name: mode_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY mode
    ADD CONSTRAINT mode_pkey PRIMARY KEY (id);


--
-- TOC entry 2449 (class 2606 OID 109774026)
-- Name: notification_account_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY notification_account
    ADD CONSTRAINT notification_account_pkey PRIMARY KEY (id);


--
-- TOC entry 2454 (class 2606 OID 109774030)
-- Name: notification_account_sender_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY notification_account_sender
    ADD CONSTRAINT notification_account_sender_pkey PRIMARY KEY (id);


--
-- TOC entry 2446 (class 2606 OID 109774020)
-- Name: notification_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- TOC entry 2367 (class 2606 OID 109773944)
-- Name: pk_databasechangeloglock; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY databasechangeloglock
    ADD CONSTRAINT pk_databasechangeloglock PRIMARY KEY (id);


--
-- TOC entry 2512 (class 2606 OID 109774523)
-- Name: planning_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY planning
    ADD CONSTRAINT planning_pkey PRIMARY KEY (id);


--
-- TOC entry 2521 (class 2606 OID 109782146)
-- Name: producteur_energie_action_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY producteur_energie_action
    ADD CONSTRAINT producteur_energie_action_pkey PRIMARY KEY (id);


--
-- TOC entry 2519 (class 2606 OID 109782141)
-- Name: producteur_energie_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY producteur_energie
    ADD CONSTRAINT producteur_energie_pkey PRIMARY KEY (id);


--
-- TOC entry 2458 (class 2606 OID 109774035)
-- Name: registration_code_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY registration_code
    ADD CONSTRAINT registration_code_pkey PRIMARY KEY (id);


--
-- TOC entry 2460 (class 2606 OID 109774037)
-- Name: role_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 2464 (class 2606 OID 109774039)
-- Name: scenario_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY scenario
    ADD CONSTRAINT scenario_pkey PRIMARY KEY (id);


--
-- TOC entry 2467 (class 2606 OID 109774044)
-- Name: script_rule_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY script_rule
    ADD CONSTRAINT script_rule_pkey PRIMARY KEY (id);


--
-- TOC entry 2403 (class 2606 OID 109773983)
-- Name: uk_acelnk72phh048yv9nbtguy7u; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_type_provider
    ADD CONSTRAINT uk_acelnk72phh048yv9nbtguy7u UNIQUE (libelle);


--
-- TOC entry 2497 (class 2606 OID 109774072)
-- Name: uk_bakmmy47if4gwgbxfi1rb09v0; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY workflow
    ADD CONSTRAINT uk_bakmmy47if4gwgbxfi1rb09v0 UNIQUE (libelle);


--
-- TOC entry 2456 (class 2606 OID 109774033)
-- Name: uk_cbf80a9p8895tthueljed9trp; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY notification_account_sender
    ADD CONSTRAINT uk_cbf80a9p8895tthueljed9trp UNIQUE (libelle);


--
-- TOC entry 2355 (class 2606 OID 109773932)
-- Name: uk_e2mt53wf4767ukwej7rrtedxy; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY agent_token
    ADD CONSTRAINT uk_e2mt53wf4767ukwej7rrtedxy UNIQUE (token);


--
-- TOC entry 2474 (class 2606 OID 109774057)
-- Name: uk_gqve0ke8hruo4ugv8oc5sw2s2; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_application
    ADD CONSTRAINT uk_gqve0ke8hruo4ugv8oc5sw2s2 UNIQUE (token);


--
-- TOC entry 2462 (class 2606 OID 109774041)
-- Name: uk_irsamgnera6angm0prq1kemt2; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY role
    ADD CONSTRAINT uk_irsamgnera6angm0prq1kemt2 UNIQUE (authority);


--
-- TOC entry 2333 (class 2606 OID 109773898)
-- Name: uk_iy7ua5fso3il3u3ymoc4uf35w; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY acl_class
    ADD CONSTRAINT uk_iy7ua5fso3il3u3ymoc4uf35w UNIQUE (class);


--
-- TOC entry 2365 (class 2606 OID 109773948)
-- Name: uk_jymi1ggsm13fhv0nulfl5gfj5; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY chauffage
    ADD CONSTRAINT uk_jymi1ggsm13fhv0nulfl5gfj5 UNIQUE (libelle);


--
-- TOC entry 2397 (class 2606 OID 109773989)
-- Name: uk_kpojn1a65dfixkqf4j6t7dk84; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_type
    ADD CONSTRAINT uk_kpojn1a65dfixkqf4j6t7dk84 UNIQUE (libelle);


--
-- TOC entry 2490 (class 2606 OID 109774067)
-- Name: uk_kq7nt5wyq9v9lpcpgxag2f24a; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY utilisateur
    ADD CONSTRAINT uk_kq7nt5wyq9v9lpcpgxag2f24a UNIQUE (username);


--
-- TOC entry 2469 (class 2606 OID 109774048)
-- Name: uk_n6jyf5k7smf9y6h97q38vtbdp; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY script_rule
    ADD CONSTRAINT uk_n6jyf5k7smf9y6h97q38vtbdp UNIQUE (rule_name);


--
-- TOC entry 2337 (class 2606 OID 109773904)
-- Name: unique_ace_order; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY acl_entry
    ADD CONSTRAINT unique_ace_order UNIQUE (acl_object_identity, ace_order);


--
-- TOC entry 2393 (class 2606 OID 109773968)
-- Name: unique_device_id; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_share
    ADD CONSTRAINT unique_device_id UNIQUE (device_id, shared_user_id);


--
-- TOC entry 2482 (class 2606 OID 109774051)
-- Name: unique_friend_id; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_friend
    ADD CONSTRAINT unique_friend_id UNIQUE (user_id, friend_id);


--
-- TOC entry 2437 (class 2606 OID 109774010)
-- Name: unique_house_id; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house_conso
    ADD CONSTRAINT unique_house_id UNIQUE (date_conso, house_id);


--
-- TOC entry 2350 (class 2606 OID 109773930)
-- Name: unique_mac; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY agent
    ADD CONSTRAINT unique_mac UNIQUE (user_id, mac);


--
-- TOC entry 2507 (class 2606 OID 109774539)
-- Name: unique_name; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY composant_vue
    ADD CONSTRAINT unique_name UNIQUE (user_id, page, name);


--
-- TOC entry 2452 (class 2606 OID 109774028)
-- Name: unique_notification_account_sender_id; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY notification_account
    ADD CONSTRAINT unique_notification_account_sender_id UNIQUE (user_id, notification_account_sender_id);


--
-- TOC entry 2341 (class 2606 OID 109773906)
-- Name: unique_object_id_identity; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT unique_object_id_identity UNIQUE (object_id_class, object_id_identity);


--
-- TOC entry 2345 (class 2606 OID 109773912)
-- Name: unique_principal; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY acl_sid
    ADD CONSTRAINT unique_principal UNIQUE (sid, principal);


--
-- TOC entry 2476 (class 2606 OID 109774061)
-- Name: unique_user_id; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_application
    ADD CONSTRAINT unique_user_id UNIQUE (application_id, user_id);


--
-- TOC entry 2471 (class 2606 OID 109774046)
-- Name: user_admin_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_admin
    ADD CONSTRAINT user_admin_pkey PRIMARY KEY (user_id, admin_id);


--
-- TOC entry 2478 (class 2606 OID 109774065)
-- Name: user_application_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_application
    ADD CONSTRAINT user_application_pkey PRIMARY KEY (id);


--
-- TOC entry 2484 (class 2606 OID 109774055)
-- Name: user_friend_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_friend
    ADD CONSTRAINT user_friend_pkey PRIMARY KEY (id);


--
-- TOC entry 2488 (class 2606 OID 109774053)
-- Name: user_role_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (role_id, user_id);


--
-- TOC entry 2493 (class 2606 OID 109774070)
-- Name: utilisateur_application_key_key; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY utilisateur
    ADD CONSTRAINT utilisateur_application_key_key UNIQUE (application_key);


--
-- TOC entry 2495 (class 2606 OID 109774074)
-- Name: utilisateur_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY utilisateur
    ADD CONSTRAINT utilisateur_pkey PRIMARY KEY (id);


--
-- TOC entry 2514 (class 2606 OID 109774584)
-- Name: widget_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY widget
    ADD CONSTRAINT widget_pkey PRIMARY KEY (id);


--
-- TOC entry 2516 (class 2606 OID 109774589)
-- Name: widget_user_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY widget_user
    ADD CONSTRAINT widget_user_pkey PRIMARY KEY (id);


--
-- TOC entry 2499 (class 2606 OID 109774078)
-- Name: workflow_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY workflow
    ADD CONSTRAINT workflow_pkey PRIMARY KEY (id);


SET search_path = public, pg_catalog;

--
-- TOC entry 2266 (class 1259 OID 109773838)
-- Name: act_idx_athrz_procedef; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_athrz_procedef ON public.act_ru_identitylink USING btree (proc_def_id_);


--
-- TOC entry 2231 (class 1259 OID 109773810)
-- Name: act_idx_bytear_depl; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_bytear_depl ON public.act_ge_bytearray USING btree (deployment_id_);


--
-- TOC entry 2255 (class 1259 OID 109773852)
-- Name: act_idx_event_subscr; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_event_subscr ON public.act_ru_event_subscr USING btree (execution_id_);


--
-- TOC entry 2256 (class 1259 OID 109773853)
-- Name: act_idx_event_subscr_config_; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_event_subscr_config_ ON public.act_ru_event_subscr USING btree (configuration_);


--
-- TOC entry 2259 (class 1259 OID 109773839)
-- Name: act_idx_exe_parent; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_exe_parent ON public.act_ru_execution USING btree (parent_id_);


--
-- TOC entry 2260 (class 1259 OID 109773840)
-- Name: act_idx_exe_procdef; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_exe_procdef ON public.act_ru_execution USING btree (proc_def_id_);


--
-- TOC entry 2261 (class 1259 OID 109773841)
-- Name: act_idx_exe_procinst; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_exe_procinst ON public.act_ru_execution USING btree (proc_inst_id_);


--
-- TOC entry 2262 (class 1259 OID 109773842)
-- Name: act_idx_exe_super; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_exe_super ON public.act_ru_execution USING btree (super_exec_);


--
-- TOC entry 2263 (class 1259 OID 109773843)
-- Name: act_idx_exec_buskey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_exec_buskey ON public.act_ru_execution USING btree (business_key_);


--
-- TOC entry 2267 (class 1259 OID 109773844)
-- Name: act_idx_ident_lnk_group; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_ident_lnk_group ON public.act_ru_identitylink USING btree (group_id_);


--
-- TOC entry 2268 (class 1259 OID 109773845)
-- Name: act_idx_ident_lnk_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_ident_lnk_user ON public.act_ru_identitylink USING btree (user_id_);


--
-- TOC entry 2269 (class 1259 OID 109773846)
-- Name: act_idx_idl_procinst; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_idl_procinst ON public.act_ru_identitylink USING btree (proc_inst_id_);


--
-- TOC entry 2273 (class 1259 OID 109773854)
-- Name: act_idx_job_exception; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_job_exception ON public.act_ru_job USING btree (exception_stack_id_);


--
-- TOC entry 2240 (class 1259 OID 109773817)
-- Name: act_idx_memb_group; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_memb_group ON public.act_id_membership USING btree (group_id_);


--
-- TOC entry 2241 (class 1259 OID 109773818)
-- Name: act_idx_memb_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_memb_user ON public.act_id_membership USING btree (user_id_);


--
-- TOC entry 2246 (class 1259 OID 109773827)
-- Name: act_idx_model_deployment; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_model_deployment ON public.act_re_model USING btree (deployment_id_);


--
-- TOC entry 2247 (class 1259 OID 109773828)
-- Name: act_idx_model_source; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_model_source ON public.act_re_model USING btree (editor_source_value_id_);


--
-- TOC entry 2248 (class 1259 OID 109773829)
-- Name: act_idx_model_source_extra; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_model_source_extra ON public.act_re_model USING btree (editor_source_extra_value_id_);


--
-- TOC entry 2276 (class 1259 OID 109773859)
-- Name: act_idx_task_create; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_task_create ON public.act_ru_task USING btree (create_time_);


--
-- TOC entry 2277 (class 1259 OID 109773860)
-- Name: act_idx_task_exec; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_task_exec ON public.act_ru_task USING btree (execution_id_);


--
-- TOC entry 2278 (class 1259 OID 109773861)
-- Name: act_idx_task_procdef; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_task_procdef ON public.act_ru_task USING btree (proc_def_id_);


--
-- TOC entry 2279 (class 1259 OID 109773862)
-- Name: act_idx_task_procinst; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_task_procinst ON public.act_ru_task USING btree (proc_inst_id_);


--
-- TOC entry 2270 (class 1259 OID 109773847)
-- Name: act_idx_tskass_task; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_tskass_task ON public.act_ru_identitylink USING btree (task_id_);


--
-- TOC entry 2282 (class 1259 OID 109773863)
-- Name: act_idx_var_bytearray; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_var_bytearray ON public.act_ru_variable USING btree (bytearray_id_);


--
-- TOC entry 2283 (class 1259 OID 109773864)
-- Name: act_idx_var_exe; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_var_exe ON public.act_ru_variable USING btree (execution_id_);


--
-- TOC entry 2284 (class 1259 OID 109773865)
-- Name: act_idx_var_procinst; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_var_procinst ON public.act_ru_variable USING btree (proc_inst_id_);


--
-- TOC entry 2285 (class 1259 OID 109773866)
-- Name: act_idx_variable_task_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_variable_task_id ON public.act_ru_variable USING btree (task_id_);


SET search_path = quartz, pg_catalog;

--
-- TOC entry 2294 (class 1259 OID 109773879)
-- Name: idx_qrtz_ft_inst_job_req_rcvry; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_ft_inst_job_req_rcvry ON quartz.qrtz_fired_triggers USING btree (sched_name, instance_name, requests_recovery);


--
-- TOC entry 2295 (class 1259 OID 109773880)
-- Name: idx_qrtz_ft_j_g; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_ft_j_g ON quartz.qrtz_fired_triggers USING btree (sched_name, job_name, job_group);


--
-- TOC entry 2296 (class 1259 OID 109773881)
-- Name: idx_qrtz_ft_jg; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_ft_jg ON quartz.qrtz_fired_triggers USING btree (sched_name, job_group);


--
-- TOC entry 2297 (class 1259 OID 109773882)
-- Name: idx_qrtz_ft_t_g; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_ft_t_g ON quartz.qrtz_fired_triggers USING btree (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2298 (class 1259 OID 109773883)
-- Name: idx_qrtz_ft_tg; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_ft_tg ON quartz.qrtz_fired_triggers USING btree (sched_name, trigger_group);


--
-- TOC entry 2299 (class 1259 OID 109773884)
-- Name: idx_qrtz_ft_trig_inst_name; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_ft_trig_inst_name ON quartz.qrtz_fired_triggers USING btree (sched_name, instance_name);


--
-- TOC entry 2302 (class 1259 OID 109773889)
-- Name: idx_qrtz_j_grp; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_j_grp ON quartz.qrtz_job_details USING btree (sched_name, job_group);


--
-- TOC entry 2303 (class 1259 OID 109773890)
-- Name: idx_qrtz_j_req_recovery; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_j_req_recovery ON quartz.qrtz_job_details USING btree (sched_name, requests_recovery);


--
-- TOC entry 2316 (class 1259 OID 109773913)
-- Name: idx_qrtz_t_c; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_c ON quartz.qrtz_triggers USING btree (sched_name, calendar_name);


--
-- TOC entry 2317 (class 1259 OID 109773914)
-- Name: idx_qrtz_t_g; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_g ON quartz.qrtz_triggers USING btree (sched_name, trigger_group);


--
-- TOC entry 2318 (class 1259 OID 109773915)
-- Name: idx_qrtz_t_j; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_j ON quartz.qrtz_triggers USING btree (sched_name, job_name, job_group);


--
-- TOC entry 2319 (class 1259 OID 109773916)
-- Name: idx_qrtz_t_jg; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_jg ON quartz.qrtz_triggers USING btree (sched_name, job_group);


--
-- TOC entry 2320 (class 1259 OID 109773917)
-- Name: idx_qrtz_t_n_g_state; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_n_g_state ON quartz.qrtz_triggers USING btree (sched_name, trigger_group, trigger_state);


--
-- TOC entry 2321 (class 1259 OID 109773918)
-- Name: idx_qrtz_t_n_state; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_n_state ON quartz.qrtz_triggers USING btree (sched_name, trigger_name, trigger_group, trigger_state);


--
-- TOC entry 2322 (class 1259 OID 109773919)
-- Name: idx_qrtz_t_next_fire_time; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_next_fire_time ON quartz.qrtz_triggers USING btree (sched_name, next_fire_time);


--
-- TOC entry 2323 (class 1259 OID 109773920)
-- Name: idx_qrtz_t_nft_misfire; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_nft_misfire ON quartz.qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time);


--
-- TOC entry 2324 (class 1259 OID 109773921)
-- Name: idx_qrtz_t_nft_st; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_nft_st ON quartz.qrtz_triggers USING btree (sched_name, trigger_state, next_fire_time);


--
-- TOC entry 2325 (class 1259 OID 109773922)
-- Name: idx_qrtz_t_nft_st_misfire; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_nft_st_misfire ON quartz.qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_state);


--
-- TOC entry 2326 (class 1259 OID 109773923)
-- Name: idx_qrtz_t_nft_st_misfire_grp; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_nft_st_misfire_grp ON quartz.qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);


--
-- TOC entry 2327 (class 1259 OID 109773924)
-- Name: idx_qrtz_t_state; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_state ON quartz.qrtz_triggers USING btree (sched_name, trigger_state);


SET search_path = smarthome, pg_catalog;

--
-- TOC entry 2348 (class 1259 OID 109773933)
-- Name: agent_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX agent_user_idx ON smarthome.agent USING btree (user_id);


--
-- TOC entry 2502 (class 1259 OID 109774537)
-- Name: agentconfig_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX agentconfig_idx ON smarthome.agent_config USING btree (agent_id);


--
-- TOC entry 2353 (class 1259 OID 109773934)
-- Name: agenttoken_token_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX agenttoken_token_idx ON smarthome.agent_token USING btree (token);


--
-- TOC entry 2358 (class 1259 OID 109773939)
-- Name: chart_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX chart_user_idx ON smarthome.chart USING btree (user_id);


--
-- TOC entry 2361 (class 1259 OID 109773940)
-- Name: chartdevice_chart_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX chartdevice_chart_idx ON smarthome.chart_device USING btree (chart_id);


--
-- TOC entry 2505 (class 1259 OID 109774540)
-- Name: composantvue_namepageuser_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX composantvue_namepageuser_idx ON smarthome.composant_vue USING btree (name, page, user_id);


--
-- TOC entry 2368 (class 1259 OID 109773959)
-- Name: device_agent_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX device_agent_idx ON smarthome.device USING btree (agent_id);


--
-- TOC entry 2369 (class 1259 OID 109773960)
-- Name: device_macagent_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX device_macagent_idx ON smarthome.device USING btree (agent_id, mac);


--
-- TOC entry 2372 (class 1259 OID 109773961)
-- Name: device_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX device_user_idx ON smarthome.device USING btree (user_id);


--
-- TOC entry 2375 (class 1259 OID 109773954)
-- Name: devicealert_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicealert_idx ON smarthome.device_alert USING btree (date_debut, device_id);


--
-- TOC entry 2378 (class 1259 OID 109773962)
-- Name: devicelevelalert_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicelevelalert_idx ON smarthome.device_level_alert USING btree (device_id);


--
-- TOC entry 2381 (class 1259 OID 109773981)
-- Name: devicemetadata_device_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicemetadata_device_idx ON smarthome.device_metadata USING btree (device_id);


--
-- TOC entry 2386 (class 1259 OID 109773976)
-- Name: devicemetavalue_device_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicemetavalue_device_idx ON smarthome.device_metavalue USING btree (device_id);


--
-- TOC entry 2510 (class 1259 OID 109774541)
-- Name: deviceplanning_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX deviceplanning_idx ON smarthome.device_planning USING btree (device_id);


--
-- TOC entry 2391 (class 1259 OID 109773971)
-- Name: deviceshare_device_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX deviceshare_device_idx ON smarthome.device_share USING btree (device_id);


--
-- TOC entry 2406 (class 1259 OID 109773990)
-- Name: devicetypeproviderprix_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicetypeproviderprix_idx ON smarthome.device_type_provider_prix USING btree (annee, device_type_provider_id);


--
-- TOC entry 2409 (class 1259 OID 109774491)
-- Name: devicevalue_device_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicevalue_device_idx ON smarthome.device_value USING btree (device_id);


--
-- TOC entry 2410 (class 1259 OID 109774492)
-- Name: devicevalue_devicename_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicevalue_devicename_idx ON smarthome.device_value USING btree (date_value, device_id, name);


--
-- TOC entry 2413 (class 1259 OID 109774474)
-- Name: devicevalueday_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicevalueday_idx ON smarthome.device_value_day USING btree (date_value, device_id, name);


--
-- TOC entry 2416 (class 1259 OID 109774021)
-- Name: devicevaluemonth_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicevaluemonth_idx ON smarthome.device_value_month USING btree (date_value, device_id, name);


--
-- TOC entry 2419 (class 1259 OID 109773995)
-- Name: event_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX event_user_idx ON smarthome.event USING btree (user_id);


--
-- TOC entry 2422 (class 1259 OID 109773996)
-- Name: eventdevice_device_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX eventdevice_device_idx ON smarthome.event_device USING btree (device_id);


--
-- TOC entry 2423 (class 1259 OID 109773997)
-- Name: eventdevice_event_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX eventdevice_event_idx ON smarthome.event_device USING btree (event_id);


--
-- TOC entry 2426 (class 1259 OID 109774002)
-- Name: eventmode_event_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX eventmode_event_idx ON smarthome.event_mode USING btree (event_id);


--
-- TOC entry 2429 (class 1259 OID 109774003)
-- Name: eventtrigger_event_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX eventtrigger_event_idx ON smarthome.event_trigger USING btree (event_id);


--
-- TOC entry 2432 (class 1259 OID 109774008)
-- Name: house_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX house_user_idx ON smarthome.house USING btree (user_id);


--
-- TOC entry 2435 (class 1259 OID 109774013)
-- Name: houseconso_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX houseconso_idx ON smarthome.house_conso USING btree (house_id);


--
-- TOC entry 2444 (class 1259 OID 109774018)
-- Name: mode_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX mode_user_idx ON smarthome.mode USING btree (user_id);


--
-- TOC entry 2447 (class 1259 OID 109774022)
-- Name: notification_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX notification_user_idx ON smarthome.notification USING btree (user_id);


--
-- TOC entry 2450 (class 1259 OID 109774031)
-- Name: notificationaccount_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX notificationaccount_user_idx ON smarthome.notification_account USING btree (user_id);


--
-- TOC entry 2522 (class 1259 OID 109782147)
-- Name: producteurenergieaction_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX producteurenergieaction_idx ON smarthome.producteur_energie_action USING btree (user_id);


--
-- TOC entry 2465 (class 1259 OID 109774042)
-- Name: scenario_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX scenario_user_idx ON smarthome.scenario USING btree (user_id);


--
-- TOC entry 2472 (class 1259 OID 109774049)
-- Name: useradmin_admin_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX useradmin_admin_idx ON smarthome.user_admin USING btree (admin_id);


--
-- TOC entry 2479 (class 1259 OID 109774542)
-- Name: userapplication_token_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX userapplication_token_idx ON smarthome.user_application USING btree (token);


--
-- TOC entry 2480 (class 1259 OID 109774068)
-- Name: userapplication_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX userapplication_user_idx ON smarthome.user_application USING btree (user_id);


--
-- TOC entry 2491 (class 1259 OID 109774543)
-- Name: userapplication_username_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX userapplication_username_idx ON smarthome.utilisateur USING btree (username);


--
-- TOC entry 2485 (class 1259 OID 109774058)
-- Name: userfriend_friend_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX userfriend_friend_idx ON smarthome.user_friend USING btree (friend_id);


--
-- TOC entry 2486 (class 1259 OID 109774059)
-- Name: userfriend_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX userfriend_user_idx ON smarthome.user_friend USING btree (user_id);


--
-- TOC entry 2517 (class 1259 OID 109774600)
-- Name: widgetuser_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX widgetuser_idx ON smarthome.widget_user USING btree (user_id);


SET search_path = public, pg_catalog;

--
-- TOC entry 2535 (class 2606 OID 109774089)
-- Name: act_fk_athrz_procedef; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_identitylink
    ADD CONSTRAINT act_fk_athrz_procedef FOREIGN KEY (proc_def_id_) REFERENCES act_re_procdef(id_);


--
-- TOC entry 2523 (class 2606 OID 109774122)
-- Name: act_fk_bytearr_depl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ge_bytearray
    ADD CONSTRAINT act_fk_bytearr_depl FOREIGN KEY (deployment_id_) REFERENCES act_re_deployment(id_);


--
-- TOC entry 2529 (class 2606 OID 109774132)
-- Name: act_fk_event_exec; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_event_subscr
    ADD CONSTRAINT act_fk_event_exec FOREIGN KEY (execution_id_) REFERENCES act_ru_execution(id_);


--
-- TOC entry 2530 (class 2606 OID 109774144)
-- Name: act_fk_exe_parent; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_execution
    ADD CONSTRAINT act_fk_exe_parent FOREIGN KEY (parent_id_) REFERENCES act_ru_execution(id_);


--
-- TOC entry 2533 (class 2606 OID 109774097)
-- Name: act_fk_exe_procdef; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_execution
    ADD CONSTRAINT act_fk_exe_procdef FOREIGN KEY (proc_def_id_) REFERENCES act_re_procdef(id_);


--
-- TOC entry 2531 (class 2606 OID 109774152)
-- Name: act_fk_exe_procinst; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_execution
    ADD CONSTRAINT act_fk_exe_procinst FOREIGN KEY (proc_inst_id_) REFERENCES act_ru_execution(id_);


--
-- TOC entry 2532 (class 2606 OID 109774157)
-- Name: act_fk_exe_super; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_execution
    ADD CONSTRAINT act_fk_exe_super FOREIGN KEY (super_exec_) REFERENCES act_ru_execution(id_);


--
-- TOC entry 2534 (class 2606 OID 109774164)
-- Name: act_fk_idl_procinst; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_identitylink
    ADD CONSTRAINT act_fk_idl_procinst FOREIGN KEY (proc_inst_id_) REFERENCES act_ru_execution(id_);


--
-- TOC entry 2537 (class 2606 OID 109774075)
-- Name: act_fk_job_exception; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_job
    ADD CONSTRAINT act_fk_job_exception FOREIGN KEY (exception_stack_id_) REFERENCES act_ge_bytearray(id_);


--
-- TOC entry 2524 (class 2606 OID 109774079)
-- Name: act_fk_memb_group; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_id_membership
    ADD CONSTRAINT act_fk_memb_group FOREIGN KEY (group_id_) REFERENCES act_id_group(id_);


--
-- TOC entry 2525 (class 2606 OID 109774123)
-- Name: act_fk_memb_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_id_membership
    ADD CONSTRAINT act_fk_memb_user FOREIGN KEY (user_id_) REFERENCES act_id_user(id_);


--
-- TOC entry 2528 (class 2606 OID 109774084)
-- Name: act_fk_model_deployment; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_re_model
    ADD CONSTRAINT act_fk_model_deployment FOREIGN KEY (deployment_id_) REFERENCES act_re_deployment(id_);


--
-- TOC entry 2526 (class 2606 OID 109774102)
-- Name: act_fk_model_source; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_re_model
    ADD CONSTRAINT act_fk_model_source FOREIGN KEY (editor_source_value_id_) REFERENCES act_ge_bytearray(id_);


--
-- TOC entry 2527 (class 2606 OID 109774107)
-- Name: act_fk_model_source_extra; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_re_model
    ADD CONSTRAINT act_fk_model_source_extra FOREIGN KEY (editor_source_extra_value_id_) REFERENCES act_ge_bytearray(id_);


--
-- TOC entry 2538 (class 2606 OID 109774177)
-- Name: act_fk_task_exe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_task
    ADD CONSTRAINT act_fk_task_exe FOREIGN KEY (execution_id_) REFERENCES act_ru_execution(id_);


--
-- TOC entry 2540 (class 2606 OID 109774112)
-- Name: act_fk_task_procdef; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_task
    ADD CONSTRAINT act_fk_task_procdef FOREIGN KEY (proc_def_id_) REFERENCES act_re_procdef(id_);


--
-- TOC entry 2539 (class 2606 OID 109774187)
-- Name: act_fk_task_procinst; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_task
    ADD CONSTRAINT act_fk_task_procinst FOREIGN KEY (proc_inst_id_) REFERENCES act_ru_execution(id_);


--
-- TOC entry 2536 (class 2606 OID 109774133)
-- Name: act_fk_tskass_task; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_identitylink
    ADD CONSTRAINT act_fk_tskass_task FOREIGN KEY (task_id_) REFERENCES act_ru_task(id_);


--
-- TOC entry 2541 (class 2606 OID 109774114)
-- Name: act_fk_var_bytearray; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_variable
    ADD CONSTRAINT act_fk_var_bytearray FOREIGN KEY (bytearray_id_) REFERENCES act_ge_bytearray(id_);


--
-- TOC entry 2542 (class 2606 OID 109774197)
-- Name: act_fk_var_exe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_variable
    ADD CONSTRAINT act_fk_var_exe FOREIGN KEY (execution_id_) REFERENCES act_ru_execution(id_);


--
-- TOC entry 2543 (class 2606 OID 109774207)
-- Name: act_fk_var_procinst; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY act_ru_variable
    ADD CONSTRAINT act_fk_var_procinst FOREIGN KEY (proc_inst_id_) REFERENCES act_ru_execution(id_);


SET search_path = quartz, pg_catalog;

--
-- TOC entry 2544 (class 2606 OID 109774210)
-- Name: qrtz_blob_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);


--
-- TOC entry 2545 (class 2606 OID 109774222)
-- Name: qrtz_cron_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);


--
-- TOC entry 2546 (class 2606 OID 109774227)
-- Name: qrtz_simple_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);


--
-- TOC entry 2547 (class 2606 OID 109774240)
-- Name: qrtz_simprop_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_simprop_triggers
    ADD CONSTRAINT qrtz_simprop_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES qrtz_triggers(sched_name, trigger_name, trigger_group);


--
-- TOC entry 2548 (class 2606 OID 109774142)
-- Name: qrtz_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY qrtz_triggers
    ADD CONSTRAINT qrtz_triggers_sched_name_fkey FOREIGN KEY (sched_name, job_name, job_group) REFERENCES qrtz_job_details(sched_name, job_name, job_group);


SET search_path = smarthome, pg_catalog;

--
-- TOC entry 2554 (class 2606 OID 109774384)
-- Name: fk_1cnslwhtfglbvv3mxvlakf258; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY agent
    ADD CONSTRAINT fk_1cnslwhtfglbvv3mxvlakf258 FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2593 (class 2606 OID 109774392)
-- Name: fk_1urdwwsh2ti15ta6f6p5dbdcp; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY notification
    ADD CONSTRAINT fk_1urdwwsh2ti15ta6f6p5dbdcp FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2581 (class 2606 OID 109774262)
-- Name: fk_1yr7je7pr40foh67txdtlvim9; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house
    ADD CONSTRAINT fk_1yr7je7pr40foh67txdtlvim9 FOREIGN KEY (compteur_id) REFERENCES device(id);


--
-- TOC entry 2557 (class 2606 OID 109774252)
-- Name: fk_35pcbj8yre7q5kqyn42sj0gbw; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY chart_device
    ADD CONSTRAINT fk_35pcbj8yre7q5kqyn42sj0gbw FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 2586 (class 2606 OID 109774397)
-- Name: fk_3cuicb608pp7ye1uwdase8kdc; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house
    ADD CONSTRAINT fk_3cuicb608pp7ye1uwdase8kdc FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2589 (class 2606 OID 109774340)
-- Name: fk_4xfw1euybn4tedme3tec8cnuq; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house_mode
    ADD CONSTRAINT fk_4xfw1euybn4tedme3tec8cnuq FOREIGN KEY (mode_id) REFERENCES mode(id);


--
-- TOC entry 2605 (class 2606 OID 109774550)
-- Name: fk_5s0l070qf5n1jfum5014l25pe; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_planning
    ADD CONSTRAINT fk_5s0l070qf5n1jfum5014l25pe FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 2551 (class 2606 OID 109774162)
-- Name: fk_6c3ugmk053uy27bk2sred31lf; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT fk_6c3ugmk053uy27bk2sred31lf FOREIGN KEY (object_id_class) REFERENCES acl_class(id);


--
-- TOC entry 2565 (class 2606 OID 109774272)
-- Name: fk_6cdhvsbk2s5gtmmg9iq42gy4u; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_metadata
    ADD CONSTRAINT fk_6cdhvsbk2s5gtmmg9iq42gy4u FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 2580 (class 2606 OID 109774279)
-- Name: fk_6fnqfvsdd6lts8yxy823dge5h; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY event_trigger
    ADD CONSTRAINT fk_6fnqfvsdd6lts8yxy823dge5h FOREIGN KEY (event_id) REFERENCES event(id);


--
-- TOC entry 2576 (class 2606 OID 109774297)
-- Name: fk_6m5b3x44x7mnjpe08eci17cx3; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY event_device
    ADD CONSTRAINT fk_6m5b3x44x7mnjpe08eci17cx3 FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 2552 (class 2606 OID 109774172)
-- Name: fk_6oap2k8q5bl33yq3yffrwedhf; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT fk_6oap2k8q5bl33yq3yffrwedhf FOREIGN KEY (parent_object) REFERENCES acl_object_identity(id);


--
-- TOC entry 2559 (class 2606 OID 109774403)
-- Name: fk_6xl4jvqdalelcfj2w297pvbe4; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fk_6xl4jvqdalelcfj2w297pvbe4 FOREIGN KEY (device_type_id) REFERENCES device_type(id);


--
-- TOC entry 2585 (class 2606 OID 109774247)
-- Name: fk_7ebfumnwc8rg39c0w01419gmq; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house
    ADD CONSTRAINT fk_7ebfumnwc8rg39c0w01419gmq FOREIGN KEY (chauffage_id) REFERENCES chauffage(id);


--
-- TOC entry 2601 (class 2606 OID 109774402)
-- Name: fk_7y4q4hntapssdn8ea963uko0g; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_friend
    ADD CONSTRAINT fk_7y4q4hntapssdn8ea963uko0g FOREIGN KEY (friend_id) REFERENCES utilisateur(id);


--
-- TOC entry 2587 (class 2606 OID 109774312)
-- Name: fk_8an7omec7wwt4tjjx0ot1o6vh; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house_conso
    ADD CONSTRAINT fk_8an7omec7wwt4tjjx0ot1o6vh FOREIGN KEY (house_id) REFERENCES house(id);


--
-- TOC entry 2598 (class 2606 OID 109774412)
-- Name: fk_8qc7mfu2ibt6k1lfs57m7yv5u; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_admin
    ADD CONSTRAINT fk_8qc7mfu2ibt6k1lfs57m7yv5u FOREIGN KEY (admin_id) REFERENCES utilisateur(id);


--
-- TOC entry 2567 (class 2606 OID 109774307)
-- Name: fk_a003di1utpawx4g1sm2cxclsh; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_share
    ADD CONSTRAINT fk_a003di1utpawx4g1sm2cxclsh FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 2571 (class 2606 OID 109774257)
-- Name: fk_afmgbcw5x4o6alxb03v637h6r; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_type_provider_prix
    ADD CONSTRAINT fk_afmgbcw5x4o6alxb03v637h6r FOREIGN KEY (device_type_provider_id) REFERENCES device_type_provider(id);


--
-- TOC entry 2602 (class 2606 OID 109774417)
-- Name: fk_apcc8lxk2xnug8377fatvbn04; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT fk_apcc8lxk2xnug8377fatvbn04 FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2582 (class 2606 OID 109774352)
-- Name: fk_bc6fovewk21kac6c0c7avsedg; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house
    ADD CONSTRAINT fk_bc6fovewk21kac6c0c7avsedg FOREIGN KEY (humidite_id) REFERENCES device(id);


--
-- TOC entry 2555 (class 2606 OID 109774217)
-- Name: fk_br886ci31eerjaq0o8eea1wax; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY agent_token
    ADD CONSTRAINT fk_br886ci31eerjaq0o8eea1wax FOREIGN KEY (agent_id) REFERENCES agent(id);


--
-- TOC entry 2607 (class 2606 OID 109774560)
-- Name: fk_cope76rahuwfefx4sbqsd9cfa; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY planning
    ADD CONSTRAINT fk_cope76rahuwfefx4sbqsd9cfa FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2577 (class 2606 OID 109774284)
-- Name: fk_eu5hqnh7o991m7yvhonkt1lye; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY event_device
    ADD CONSTRAINT fk_eu5hqnh7o991m7yvhonkt1lye FOREIGN KEY (event_id) REFERENCES event(id);


--
-- TOC entry 2599 (class 2606 OID 109774422)
-- Name: fk_f3hm86vrk5vvdee5l082u5s0n; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_application
    ADD CONSTRAINT fk_f3hm86vrk5vvdee5l082u5s0n FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2610 (class 2606 OID 109782158)
-- Name: fk_femjim9f52aklresonjni5imw; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY producteur_energie_action
    ADD CONSTRAINT fk_femjim9f52aklresonjni5imw FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 2549 (class 2606 OID 109774182)
-- Name: fk_fhuoesmjef3mrv0gpja4shvcr; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY acl_entry
    ADD CONSTRAINT fk_fhuoesmjef3mrv0gpja4shvcr FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);


--
-- TOC entry 2564 (class 2606 OID 109774292)
-- Name: fk_gqwmu9wwpqfl50ol3e3uou3cn; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_level_alert
    ADD CONSTRAINT fk_gqwmu9wwpqfl50ol3e3uou3cn FOREIGN KEY (event_id) REFERENCES event(id);


--
-- TOC entry 2573 (class 2606 OID 109774475)
-- Name: fk_hp3719536wl5ut8vpb0oeq2fh; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_value_day
    ADD CONSTRAINT fk_hp3719536wl5ut8vpb0oeq2fh FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 2550 (class 2606 OID 109774190)
-- Name: fk_i6xyfccd4y3wlwhgwpo4a9rm1; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY acl_entry
    ADD CONSTRAINT fk_i6xyfccd4y3wlwhgwpo4a9rm1 FOREIGN KEY (sid) REFERENCES acl_sid(id);


--
-- TOC entry 2578 (class 2606 OID 109774302)
-- Name: fk_iagpck079c8f9qaeedorn55o4; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY event_mode
    ADD CONSTRAINT fk_iagpck079c8f9qaeedorn55o4 FOREIGN KEY (event_id) REFERENCES event(id);


--
-- TOC entry 2603 (class 2606 OID 109774374)
-- Name: fk_it77eq964jhfqtu54081ebtio; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT fk_it77eq964jhfqtu54081ebtio FOREIGN KEY (role_id) REFERENCES role(id);


--
-- TOC entry 2604 (class 2606 OID 109774545)
-- Name: fk_jk3nr1605a9s14dhh42dwfc1i; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY agent_config
    ADD CONSTRAINT fk_jk3nr1605a9s14dhh42dwfc1i FOREIGN KEY (agent_id) REFERENCES agent(id);


--
-- TOC entry 2561 (class 2606 OID 109774228)
-- Name: fk_jmdshic0js5i3i86rklyyt3k2; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fk_jmdshic0js5i3i86rklyyt3k2 FOREIGN KEY (agent_id) REFERENCES agent(id);


--
-- TOC entry 2583 (class 2606 OID 109774362)
-- Name: fk_kfqav7dhvl2e9f3ya7p2cp555; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house
    ADD CONSTRAINT fk_kfqav7dhvl2e9f3ya7p2cp555 FOREIGN KEY (temperature_id) REFERENCES device(id);


--
-- TOC entry 2556 (class 2606 OID 109774427)
-- Name: fk_kqvlhio8hb7653mcwirs2cu9; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY chart
    ADD CONSTRAINT fk_kqvlhio8hb7653mcwirs2cu9 FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2569 (class 2606 OID 109774267)
-- Name: fk_lcyq6drc08gyf6r47h48osj6d; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_type_config
    ADD CONSTRAINT fk_lcyq6drc08gyf6r47h48osj6d FOREIGN KEY (device_type_id) REFERENCES device_type(id);


--
-- TOC entry 2609 (class 2606 OID 109774590)
-- Name: fk_lke750rg7c1tsh0bc6oef8tcp; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY widget_user
    ADD CONSTRAINT fk_lke750rg7c1tsh0bc6oef8tcp FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2572 (class 2606 OID 109774493)
-- Name: fk_lr45joo6jlxnvimrwjadf1njy; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_value
    ADD CONSTRAINT fk_lr45joo6jlxnvimrwjadf1njy FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 2566 (class 2606 OID 109774315)
-- Name: fk_lukbb9rmvsvnakj8vq6o76jvk; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_metavalue
    ADD CONSTRAINT fk_lukbb9rmvsvnakj8vq6o76jvk FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 2608 (class 2606 OID 109774595)
-- Name: fk_m5scaiudukbhi9n8pebd09jar; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY widget_user
    ADD CONSTRAINT fk_m5scaiudukbhi9n8pebd09jar FOREIGN KEY (widget_id) REFERENCES widget(id);


--
-- TOC entry 2590 (class 2606 OID 109774322)
-- Name: fk_mbiswed9yydiegpk3wpuvsa1o; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house_weather
    ADD CONSTRAINT fk_mbiswed9yydiegpk3wpuvsa1o FOREIGN KEY (house_id) REFERENCES house(id);


--
-- TOC entry 2592 (class 2606 OID 109774357)
-- Name: fk_ml6ayph0ambkcxbh0o3ewoj3t; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY notification
    ADD CONSTRAINT fk_ml6ayph0ambkcxbh0o3ewoj3t FOREIGN KEY (notification_account_id) REFERENCES notification_account(id);


--
-- TOC entry 2563 (class 2606 OID 109774326)
-- Name: fk_mpkgxbnj07tdcqj8uj26lbaxc; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_level_alert
    ADD CONSTRAINT fk_mpkgxbnj07tdcqj8uj26lbaxc FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 2588 (class 2606 OID 109774332)
-- Name: fk_n6t5i62plvx3hugwa2mkg4vn9; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house_mode
    ADD CONSTRAINT fk_n6t5i62plvx3hugwa2mkg4vn9 FOREIGN KEY (house_id) REFERENCES house(id);


--
-- TOC entry 2606 (class 2606 OID 109774555)
-- Name: fk_niiqkydjhfshk1hfdbrhvbp6b; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_planning
    ADD CONSTRAINT fk_niiqkydjhfshk1hfdbrhvbp6b FOREIGN KEY (planning_id) REFERENCES planning(id);


--
-- TOC entry 2562 (class 2606 OID 109774337)
-- Name: fk_nqldpme8ymhomv4lijf1mfy6s; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_alert
    ADD CONSTRAINT fk_nqldpme8ymhomv4lijf1mfy6s FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 2553 (class 2606 OID 109774201)
-- Name: fk_nxv5we2ion9fwedbkge7syoc3; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT fk_nxv5we2ion9fwedbkge7syoc3 FOREIGN KEY (owner_sid) REFERENCES acl_sid(id);


--
-- TOC entry 2575 (class 2606 OID 109774432)
-- Name: fk_p84ruvsg7mfwb2x5p7iq3q103; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY event
    ADD CONSTRAINT fk_p84ruvsg7mfwb2x5p7iq3q103 FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2579 (class 2606 OID 109774347)
-- Name: fk_pdvj3h7bafhx1chvb02yr8rql; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY event_mode
    ADD CONSTRAINT fk_pdvj3h7bafhx1chvb02yr8rql FOREIGN KEY (mode_id) REFERENCES mode(id);


--
-- TOC entry 2584 (class 2606 OID 109774372)
-- Name: fk_pjh6ue6njvdkm0wpgsfpp5b06; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY house
    ADD CONSTRAINT fk_pjh6ue6njvdkm0wpgsfpp5b06 FOREIGN KEY (compteur_gaz_id) REFERENCES device(id);


--
-- TOC entry 2595 (class 2606 OID 109774367)
-- Name: fk_pkpj8vscd192vwytdy0muk07w; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY notification_account
    ADD CONSTRAINT fk_pkpj8vscd192vwytdy0muk07w FOREIGN KEY (notification_account_sender_id) REFERENCES notification_account_sender(id);


--
-- TOC entry 2612 (class 2606 OID 109782148)
-- Name: fk_plyj6fs6dd3onvx6kp7ww1ma1; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY producteur_energie_action
    ADD CONSTRAINT fk_plyj6fs6dd3onvx6kp7ww1ma1 FOREIGN KEY (producteur_id) REFERENCES producteur_energie(id);


--
-- TOC entry 2596 (class 2606 OID 109774437)
-- Name: fk_qhkmwhsklsps2wwxr6j1vbwfm; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY scenario
    ADD CONSTRAINT fk_qhkmwhsklsps2wwxr6j1vbwfm FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2597 (class 2606 OID 109774442)
-- Name: fk_qsbew1kq0sx0ixgrmyr61h765; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_admin
    ADD CONSTRAINT fk_qsbew1kq0sx0ixgrmyr61h765 FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2594 (class 2606 OID 109774447)
-- Name: fk_r1adfviyy0kbx8c3j3ymq57gc; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY notification_account
    ADD CONSTRAINT fk_r1adfviyy0kbx8c3j3ymq57gc FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2558 (class 2606 OID 109774237)
-- Name: fk_r3xmuaiat9nrpt0o2w11ct560; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY chart_device
    ADD CONSTRAINT fk_r3xmuaiat9nrpt0o2w11ct560 FOREIGN KEY (chart_id) REFERENCES chart(id);


--
-- TOC entry 2568 (class 2606 OID 109774452)
-- Name: fk_rng1qa0gnt7j28twkjv4bfcii; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_share
    ADD CONSTRAINT fk_rng1qa0gnt7j28twkjv4bfcii FOREIGN KEY (shared_user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2574 (class 2606 OID 109774382)
-- Name: fk_rrpcrl3r629m0ybi0t7sr35xo; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_value_month
    ADD CONSTRAINT fk_rrpcrl3r629m0ybi0t7sr35xo FOREIGN KEY (device_id) REFERENCES device(id);


--
-- TOC entry 2591 (class 2606 OID 109774457)
-- Name: fk_rw6xcqu9fmupf8kw8nrg7lkex; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY mode
    ADD CONSTRAINT fk_rw6xcqu9fmupf8kw8nrg7lkex FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2560 (class 2606 OID 109774462)
-- Name: fk_s9ldpb0w8p735xk2hkbgrhdol; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device
    ADD CONSTRAINT fk_s9ldpb0w8p735xk2hkbgrhdol FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2611 (class 2606 OID 109782153)
-- Name: fk_sftxns51a4rg5qa3d38jlj6v2; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY producteur_energie_action
    ADD CONSTRAINT fk_sftxns51a4rg5qa3d38jlj6v2 FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2570 (class 2606 OID 109774274)
-- Name: fk_tpgklf51c9yp0etblju6870g0; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY device_type_provider
    ADD CONSTRAINT fk_tpgklf51c9yp0etblju6870g0 FOREIGN KEY (device_type_id) REFERENCES device_type(id);


--
-- TOC entry 2600 (class 2606 OID 109774467)
-- Name: fk_yqo5tjhs5j9v500vx9dsciks; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY user_friend
    ADD CONSTRAINT fk_yqo5tjhs5j9v500vx9dsciks FOREIGN KEY (user_id) REFERENCES utilisateur(id);


--
-- TOC entry 2726 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- TOC entry 2728 (class 0 OID 0)
-- Dependencies: 9
-- Name: smarthome; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA smarthome FROM PUBLIC;
REVOKE ALL ON SCHEMA smarthome FROM postgres;
GRANT ALL ON SCHEMA smarthome TO postgres;
GRANT ALL ON SCHEMA smarthome TO PUBLIC;


--
-- TOC entry 2731 (class 0 OID 0)
-- Dependencies: 252
-- Name: widget; Type: ACL; Schema: smarthome; Owner: postgres
--

REVOKE ALL ON TABLE widget FROM PUBLIC;
REVOKE ALL ON TABLE widget FROM postgres;
GRANT ALL ON TABLE widget TO postgres;
GRANT ALL ON TABLE widget TO smarthome;


--
-- TOC entry 2732 (class 0 OID 0)
-- Dependencies: 253
-- Name: widget_user; Type: ACL; Schema: smarthome; Owner: postgres
--

REVOKE ALL ON TABLE widget_user FROM PUBLIC;
REVOKE ALL ON TABLE widget_user FROM postgres;
GRANT ALL ON TABLE widget_user TO postgres;
GRANT ALL ON TABLE widget_user TO smarthome;


-- Completed on 2019-06-28 10:30:54

--
-- PostgreSQL database dump complete
--

