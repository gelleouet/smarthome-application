-- Efface les valeurs négatives pour les devices quantitatifs
-- Ex : détecteur présence, on ne garde que les positifs car le négatif correspond à la fin du mouvement

delete from smarthome.device_value
where device_id in 
	(
		select d.id from smarthome.device d inner join smarthome.device_type dt on d.device_type_id = dt.id
		where dt.qualitatif = false and dt.libelle <> 'Volet roulant'
	)
and value = 0;



delete from smarthome.device_value
where device_id in 
	(
		select d.id from smarthome.device d inner join smarthome.device_type dt on d.device_type_id = dt.id
		where dt.libelle = 'Compteur électrique'
	)
and date_value between '2018-03-27 23:30' and '2018-03-28 10:03'
and name in ('hcinst', 'hpinst');