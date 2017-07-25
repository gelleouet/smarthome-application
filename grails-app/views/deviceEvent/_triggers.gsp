<app:datatable datatableId="events-datatable" recordsTotal="${ deviceEvent.triggers?.size() ?: 0 }">
    <thead>
        <tr>
            <th>Objet</th>
            <th>Scénario</th>
            <th class="column-1-buttons"></th>
        </tr>
    </thead>
    <tbody>
    	<g:each var="bean" in="${ deviceEvent.triggers }" status="status">
	        <tr>
	            <td>
	            	<g:hiddenField name="triggers[${status}].status" value="${ status }"/>
	            	<g:hiddenField name="triggers[${status}].persist" value="true"/>
	            	<g:if test="${ bean.id }">
	            		<g:hiddenField name="triggers[${status}].id" value="${ bean.id }"/>
	            	</g:if>
	            	
	            	<g:textField name="triggers[${status}].preScript" value="${ bean.preScript }" class="text long-field" placeholder="Script Groovy conditionnel"/>
	            	<g:select id="triggered-device-select" name="triggers[${status}].device.id" value="${ bean.device?.id }" from="${ devices }" optionKey="id" optionValue="label" class="select"
	            		data-url="${ g.createLink(action: 'templateTriggers') }" noSelection="[null: '']"></g:select>
	            	<g:select name="triggers[${status}].actionName" value="${ bean.actionName }" from="${ bean.device?.deviceType?.newDeviceType()?.events() }" class="select" noSelection="['': 'Action synchronisée']"></g:select>
	            </td>
	            <td><g:select name="triggers[${status}].scenario.id" value="${ bean.scenario?.id }" from="${ scenarios }" optionKey="id" optionValue="label" class="select" noSelection="[null: '']"></g:select></td>
	            <td class="column-1-buttons command-column">
	            	<a id="delete-trigger-button" class="aui-button aui-button-subtle" title="Suppimer" data-url="${ g.createLink(action: 'deleteTrigger', params: [status: status]) }">
	            		<span class="aui-icon aui-icon-small aui-iconfont-delete">
	            	</a>
	            </td>
	        </tr>
        </g:each>
    </tbody>
</app:datatable>