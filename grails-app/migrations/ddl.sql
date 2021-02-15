--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.12
-- Dumped by pg_dump version 13.1

-- Started on 2021-02-15 11:03:49 CET


--
-- TOC entry 10 (class 2615 OID 24244)
-- Name: application; Type: SCHEMA; Schema: -; Owner: pg_signal_backend
--

CREATE SCHEMA application;


ALTER SCHEMA application OWNER TO pg_signal_backend;

--
-- TOC entry 6 (class 2615 OID 24245)
-- Name: quartz; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA quartz;


ALTER SCHEMA quartz OWNER TO postgres;

--
-- TOC entry 9 (class 2615 OID 24246)
-- Name: smarthome; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA smarthome;


ALTER SCHEMA smarthome OWNER TO postgres;

--
-- TOC entry 3289 (class 0 OID 0)
-- Dependencies: 9
-- Name: SCHEMA smarthome; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA smarthome IS 'standard public schema';


SET default_tablespace = '';

--
-- TOC entry 188 (class 1259 OID 24247)
-- Name: defi; Type: TABLE; Schema: application; Owner: postgres
--

CREATE TABLE application.defi (
    id bigint NOT NULL,
    version bigint NOT NULL,
    actif boolean NOT NULL,
    action_debut timestamp without time zone NOT NULL,
    action_fin timestamp without time zone NOT NULL,
    action_electricite double precision,
    action_gaz double precision,
    classement_electricite integer,
    classement_gaz integer,
    classement_global integer,
    economie_electricite double precision,
    economie_gaz double precision,
    economie_global double precision,
    libelle character varying(255) NOT NULL,
    reference_debut timestamp without time zone NOT NULL,
    reference_fin timestamp without time zone NOT NULL,
    reference_electricite double precision,
    reference_gaz double precision,
    user_id bigint NOT NULL,
    total_electricite integer,
    total_gaz integer,
    total_global integer,
    moyenne_electricite double precision,
    moyenne_gaz double precision,
    moyenne_global double precision,
    organisation character varying(255),
    publique boolean DEFAULT true NOT NULL,
    action_eau double precision,
    classement_eau integer,
    economie_eau double precision,
    moyenne_eau double precision,
    reference_eau double precision,
    total_eau integer,
    modele character varying(255)
);


ALTER TABLE application.defi OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 24250)
-- Name: defi_equipe; Type: TABLE; Schema: application; Owner: postgres
--

CREATE TABLE application.defi_equipe (
    id bigint NOT NULL,
    version bigint NOT NULL,
    action_electricite double precision,
    action_gaz double precision,
    classement_electricite integer,
    classement_gaz integer,
    classement_global integer,
    defi_id bigint NOT NULL,
    economie_electricite double precision,
    economie_gaz double precision,
    economie_global double precision,
    libelle character varying(255) NOT NULL,
    reference_electricite double precision,
    reference_gaz double precision,
    total_electricite integer,
    total_gaz integer,
    total_global integer,
    moyenne_electricite double precision,
    moyenne_gaz double precision,
    moyenne_global double precision,
    action_eau double precision,
    classement_eau integer,
    economie_eau double precision,
    moyenne_eau double precision,
    reference_eau double precision,
    total_eau integer
);


ALTER TABLE application.defi_equipe OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 24253)
-- Name: defi_equipe_participant; Type: TABLE; Schema: application; Owner: postgres
--

CREATE TABLE application.defi_equipe_participant (
    id bigint NOT NULL,
    version bigint NOT NULL,
    action_electricite double precision,
    action_gaz double precision,
    classement_electricite integer,
    classement_gaz integer,
    classement_global integer,
    defi_equipe_id bigint NOT NULL,
    economie_electricite double precision,
    economie_gaz double precision,
    economie_global double precision,
    reference_electricite double precision,
    reference_gaz double precision,
    user_id bigint NOT NULL,
    total_electricite integer,
    total_gaz integer,
    total_global integer,
    moyenne_electricite double precision,
    moyenne_gaz double precision,
    moyenne_global double precision,
    action_eau double precision,
    classement_eau integer,
    economie_eau double precision,
    moyenne_eau double precision,
    reference_eau double precision,
    total_eau integer
);


ALTER TABLE application.defi_equipe_participant OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 24256)
-- Name: defi_equipe_profil; Type: TABLE; Schema: application; Owner: postgres
--

CREATE TABLE application.defi_equipe_profil (
    id bigint NOT NULL,
    version bigint NOT NULL,
    action_electricite double precision,
    action_gaz double precision,
    classement_electricite integer,
    classement_gaz integer,
    classement_global integer,
    defi_equipe_id bigint NOT NULL,
    economie_electricite double precision,
    economie_gaz double precision,
    economie_global double precision,
    profil_id bigint NOT NULL,
    reference_electricite double precision,
    reference_gaz double precision,
    total_electricite integer,
    total_gaz integer,
    total_global integer,
    moyenne_electricite double precision,
    moyenne_gaz double precision,
    moyenne_global double precision,
    action_eau double precision,
    classement_eau integer,
    economie_eau double precision,
    moyenne_eau double precision,
    reference_eau double precision,
    total_eau integer
);


ALTER TABLE application.defi_equipe_profil OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 24259)
-- Name: defi_profil; Type: TABLE; Schema: application; Owner: postgres
--

CREATE TABLE application.defi_profil (
    id bigint NOT NULL,
    version bigint NOT NULL,
    action_electricite double precision,
    action_gaz double precision,
    classement_electricite integer,
    classement_gaz integer,
    classement_global integer,
    defi_id bigint NOT NULL,
    economie_electricite double precision,
    economie_gaz double precision,
    economie_global double precision,
    profil_id bigint NOT NULL,
    reference_electricite double precision,
    reference_gaz double precision,
    total_electricite integer,
    total_gaz integer,
    total_global integer,
    moyenne_electricite double precision,
    moyenne_gaz double precision,
    moyenne_global double precision,
    action_eau double precision,
    classement_eau integer,
    economie_eau double precision,
    moyenne_eau double precision,
    reference_eau double precision,
    total_eau integer
);


ALTER TABLE application.defi_profil OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 24262)
-- Name: act_evt_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_evt_log (
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


ALTER TABLE public.act_evt_log OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 24269)
-- Name: act_evt_log_log_nr__seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.act_evt_log_log_nr__seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.act_evt_log_log_nr__seq OWNER TO postgres;

--
-- TOC entry 3297 (class 0 OID 0)
-- Dependencies: 194
-- Name: act_evt_log_log_nr__seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.act_evt_log_log_nr__seq OWNED BY public.act_evt_log.log_nr_;


--
-- TOC entry 195 (class 1259 OID 24271)
-- Name: act_ge_bytearray; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_ge_bytearray (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    name_ character varying(255),
    deployment_id_ character varying(64),
    bytes_ bytea,
    generated_ boolean
);


ALTER TABLE public.act_ge_bytearray OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 24277)
-- Name: act_ge_property; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_ge_property (
    name_ character varying(64) NOT NULL,
    value_ character varying(300),
    rev_ integer
);


ALTER TABLE public.act_ge_property OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 24280)
-- Name: act_id_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_id_group (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    name_ character varying(255),
    type_ character varying(255)
);


ALTER TABLE public.act_id_group OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 24286)
-- Name: act_id_info; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_id_info (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    user_id_ character varying(64),
    type_ character varying(64),
    key_ character varying(255),
    value_ character varying(255),
    password_ bytea,
    parent_id_ character varying(255)
);


ALTER TABLE public.act_id_info OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 24292)
-- Name: act_id_membership; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_id_membership (
    user_id_ character varying(64) NOT NULL,
    group_id_ character varying(64) NOT NULL
);


ALTER TABLE public.act_id_membership OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 24295)
-- Name: act_id_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_id_user (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    first_ character varying(255),
    last_ character varying(255),
    email_ character varying(255),
    pwd_ character varying(255),
    picture_id_ character varying(64)
);


ALTER TABLE public.act_id_user OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 24301)
-- Name: act_re_deployment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_re_deployment (
    id_ character varying(64) NOT NULL,
    name_ character varying(255),
    category_ character varying(255),
    tenant_id_ character varying(255) DEFAULT ''::character varying,
    deploy_time_ timestamp without time zone
);


ALTER TABLE public.act_re_deployment OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 24308)
-- Name: act_re_model; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_re_model (
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


ALTER TABLE public.act_re_model OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 24315)
-- Name: act_re_procdef; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_re_procdef (
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


ALTER TABLE public.act_re_procdef OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 24322)
-- Name: act_ru_event_subscr; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_ru_event_subscr (
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


ALTER TABLE public.act_ru_event_subscr OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 24329)
-- Name: act_ru_execution; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_ru_execution (
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


ALTER TABLE public.act_ru_execution OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 24336)
-- Name: act_ru_identitylink; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_ru_identitylink (
    id_ character varying(64) NOT NULL,
    rev_ integer,
    group_id_ character varying(255),
    type_ character varying(255),
    user_id_ character varying(255),
    task_id_ character varying(64),
    proc_inst_id_ character varying(64),
    proc_def_id_ character varying(64)
);


ALTER TABLE public.act_ru_identitylink OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 24342)
-- Name: act_ru_job; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_ru_job (
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


ALTER TABLE public.act_ru_job OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 24349)
-- Name: act_ru_task; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_ru_task (
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


ALTER TABLE public.act_ru_task OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 24356)
-- Name: act_ru_variable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.act_ru_variable (
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


ALTER TABLE public.act_ru_variable OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 24362)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 24364)
-- Name: qrtz_blob_triggers; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE quartz.qrtz_blob_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    blob_data bytea
);


ALTER TABLE quartz.qrtz_blob_triggers OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 24370)
-- Name: qrtz_calendars; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE quartz.qrtz_calendars (
    sched_name character varying(120) NOT NULL,
    calendar_name character varying(200) NOT NULL,
    calendar bytea NOT NULL
);


ALTER TABLE quartz.qrtz_calendars OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 24376)
-- Name: qrtz_cron_triggers; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE quartz.qrtz_cron_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    cron_expression character varying(120) NOT NULL,
    time_zone_id character varying(80)
);


ALTER TABLE quartz.qrtz_cron_triggers OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 24382)
-- Name: qrtz_fired_triggers; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE quartz.qrtz_fired_triggers (
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


ALTER TABLE quartz.qrtz_fired_triggers OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 24388)
-- Name: qrtz_job_details; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE quartz.qrtz_job_details (
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


ALTER TABLE quartz.qrtz_job_details OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 24394)
-- Name: qrtz_locks; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE quartz.qrtz_locks (
    sched_name character varying(120) NOT NULL,
    lock_name character varying(40) NOT NULL
);


ALTER TABLE quartz.qrtz_locks OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 24397)
-- Name: qrtz_paused_trigger_grps; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE quartz.qrtz_paused_trigger_grps (
    sched_name character varying(120) NOT NULL,
    trigger_group character varying(200) NOT NULL
);


ALTER TABLE quartz.qrtz_paused_trigger_grps OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 24400)
-- Name: qrtz_scheduler_state; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE quartz.qrtz_scheduler_state (
    sched_name character varying(120) NOT NULL,
    instance_name character varying(200) NOT NULL,
    last_checkin_time bigint NOT NULL,
    checkin_interval bigint NOT NULL
);


ALTER TABLE quartz.qrtz_scheduler_state OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 24403)
-- Name: qrtz_simple_triggers; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE quartz.qrtz_simple_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    repeat_count bigint NOT NULL,
    repeat_interval bigint NOT NULL,
    times_triggered bigint NOT NULL
);


ALTER TABLE quartz.qrtz_simple_triggers OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 24409)
-- Name: qrtz_simprop_triggers; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE quartz.qrtz_simprop_triggers (
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


ALTER TABLE quartz.qrtz_simprop_triggers OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 24415)
-- Name: qrtz_triggers; Type: TABLE; Schema: quartz; Owner: postgres
--

CREATE TABLE quartz.qrtz_triggers (
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


ALTER TABLE quartz.qrtz_triggers OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 24421)
-- Name: acl_class; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.acl_class (
    id bigint NOT NULL,
    class character varying(255) NOT NULL
);


ALTER TABLE smarthome.acl_class OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 24424)
-- Name: acl_entry; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.acl_entry (
    id bigint NOT NULL,
    ace_order integer NOT NULL,
    acl_object_identity bigint NOT NULL,
    audit_failure boolean NOT NULL,
    audit_success boolean NOT NULL,
    granting boolean NOT NULL,
    mask integer NOT NULL,
    sid bigint NOT NULL
);


ALTER TABLE smarthome.acl_entry OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 24427)
-- Name: acl_object_identity; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.acl_object_identity (
    id bigint NOT NULL,
    object_id_class bigint NOT NULL,
    entries_inheriting boolean NOT NULL,
    object_id_identity bigint NOT NULL,
    owner_sid bigint,
    parent_object bigint
);


ALTER TABLE smarthome.acl_object_identity OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 24430)
-- Name: acl_sid; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.acl_sid (
    id bigint NOT NULL,
    principal boolean NOT NULL,
    sid character varying(255) NOT NULL
);


ALTER TABLE smarthome.acl_sid OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 24433)
-- Name: agent; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.agent (
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


ALTER TABLE smarthome.agent OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 24441)
-- Name: agent_config; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.agent_config (
    id bigint NOT NULL,
    version bigint NOT NULL,
    agent_id bigint NOT NULL,
    data character varying(255) NOT NULL,
    last_sync timestamp without time zone NOT NULL
);


ALTER TABLE smarthome.agent_config OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 24444)
-- Name: agent_token; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.agent_token (
    id bigint NOT NULL,
    version bigint NOT NULL,
    agent_id bigint NOT NULL,
    date_expiration timestamp without time zone NOT NULL,
    token character varying(255) NOT NULL,
    websocket_key character varying(255),
    server_id character varying(255)
);


ALTER TABLE smarthome.agent_token OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 24450)
-- Name: chart; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.chart (
    id bigint NOT NULL,
    version bigint NOT NULL,
    chart_type character varying(255) NOT NULL,
    groupe character varying(255) NOT NULL,
    label character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    ylegend character varying(255)
);


ALTER TABLE smarthome.chart OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 24456)
-- Name: chart_device; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.chart_device (
    id bigint NOT NULL,
    version bigint NOT NULL,
    chart_id bigint NOT NULL,
    chart_type character varying(255) NOT NULL,
    device_id bigint NOT NULL,
    function character varying(255) NOT NULL,
    metavalue character varying(255),
    "position" integer NOT NULL,
    color character varying(16),
    legend character varying(255),
    transformer text
);


ALTER TABLE smarthome.chart_device OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 24462)
-- Name: chauffage; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.chauffage (
    id bigint NOT NULL,
    version bigint NOT NULL,
    libelle character varying(255) NOT NULL
);


ALTER TABLE smarthome.chauffage OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 24465)
-- Name: commune; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.commune (
    id bigint NOT NULL,
    version bigint NOT NULL,
    code_postal character varying(8) NOT NULL,
    libelle character varying(255) NOT NULL
);


ALTER TABLE smarthome.commune OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 24468)
-- Name: composant_vue; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.composant_vue (
    id bigint NOT NULL,
    version bigint NOT NULL,
    data character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    page character varying(255) NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE smarthome.composant_vue OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 24474)
-- Name: compteur_index; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.compteur_index (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_index timestamp without time zone NOT NULL,
    device_id bigint NOT NULL,
    index1 double precision NOT NULL,
    index2 double precision,
    param1 character varying(255),
    photo bytea
);


ALTER TABLE smarthome.compteur_index OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 24480)
-- Name: config; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.config (
    id bigint NOT NULL,
    version bigint NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE smarthome.config OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 24486)
-- Name: databasechangelog; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.databasechangelog (
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


ALTER TABLE smarthome.databasechangelog OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 24492)
-- Name: databasechangeloglock; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE smarthome.databasechangeloglock OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 24495)
-- Name: device; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device (
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


ALTER TABLE smarthome.device OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 24503)
-- Name: device_alert; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device_alert (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_debut timestamp without time zone NOT NULL,
    date_fin timestamp without time zone,
    device_id bigint NOT NULL,
    level character varying(16) NOT NULL,
    relance integer NOT NULL,
    status character varying(16) NOT NULL
);


ALTER TABLE smarthome.device_alert OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 24506)
-- Name: device_level_alert; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device_level_alert (
    id bigint NOT NULL,
    version bigint NOT NULL,
    device_id bigint NOT NULL,
    event_id bigint,
    level character varying(255) NOT NULL,
    mode character varying(255) NOT NULL,
    tempo integer NOT NULL,
    value double precision NOT NULL
);


ALTER TABLE smarthome.device_level_alert OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 24512)
-- Name: device_metadata; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device_metadata (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    device_id bigint NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255),
    label character varying(512),
    type character varying(255),
    "values" text
);


ALTER TABLE smarthome.device_metadata OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 24519)
-- Name: device_metavalue; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device_metavalue (
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


ALTER TABLE smarthome.device_metavalue OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 24529)
-- Name: device_planning; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device_planning (
    id bigint NOT NULL,
    device_id bigint NOT NULL,
    planning_id bigint NOT NULL
);


ALTER TABLE smarthome.device_planning OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 24532)
-- Name: device_share; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device_share (
    id bigint NOT NULL,
    version bigint NOT NULL,
    device_id bigint NOT NULL,
    shared_user_id bigint NOT NULL
);


ALTER TABLE smarthome.device_share OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 24535)
-- Name: device_type; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device_type (
    id bigint NOT NULL,
    version bigint NOT NULL,
    libelle character varying(255) NOT NULL,
    impl_class character varying(255) NOT NULL,
    qualitatif boolean DEFAULT false NOT NULL,
    planning boolean DEFAULT false NOT NULL
);


ALTER TABLE smarthome.device_type OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 24543)
-- Name: device_type_config; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device_type_config (
    device_type_id bigint NOT NULL,
    version bigint NOT NULL,
    data text NOT NULL
);


ALTER TABLE smarthome.device_type_config OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 24549)
-- Name: device_type_provider; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device_type_provider (
    id bigint NOT NULL,
    version bigint NOT NULL,
    device_type_id bigint NOT NULL,
    libelle character varying(255) NOT NULL
);


ALTER TABLE smarthome.device_type_provider OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 24552)
-- Name: device_type_provider_prix; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device_type_provider_prix (
    id bigint NOT NULL,
    version bigint NOT NULL,
    annee integer NOT NULL,
    contrat character varying(16) NOT NULL,
    device_type_provider_id bigint NOT NULL,
    period character varying(16) NOT NULL,
    prix_unitaire double precision NOT NULL
);


ALTER TABLE smarthome.device_type_provider_prix OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 24555)
-- Name: device_value; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device_value (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_value timestamp without time zone NOT NULL,
    device_id bigint NOT NULL,
    name character varying(64),
    value numeric NOT NULL,
    alert_level character varying(16)
);


ALTER TABLE smarthome.device_value OWNER TO postgres;

--
-- TOC entry 250 (class 1259 OID 24561)
-- Name: device_value_day; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device_value_day (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    date_value timestamp without time zone NOT NULL,
    device_id bigint NOT NULL,
    name character varying(64) NOT NULL,
    value double precision NOT NULL
);


ALTER TABLE smarthome.device_value_day OWNER TO postgres;

--
-- TOC entry 251 (class 1259 OID 24565)
-- Name: device_value_month; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.device_value_month (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    date_value timestamp without time zone NOT NULL,
    device_id bigint NOT NULL,
    name character varying(64) NOT NULL,
    value double precision NOT NULL
);


ALTER TABLE smarthome.device_value_month OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 24569)
-- Name: ecs; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.ecs (
    id bigint NOT NULL,
    version bigint NOT NULL,
    libelle character varying(255) NOT NULL
);


ALTER TABLE smarthome.ecs OWNER TO postgres;

--
-- TOC entry 253 (class 1259 OID 24572)
-- Name: event; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.event (
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


ALTER TABLE smarthome.event OWNER TO postgres;

--
-- TOC entry 254 (class 1259 OID 24580)
-- Name: event_device; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.event_device (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    device_id bigint NOT NULL,
    event_id bigint NOT NULL
);


ALTER TABLE smarthome.event_device OWNER TO postgres;

--
-- TOC entry 255 (class 1259 OID 24584)
-- Name: event_mode; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.event_mode (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    event_id bigint NOT NULL,
    mode_id bigint NOT NULL
);


ALTER TABLE smarthome.event_mode OWNER TO postgres;

--
-- TOC entry 256 (class 1259 OID 24588)
-- Name: event_trigger; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.event_trigger (
    id bigint NOT NULL,
    version bigint DEFAULT 0 NOT NULL,
    action_name character varying(255) NOT NULL,
    domain_class_name character varying(255) NOT NULL,
    domain_id bigint NOT NULL,
    event_id bigint NOT NULL,
    parameters text
);


ALTER TABLE smarthome.event_trigger OWNER TO postgres;

--
-- TOC entry 257 (class 1259 OID 24595)
-- Name: house; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.house (
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
    compteur_gaz_id bigint,
    ecs_id bigint,
    compteur_eau_id bigint
);


ALTER TABLE smarthome.house OWNER TO postgres;

--
-- TOC entry 258 (class 1259 OID 24601)
-- Name: house_conso; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.house_conso (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_conso timestamp without time zone NOT NULL,
    house_id bigint NOT NULL,
    kwhc double precision NOT NULL,
    kwhp double precision NOT NULL,
    kwbase double precision DEFAULT (0)::double precision NOT NULL,
    kwgaz double precision DEFAULT (0)::double precision NOT NULL
);


ALTER TABLE smarthome.house_conso OWNER TO postgres;

--
-- TOC entry 259 (class 1259 OID 24606)
-- Name: house_mode; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.house_mode (
    house_id bigint NOT NULL,
    mode_id bigint NOT NULL
);


ALTER TABLE smarthome.house_mode OWNER TO postgres;

--
-- TOC entry 260 (class 1259 OID 24609)
-- Name: house_weather; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.house_weather (
    house_id bigint NOT NULL,
    data text NOT NULL,
    date_weather timestamp without time zone NOT NULL,
    provider_class character varying(255) NOT NULL
);


ALTER TABLE smarthome.house_weather OWNER TO postgres;

--
-- TOC entry 261 (class 1259 OID 24615)
-- Name: mode; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.mode (
    id bigint NOT NULL,
    version bigint NOT NULL,
    name character varying(32) NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE smarthome.mode OWNER TO postgres;

--
-- TOC entry 262 (class 1259 OID 24618)
-- Name: notification; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.notification (
    id bigint NOT NULL,
    version bigint NOT NULL,
    description character varying(255) NOT NULL,
    message text NOT NULL,
    notification_account_id bigint NOT NULL,
    parameters text,
    user_id bigint NOT NULL
);


ALTER TABLE smarthome.notification OWNER TO postgres;

--
-- TOC entry 263 (class 1259 OID 24624)
-- Name: notification_account; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.notification_account (
    id bigint NOT NULL,
    version bigint NOT NULL,
    config text,
    notification_account_sender_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE smarthome.notification_account OWNER TO postgres;

--
-- TOC entry 264 (class 1259 OID 24630)
-- Name: notification_account_sender; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.notification_account_sender (
    id bigint NOT NULL,
    version bigint NOT NULL,
    impl_class character varying(255) NOT NULL,
    libelle character varying(255) NOT NULL,
    role character varying(255),
    cron character varying(255)
);


ALTER TABLE smarthome.notification_account_sender OWNER TO postgres;

--
-- TOC entry 265 (class 1259 OID 24636)
-- Name: planning; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.planning (
    id bigint NOT NULL,
    version bigint NOT NULL,
    data text NOT NULL,
    label character varying(255) NOT NULL,
    rule text,
    user_id bigint NOT NULL
);


ALTER TABLE smarthome.planning OWNER TO postgres;

--
-- TOC entry 266 (class 1259 OID 24642)
-- Name: producteur_energie; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.producteur_energie (
    id bigint NOT NULL,
    version bigint NOT NULL,
    investissement double precision NOT NULL,
    libelle character varying(255) NOT NULL,
    surface double precision NOT NULL,
    nbaction integer NOT NULL
);


ALTER TABLE smarthome.producteur_energie OWNER TO postgres;

--
-- TOC entry 267 (class 1259 OID 24645)
-- Name: producteur_energie_action; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.producteur_energie_action (
    id bigint NOT NULL,
    version bigint NOT NULL,
    nbaction integer NOT NULL,
    producteur_id bigint NOT NULL,
    user_id bigint NOT NULL,
    device_id bigint
);


ALTER TABLE smarthome.producteur_energie_action OWNER TO postgres;

--
-- TOC entry 268 (class 1259 OID 24648)
-- Name: profil; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.profil (
    id bigint NOT NULL,
    version bigint NOT NULL,
    libelle character varying(255) NOT NULL,
    view character varying(255) NOT NULL,
    icon character varying(255)
);


ALTER TABLE smarthome.profil OWNER TO postgres;

--
-- TOC entry 269 (class 1259 OID 24654)
-- Name: registration_code; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.registration_code (
    id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    server_url character varying(255) NOT NULL,
    token character varying(255) NOT NULL,
    username character varying(255) NOT NULL
);


ALTER TABLE smarthome.registration_code OWNER TO postgres;

--
-- TOC entry 270 (class 1259 OID 24660)
-- Name: role; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.role (
    id bigint NOT NULL,
    version bigint NOT NULL,
    authority character varying(255) NOT NULL
);


ALTER TABLE smarthome.role OWNER TO postgres;

--
-- TOC entry 271 (class 1259 OID 24663)
-- Name: scenario; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.scenario (
    id bigint NOT NULL,
    version bigint NOT NULL,
    description character varying(255),
    label character varying(255) NOT NULL,
    script text NOT NULL,
    user_id bigint NOT NULL,
    last_execution timestamp(6) without time zone
);


ALTER TABLE smarthome.scenario OWNER TO postgres;

--
-- TOC entry 272 (class 1259 OID 24669)
-- Name: script_rule; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.script_rule (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone,
    description character varying(255) NOT NULL,
    last_updated timestamp without time zone,
    rule_name character varying(255) NOT NULL,
    script text NOT NULL
);


ALTER TABLE smarthome.script_rule OWNER TO postgres;

--
-- TOC entry 273 (class 1259 OID 24675)
-- Name: user_admin; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.user_admin (
    user_id bigint NOT NULL,
    admin_id bigint NOT NULL
);


ALTER TABLE smarthome.user_admin OWNER TO postgres;

--
-- TOC entry 274 (class 1259 OID 24678)
-- Name: user_application; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.user_application (
    id bigint NOT NULL,
    version bigint NOT NULL,
    application_id character varying(255) NOT NULL,
    token character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    date_auth timestamp without time zone NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE smarthome.user_application OWNER TO postgres;

--
-- TOC entry 275 (class 1259 OID 24684)
-- Name: user_friend; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.user_friend (
    id bigint NOT NULL,
    version bigint NOT NULL,
    friend_id bigint NOT NULL,
    user_id bigint NOT NULL,
    confirm boolean NOT NULL
);


ALTER TABLE smarthome.user_friend OWNER TO postgres;

--
-- TOC entry 276 (class 1259 OID 24687)
-- Name: user_role; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.user_role (
    role_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE smarthome.user_role OWNER TO postgres;

--
-- TOC entry 277 (class 1259 OID 24690)
-- Name: utilisateur; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.utilisateur (
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
    last_connexion timestamp without time zone,
    profil_id bigint
);


ALTER TABLE smarthome.utilisateur OWNER TO postgres;

--
-- TOC entry 278 (class 1259 OID 24697)
-- Name: widget; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.widget (
    id bigint NOT NULL,
    version bigint NOT NULL,
    description character varying(255) NOT NULL,
    libelle character varying(255) NOT NULL,
    action_name character varying(255) NOT NULL,
    controller_name character varying(255) NOT NULL,
    refresh_period integer,
    config_name character varying(255)
);


ALTER TABLE smarthome.widget OWNER TO postgres;

--
-- TOC entry 279 (class 1259 OID 24703)
-- Name: widget_user; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.widget_user (
    id bigint NOT NULL,
    version bigint NOT NULL,
    col integer NOT NULL,
    data character varying(255),
    param_id bigint,
    "row" integer NOT NULL,
    user_id bigint NOT NULL,
    widget_id bigint NOT NULL
);


ALTER TABLE smarthome.widget_user OWNER TO postgres;

--
-- TOC entry 280 (class 1259 OID 24706)
-- Name: workflow; Type: TABLE; Schema: smarthome; Owner: postgres
--

CREATE TABLE smarthome.workflow (
    id bigint NOT NULL,
    version bigint NOT NULL,
    libelle character varying(255) NOT NULL,
    data bytea,
    description character varying(255)
);


ALTER TABLE smarthome.workflow OWNER TO postgres;

--
-- TOC entry 2688 (class 2604 OID 24712)
-- Name: act_evt_log log_nr_; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_evt_log ALTER COLUMN log_nr_ SET DEFAULT nextval('public.act_evt_log_log_nr__seq'::regclass);


--
-- TOC entry 2728 (class 2606 OID 25003)
-- Name: defi_equipe_participant defi_equipe_participant_pkey; Type: CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_equipe_participant
    ADD CONSTRAINT defi_equipe_participant_pkey PRIMARY KEY (id);


--
-- TOC entry 2723 (class 2606 OID 25001)
-- Name: defi_equipe defi_equipe_pkey; Type: CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_equipe
    ADD CONSTRAINT defi_equipe_pkey PRIMARY KEY (id);


--
-- TOC entry 2734 (class 2606 OID 25011)
-- Name: defi_equipe_profil defi_equipe_profil_pkey; Type: CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_equipe_profil
    ADD CONSTRAINT defi_equipe_profil_pkey PRIMARY KEY (id);


--
-- TOC entry 2719 (class 2606 OID 24995)
-- Name: defi defi_pkey; Type: CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi
    ADD CONSTRAINT defi_pkey PRIMARY KEY (id);


--
-- TOC entry 2739 (class 2606 OID 25017)
-- Name: defi_profil defi_profil_pkey; Type: CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_profil
    ADD CONSTRAINT defi_profil_pkey PRIMARY KEY (id);


--
-- TOC entry 2721 (class 2606 OID 24998)
-- Name: defi uk_b9ap722dj9uv34y31p67kepkh; Type: CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi
    ADD CONSTRAINT uk_b9ap722dj9uv34y31p67kepkh UNIQUE (libelle);


--
-- TOC entry 2732 (class 2606 OID 25008)
-- Name: defi_equipe_participant unique_defi_equipe_id; Type: CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_equipe_participant
    ADD CONSTRAINT unique_defi_equipe_id UNIQUE (user_id, defi_equipe_id);


--
-- TOC entry 2737 (class 2606 OID 25014)
-- Name: defi_equipe_profil unique_defi_equipe_profil; Type: CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_equipe_profil
    ADD CONSTRAINT unique_defi_equipe_profil UNIQUE (profil_id, defi_equipe_id);


--
-- TOC entry 2742 (class 2606 OID 25021)
-- Name: defi_profil unique_defi_id; Type: CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_profil
    ADD CONSTRAINT unique_defi_id UNIQUE (profil_id, defi_id);


--
-- TOC entry 2726 (class 2606 OID 25005)
-- Name: defi_equipe unique_libelle; Type: CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_equipe
    ADD CONSTRAINT unique_libelle UNIQUE (defi_id, libelle);


--
-- TOC entry 2744 (class 2606 OID 25024)
-- Name: act_evt_log act_evt_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_evt_log
    ADD CONSTRAINT act_evt_log_pkey PRIMARY KEY (log_nr_);


--
-- TOC entry 2746 (class 2606 OID 25126)
-- Name: act_ge_bytearray act_ge_bytearray_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ge_bytearray
    ADD CONSTRAINT act_ge_bytearray_pkey PRIMARY KEY (id_);


--
-- TOC entry 2749 (class 2606 OID 25019)
-- Name: act_ge_property act_ge_property_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ge_property
    ADD CONSTRAINT act_ge_property_pkey PRIMARY KEY (name_);


--
-- TOC entry 2751 (class 2606 OID 25028)
-- Name: act_id_group act_id_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_id_group
    ADD CONSTRAINT act_id_group_pkey PRIMARY KEY (id_);


--
-- TOC entry 2753 (class 2606 OID 25027)
-- Name: act_id_info act_id_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_id_info
    ADD CONSTRAINT act_id_info_pkey PRIMARY KEY (id_);


--
-- TOC entry 2755 (class 2606 OID 25032)
-- Name: act_id_membership act_id_membership_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_id_membership
    ADD CONSTRAINT act_id_membership_pkey PRIMARY KEY (user_id_, group_id_);


--
-- TOC entry 2759 (class 2606 OID 25031)
-- Name: act_id_user act_id_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_id_user
    ADD CONSTRAINT act_id_user_pkey PRIMARY KEY (id_);


--
-- TOC entry 2761 (class 2606 OID 25035)
-- Name: act_re_deployment act_re_deployment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_re_deployment
    ADD CONSTRAINT act_re_deployment_pkey PRIMARY KEY (id_);


--
-- TOC entry 2766 (class 2606 OID 25038)
-- Name: act_re_model act_re_model_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_re_model
    ADD CONSTRAINT act_re_model_pkey PRIMARY KEY (id_);


--
-- TOC entry 2768 (class 2606 OID 25041)
-- Name: act_re_procdef act_re_procdef_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_re_procdef
    ADD CONSTRAINT act_re_procdef_pkey PRIMARY KEY (id_);


--
-- TOC entry 2774 (class 2606 OID 25046)
-- Name: act_ru_event_subscr act_ru_event_subscr_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_event_subscr
    ADD CONSTRAINT act_ru_event_subscr_pkey PRIMARY KEY (id_);


--
-- TOC entry 2781 (class 2606 OID 25058)
-- Name: act_ru_execution act_ru_execution_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_execution
    ADD CONSTRAINT act_ru_execution_pkey PRIMARY KEY (id_);


--
-- TOC entry 2788 (class 2606 OID 25050)
-- Name: act_ru_identitylink act_ru_identitylink_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_identitylink
    ADD CONSTRAINT act_ru_identitylink_pkey PRIMARY KEY (id_);


--
-- TOC entry 2791 (class 2606 OID 25060)
-- Name: act_ru_job act_ru_job_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_job
    ADD CONSTRAINT act_ru_job_pkey PRIMARY KEY (id_);


--
-- TOC entry 2797 (class 2606 OID 25068)
-- Name: act_ru_task act_ru_task_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_task
    ADD CONSTRAINT act_ru_task_pkey PRIMARY KEY (id_);


--
-- TOC entry 2803 (class 2606 OID 25075)
-- Name: act_ru_variable act_ru_variable_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_variable
    ADD CONSTRAINT act_ru_variable_pkey PRIMARY KEY (id_);


--
-- TOC entry 2770 (class 2606 OID 25047)
-- Name: act_re_procdef act_uniq_procdef; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_re_procdef
    ADD CONSTRAINT act_uniq_procdef UNIQUE (key_, version_, tenant_id_);


--
-- TOC entry 2805 (class 2606 OID 25076)
-- Name: qrtz_blob_triggers qrtz_blob_triggers_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2807 (class 2606 OID 25079)
-- Name: qrtz_calendars qrtz_calendars_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_calendars
    ADD CONSTRAINT qrtz_calendars_pkey PRIMARY KEY (sched_name, calendar_name);


--
-- TOC entry 2809 (class 2606 OID 25084)
-- Name: qrtz_cron_triggers qrtz_cron_triggers_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2817 (class 2606 OID 25086)
-- Name: qrtz_fired_triggers qrtz_fired_triggers_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_fired_triggers
    ADD CONSTRAINT qrtz_fired_triggers_pkey PRIMARY KEY (sched_name, entry_id);


--
-- TOC entry 2821 (class 2606 OID 25100)
-- Name: qrtz_job_details qrtz_job_details_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_job_details
    ADD CONSTRAINT qrtz_job_details_pkey PRIMARY KEY (sched_name, job_name, job_group);


--
-- TOC entry 2823 (class 2606 OID 25096)
-- Name: qrtz_locks qrtz_locks_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_locks
    ADD CONSTRAINT qrtz_locks_pkey PRIMARY KEY (sched_name, lock_name);


--
-- TOC entry 2825 (class 2606 OID 25089)
-- Name: qrtz_paused_trigger_grps qrtz_paused_trigger_grps_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_paused_trigger_grps
    ADD CONSTRAINT qrtz_paused_trigger_grps_pkey PRIMARY KEY (sched_name, trigger_group);


--
-- TOC entry 2827 (class 2606 OID 25099)
-- Name: qrtz_scheduler_state qrtz_scheduler_state_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_scheduler_state
    ADD CONSTRAINT qrtz_scheduler_state_pkey PRIMARY KEY (sched_name, instance_name);


--
-- TOC entry 2829 (class 2606 OID 25103)
-- Name: qrtz_simple_triggers qrtz_simple_triggers_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2831 (class 2606 OID 25106)
-- Name: qrtz_simprop_triggers qrtz_simprop_triggers_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_simprop_triggers
    ADD CONSTRAINT qrtz_simprop_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2845 (class 2606 OID 25109)
-- Name: qrtz_triggers qrtz_triggers_pkey; Type: CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_triggers
    ADD CONSTRAINT qrtz_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2847 (class 2606 OID 25110)
-- Name: acl_class acl_class_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.acl_class
    ADD CONSTRAINT acl_class_pkey PRIMARY KEY (id);


--
-- TOC entry 2851 (class 2606 OID 25129)
-- Name: acl_entry acl_entry_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.acl_entry
    ADD CONSTRAINT acl_entry_pkey PRIMARY KEY (id);


--
-- TOC entry 2855 (class 2606 OID 25133)
-- Name: acl_object_identity acl_object_identity_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.acl_object_identity
    ADD CONSTRAINT acl_object_identity_pkey PRIMARY KEY (id);


--
-- TOC entry 2859 (class 2606 OID 25137)
-- Name: acl_sid acl_sid_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.acl_sid
    ADD CONSTRAINT acl_sid_pkey PRIMARY KEY (id);


--
-- TOC entry 2868 (class 2606 OID 25141)
-- Name: agent_config agent_config_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.agent_config
    ADD CONSTRAINT agent_config_pkey PRIMARY KEY (id);


--
-- TOC entry 2863 (class 2606 OID 25153)
-- Name: agent agent_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.agent
    ADD CONSTRAINT agent_pkey PRIMARY KEY (id);


--
-- TOC entry 2871 (class 2606 OID 25144)
-- Name: agent_token agent_token_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.agent_token
    ADD CONSTRAINT agent_token_pkey PRIMARY KEY (id);


--
-- TOC entry 2879 (class 2606 OID 25165)
-- Name: chart_device chart_device_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.chart_device
    ADD CONSTRAINT chart_device_pkey PRIMARY KEY (id);


--
-- TOC entry 2876 (class 2606 OID 25148)
-- Name: chart chart_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.chart
    ADD CONSTRAINT chart_pkey PRIMARY KEY (id);


--
-- TOC entry 2882 (class 2606 OID 25164)
-- Name: chauffage chauffage_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.chauffage
    ADD CONSTRAINT chauffage_pkey PRIMARY KEY (id);


--
-- TOC entry 2886 (class 2606 OID 25154)
-- Name: commune commune_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.commune
    ADD CONSTRAINT commune_pkey PRIMARY KEY (id);


--
-- TOC entry 2892 (class 2606 OID 25170)
-- Name: composant_vue composant_vue_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.composant_vue
    ADD CONSTRAINT composant_vue_pkey PRIMARY KEY (id);


--
-- TOC entry 2897 (class 2606 OID 25223)
-- Name: compteur_index compteur_index_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.compteur_index
    ADD CONSTRAINT compteur_index_pkey PRIMARY KEY (id);


--
-- TOC entry 2900 (class 2606 OID 25172)
-- Name: config config_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.config
    ADD CONSTRAINT config_pkey PRIMARY KEY (id);


--
-- TOC entry 2911 (class 2606 OID 25181)
-- Name: device_alert device_alert_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_alert
    ADD CONSTRAINT device_alert_pkey PRIMARY KEY (id);


--
-- TOC entry 2914 (class 2606 OID 25190)
-- Name: device_level_alert device_level_alert_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_level_alert
    ADD CONSTRAINT device_level_alert_pkey PRIMARY KEY (id);


--
-- TOC entry 2917 (class 2606 OID 25205)
-- Name: device_metadata device_metadata_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_metadata
    ADD CONSTRAINT device_metadata_pkey PRIMARY KEY (id);


--
-- TOC entry 2922 (class 2606 OID 25197)
-- Name: device_metavalue device_metavalue_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_metavalue
    ADD CONSTRAINT device_metavalue_pkey PRIMARY KEY (id);


--
-- TOC entry 2908 (class 2606 OID 25183)
-- Name: device device_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device
    ADD CONSTRAINT device_pkey PRIMARY KEY (id);


--
-- TOC entry 2927 (class 2606 OID 25191)
-- Name: device_planning device_planning_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_planning
    ADD CONSTRAINT device_planning_pkey PRIMARY KEY (id);


--
-- TOC entry 2930 (class 2606 OID 25196)
-- Name: device_share device_share_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_share
    ADD CONSTRAINT device_share_pkey PRIMARY KEY (id);


--
-- TOC entry 2939 (class 2606 OID 25214)
-- Name: device_type_config device_type_config_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_type_config
    ADD CONSTRAINT device_type_config_pkey PRIMARY KEY (device_type_id);


--
-- TOC entry 2935 (class 2606 OID 25216)
-- Name: device_type device_type_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_type
    ADD CONSTRAINT device_type_pkey PRIMARY KEY (id);


--
-- TOC entry 2941 (class 2606 OID 25207)
-- Name: device_type_provider device_type_provider_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_type_provider
    ADD CONSTRAINT device_type_provider_pkey PRIMARY KEY (id);


--
-- TOC entry 2947 (class 2606 OID 25219)
-- Name: device_type_provider_prix device_type_provider_prix_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_type_provider_prix
    ADD CONSTRAINT device_type_provider_prix_pkey PRIMARY KEY (id);


--
-- TOC entry 2954 (class 2606 OID 25825)
-- Name: device_value_day device_value_day_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_value_day
    ADD CONSTRAINT device_value_day_pkey PRIMARY KEY (id);


--
-- TOC entry 2957 (class 2606 OID 25242)
-- Name: device_value_month device_value_month_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_value_month
    ADD CONSTRAINT device_value_month_pkey PRIMARY KEY (id);


--
-- TOC entry 2950 (class 2606 OID 25842)
-- Name: device_value device_value_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_value
    ADD CONSTRAINT device_value_pkey PRIMARY KEY (id);


--
-- TOC entry 2920 (class 2606 OID 25209)
-- Name: device_metadata devicemetadata_uniq; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_metadata
    ADD CONSTRAINT devicemetadata_uniq UNIQUE (device_id, name);


--
-- TOC entry 2925 (class 2606 OID 25201)
-- Name: device_metavalue devicemetavalue_uniq; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_metavalue
    ADD CONSTRAINT devicemetavalue_uniq UNIQUE (device_id, name);


--
-- TOC entry 2960 (class 2606 OID 25225)
-- Name: ecs ecs_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.ecs
    ADD CONSTRAINT ecs_pkey PRIMARY KEY (id);


--
-- TOC entry 2967 (class 2606 OID 25232)
-- Name: event_device event_device_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.event_device
    ADD CONSTRAINT event_device_pkey PRIMARY KEY (id);


--
-- TOC entry 2971 (class 2606 OID 25234)
-- Name: event_mode event_mode_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.event_mode
    ADD CONSTRAINT event_mode_pkey PRIMARY KEY (id);


--
-- TOC entry 2964 (class 2606 OID 25227)
-- Name: event event_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.event
    ADD CONSTRAINT event_pkey PRIMARY KEY (id);


--
-- TOC entry 2974 (class 2606 OID 25239)
-- Name: event_trigger event_trigger_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.event_trigger
    ADD CONSTRAINT event_trigger_pkey PRIMARY KEY (id);


--
-- TOC entry 2980 (class 2606 OID 25247)
-- Name: house_conso house_conso_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house_conso
    ADD CONSTRAINT house_conso_pkey PRIMARY KEY (id);


--
-- TOC entry 2985 (class 2606 OID 25252)
-- Name: house_mode house_mode_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house_mode
    ADD CONSTRAINT house_mode_pkey PRIMARY KEY (house_id, mode_id);


--
-- TOC entry 2977 (class 2606 OID 25244)
-- Name: house house_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house
    ADD CONSTRAINT house_pkey PRIMARY KEY (id);


--
-- TOC entry 2987 (class 2606 OID 25274)
-- Name: house_weather house_weather_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house_weather
    ADD CONSTRAINT house_weather_pkey PRIMARY KEY (house_id);


--
-- TOC entry 2989 (class 2606 OID 25254)
-- Name: mode mode_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.mode
    ADD CONSTRAINT mode_pkey PRIMARY KEY (id);


--
-- TOC entry 2995 (class 2606 OID 25260)
-- Name: notification_account notification_account_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.notification_account
    ADD CONSTRAINT notification_account_pkey PRIMARY KEY (id);


--
-- TOC entry 3000 (class 2606 OID 25265)
-- Name: notification_account_sender notification_account_sender_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.notification_account_sender
    ADD CONSTRAINT notification_account_sender_pkey PRIMARY KEY (id);


--
-- TOC entry 2992 (class 2606 OID 25257)
-- Name: notification notification_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- TOC entry 2904 (class 2606 OID 25179)
-- Name: databasechangeloglock pk_databasechangeloglock; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.databasechangeloglock
    ADD CONSTRAINT pk_databasechangeloglock PRIMARY KEY (id);


--
-- TOC entry 3004 (class 2606 OID 25270)
-- Name: planning planning_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.planning
    ADD CONSTRAINT planning_pkey PRIMARY KEY (id);


--
-- TOC entry 3008 (class 2606 OID 25276)
-- Name: producteur_energie_action producteur_energie_action_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.producteur_energie_action
    ADD CONSTRAINT producteur_energie_action_pkey PRIMARY KEY (id);


--
-- TOC entry 3006 (class 2606 OID 25272)
-- Name: producteur_energie producteur_energie_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.producteur_energie
    ADD CONSTRAINT producteur_energie_pkey PRIMARY KEY (id);


--
-- TOC entry 3011 (class 2606 OID 25279)
-- Name: profil profil_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.profil
    ADD CONSTRAINT profil_pkey PRIMARY KEY (id);


--
-- TOC entry 3015 (class 2606 OID 25283)
-- Name: registration_code registration_code_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.registration_code
    ADD CONSTRAINT registration_code_pkey PRIMARY KEY (id);


--
-- TOC entry 3017 (class 2606 OID 25285)
-- Name: role role_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 3021 (class 2606 OID 25289)
-- Name: scenario scenario_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.scenario
    ADD CONSTRAINT scenario_pkey PRIMARY KEY (id);


--
-- TOC entry 3024 (class 2606 OID 25295)
-- Name: script_rule script_rule_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.script_rule
    ADD CONSTRAINT script_rule_pkey PRIMARY KEY (id);


--
-- TOC entry 2943 (class 2606 OID 25211)
-- Name: device_type_provider uk_acelnk72phh048yv9nbtguy7u; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_type_provider
    ADD CONSTRAINT uk_acelnk72phh048yv9nbtguy7u UNIQUE (device_type_id, libelle);


--
-- TOC entry 3059 (class 2606 OID 25330)
-- Name: workflow uk_bakmmy47if4gwgbxfi1rb09v0; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.workflow
    ADD CONSTRAINT uk_bakmmy47if4gwgbxfi1rb09v0 UNIQUE (libelle);


--
-- TOC entry 3002 (class 2606 OID 25267)
-- Name: notification_account_sender uk_cbf80a9p8895tthueljed9trp; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.notification_account_sender
    ADD CONSTRAINT uk_cbf80a9p8895tthueljed9trp UNIQUE (libelle);


--
-- TOC entry 2962 (class 2606 OID 25229)
-- Name: ecs uk_dnstgpijeixvef414f5hy0fbb; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.ecs
    ADD CONSTRAINT uk_dnstgpijeixvef414f5hy0fbb UNIQUE (libelle);


--
-- TOC entry 3013 (class 2606 OID 25281)
-- Name: profil uk_dtxgw1r8lv8fxlofthwiv6am5; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.profil
    ADD CONSTRAINT uk_dtxgw1r8lv8fxlofthwiv6am5 UNIQUE (libelle);


--
-- TOC entry 2874 (class 2606 OID 25147)
-- Name: agent_token uk_e2mt53wf4767ukwej7rrtedxy; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.agent_token
    ADD CONSTRAINT uk_e2mt53wf4767ukwej7rrtedxy UNIQUE (token);


--
-- TOC entry 3031 (class 2606 OID 25302)
-- Name: user_application uk_gqve0ke8hruo4ugv8oc5sw2s2; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_application
    ADD CONSTRAINT uk_gqve0ke8hruo4ugv8oc5sw2s2 UNIQUE (token);


--
-- TOC entry 3019 (class 2606 OID 25287)
-- Name: role uk_irsamgnera6angm0prq1kemt2; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.role
    ADD CONSTRAINT uk_irsamgnera6angm0prq1kemt2 UNIQUE (authority);


--
-- TOC entry 2849 (class 2606 OID 25113)
-- Name: acl_class uk_iy7ua5fso3il3u3ymoc4uf35w; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.acl_class
    ADD CONSTRAINT uk_iy7ua5fso3il3u3ymoc4uf35w UNIQUE (class);


--
-- TOC entry 2884 (class 2606 OID 25168)
-- Name: chauffage uk_jymi1ggsm13fhv0nulfl5gfj5; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.chauffage
    ADD CONSTRAINT uk_jymi1ggsm13fhv0nulfl5gfj5 UNIQUE (libelle);


--
-- TOC entry 2902 (class 2606 OID 25176)
-- Name: config uk_kjjh66cda2b9nc24it8fhbfwx; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.config
    ADD CONSTRAINT uk_kjjh66cda2b9nc24it8fhbfwx UNIQUE (name);


--
-- TOC entry 2937 (class 2606 OID 25220)
-- Name: device_type uk_kpojn1a65dfixkqf4j6t7dk84; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_type
    ADD CONSTRAINT uk_kpojn1a65dfixkqf4j6t7dk84 UNIQUE (libelle);


--
-- TOC entry 3047 (class 2606 OID 25313)
-- Name: utilisateur uk_kq7nt5wyq9v9lpcpgxag2f24a; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.utilisateur
    ADD CONSTRAINT uk_kq7nt5wyq9v9lpcpgxag2f24a UNIQUE (username);


--
-- TOC entry 2888 (class 2606 OID 25156)
-- Name: commune uk_mkujenk3qdcwa887ftd32w94t; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.commune
    ADD CONSTRAINT uk_mkujenk3qdcwa887ftd32w94t UNIQUE (libelle);


--
-- TOC entry 3026 (class 2606 OID 25297)
-- Name: script_rule uk_n6jyf5k7smf9y6h97q38vtbdp; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.script_rule
    ADD CONSTRAINT uk_n6jyf5k7smf9y6h97q38vtbdp UNIQUE (rule_name);


--
-- TOC entry 2890 (class 2606 OID 25161)
-- Name: commune uk_o5uj9yuxtkxtbu6ebv3b2o1x8; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.commune
    ADD CONSTRAINT uk_o5uj9yuxtkxtbu6ebv3b2o1x8 UNIQUE (code_postal);


--
-- TOC entry 2853 (class 2606 OID 25132)
-- Name: acl_entry unique_ace_order; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.acl_entry
    ADD CONSTRAINT unique_ace_order UNIQUE (acl_object_identity, ace_order);


--
-- TOC entry 2933 (class 2606 OID 25199)
-- Name: device_share unique_device_id; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_share
    ADD CONSTRAINT unique_device_id UNIQUE (device_id, shared_user_id);


--
-- TOC entry 3039 (class 2606 OID 25299)
-- Name: user_friend unique_friend_id; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_friend
    ADD CONSTRAINT unique_friend_id UNIQUE (user_id, friend_id);


--
-- TOC entry 2983 (class 2606 OID 25249)
-- Name: house_conso unique_house_id; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house_conso
    ADD CONSTRAINT unique_house_id UNIQUE (date_conso, house_id);


--
-- TOC entry 2945 (class 2606 OID 25868)
-- Name: device_type_provider unique_libelle; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_type_provider
    ADD CONSTRAINT unique_libelle UNIQUE (device_type_id, libelle);


--
-- TOC entry 2866 (class 2606 OID 25158)
-- Name: agent unique_mac; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.agent
    ADD CONSTRAINT unique_mac UNIQUE (user_id, mac);


--
-- TOC entry 2895 (class 2606 OID 25175)
-- Name: composant_vue unique_name; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.composant_vue
    ADD CONSTRAINT unique_name UNIQUE (user_id, page, name);


--
-- TOC entry 2998 (class 2606 OID 25262)
-- Name: notification_account unique_notification_account_sender_id; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.notification_account
    ADD CONSTRAINT unique_notification_account_sender_id UNIQUE (user_id, notification_account_sender_id);


--
-- TOC entry 2857 (class 2606 OID 25136)
-- Name: acl_object_identity unique_object_id_identity; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.acl_object_identity
    ADD CONSTRAINT unique_object_id_identity UNIQUE (object_id_class, object_id_identity);


--
-- TOC entry 2861 (class 2606 OID 25140)
-- Name: acl_sid unique_principal; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.acl_sid
    ADD CONSTRAINT unique_principal UNIQUE (sid, principal);


--
-- TOC entry 3033 (class 2606 OID 25309)
-- Name: user_application unique_user_id; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_application
    ADD CONSTRAINT unique_user_id UNIQUE (application_id, user_id);


--
-- TOC entry 3028 (class 2606 OID 25292)
-- Name: user_admin user_admin_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_admin
    ADD CONSTRAINT user_admin_pkey PRIMARY KEY (user_id, admin_id);


--
-- TOC entry 3035 (class 2606 OID 25312)
-- Name: user_application user_application_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_application
    ADD CONSTRAINT user_application_pkey PRIMARY KEY (id);


--
-- TOC entry 3041 (class 2606 OID 25303)
-- Name: user_friend user_friend_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_friend
    ADD CONSTRAINT user_friend_pkey PRIMARY KEY (id);


--
-- TOC entry 3045 (class 2606 OID 25308)
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (role_id, user_id);


--
-- TOC entry 3050 (class 2606 OID 25316)
-- Name: utilisateur utilisateur_application_key_key; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.utilisateur
    ADD CONSTRAINT utilisateur_application_key_key UNIQUE (application_key);


--
-- TOC entry 3052 (class 2606 OID 25320)
-- Name: utilisateur utilisateur_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.utilisateur
    ADD CONSTRAINT utilisateur_pkey PRIMARY KEY (id);


--
-- TOC entry 3054 (class 2606 OID 25318)
-- Name: widget widget_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.widget
    ADD CONSTRAINT widget_pkey PRIMARY KEY (id);


--
-- TOC entry 3056 (class 2606 OID 25322)
-- Name: widget_user widget_user_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.widget_user
    ADD CONSTRAINT widget_user_pkey PRIMARY KEY (id);


--
-- TOC entry 3061 (class 2606 OID 25333)
-- Name: workflow workflow_pkey; Type: CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.workflow
    ADD CONSTRAINT workflow_pkey PRIMARY KEY (id);


--
-- TOC entry 2717 (class 1259 OID 25000)
-- Name: defi_idx; Type: INDEX; Schema: application; Owner: postgres
--

CREATE INDEX defi_idx ON application.defi USING btree (user_id);


--
-- TOC entry 2724 (class 1259 OID 25006)
-- Name: defiequipe_idx; Type: INDEX; Schema: application; Owner: postgres
--

CREATE INDEX defiequipe_idx ON application.defi_equipe USING btree (defi_id);


--
-- TOC entry 2729 (class 1259 OID 25010)
-- Name: defiequipeparticipant_idx; Type: INDEX; Schema: application; Owner: postgres
--

CREATE INDEX defiequipeparticipant_idx ON application.defi_equipe_participant USING btree (defi_equipe_id);


--
-- TOC entry 2730 (class 1259 OID 25012)
-- Name: defiequipeparticipant_user_idx; Type: INDEX; Schema: application; Owner: postgres
--

CREATE INDEX defiequipeparticipant_user_idx ON application.defi_equipe_participant USING btree (user_id);


--
-- TOC entry 2735 (class 1259 OID 25015)
-- Name: defiequipeprofil_idx; Type: INDEX; Schema: application; Owner: postgres
--

CREATE INDEX defiequipeprofil_idx ON application.defi_equipe_profil USING btree (defi_equipe_id);


--
-- TOC entry 2740 (class 1259 OID 25022)
-- Name: defiprofil_idx; Type: INDEX; Schema: application; Owner: postgres
--

CREATE INDEX defiprofil_idx ON application.defi_profil USING btree (defi_id);


--
-- TOC entry 2782 (class 1259 OID 25051)
-- Name: act_idx_athrz_procedef; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_athrz_procedef ON public.act_ru_identitylink USING btree (proc_def_id_);


--
-- TOC entry 2747 (class 1259 OID 25128)
-- Name: act_idx_bytear_depl; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_bytear_depl ON public.act_ge_bytearray USING btree (deployment_id_);


--
-- TOC entry 2771 (class 1259 OID 25049)
-- Name: act_idx_event_subscr; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_event_subscr ON public.act_ru_event_subscr USING btree (execution_id_);


--
-- TOC entry 2772 (class 1259 OID 25052)
-- Name: act_idx_event_subscr_config_; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_event_subscr_config_ ON public.act_ru_event_subscr USING btree (configuration_);


--
-- TOC entry 2775 (class 1259 OID 25061)
-- Name: act_idx_exe_parent; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_exe_parent ON public.act_ru_execution USING btree (parent_id_);


--
-- TOC entry 2776 (class 1259 OID 25062)
-- Name: act_idx_exe_procdef; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_exe_procdef ON public.act_ru_execution USING btree (proc_def_id_);


--
-- TOC entry 2777 (class 1259 OID 25063)
-- Name: act_idx_exe_procinst; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_exe_procinst ON public.act_ru_execution USING btree (proc_inst_id_);


--
-- TOC entry 2778 (class 1259 OID 25064)
-- Name: act_idx_exe_super; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_exe_super ON public.act_ru_execution USING btree (super_exec_);


--
-- TOC entry 2779 (class 1259 OID 25065)
-- Name: act_idx_exec_buskey; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_exec_buskey ON public.act_ru_execution USING btree (business_key_);


--
-- TOC entry 2783 (class 1259 OID 25053)
-- Name: act_idx_ident_lnk_group; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_ident_lnk_group ON public.act_ru_identitylink USING btree (group_id_);


--
-- TOC entry 2784 (class 1259 OID 25054)
-- Name: act_idx_ident_lnk_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_ident_lnk_user ON public.act_ru_identitylink USING btree (user_id_);


--
-- TOC entry 2785 (class 1259 OID 25055)
-- Name: act_idx_idl_procinst; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_idl_procinst ON public.act_ru_identitylink USING btree (proc_inst_id_);


--
-- TOC entry 2789 (class 1259 OID 25066)
-- Name: act_idx_job_exception; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_job_exception ON public.act_ru_job USING btree (exception_stack_id_);


--
-- TOC entry 2756 (class 1259 OID 25034)
-- Name: act_idx_memb_group; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_memb_group ON public.act_id_membership USING btree (group_id_);


--
-- TOC entry 2757 (class 1259 OID 25036)
-- Name: act_idx_memb_user; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_memb_user ON public.act_id_membership USING btree (user_id_);


--
-- TOC entry 2762 (class 1259 OID 25040)
-- Name: act_idx_model_deployment; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_model_deployment ON public.act_re_model USING btree (deployment_id_);


--
-- TOC entry 2763 (class 1259 OID 25042)
-- Name: act_idx_model_source; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_model_source ON public.act_re_model USING btree (editor_source_value_id_);


--
-- TOC entry 2764 (class 1259 OID 25043)
-- Name: act_idx_model_source_extra; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_model_source_extra ON public.act_re_model USING btree (editor_source_extra_value_id_);


--
-- TOC entry 2792 (class 1259 OID 25070)
-- Name: act_idx_task_create; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_task_create ON public.act_ru_task USING btree (create_time_);


--
-- TOC entry 2793 (class 1259 OID 25069)
-- Name: act_idx_task_exec; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_task_exec ON public.act_ru_task USING btree (execution_id_);


--
-- TOC entry 2794 (class 1259 OID 25071)
-- Name: act_idx_task_procdef; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_task_procdef ON public.act_ru_task USING btree (proc_def_id_);


--
-- TOC entry 2795 (class 1259 OID 25072)
-- Name: act_idx_task_procinst; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_task_procinst ON public.act_ru_task USING btree (proc_inst_id_);


--
-- TOC entry 2786 (class 1259 OID 25056)
-- Name: act_idx_tskass_task; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_tskass_task ON public.act_ru_identitylink USING btree (task_id_);


--
-- TOC entry 2798 (class 1259 OID 25078)
-- Name: act_idx_var_bytearray; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_var_bytearray ON public.act_ru_variable USING btree (bytearray_id_);


--
-- TOC entry 2799 (class 1259 OID 25080)
-- Name: act_idx_var_exe; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_var_exe ON public.act_ru_variable USING btree (execution_id_);


--
-- TOC entry 2800 (class 1259 OID 25081)
-- Name: act_idx_var_procinst; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_var_procinst ON public.act_ru_variable USING btree (proc_inst_id_);


--
-- TOC entry 2801 (class 1259 OID 25082)
-- Name: act_idx_variable_task_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX act_idx_variable_task_id ON public.act_ru_variable USING btree (task_id_);


--
-- TOC entry 2810 (class 1259 OID 25088)
-- Name: idx_qrtz_ft_inst_job_req_rcvry; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_ft_inst_job_req_rcvry ON quartz.qrtz_fired_triggers USING btree (sched_name, instance_name, requests_recovery);


--
-- TOC entry 2811 (class 1259 OID 25090)
-- Name: idx_qrtz_ft_j_g; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_ft_j_g ON quartz.qrtz_fired_triggers USING btree (sched_name, job_name, job_group);


--
-- TOC entry 2812 (class 1259 OID 25091)
-- Name: idx_qrtz_ft_jg; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_ft_jg ON quartz.qrtz_fired_triggers USING btree (sched_name, job_group);


--
-- TOC entry 2813 (class 1259 OID 25092)
-- Name: idx_qrtz_ft_t_g; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_ft_t_g ON quartz.qrtz_fired_triggers USING btree (sched_name, trigger_name, trigger_group);


--
-- TOC entry 2814 (class 1259 OID 25093)
-- Name: idx_qrtz_ft_tg; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_ft_tg ON quartz.qrtz_fired_triggers USING btree (sched_name, trigger_group);


--
-- TOC entry 2815 (class 1259 OID 25094)
-- Name: idx_qrtz_ft_trig_inst_name; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_ft_trig_inst_name ON quartz.qrtz_fired_triggers USING btree (sched_name, instance_name);


--
-- TOC entry 2818 (class 1259 OID 25101)
-- Name: idx_qrtz_j_grp; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_j_grp ON quartz.qrtz_job_details USING btree (sched_name, job_group);


--
-- TOC entry 2819 (class 1259 OID 25104)
-- Name: idx_qrtz_j_req_recovery; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_j_req_recovery ON quartz.qrtz_job_details USING btree (sched_name, requests_recovery);


--
-- TOC entry 2832 (class 1259 OID 25112)
-- Name: idx_qrtz_t_c; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_c ON quartz.qrtz_triggers USING btree (sched_name, calendar_name);


--
-- TOC entry 2833 (class 1259 OID 25114)
-- Name: idx_qrtz_t_g; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_g ON quartz.qrtz_triggers USING btree (sched_name, trigger_group);


--
-- TOC entry 2834 (class 1259 OID 25115)
-- Name: idx_qrtz_t_j; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_j ON quartz.qrtz_triggers USING btree (sched_name, job_name, job_group);


--
-- TOC entry 2835 (class 1259 OID 25116)
-- Name: idx_qrtz_t_jg; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_jg ON quartz.qrtz_triggers USING btree (sched_name, job_group);


--
-- TOC entry 2836 (class 1259 OID 25117)
-- Name: idx_qrtz_t_n_g_state; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_n_g_state ON quartz.qrtz_triggers USING btree (sched_name, trigger_group, trigger_state);


--
-- TOC entry 2837 (class 1259 OID 25118)
-- Name: idx_qrtz_t_n_state; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_n_state ON quartz.qrtz_triggers USING btree (sched_name, trigger_name, trigger_group, trigger_state);


--
-- TOC entry 2838 (class 1259 OID 25119)
-- Name: idx_qrtz_t_next_fire_time; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_next_fire_time ON quartz.qrtz_triggers USING btree (sched_name, next_fire_time);


--
-- TOC entry 2839 (class 1259 OID 25121)
-- Name: idx_qrtz_t_nft_misfire; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_nft_misfire ON quartz.qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time);


--
-- TOC entry 2840 (class 1259 OID 25120)
-- Name: idx_qrtz_t_nft_st; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_nft_st ON quartz.qrtz_triggers USING btree (sched_name, trigger_state, next_fire_time);


--
-- TOC entry 2841 (class 1259 OID 25122)
-- Name: idx_qrtz_t_nft_st_misfire; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_nft_st_misfire ON quartz.qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_state);


--
-- TOC entry 2842 (class 1259 OID 25123)
-- Name: idx_qrtz_t_nft_st_misfire_grp; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_nft_st_misfire_grp ON quartz.qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);


--
-- TOC entry 2843 (class 1259 OID 25124)
-- Name: idx_qrtz_t_state; Type: INDEX; Schema: quartz; Owner: postgres
--

CREATE INDEX idx_qrtz_t_state ON quartz.qrtz_triggers USING btree (sched_name, trigger_state);


--
-- TOC entry 2864 (class 1259 OID 25160)
-- Name: agent_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX agent_user_idx ON smarthome.agent USING btree (user_id);


--
-- TOC entry 2869 (class 1259 OID 25143)
-- Name: agentconfig_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX agentconfig_idx ON smarthome.agent_config USING btree (agent_id);


--
-- TOC entry 2872 (class 1259 OID 25149)
-- Name: agenttoken_token_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX agenttoken_token_idx ON smarthome.agent_token USING btree (token);


--
-- TOC entry 2877 (class 1259 OID 25150)
-- Name: chart_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX chart_user_idx ON smarthome.chart USING btree (user_id);


--
-- TOC entry 2880 (class 1259 OID 25167)
-- Name: chartdevice_chart_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX chartdevice_chart_idx ON smarthome.chart_device USING btree (chart_id);


--
-- TOC entry 2893 (class 1259 OID 25177)
-- Name: composantvue_namepageuser_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX composantvue_namepageuser_idx ON smarthome.composant_vue USING btree (name, page, user_id);


--
-- TOC entry 2898 (class 1259 OID 25866)
-- Name: config_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX config_idx ON smarthome.config USING btree (name);


--
-- TOC entry 2905 (class 1259 OID 25185)
-- Name: device_agent_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX device_agent_idx ON smarthome.device USING btree (agent_id);


--
-- TOC entry 2906 (class 1259 OID 25186)
-- Name: device_macagent_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX device_macagent_idx ON smarthome.device USING btree (agent_id, mac);


--
-- TOC entry 2909 (class 1259 OID 25187)
-- Name: device_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX device_user_idx ON smarthome.device USING btree (user_id);


--
-- TOC entry 2912 (class 1259 OID 25184)
-- Name: devicealert_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicealert_idx ON smarthome.device_alert USING btree (date_debut, device_id);


--
-- TOC entry 2915 (class 1259 OID 25192)
-- Name: devicelevelalert_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicelevelalert_idx ON smarthome.device_level_alert USING btree (device_id);


--
-- TOC entry 2918 (class 1259 OID 25213)
-- Name: devicemetadata_device_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicemetadata_device_idx ON smarthome.device_metadata USING btree (device_id);


--
-- TOC entry 2923 (class 1259 OID 25203)
-- Name: devicemetavalue_device_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicemetavalue_device_idx ON smarthome.device_metavalue USING btree (device_id);


--
-- TOC entry 2928 (class 1259 OID 25193)
-- Name: deviceplanning_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX deviceplanning_idx ON smarthome.device_planning USING btree (device_id);


--
-- TOC entry 2931 (class 1259 OID 25202)
-- Name: deviceshare_device_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX deviceshare_device_idx ON smarthome.device_share USING btree (device_id);


--
-- TOC entry 2948 (class 1259 OID 25221)
-- Name: devicetypeproviderprix_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicetypeproviderprix_idx ON smarthome.device_type_provider_prix USING btree (annee, device_type_provider_id);


--
-- TOC entry 2951 (class 1259 OID 25845)
-- Name: devicevalue_device_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicevalue_device_idx ON smarthome.device_value USING btree (device_id);


--
-- TOC entry 2952 (class 1259 OID 25846)
-- Name: devicevalue_devicename_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicevalue_devicename_idx ON smarthome.device_value USING btree (date_value, device_id, name);


--
-- TOC entry 2955 (class 1259 OID 25826)
-- Name: devicevalueday_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicevalueday_idx ON smarthome.device_value_day USING btree (date_value, device_id, name);


--
-- TOC entry 2958 (class 1259 OID 25268)
-- Name: devicevaluemonth_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX devicevaluemonth_idx ON smarthome.device_value_month USING btree (date_value, device_id, name);


--
-- TOC entry 2965 (class 1259 OID 25230)
-- Name: event_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX event_user_idx ON smarthome.event USING btree (user_id);


--
-- TOC entry 2968 (class 1259 OID 25235)
-- Name: eventdevice_device_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX eventdevice_device_idx ON smarthome.event_device USING btree (device_id);


--
-- TOC entry 2969 (class 1259 OID 25236)
-- Name: eventdevice_event_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX eventdevice_event_idx ON smarthome.event_device USING btree (event_id);


--
-- TOC entry 2972 (class 1259 OID 25237)
-- Name: eventmode_event_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX eventmode_event_idx ON smarthome.event_mode USING btree (event_id);


--
-- TOC entry 2975 (class 1259 OID 25240)
-- Name: eventtrigger_event_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX eventtrigger_event_idx ON smarthome.event_trigger USING btree (event_id);


--
-- TOC entry 2978 (class 1259 OID 25245)
-- Name: house_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX house_user_idx ON smarthome.house USING btree (user_id);


--
-- TOC entry 2981 (class 1259 OID 25250)
-- Name: houseconso_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX houseconso_idx ON smarthome.house_conso USING btree (house_id);


--
-- TOC entry 2990 (class 1259 OID 25255)
-- Name: mode_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX mode_user_idx ON smarthome.mode USING btree (user_id);


--
-- TOC entry 2993 (class 1259 OID 25258)
-- Name: notification_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX notification_user_idx ON smarthome.notification USING btree (user_id);


--
-- TOC entry 2996 (class 1259 OID 25263)
-- Name: notificationaccount_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX notificationaccount_user_idx ON smarthome.notification_account USING btree (user_id);


--
-- TOC entry 3009 (class 1259 OID 25277)
-- Name: producteurenergieaction_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX producteurenergieaction_idx ON smarthome.producteur_energie_action USING btree (user_id);


--
-- TOC entry 3022 (class 1259 OID 25290)
-- Name: scenario_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX scenario_user_idx ON smarthome.scenario USING btree (user_id);


--
-- TOC entry 3029 (class 1259 OID 25293)
-- Name: useradmin_admin_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX useradmin_admin_idx ON smarthome.user_admin USING btree (admin_id);


--
-- TOC entry 3036 (class 1259 OID 25869)
-- Name: userapplication_token_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX userapplication_token_idx ON smarthome.user_application USING btree (token);


--
-- TOC entry 3037 (class 1259 OID 25314)
-- Name: userapplication_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX userapplication_user_idx ON smarthome.user_application USING btree (user_id);


--
-- TOC entry 3048 (class 1259 OID 25870)
-- Name: userapplication_username_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX userapplication_username_idx ON smarthome.utilisateur USING btree (username);


--
-- TOC entry 3042 (class 1259 OID 25305)
-- Name: userfriend_friend_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX userfriend_friend_idx ON smarthome.user_friend USING btree (friend_id);


--
-- TOC entry 3043 (class 1259 OID 25304)
-- Name: userfriend_user_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX userfriend_user_idx ON smarthome.user_friend USING btree (user_id);


--
-- TOC entry 3057 (class 1259 OID 25324)
-- Name: widgetuser_idx; Type: INDEX; Schema: smarthome; Owner: postgres
--

CREATE INDEX widgetuser_idx ON smarthome.widget_user USING btree (user_id);


--
-- TOC entry 3065 (class 2606 OID 25340)
-- Name: defi_equipe_participant fk_4xhl6sbuct64v8k7op2byhu7g; Type: FK CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_equipe_participant
    ADD CONSTRAINT fk_4xhl6sbuct64v8k7op2byhu7g FOREIGN KEY (defi_equipe_id) REFERENCES application.defi_equipe(id);


--
-- TOC entry 3067 (class 2606 OID 25348)
-- Name: defi_equipe_profil fk_54g6w5av2b6obj12srcwn2238; Type: FK CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_equipe_profil
    ADD CONSTRAINT fk_54g6w5av2b6obj12srcwn2238 FOREIGN KEY (defi_equipe_id) REFERENCES application.defi_equipe(id);


--
-- TOC entry 3068 (class 2606 OID 25688)
-- Name: defi_profil fk_g9112pvhja8kf9faacx9q3kev; Type: FK CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_profil
    ADD CONSTRAINT fk_g9112pvhja8kf9faacx9q3kev FOREIGN KEY (profil_id) REFERENCES smarthome.profil(id);


--
-- TOC entry 3064 (class 2606 OID 25718)
-- Name: defi_equipe_participant fk_ihk4fajkm4iytng1etrhd9d5d; Type: FK CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_equipe_participant
    ADD CONSTRAINT fk_ihk4fajkm4iytng1etrhd9d5d FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3069 (class 2606 OID 25323)
-- Name: defi_profil fk_l80drwneeec5601o7mmyq7oa7; Type: FK CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_profil
    ADD CONSTRAINT fk_l80drwneeec5601o7mmyq7oa7 FOREIGN KEY (defi_id) REFERENCES application.defi(id);


--
-- TOC entry 3066 (class 2606 OID 25693)
-- Name: defi_equipe_profil fk_m4xmoshcibheec5vcpv7mmquf; Type: FK CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_equipe_profil
    ADD CONSTRAINT fk_m4xmoshcibheec5vcpv7mmquf FOREIGN KEY (profil_id) REFERENCES smarthome.profil(id);


--
-- TOC entry 3062 (class 2606 OID 25723)
-- Name: defi fk_mp4r3wtkt2apjrtavo615uvfk; Type: FK CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi
    ADD CONSTRAINT fk_mp4r3wtkt2apjrtavo615uvfk FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3063 (class 2606 OID 25332)
-- Name: defi_equipe fk_srs7shurfr54rdcfsdj3g3mbb; Type: FK CONSTRAINT; Schema: application; Owner: postgres
--

ALTER TABLE ONLY application.defi_equipe
    ADD CONSTRAINT fk_srs7shurfr54rdcfsdj3g3mbb FOREIGN KEY (defi_id) REFERENCES application.defi(id);


--
-- TOC entry 3082 (class 2606 OID 25358)
-- Name: act_ru_identitylink act_fk_athrz_procedef; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_identitylink
    ADD CONSTRAINT act_fk_athrz_procedef FOREIGN KEY (proc_def_id_) REFERENCES public.act_re_procdef(id_);


--
-- TOC entry 3070 (class 2606 OID 25359)
-- Name: act_ge_bytearray act_fk_bytearr_depl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ge_bytearray
    ADD CONSTRAINT act_fk_bytearr_depl FOREIGN KEY (deployment_id_) REFERENCES public.act_re_deployment(id_);


--
-- TOC entry 3076 (class 2606 OID 25378)
-- Name: act_ru_event_subscr act_fk_event_exec; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_event_subscr
    ADD CONSTRAINT act_fk_event_exec FOREIGN KEY (execution_id_) REFERENCES public.act_ru_execution(id_);


--
-- TOC entry 3078 (class 2606 OID 25388)
-- Name: act_ru_execution act_fk_exe_parent; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_execution
    ADD CONSTRAINT act_fk_exe_parent FOREIGN KEY (parent_id_) REFERENCES public.act_ru_execution(id_);


--
-- TOC entry 3077 (class 2606 OID 25371)
-- Name: act_ru_execution act_fk_exe_procdef; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_execution
    ADD CONSTRAINT act_fk_exe_procdef FOREIGN KEY (proc_def_id_) REFERENCES public.act_re_procdef(id_);


--
-- TOC entry 3079 (class 2606 OID 25398)
-- Name: act_ru_execution act_fk_exe_procinst; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_execution
    ADD CONSTRAINT act_fk_exe_procinst FOREIGN KEY (proc_inst_id_) REFERENCES public.act_ru_execution(id_);


--
-- TOC entry 3080 (class 2606 OID 25408)
-- Name: act_ru_execution act_fk_exe_super; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_execution
    ADD CONSTRAINT act_fk_exe_super FOREIGN KEY (super_exec_) REFERENCES public.act_ru_execution(id_);


--
-- TOC entry 3083 (class 2606 OID 25418)
-- Name: act_ru_identitylink act_fk_idl_procinst; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_identitylink
    ADD CONSTRAINT act_fk_idl_procinst FOREIGN KEY (proc_inst_id_) REFERENCES public.act_ru_execution(id_);


--
-- TOC entry 3084 (class 2606 OID 25464)
-- Name: act_ru_job act_fk_job_exception; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_job
    ADD CONSTRAINT act_fk_job_exception FOREIGN KEY (exception_stack_id_) REFERENCES public.act_ge_bytearray(id_);


--
-- TOC entry 3071 (class 2606 OID 25338)
-- Name: act_id_membership act_fk_memb_group; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_id_membership
    ADD CONSTRAINT act_fk_memb_group FOREIGN KEY (group_id_) REFERENCES public.act_id_group(id_);


--
-- TOC entry 3072 (class 2606 OID 25350)
-- Name: act_id_membership act_fk_memb_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_id_membership
    ADD CONSTRAINT act_fk_memb_user FOREIGN KEY (user_id_) REFERENCES public.act_id_user(id_);


--
-- TOC entry 3073 (class 2606 OID 25368)
-- Name: act_re_model act_fk_model_deployment; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_re_model
    ADD CONSTRAINT act_fk_model_deployment FOREIGN KEY (deployment_id_) REFERENCES public.act_re_deployment(id_);


--
-- TOC entry 3074 (class 2606 OID 25474)
-- Name: act_re_model act_fk_model_source; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_re_model
    ADD CONSTRAINT act_fk_model_source FOREIGN KEY (editor_source_value_id_) REFERENCES public.act_ge_bytearray(id_);


--
-- TOC entry 3075 (class 2606 OID 25485)
-- Name: act_re_model act_fk_model_source_extra; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_re_model
    ADD CONSTRAINT act_fk_model_source_extra FOREIGN KEY (editor_source_extra_value_id_) REFERENCES public.act_ge_bytearray(id_);


--
-- TOC entry 3086 (class 2606 OID 25428)
-- Name: act_ru_task act_fk_task_exe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_task
    ADD CONSTRAINT act_fk_task_exe FOREIGN KEY (execution_id_) REFERENCES public.act_ru_execution(id_);


--
-- TOC entry 3085 (class 2606 OID 25389)
-- Name: act_ru_task act_fk_task_procdef; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_task
    ADD CONSTRAINT act_fk_task_procdef FOREIGN KEY (proc_def_id_) REFERENCES public.act_re_procdef(id_);


--
-- TOC entry 3087 (class 2606 OID 25438)
-- Name: act_ru_task act_fk_task_procinst; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_task
    ADD CONSTRAINT act_fk_task_procinst FOREIGN KEY (proc_inst_id_) REFERENCES public.act_ru_execution(id_);


--
-- TOC entry 3081 (class 2606 OID 25379)
-- Name: act_ru_identitylink act_fk_tskass_task; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_identitylink
    ADD CONSTRAINT act_fk_tskass_task FOREIGN KEY (task_id_) REFERENCES public.act_ru_task(id_);


--
-- TOC entry 3089 (class 2606 OID 25493)
-- Name: act_ru_variable act_fk_var_bytearray; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_variable
    ADD CONSTRAINT act_fk_var_bytearray FOREIGN KEY (bytearray_id_) REFERENCES public.act_ge_bytearray(id_);


--
-- TOC entry 3090 (class 2606 OID 25447)
-- Name: act_ru_variable act_fk_var_exe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_variable
    ADD CONSTRAINT act_fk_var_exe FOREIGN KEY (execution_id_) REFERENCES public.act_ru_execution(id_);


--
-- TOC entry 3088 (class 2606 OID 25454)
-- Name: act_ru_variable act_fk_var_procinst; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.act_ru_variable
    ADD CONSTRAINT act_fk_var_procinst FOREIGN KEY (proc_inst_id_) REFERENCES public.act_ru_execution(id_);


--
-- TOC entry 3091 (class 2606 OID 25420)
-- Name: qrtz_blob_triggers qrtz_blob_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES quartz.qrtz_triggers(sched_name, trigger_name, trigger_group);


--
-- TOC entry 3092 (class 2606 OID 25433)
-- Name: qrtz_cron_triggers qrtz_cron_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES quartz.qrtz_triggers(sched_name, trigger_name, trigger_group);


--
-- TOC entry 3093 (class 2606 OID 25443)
-- Name: qrtz_simple_triggers qrtz_simple_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES quartz.qrtz_triggers(sched_name, trigger_name, trigger_group);


--
-- TOC entry 3094 (class 2606 OID 25453)
-- Name: qrtz_simprop_triggers qrtz_simprop_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_simprop_triggers
    ADD CONSTRAINT qrtz_simprop_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES quartz.qrtz_triggers(sched_name, trigger_name, trigger_group);


--
-- TOC entry 3095 (class 2606 OID 25400)
-- Name: qrtz_triggers qrtz_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: quartz; Owner: postgres
--

ALTER TABLE ONLY quartz.qrtz_triggers
    ADD CONSTRAINT qrtz_triggers_sched_name_fkey FOREIGN KEY (sched_name, job_name, job_group) REFERENCES quartz.qrtz_job_details(sched_name, job_name, job_group);


--
-- TOC entry 3101 (class 2606 OID 25728)
-- Name: agent fk_1cnslwhtfglbvv3mxvlakf258; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.agent
    ADD CONSTRAINT fk_1cnslwhtfglbvv3mxvlakf258 FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3161 (class 2606 OID 25698)
-- Name: utilisateur fk_1evy87s3npvykd184dfbchn6y; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.utilisateur
    ADD CONSTRAINT fk_1evy87s3npvykd184dfbchn6y FOREIGN KEY (profil_id) REFERENCES smarthome.profil(id);


--
-- TOC entry 3146 (class 2606 OID 25733)
-- Name: notification fk_1urdwwsh2ti15ta6f6p5dbdcp; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.notification
    ADD CONSTRAINT fk_1urdwwsh2ti15ta6f6p5dbdcp FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3134 (class 2606 OID 25534)
-- Name: house fk_1yr7je7pr40foh67txdtlvim9; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house
    ADD CONSTRAINT fk_1yr7je7pr40foh67txdtlvim9 FOREIGN KEY (compteur_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3106 (class 2606 OID 25545)
-- Name: chart_device fk_35pcbj8yre7q5kqyn42sj0gbw; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.chart_device
    ADD CONSTRAINT fk_35pcbj8yre7q5kqyn42sj0gbw FOREIGN KEY (device_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3138 (class 2606 OID 25738)
-- Name: house fk_3cuicb608pp7ye1uwdase8kdc; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house
    ADD CONSTRAINT fk_3cuicb608pp7ye1uwdase8kdc FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3141 (class 2606 OID 25603)
-- Name: house_mode fk_4xfw1euybn4tedme3tec8cnuq; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house_mode
    ADD CONSTRAINT fk_4xfw1euybn4tedme3tec8cnuq FOREIGN KEY (mode_id) REFERENCES smarthome.mode(id);


--
-- TOC entry 3116 (class 2606 OID 25554)
-- Name: device_planning fk_5s0l070qf5n1jfum5014l25pe; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_planning
    ADD CONSTRAINT fk_5s0l070qf5n1jfum5014l25pe FOREIGN KEY (device_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3098 (class 2606 OID 25411)
-- Name: acl_object_identity fk_6c3ugmk053uy27bk2sred31lf; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.acl_object_identity
    ADD CONSTRAINT fk_6c3ugmk053uy27bk2sred31lf FOREIGN KEY (object_id_class) REFERENCES smarthome.acl_class(id);


--
-- TOC entry 3114 (class 2606 OID 25563)
-- Name: device_metadata fk_6cdhvsbk2s5gtmmg9iq42gy4u; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_metadata
    ADD CONSTRAINT fk_6cdhvsbk2s5gtmmg9iq42gy4u FOREIGN KEY (device_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3131 (class 2606 OID 25564)
-- Name: event_trigger fk_6fnqfvsdd6lts8yxy823dge5h; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.event_trigger
    ADD CONSTRAINT fk_6fnqfvsdd6lts8yxy823dge5h FOREIGN KEY (event_id) REFERENCES smarthome.event(id);


--
-- TOC entry 3128 (class 2606 OID 25588)
-- Name: event_device fk_6m5b3x44x7mnjpe08eci17cx3; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.event_device
    ADD CONSTRAINT fk_6m5b3x44x7mnjpe08eci17cx3 FOREIGN KEY (device_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3099 (class 2606 OID 25463)
-- Name: acl_object_identity fk_6oap2k8q5bl33yq3yffrwedhf; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.acl_object_identity
    ADD CONSTRAINT fk_6oap2k8q5bl33yq3yffrwedhf FOREIGN KEY (parent_object) REFERENCES smarthome.acl_object_identity(id);


--
-- TOC entry 3133 (class 2606 OID 25553)
-- Name: house fk_6t1997kwlsxepecqk7hn0xvy1; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house
    ADD CONSTRAINT fk_6t1997kwlsxepecqk7hn0xvy1 FOREIGN KEY (ecs_id) REFERENCES smarthome.ecs(id);


--
-- TOC entry 3109 (class 2606 OID 25712)
-- Name: device fk_6xl4jvqdalelcfj2w297pvbe4; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device
    ADD CONSTRAINT fk_6xl4jvqdalelcfj2w297pvbe4 FOREIGN KEY (device_type_id) REFERENCES smarthome.device_type(id);


--
-- TOC entry 3132 (class 2606 OID 25513)
-- Name: house fk_7ebfumnwc8rg39c0w01419gmq; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house
    ADD CONSTRAINT fk_7ebfumnwc8rg39c0w01419gmq FOREIGN KEY (chauffage_id) REFERENCES smarthome.chauffage(id);


--
-- TOC entry 3157 (class 2606 OID 25743)
-- Name: user_friend fk_7y4q4hntapssdn8ea963uko0g; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_friend
    ADD CONSTRAINT fk_7y4q4hntapssdn8ea963uko0g FOREIGN KEY (friend_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3140 (class 2606 OID 25609)
-- Name: house_conso fk_8an7omec7wwt4tjjx0ot1o6vh; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house_conso
    ADD CONSTRAINT fk_8an7omec7wwt4tjjx0ot1o6vh FOREIGN KEY (house_id) REFERENCES smarthome.house(id);


--
-- TOC entry 3154 (class 2606 OID 25748)
-- Name: user_admin fk_8qc7mfu2ibt6k1lfs57m7yv5u; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_admin
    ADD CONSTRAINT fk_8qc7mfu2ibt6k1lfs57m7yv5u FOREIGN KEY (admin_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3118 (class 2606 OID 25574)
-- Name: device_share fk_a003di1utpawx4g1sm2cxclsh; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_share
    ADD CONSTRAINT fk_a003di1utpawx4g1sm2cxclsh FOREIGN KEY (device_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3122 (class 2606 OID 25524)
-- Name: device_type_provider_prix fk_afmgbcw5x4o6alxb03v637h6r; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_type_provider_prix
    ADD CONSTRAINT fk_afmgbcw5x4o6alxb03v637h6r FOREIGN KEY (device_type_provider_id) REFERENCES smarthome.device_type_provider(id);


--
-- TOC entry 3160 (class 2606 OID 25753)
-- Name: user_role fk_apcc8lxk2xnug8377fatvbn04; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_role
    ADD CONSTRAINT fk_apcc8lxk2xnug8377fatvbn04 FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3139 (class 2606 OID 25871)
-- Name: house fk_aqf7n6578smgqunc3fo7rvvb9; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house
    ADD CONSTRAINT fk_aqf7n6578smgqunc3fo7rvvb9 FOREIGN KEY (compteur_eau_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3135 (class 2606 OID 25598)
-- Name: house fk_bc6fovewk21kac6c0c7avsedg; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house
    ADD CONSTRAINT fk_bc6fovewk21kac6c0c7avsedg FOREIGN KEY (humidite_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3103 (class 2606 OID 25504)
-- Name: agent_token fk_br886ci31eerjaq0o8eea1wax; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.agent_token
    ADD CONSTRAINT fk_br886ci31eerjaq0o8eea1wax FOREIGN KEY (agent_id) REFERENCES smarthome.agent(id);


--
-- TOC entry 3107 (class 2606 OID 25608)
-- Name: compteur_index fk_bwp6gdc66vqrpxtbg66vkk4je; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.compteur_index
    ADD CONSTRAINT fk_bwp6gdc66vqrpxtbg66vkk4je FOREIGN KEY (device_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3149 (class 2606 OID 25758)
-- Name: planning fk_cope76rahuwfefx4sbqsd9cfa; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.planning
    ADD CONSTRAINT fk_cope76rahuwfefx4sbqsd9cfa FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3127 (class 2606 OID 25573)
-- Name: event_device fk_eu5hqnh7o991m7yvhonkt1lye; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.event_device
    ADD CONSTRAINT fk_eu5hqnh7o991m7yvhonkt1lye FOREIGN KEY (event_id) REFERENCES smarthome.event(id);


--
-- TOC entry 3156 (class 2606 OID 25763)
-- Name: user_application fk_f3hm86vrk5vvdee5l082u5s0n; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_application
    ADD CONSTRAINT fk_f3hm86vrk5vvdee5l082u5s0n FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3150 (class 2606 OID 25619)
-- Name: producteur_energie_action fk_femjim9f52aklresonjni5imw; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.producteur_energie_action
    ADD CONSTRAINT fk_femjim9f52aklresonjni5imw FOREIGN KEY (device_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3096 (class 2606 OID 25473)
-- Name: acl_entry fk_fhuoesmjef3mrv0gpja4shvcr; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.acl_entry
    ADD CONSTRAINT fk_fhuoesmjef3mrv0gpja4shvcr FOREIGN KEY (acl_object_identity) REFERENCES smarthome.acl_object_identity(id);


--
-- TOC entry 3112 (class 2606 OID 25583)
-- Name: device_level_alert fk_gqwmu9wwpqfl50ol3e3uou3cn; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_level_alert
    ADD CONSTRAINT fk_gqwmu9wwpqfl50ol3e3uou3cn FOREIGN KEY (event_id) REFERENCES smarthome.event(id);


--
-- TOC entry 3124 (class 2606 OID 25829)
-- Name: device_value_day fk_hp3719536wl5ut8vpb0oeq2fh; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_value_day
    ADD CONSTRAINT fk_hp3719536wl5ut8vpb0oeq2fh FOREIGN KEY (device_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3097 (class 2606 OID 25483)
-- Name: acl_entry fk_i6xyfccd4y3wlwhgwpo4a9rm1; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.acl_entry
    ADD CONSTRAINT fk_i6xyfccd4y3wlwhgwpo4a9rm1 FOREIGN KEY (sid) REFERENCES smarthome.acl_sid(id);


--
-- TOC entry 3129 (class 2606 OID 25593)
-- Name: event_mode fk_iagpck079c8f9qaeedorn55o4; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.event_mode
    ADD CONSTRAINT fk_iagpck079c8f9qaeedorn55o4 FOREIGN KEY (event_id) REFERENCES smarthome.event(id);


--
-- TOC entry 3159 (class 2606 OID 25703)
-- Name: user_role fk_it77eq964jhfqtu54081ebtio; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_role
    ADD CONSTRAINT fk_it77eq964jhfqtu54081ebtio FOREIGN KEY (role_id) REFERENCES smarthome.role(id);


--
-- TOC entry 3102 (class 2606 OID 25514)
-- Name: agent_config fk_jk3nr1605a9s14dhh42dwfc1i; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.agent_config
    ADD CONSTRAINT fk_jk3nr1605a9s14dhh42dwfc1i FOREIGN KEY (agent_id) REFERENCES smarthome.agent(id);


--
-- TOC entry 3108 (class 2606 OID 25523)
-- Name: device fk_jmdshic0js5i3i86rklyyt3k2; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device
    ADD CONSTRAINT fk_jmdshic0js5i3i86rklyyt3k2 FOREIGN KEY (agent_id) REFERENCES smarthome.agent(id);


--
-- TOC entry 3136 (class 2606 OID 25640)
-- Name: house fk_kfqav7dhvl2e9f3ya7p2cp555; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house
    ADD CONSTRAINT fk_kfqav7dhvl2e9f3ya7p2cp555 FOREIGN KEY (temperature_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3104 (class 2606 OID 25768)
-- Name: chart fk_kqvlhio8hb7653mcwirs2cu9; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.chart
    ADD CONSTRAINT fk_kqvlhio8hb7653mcwirs2cu9 FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3120 (class 2606 OID 25533)
-- Name: device_type_config fk_lcyq6drc08gyf6r47h48osj6d; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_type_config
    ADD CONSTRAINT fk_lcyq6drc08gyf6r47h48osj6d FOREIGN KEY (device_type_id) REFERENCES smarthome.device_type(id);


--
-- TOC entry 3163 (class 2606 OID 25773)
-- Name: widget_user fk_lke750rg7c1tsh0bc6oef8tcp; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.widget_user
    ADD CONSTRAINT fk_lke750rg7c1tsh0bc6oef8tcp FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3123 (class 2606 OID 25847)
-- Name: device_value fk_lr45joo6jlxnvimrwjadf1njy; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_value
    ADD CONSTRAINT fk_lr45joo6jlxnvimrwjadf1njy FOREIGN KEY (device_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3115 (class 2606 OID 25629)
-- Name: device_metavalue fk_lukbb9rmvsvnakj8vq6o76jvk; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_metavalue
    ADD CONSTRAINT fk_lukbb9rmvsvnakj8vq6o76jvk FOREIGN KEY (device_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3162 (class 2606 OID 25708)
-- Name: widget_user fk_m5scaiudukbhi9n8pebd09jar; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.widget_user
    ADD CONSTRAINT fk_m5scaiudukbhi9n8pebd09jar FOREIGN KEY (widget_id) REFERENCES smarthome.widget(id);


--
-- TOC entry 3143 (class 2606 OID 25618)
-- Name: house_weather fk_mbiswed9yydiegpk3wpuvsa1o; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house_weather
    ADD CONSTRAINT fk_mbiswed9yydiegpk3wpuvsa1o FOREIGN KEY (house_id) REFERENCES smarthome.house(id);


--
-- TOC entry 3145 (class 2606 OID 25648)
-- Name: notification fk_ml6ayph0ambkcxbh0o3ewoj3t; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.notification
    ADD CONSTRAINT fk_ml6ayph0ambkcxbh0o3ewoj3t FOREIGN KEY (notification_account_id) REFERENCES smarthome.notification_account(id);


--
-- TOC entry 3113 (class 2606 OID 25650)
-- Name: device_level_alert fk_mpkgxbnj07tdcqj8uj26lbaxc; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_level_alert
    ADD CONSTRAINT fk_mpkgxbnj07tdcqj8uj26lbaxc FOREIGN KEY (device_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3142 (class 2606 OID 25628)
-- Name: house_mode fk_n6t5i62plvx3hugwa2mkg4vn9; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house_mode
    ADD CONSTRAINT fk_n6t5i62plvx3hugwa2mkg4vn9 FOREIGN KEY (house_id) REFERENCES smarthome.house(id);


--
-- TOC entry 3117 (class 2606 OID 25669)
-- Name: device_planning fk_niiqkydjhfshk1hfdbrhvbp6b; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_planning
    ADD CONSTRAINT fk_niiqkydjhfshk1hfdbrhvbp6b FOREIGN KEY (planning_id) REFERENCES smarthome.planning(id);


--
-- TOC entry 3111 (class 2606 OID 25659)
-- Name: device_alert fk_nqldpme8ymhomv4lijf1mfy6s; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_alert
    ADD CONSTRAINT fk_nqldpme8ymhomv4lijf1mfy6s FOREIGN KEY (device_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3100 (class 2606 OID 25494)
-- Name: acl_object_identity fk_nxv5we2ion9fwedbkge7syoc3; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.acl_object_identity
    ADD CONSTRAINT fk_nxv5we2ion9fwedbkge7syoc3 FOREIGN KEY (owner_sid) REFERENCES smarthome.acl_sid(id);


--
-- TOC entry 3126 (class 2606 OID 25778)
-- Name: event fk_p84ruvsg7mfwb2x5p7iq3q103; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.event
    ADD CONSTRAINT fk_p84ruvsg7mfwb2x5p7iq3q103 FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3130 (class 2606 OID 25638)
-- Name: event_mode fk_pdvj3h7bafhx1chvb02yr8rql; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.event_mode
    ADD CONSTRAINT fk_pdvj3h7bafhx1chvb02yr8rql FOREIGN KEY (mode_id) REFERENCES smarthome.mode(id);


--
-- TOC entry 3137 (class 2606 OID 25668)
-- Name: house fk_pjh6ue6njvdkm0wpgsfpp5b06; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.house
    ADD CONSTRAINT fk_pjh6ue6njvdkm0wpgsfpp5b06 FOREIGN KEY (compteur_gaz_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3147 (class 2606 OID 25658)
-- Name: notification_account fk_pkpj8vscd192vwytdy0muk07w; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.notification_account
    ADD CONSTRAINT fk_pkpj8vscd192vwytdy0muk07w FOREIGN KEY (notification_account_sender_id) REFERENCES smarthome.notification_account_sender(id);


--
-- TOC entry 3151 (class 2606 OID 25678)
-- Name: producteur_energie_action fk_plyj6fs6dd3onvx6kp7ww1ma1; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.producteur_energie_action
    ADD CONSTRAINT fk_plyj6fs6dd3onvx6kp7ww1ma1 FOREIGN KEY (producteur_id) REFERENCES smarthome.producteur_energie(id);


--
-- TOC entry 3153 (class 2606 OID 25783)
-- Name: scenario fk_qhkmwhsklsps2wwxr6j1vbwfm; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.scenario
    ADD CONSTRAINT fk_qhkmwhsklsps2wwxr6j1vbwfm FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3155 (class 2606 OID 25788)
-- Name: user_admin fk_qsbew1kq0sx0ixgrmyr61h765; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_admin
    ADD CONSTRAINT fk_qsbew1kq0sx0ixgrmyr61h765 FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3148 (class 2606 OID 25793)
-- Name: notification_account fk_r1adfviyy0kbx8c3j3ymq57gc; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.notification_account
    ADD CONSTRAINT fk_r1adfviyy0kbx8c3j3ymq57gc FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3105 (class 2606 OID 25503)
-- Name: chart_device fk_r3xmuaiat9nrpt0o2w11ct560; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.chart_device
    ADD CONSTRAINT fk_r3xmuaiat9nrpt0o2w11ct560 FOREIGN KEY (chart_id) REFERENCES smarthome.chart(id);


--
-- TOC entry 3119 (class 2606 OID 25798)
-- Name: device_share fk_rng1qa0gnt7j28twkjv4bfcii; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_share
    ADD CONSTRAINT fk_rng1qa0gnt7j28twkjv4bfcii FOREIGN KEY (shared_user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3125 (class 2606 OID 25679)
-- Name: device_value_month fk_rrpcrl3r629m0ybi0t7sr35xo; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_value_month
    ADD CONSTRAINT fk_rrpcrl3r629m0ybi0t7sr35xo FOREIGN KEY (device_id) REFERENCES smarthome.device(id);


--
-- TOC entry 3144 (class 2606 OID 25803)
-- Name: mode fk_rw6xcqu9fmupf8kw8nrg7lkex; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.mode
    ADD CONSTRAINT fk_rw6xcqu9fmupf8kw8nrg7lkex FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3110 (class 2606 OID 25808)
-- Name: device fk_s9ldpb0w8p735xk2hkbgrhdol; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device
    ADD CONSTRAINT fk_s9ldpb0w8p735xk2hkbgrhdol FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3152 (class 2606 OID 25813)
-- Name: producteur_energie_action fk_sftxns51a4rg5qa3d38jlj6v2; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.producteur_energie_action
    ADD CONSTRAINT fk_sftxns51a4rg5qa3d38jlj6v2 FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3121 (class 2606 OID 25543)
-- Name: device_type_provider fk_tpgklf51c9yp0etblju6870g0; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.device_type_provider
    ADD CONSTRAINT fk_tpgklf51c9yp0etblju6870g0 FOREIGN KEY (device_type_id) REFERENCES smarthome.device_type(id);


--
-- TOC entry 3158 (class 2606 OID 25818)
-- Name: user_friend fk_yqo5tjhs5j9v500vx9dsciks; Type: FK CONSTRAINT; Schema: smarthome; Owner: postgres
--

ALTER TABLE ONLY smarthome.user_friend
    ADD CONSTRAINT fk_yqo5tjhs5j9v500vx9dsciks FOREIGN KEY (user_id) REFERENCES smarthome.utilisateur(id);


--
-- TOC entry 3286 (class 0 OID 0)
-- Dependencies: 10
-- Name: SCHEMA application; Type: ACL; Schema: -; Owner: pg_signal_backend
--

GRANT ALL ON SCHEMA application TO smarthome;


--
-- TOC entry 3287 (class 0 OID 0)
-- Dependencies: 3
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA public TO smarthome;


--
-- TOC entry 3288 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA quartz; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA quartz TO smarthome;


--
-- TOC entry 3290 (class 0 OID 0)
-- Dependencies: 9
-- Name: SCHEMA smarthome; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA smarthome TO PUBLIC;
GRANT ALL ON SCHEMA smarthome TO smarthome;


--
-- TOC entry 3291 (class 0 OID 0)
-- Dependencies: 188
-- Name: TABLE defi; Type: ACL; Schema: application; Owner: postgres
--

GRANT ALL ON TABLE application.defi TO smarthome;


--
-- TOC entry 3292 (class 0 OID 0)
-- Dependencies: 189
-- Name: TABLE defi_equipe; Type: ACL; Schema: application; Owner: postgres
--

GRANT ALL ON TABLE application.defi_equipe TO smarthome;


--
-- TOC entry 3293 (class 0 OID 0)
-- Dependencies: 190
-- Name: TABLE defi_equipe_participant; Type: ACL; Schema: application; Owner: postgres
--

GRANT ALL ON TABLE application.defi_equipe_participant TO smarthome;


--
-- TOC entry 3294 (class 0 OID 0)
-- Dependencies: 191
-- Name: TABLE defi_equipe_profil; Type: ACL; Schema: application; Owner: postgres
--

GRANT ALL ON TABLE application.defi_equipe_profil TO smarthome;


--
-- TOC entry 3295 (class 0 OID 0)
-- Dependencies: 192
-- Name: TABLE defi_profil; Type: ACL; Schema: application; Owner: postgres
--

GRANT ALL ON TABLE application.defi_profil TO smarthome;


--
-- TOC entry 3296 (class 0 OID 0)
-- Dependencies: 193
-- Name: TABLE act_evt_log; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_evt_log TO smarthome;


--
-- TOC entry 3298 (class 0 OID 0)
-- Dependencies: 194
-- Name: SEQUENCE act_evt_log_log_nr__seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.act_evt_log_log_nr__seq TO smarthome;


--
-- TOC entry 3299 (class 0 OID 0)
-- Dependencies: 195
-- Name: TABLE act_ge_bytearray; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_ge_bytearray TO smarthome;


--
-- TOC entry 3300 (class 0 OID 0)
-- Dependencies: 196
-- Name: TABLE act_ge_property; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_ge_property TO smarthome;


--
-- TOC entry 3301 (class 0 OID 0)
-- Dependencies: 197
-- Name: TABLE act_id_group; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_id_group TO smarthome;


--
-- TOC entry 3302 (class 0 OID 0)
-- Dependencies: 198
-- Name: TABLE act_id_info; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_id_info TO smarthome;


--
-- TOC entry 3303 (class 0 OID 0)
-- Dependencies: 199
-- Name: TABLE act_id_membership; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_id_membership TO smarthome;


--
-- TOC entry 3304 (class 0 OID 0)
-- Dependencies: 200
-- Name: TABLE act_id_user; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_id_user TO smarthome;


--
-- TOC entry 3305 (class 0 OID 0)
-- Dependencies: 201
-- Name: TABLE act_re_deployment; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_re_deployment TO smarthome;


--
-- TOC entry 3306 (class 0 OID 0)
-- Dependencies: 202
-- Name: TABLE act_re_model; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_re_model TO smarthome;


--
-- TOC entry 3307 (class 0 OID 0)
-- Dependencies: 203
-- Name: TABLE act_re_procdef; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_re_procdef TO smarthome;


--
-- TOC entry 3308 (class 0 OID 0)
-- Dependencies: 204
-- Name: TABLE act_ru_event_subscr; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_ru_event_subscr TO smarthome;


--
-- TOC entry 3309 (class 0 OID 0)
-- Dependencies: 205
-- Name: TABLE act_ru_execution; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_ru_execution TO smarthome;


--
-- TOC entry 3310 (class 0 OID 0)
-- Dependencies: 206
-- Name: TABLE act_ru_identitylink; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_ru_identitylink TO smarthome;


--
-- TOC entry 3311 (class 0 OID 0)
-- Dependencies: 207
-- Name: TABLE act_ru_job; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_ru_job TO smarthome;


--
-- TOC entry 3312 (class 0 OID 0)
-- Dependencies: 208
-- Name: TABLE act_ru_task; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_ru_task TO smarthome;


--
-- TOC entry 3313 (class 0 OID 0)
-- Dependencies: 209
-- Name: TABLE act_ru_variable; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.act_ru_variable TO smarthome;


--
-- TOC entry 3314 (class 0 OID 0)
-- Dependencies: 210
-- Name: SEQUENCE hibernate_sequence; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.hibernate_sequence TO smarthome;


--
-- TOC entry 3315 (class 0 OID 0)
-- Dependencies: 211
-- Name: TABLE qrtz_blob_triggers; Type: ACL; Schema: quartz; Owner: postgres
--

GRANT ALL ON TABLE quartz.qrtz_blob_triggers TO smarthome;


--
-- TOC entry 3316 (class 0 OID 0)
-- Dependencies: 212
-- Name: TABLE qrtz_calendars; Type: ACL; Schema: quartz; Owner: postgres
--

GRANT ALL ON TABLE quartz.qrtz_calendars TO smarthome;


--
-- TOC entry 3317 (class 0 OID 0)
-- Dependencies: 213
-- Name: TABLE qrtz_cron_triggers; Type: ACL; Schema: quartz; Owner: postgres
--

GRANT ALL ON TABLE quartz.qrtz_cron_triggers TO smarthome;


--
-- TOC entry 3318 (class 0 OID 0)
-- Dependencies: 214
-- Name: TABLE qrtz_fired_triggers; Type: ACL; Schema: quartz; Owner: postgres
--

GRANT ALL ON TABLE quartz.qrtz_fired_triggers TO smarthome;


--
-- TOC entry 3319 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE qrtz_job_details; Type: ACL; Schema: quartz; Owner: postgres
--

GRANT ALL ON TABLE quartz.qrtz_job_details TO smarthome;


--
-- TOC entry 3320 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE qrtz_locks; Type: ACL; Schema: quartz; Owner: postgres
--

GRANT ALL ON TABLE quartz.qrtz_locks TO smarthome;


--
-- TOC entry 3321 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE qrtz_paused_trigger_grps; Type: ACL; Schema: quartz; Owner: postgres
--

GRANT ALL ON TABLE quartz.qrtz_paused_trigger_grps TO smarthome;


--
-- TOC entry 3322 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE qrtz_scheduler_state; Type: ACL; Schema: quartz; Owner: postgres
--

GRANT ALL ON TABLE quartz.qrtz_scheduler_state TO smarthome;


--
-- TOC entry 3323 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE qrtz_simple_triggers; Type: ACL; Schema: quartz; Owner: postgres
--

GRANT ALL ON TABLE quartz.qrtz_simple_triggers TO smarthome;


--
-- TOC entry 3324 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE qrtz_simprop_triggers; Type: ACL; Schema: quartz; Owner: postgres
--

GRANT ALL ON TABLE quartz.qrtz_simprop_triggers TO smarthome;


--
-- TOC entry 3325 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE qrtz_triggers; Type: ACL; Schema: quartz; Owner: postgres
--

GRANT ALL ON TABLE quartz.qrtz_triggers TO smarthome;


--
-- TOC entry 3326 (class 0 OID 0)
-- Dependencies: 222
-- Name: TABLE acl_class; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.acl_class TO smarthome;


--
-- TOC entry 3327 (class 0 OID 0)
-- Dependencies: 223
-- Name: TABLE acl_entry; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.acl_entry TO smarthome;


--
-- TOC entry 3328 (class 0 OID 0)
-- Dependencies: 224
-- Name: TABLE acl_object_identity; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.acl_object_identity TO smarthome;


--
-- TOC entry 3329 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE acl_sid; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.acl_sid TO smarthome;


--
-- TOC entry 3330 (class 0 OID 0)
-- Dependencies: 226
-- Name: TABLE agent; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.agent TO smarthome;


--
-- TOC entry 3331 (class 0 OID 0)
-- Dependencies: 227
-- Name: TABLE agent_config; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.agent_config TO smarthome;


--
-- TOC entry 3332 (class 0 OID 0)
-- Dependencies: 228
-- Name: TABLE agent_token; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.agent_token TO smarthome;


--
-- TOC entry 3333 (class 0 OID 0)
-- Dependencies: 229
-- Name: TABLE chart; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.chart TO smarthome;


--
-- TOC entry 3334 (class 0 OID 0)
-- Dependencies: 230
-- Name: TABLE chart_device; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.chart_device TO smarthome;


--
-- TOC entry 3335 (class 0 OID 0)
-- Dependencies: 231
-- Name: TABLE chauffage; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.chauffage TO smarthome;


--
-- TOC entry 3336 (class 0 OID 0)
-- Dependencies: 232
-- Name: TABLE commune; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.commune TO smarthome;


--
-- TOC entry 3337 (class 0 OID 0)
-- Dependencies: 233
-- Name: TABLE composant_vue; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.composant_vue TO smarthome;


--
-- TOC entry 3338 (class 0 OID 0)
-- Dependencies: 234
-- Name: TABLE compteur_index; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.compteur_index TO smarthome;


--
-- TOC entry 3339 (class 0 OID 0)
-- Dependencies: 235
-- Name: TABLE config; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.config TO smarthome;


--
-- TOC entry 3340 (class 0 OID 0)
-- Dependencies: 236
-- Name: TABLE databasechangelog; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.databasechangelog TO smarthome;


--
-- TOC entry 3341 (class 0 OID 0)
-- Dependencies: 237
-- Name: TABLE databasechangeloglock; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.databasechangeloglock TO smarthome;


--
-- TOC entry 3342 (class 0 OID 0)
-- Dependencies: 238
-- Name: TABLE device; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device TO smarthome;


--
-- TOC entry 3343 (class 0 OID 0)
-- Dependencies: 239
-- Name: TABLE device_alert; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device_alert TO smarthome;


--
-- TOC entry 3344 (class 0 OID 0)
-- Dependencies: 240
-- Name: TABLE device_level_alert; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device_level_alert TO smarthome;


--
-- TOC entry 3345 (class 0 OID 0)
-- Dependencies: 241
-- Name: TABLE device_metadata; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device_metadata TO smarthome;


--
-- TOC entry 3346 (class 0 OID 0)
-- Dependencies: 242
-- Name: TABLE device_metavalue; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device_metavalue TO smarthome;


--
-- TOC entry 3347 (class 0 OID 0)
-- Dependencies: 243
-- Name: TABLE device_planning; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device_planning TO smarthome;


--
-- TOC entry 3348 (class 0 OID 0)
-- Dependencies: 244
-- Name: TABLE device_share; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device_share TO smarthome;


--
-- TOC entry 3349 (class 0 OID 0)
-- Dependencies: 245
-- Name: TABLE device_type; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device_type TO smarthome;


--
-- TOC entry 3350 (class 0 OID 0)
-- Dependencies: 246
-- Name: TABLE device_type_config; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device_type_config TO smarthome;


--
-- TOC entry 3351 (class 0 OID 0)
-- Dependencies: 247
-- Name: TABLE device_type_provider; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device_type_provider TO smarthome;


--
-- TOC entry 3352 (class 0 OID 0)
-- Dependencies: 248
-- Name: TABLE device_type_provider_prix; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device_type_provider_prix TO smarthome;


--
-- TOC entry 3353 (class 0 OID 0)
-- Dependencies: 249
-- Name: TABLE device_value; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device_value TO smarthome;


--
-- TOC entry 3354 (class 0 OID 0)
-- Dependencies: 250
-- Name: TABLE device_value_day; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device_value_day TO smarthome;


--
-- TOC entry 3355 (class 0 OID 0)
-- Dependencies: 251
-- Name: TABLE device_value_month; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.device_value_month TO smarthome;


--
-- TOC entry 3356 (class 0 OID 0)
-- Dependencies: 252
-- Name: TABLE ecs; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.ecs TO smarthome;


--
-- TOC entry 3357 (class 0 OID 0)
-- Dependencies: 253
-- Name: TABLE event; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.event TO smarthome;


--
-- TOC entry 3358 (class 0 OID 0)
-- Dependencies: 254
-- Name: TABLE event_device; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.event_device TO smarthome;


--
-- TOC entry 3359 (class 0 OID 0)
-- Dependencies: 255
-- Name: TABLE event_mode; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.event_mode TO smarthome;


--
-- TOC entry 3360 (class 0 OID 0)
-- Dependencies: 256
-- Name: TABLE event_trigger; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.event_trigger TO smarthome;


--
-- TOC entry 3361 (class 0 OID 0)
-- Dependencies: 257
-- Name: TABLE house; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.house TO smarthome;


--
-- TOC entry 3362 (class 0 OID 0)
-- Dependencies: 258
-- Name: TABLE house_conso; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.house_conso TO smarthome;


--
-- TOC entry 3363 (class 0 OID 0)
-- Dependencies: 259
-- Name: TABLE house_mode; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.house_mode TO smarthome;


--
-- TOC entry 3364 (class 0 OID 0)
-- Dependencies: 260
-- Name: TABLE house_weather; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.house_weather TO smarthome;


--
-- TOC entry 3365 (class 0 OID 0)
-- Dependencies: 261
-- Name: TABLE mode; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.mode TO smarthome;


--
-- TOC entry 3366 (class 0 OID 0)
-- Dependencies: 262
-- Name: TABLE notification; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.notification TO smarthome;


--
-- TOC entry 3367 (class 0 OID 0)
-- Dependencies: 263
-- Name: TABLE notification_account; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.notification_account TO smarthome;


--
-- TOC entry 3368 (class 0 OID 0)
-- Dependencies: 264
-- Name: TABLE notification_account_sender; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.notification_account_sender TO smarthome;


--
-- TOC entry 3369 (class 0 OID 0)
-- Dependencies: 265
-- Name: TABLE planning; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.planning TO smarthome;


--
-- TOC entry 3370 (class 0 OID 0)
-- Dependencies: 266
-- Name: TABLE producteur_energie; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.producteur_energie TO smarthome;


--
-- TOC entry 3371 (class 0 OID 0)
-- Dependencies: 267
-- Name: TABLE producteur_energie_action; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.producteur_energie_action TO smarthome;


--
-- TOC entry 3372 (class 0 OID 0)
-- Dependencies: 268
-- Name: TABLE profil; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.profil TO smarthome;


--
-- TOC entry 3373 (class 0 OID 0)
-- Dependencies: 269
-- Name: TABLE registration_code; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.registration_code TO smarthome;


--
-- TOC entry 3374 (class 0 OID 0)
-- Dependencies: 270
-- Name: TABLE role; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.role TO smarthome;


--
-- TOC entry 3375 (class 0 OID 0)
-- Dependencies: 271
-- Name: TABLE scenario; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.scenario TO smarthome;


--
-- TOC entry 3376 (class 0 OID 0)
-- Dependencies: 272
-- Name: TABLE script_rule; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.script_rule TO smarthome;


--
-- TOC entry 3377 (class 0 OID 0)
-- Dependencies: 273
-- Name: TABLE user_admin; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.user_admin TO smarthome;


--
-- TOC entry 3378 (class 0 OID 0)
-- Dependencies: 274
-- Name: TABLE user_application; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.user_application TO smarthome;


--
-- TOC entry 3379 (class 0 OID 0)
-- Dependencies: 275
-- Name: TABLE user_friend; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.user_friend TO smarthome;


--
-- TOC entry 3380 (class 0 OID 0)
-- Dependencies: 276
-- Name: TABLE user_role; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.user_role TO smarthome;


--
-- TOC entry 3381 (class 0 OID 0)
-- Dependencies: 277
-- Name: TABLE utilisateur; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.utilisateur TO smarthome;


--
-- TOC entry 3382 (class 0 OID 0)
-- Dependencies: 278
-- Name: TABLE widget; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.widget TO smarthome;


--
-- TOC entry 3383 (class 0 OID 0)
-- Dependencies: 279
-- Name: TABLE widget_user; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.widget_user TO smarthome;


--
-- TOC entry 3384 (class 0 OID 0)
-- Dependencies: 280
-- Name: TABLE workflow; Type: ACL; Schema: smarthome; Owner: postgres
--

GRANT ALL ON TABLE smarthome.workflow TO smarthome;


-- Completed on 2021-02-15 11:03:50 CET

--
-- PostgreSQL database dump complete
--

