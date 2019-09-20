<app:datatable datatableId="events-datatable" recordsTotal="${ event.triggers?.size() ?: 0 }">
    <thead>
        <tr>
            <th>Type</th>
            <th>Elément</th>
            <th>Action</th>
            <th>Paramètres</th>
            <th class="column-1-buttons"></th>
        </tr>
    </thead>
    <tbody>
    	<g:each var="bean" in="${ event.triggers?.sort() }" status="status">
	        <tr>
	            <td>
	            	<g:hiddenField name="triggers[${status}].status" value="${ status }"/>
	            	<g:if test="${ bean.id }">
	            		<g:hiddenField name="triggers[${status}].id" value="${ bean.id }"/>
	            	</g:if>
	            	<g:select name="triggers[${status}].domainClassName" from="${ triggerActions }" class="form-control" required="true" noSelection="['': '']"
	            		optionKey="name" optionValue="label" value="${ bean.domainClassName }"
	            		data-url="${ g.createLink(action: 'templateTriggers') }" onChange="onChangeEventTriggerDomainClassName(this)"/>
	            </td>
	            <td>
	            	<g:select name="triggers[${status}].domainId" from="${ bean.domainList }" class="form-control combobox" required="true" noSelection="['': '']"
	            		optionKey="id" optionValue="${ bean.domainValue }" value="${ bean.domainId }"
	            		data-url="${ g.createLink(action: 'templateTriggers') }" onChange="onChangeEventTriggerDomainId(this)"/>
	            </td>
	            <td>
	            	<g:select name="triggers[${status}].actionName" from="${ bean.actionList }" class="form-control" required="true" noSelection="['': '']"
	            		value="${ bean.actionName }"
	            		data-url="${ g.createLink(action: 'templateTriggers') }" onChange="onChangeEventTriggerActionName(this)"/>
	            </td>
	            <td>
	            	<g:each var="field" in="${ bean.parameterList }">
	            		<div class="form-group ${ field.required() ? 'required' : '' }">
							<label for="${ field.name() }">${ field.label() }</label>
							<g:field type="${ field.type() }" name="triggers[${status}].jsonParameters.${field.name()}"
								value="${bean.jsonParameters[(field.name())]}" class="text medium-field" min="${ field.minValue() }"
								max="${ field.maxValue() }" required="${ field.required() ? 'true' : null }" class="form-control"/>
						</div>
	            	</g:each>
	            </td>
	            <td class="column-1-buttons command-column">
	            	<a id="delete-trigger-button" class="btn btn-light" title="Suppimer" data-url="${ g.createLink(action: 'deleteTrigger', params: [status: status]) }">
	            		<app:icon name="trash"/>
	            	</a>
	            </td>
	        </tr>
        </g:each>
    </tbody>
</app:datatable>