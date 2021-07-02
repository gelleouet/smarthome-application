CREATE TABLE IF NOT EXISTS smarthome.user_export
(
    id bigint NOT NULL,
    version bigint NOT NULL,
    export_impl character varying(255) COLLATE pg_catalog."default" NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT user_export_pkey PRIMARY KEY (id),
    CONSTRAINT user_export_uniq UNIQUE (user_id, export_impl),
    CONSTRAINT fk_mq35q9djx8p2ffetnuck9f2sg FOREIGN KEY (user_id)
        REFERENCES smarthome.utilisateur (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

ALTER TABLE smarthome.user_export
    OWNER to postgres;
    
    
DELETE FROM smarthome.user_admin
WHERE admin_id=22;
delete FROM smarthome.user_admin
where admin_id=73561464;
delete FROM smarthome.user_admin
where admin_id=54211159;
delete FROM smarthome.user_admin
where admin_id=69719341;

insert into smarthome.user_admin(user_id, admin_id)
values (1, 1); 
insert into smarthome.user_admin(user_id, admin_id)
values (76639935, 1); 
insert into smarthome.user_admin(user_id, admin_id)
values (76639935, 76639935); 

INSERT INTO smarthome.user_export(
	id, version, export_impl, user_id)
	VALUES (1, 0, 'smarthome.automation.export.AezeoAdminDeviceValueExport', 76639935);
	
INSERT INTO smarthome.user_export(
	id, version, export_impl, user_id)
	VALUES (2, 0, 'smarthome.automation.export.AezeoAdminDeviceValueExport', 1);
