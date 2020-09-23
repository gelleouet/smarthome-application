set VERSION=%1
call %LIQUIBASE_HOME%\liquibase.bat --defaultsFile=%LIQUIBASE_PROPERTIES% --changeLogFile=%VERSION%\smarthome-%VERSION%.xml --defaultSchemaName=smarthome diffChangeLog
call %LIQUIBASE_HOME%\liquibase.bat --defaultsFile=%LIQUIBASE_PROPERTIES% --changeLogFile=%VERSION%\application-%VERSION%.xml --defaultSchemaName=application diffChangeLog
