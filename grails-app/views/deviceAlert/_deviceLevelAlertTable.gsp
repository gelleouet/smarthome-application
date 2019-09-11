<%@ page import="smarthome.automation.LevelAlertEnum" %>
<%@ page import="smarthome.automation.ModeAlertEnum" %>

<table class="table">
    <thead>
        <tr>
            <th>Alerte</th>
            <th>Valeur limite</th>
            <th>Limite</th>
            <th>Tempo *</th>
            <th>Evénement</th>
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
	    			<g:select name="levelAlerts[${status}].level" value="${ alerte.level }" from="${ LevelAlertEnum.values() }" class="form-control"/>
	    		</td>
	    		<td><g:field name="levelAlerts[${status}].value" type="number decimal" value="${ alerte.value }" class="form-control" required="true"/></td>
	    		<td><g:select name="levelAlerts[${status}].mode" value="${ alerte.mode }" from="${ ModeAlertEnum.values() }" class="form-control"/></td>
				<td><g:field name="levelAlerts[${status}].tempo" type="number" value="${ alerte.tempo }" class="form-control" required="true" min="1"/></td>
	    		<td><g:select name="levelAlerts[${status}].event.id" value="${ alerte.event?.id }" from="${ deviceEvents }"
	    			class="form-control combobox" optionKey="id" optionValue="libelle" noSelection="[null: '']"/></td>
	    		<td class="table-control">
	            	<a class="btn btn-light" id="level-alert-delete-button" data-url="${ g.createLink(action: 'deleteLevelAlert', controller: 'deviceAlert', params: [status: status]) }">
						<app:icon name="trash"/></a>
	            </td>
	    	</tr>
		</g:each>
    </tbody>
</table>

<h6>* Tempo : temps en minutes en dehors des limites avant déclenchement de l'alerte</h6>