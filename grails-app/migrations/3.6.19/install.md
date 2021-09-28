DROP TABLE smarthome.producteur_energie_action;
DROP TABLE smarthome.producteur_energie;

ALTER TABLE smarthome.widget
    ADD COLUMN content_view character varying(255);

ALTER TABLE smarthome.widget
    ADD COLUMN style character varying(255);