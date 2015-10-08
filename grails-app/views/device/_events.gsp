<app:datatable datatableId="events-datatable" recordsTotal="${ device.events.size() ?: 0 }">
    <thead>
        <tr>
            <th>Description</th>
            <th>Condition</th>
            <th>Device à déclencher</th>
            <th>Scénarion à déclencher</th>
            <th class="column-1-buttons"></th>
        </tr>
    </thead>
    <tbody>
    	<g:each var="bean" in="${ device.events }" status="status">
	        <tr>
	            <td>
	            	<g:field name="events[${status}].libelle" type="text" class="text medium-field" value="${ bean.libelle }"/>
	            	<g:hiddenField name="events[${status}].status" value="${ status }"/>
	            	<g:hiddenField name="events[${status}].persist" value="true"/>
	            </td>
	            <td><g:field name="events[${status}].condition" type="text" class="text long-field" value="${ bean.condition }"/></td>
	            <td>
	            	<g:select id="triggered-device-select" name="events[${status}].triggeredDevice.id" value="${ bean.triggeredDevice?.id }" from="${ devices }" optionKey="id" optionValue="label" class="select"
	            		data-url="${ g.createLink(action: 'templateEvents') }" noSelection="[null: '']"></g:select>
	            	<g:select name="events[${status}].triggeredAction" value="${ bean.triggeredAction }" from="${ bean.triggeredDevice?.deviceType?.newDeviceType()?.events() }" optionKey="id" optionValue="label" class="select"></g:select>
	            </td>
	            <td><g:select name="events[${status}].triggeredWorkflow.id" value="${ bean.triggeredWorkflow?.id }" from="${ workflows }" optionKey="id" optionValue="label" class="select" noSelection="[null: '']"></g:select></td>
	            <td class="column-1-buttons command-column">
	            	<a id="delete-event-button" class="aui-button aui-button-subtle" title="Suppimer" data-url="${ g.createLink(action: 'deleteEvent', params: [status: status]) }">
	            		<span class="aui-icon aui-icon-small aui-iconfont-delete">
	            	</a>
	            </td>
	        </tr>
        </g:each>
    </tbody>
</app:datatable>