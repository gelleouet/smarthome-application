update device_value set
	double_value = to_number(value, '000000000.00')
where (value ~ '^(-?)([0-9]+)(.?)([0-9]*)$');

update device_value set
	double_value = 1
where (value = 'true');

update device_value set
	double_value = 0
where (value = 'false');