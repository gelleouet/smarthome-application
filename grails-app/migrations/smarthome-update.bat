set VERSION=%1
if exist %VERSION%\smarthome-%VERSION%.xml (
call %LIQUIBASE_HOME%\liquibase.bat --defaultsFile=%LIQUIBASE_PROPERTIES% --changeLogFile=%VERSION%\smarthome-%VERSION%.xml --defaultSchemaName=smarthome update
)
if exist %VERSION%\application-%VERSION%.xml (
call %LIQUIBASE_HOME%\liquibase.bat --defaultsFile=%LIQUIBASE_PROPERTIES% --changeLogFile=%VERSION%\application-%VERSION%.xml --defaultSchemaName=application update
)
