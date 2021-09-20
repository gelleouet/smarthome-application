ALTER TABLE smarthome.house
    ADD COLUMN adresse character varying(255);
    
ALTER TABLE smarthome.house
    ADD COLUMN chauffage_secondaire_id bigint;
    
ALTER TABLE smarthome.house
    ADD COLUMN code_postal character varying(16);
    
ALTER TABLE smarthome.house
    ADD COLUMN nb_personne integer;
    
ALTER TABLE smarthome.house
    ADD CONSTRAINT fk_lxs64jrbwsi4sbj0qxc5ic2l7 FOREIGN KEY (chauffage_secondaire_id)
    REFERENCES smarthome.chauffage (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;
    
ALTER TABLE smarthome.utilisateur
    ADD COLUMN autorise_conso_data boolean NOT NULL DEFAULT true;
    
ALTER TABLE smarthome.utilisateur
    ADD COLUMN autorise_user_data boolean NOT NULL DEFAULT true;
    
ALTER TABLE smarthome.utilisateur
    ADD COLUMN engage_enedis_account boolean NOT NULL DEFAULT true;
    
ALTER TABLE smarthome.utilisateur
    ADD COLUMN autorise_share_data boolean NOT NULL DEFAULT false;
    
    
select id, user_id, to_timestamp(cast(json_extract_path_text(config::json, 'last_token') as bigint) / 1000) as last_token,
	json_extract_path_text(config::json, 'usage_point_id') as device_mac
FROM smarthome.notification_account
where to_timestamp(cast(json_extract_path_text(config::json, 'last_token') as bigint) / 1000) < '2021-08-01'