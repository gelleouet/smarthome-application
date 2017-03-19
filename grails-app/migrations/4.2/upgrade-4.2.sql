delete from device_metadata where device_id in (select id from device where user_id = ?);
delete from device_metavalue where device_id in (select id from device where user_id = ?);