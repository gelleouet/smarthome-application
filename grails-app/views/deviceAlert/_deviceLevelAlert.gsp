<div class="aui-toolbar2">
    <div class="aui-toolbar2-inner">
        <div class="aui-toolbar2-primary">
            <div class="aui-buttons">
				<g:submitToRemote url="[action: 'addLevelAlert', controller: 'deviceAlert']" class="aui-button" value="Ajouter alerte" update="[success: 'ajaxAlerts', failure: 'ajaxError']"/>
            </div>
        </div>
    </div><!-- .aui-toolbar-inner -->
</div>

<div id="ajaxAlerts" style="padding-top:15px">
	<g:render template="/deviceAlert/deviceLevelAlertTable" model="[levelAlerts: device.levelAlerts, deviceEvents: deviceEvents]"/>
</div>