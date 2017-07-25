<%@ page import="smarthome.automation.LevelAlertEnum" %>
<%@ page import="smarthome.automation.ModeAlertEnum" %>

<h6 class="h6">* Tempo : temps en minutes en dehors des limites avant déclenchement de l'alerte</h6>

<table class="aui">
    <thead>
        <tr>
            <th>Alerte</th>
            <th>Valeur limite</th>
            <th>Limite</th>
            <th>Tempo *</th>
            <th>Mail</th>
            <th>SMS</th>
            <th></th>
        </tr>
    </thead>
    <tbody>
    	<g:each var="alerte" in="${ levelAlerts?.sort{ it.level} }" status="status">
	    	<tr>
	    		<td>
	    			<g:if test="${ alerte.id }">
	    				<g:hiddenField name="levelAlerts[${status}].id" value="${ alerte.id }"/>
	    			</g:if>
	    			<g:hiddenField name="levelAlerts[${status}].status" value="${ status }"/>
	    			<g:select name="levelAlerts[${status}].level" value="${ alerte.level }" from="${ LevelAlertEnum.values() }" class="select"/>
	    		</td>
	    		<td><g:field name="levelAlerts[${status}].value" type="number decimal" value="${ alerte.value }" class="text medium-field" required="true"/></td>
	    		<td><g:select name="levelAlerts[${status}].mode" value="${ alerte.mode }" from="${ ModeAlertEnum.values() }" class="select"/></td>
				<td><g:field name="levelAlerts[${status}].tempo" type="number" value="${ alerte.tempo }" class="text short-field" required="true" min="1"/></td>
	    		<td><g:checkBox name="levelAlerts[${status}].notifMail" value="${ alerte.notifMail }"/></td>
	    		<td><g:checkBox name="levelAlerts[${status}].notifSms" value="${ alerte.notifSms }"/></td>
	    		<td class="table-control">
	            	<a class="aui-button aui-button-subtle" id="level-alert-delete-button" data-url="${ g.createLink(action: 'deleteLevelAlert', controller: 'deviceAlert', params: [status: status]) }">
						<span class="aui-icon aui-icon-small aui-iconfont-delete"></span></a>
	            </td>
	    	</tr>
		</g:each>
    </tbody>
</table>
