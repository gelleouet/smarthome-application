ALTER TABLE smarthome.utilisateur
    ADD COLUMN accept_use_data boolean NOT NULL default true;
ALTER TABLE smarthome.utilisateur
    ADD COLUMN accept_publish_data boolean NOT NULL default true;
ALTER TABLE smarthome.house
    ADD COLUMN code_postal character varying(8);
ALTER TABLE smarthome.house
    ADD COLUMN adresse character varying(255);
ALTER TABLE smarthome.house
    ADD COLUMN nb_personne integer;