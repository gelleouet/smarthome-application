call liquibase.bat --changeLogFile=lims2015-common-1.0.3.xml --defaultSchemaName=common update
call liquibase.bat --changeLogFile=lims2015-sample-1.0.3.xml --defaultSchemaName=sample update
call liquibase.bat --changeLogFile=lims2015-device-1.0.3.xml --defaultSchemaName=device update
call liquibase.bat --changeLogFile=lims2015-security-1.0.3.xml --defaultSchemaName=security update