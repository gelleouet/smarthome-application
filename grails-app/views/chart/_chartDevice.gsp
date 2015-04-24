<app:datatable datatableId="datatableDevice" recordsTotal="${ chart?.devices?.size() ?: 0 }">
    <thead>
        <tr>
            <th>Périphérique</th>
            <th>Graphique</th>
            <th>Donnée</th>
            <th>Fonctions</th>
            <th class="column-1-buttons"></th>
        </tr>
    </thead>
    <tbody>
    	<g:each var="device" in="${ chart?.devices?.sort { it.position } }" status="status">
	        <tr>
	            <td>
	            	<g:if test="${ device.id }">
		    			<g:hiddenField name="devices[${ status }].id" value="${ device.id }"/>
		    		</g:if>
		    		<g:hiddenField name="devices[${ status }].persist" value="true"/>
		    		<g:hiddenField name="devices[${ status }].position" value="${ status }"/>
	            	<g:select name="devices[${ status }].device.id" from="${ devices }" required="true" value="${ device.device?.id }" 
	            		optionKey="id" optionValue="label" class="select" data-url="${ g.createLink(action: 'refreshDevice') }"/>
	            </td>
	            
	            <td><g:select name="devices[${ status }].chartType" from="${ chartTypes }" required="true" value="${ device.chartType }" class="select"/></td>
	            <td><g:select name="devices[${ status }].metavalue" from="${ device.device?.metaValuesName() }" value="${ device.metavalue }" 
	            	optionKey="key" optionValue="value" class="select" noSelection="[null: 'Valeur par défaut']"/></td>
	            <td><g:select name="devices[${ status }].function" from="${ functions }" value="${ device.function }" class="select" noSelection="[null: '']"/></td>
	            <td class="column-1-buttons command-column">
	            	<a id="delete-chart-device" class="aui-button aui-button-subtle" title="Supprimer"
	            		data-url="${ g.createLink(action: 'deleteDevice', params:[position: status]) }">
	            		<span class="aui-icon aui-icon-small aui-iconfont-delete"></span>
	            	</a>
	            </td>
	        </tr>
        </g:each>
    </tbody>
</app:datatable>

