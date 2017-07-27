ALTER TABLE smarthome.workflow
	RENAME TO scenario;

ALTER TABLE smarthome.device_event_trigger
	RENAME workflow_id  TO scenario_id;

ALTER TABLE smarthome.scenario
  RENAME CONSTRAINT workflow_pkey TO scenario_pkey;
  
ALTER INDEX smarthome.workflow_user_idx
  RENAME TO scenario_user_idx;

CREATE TABLE smarthome.workflow
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  libelle character varying(255) NOT NULL,
  data bytea,
  description character varying(255),
  CONSTRAINT workflow_pkey PRIMARY KEY (id),
  CONSTRAINT uk_bakmmy47if4gwgbxfi1rb09v0 UNIQUE (libelle)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE smarthome.workflow
  OWNER TO postgres;


