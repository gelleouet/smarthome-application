set VERSION=%1
call %LIQUIBASE_HOME%\liquibase.bat --changeLogFile=%VERSION%\smarthome-%VERSION%.xml --defaultSchemaName=smarthome diffChangeLog
call %LIQUIBASE_HOME%\liquibase.bat --changeLogFile=%VERSION%\application-%VERSION%.xml --defaultSchemaName=application diffChangeLog
