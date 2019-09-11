<div class="btn-toolbar">
    <div class="btn-group">
		<g:submitToRemote url="[action: 'addLevelAlert', controller: 'deviceAlert']" class="btn btn-light" value="Ajouter" update="[success: 'ajaxAlerts', failure: 'ajaxError']"/>
    </div>
</div>

<div id="ajaxAlerts" style="padding-top:15px">
	<g:render template="/deviceAlert/deviceLevelAlertTable" model="[levelAlerts: device.levelAlerts, deviceEvents: deviceEvents]"/>
</div>