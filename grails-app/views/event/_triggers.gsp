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
	            	<g:select name="triggers[${status}].domainClassName" from="${ triggerActions }" class="select" required="true" noSelection="['': '']"
	            		optionKey="name" optionValue="label" value="${ bean.domainClassName }"
	            		data-url="${ g.createLink(action: 'templateTriggers') }" onChange="onChangeEventTriggerDomainClassName(this)"/>
	            </td>
	            <td>
	            	<g:select name="triggers[${status}].domainId" from="${ bean.domainList }" class="select combobox" required="true" noSelection="['': '']"
	            		optionKey="id" optionValue="${ bean.domainValue }" value="${ bean.domainId }"
	            		data-url="${ g.createLink(action: 'templateTriggers') }" onChange="onChangeEventTriggerDomainId(this)"/>
	            </td>
	            <td>
	            	<g:select name="triggers[${status}].actionName" from="${ bean.actionList }" class="select" required="true" noSelection="['': '']"
	            		value="${ bean.actionName }"
	            		data-url="${ g.createLink(action: 'templateTriggers') }" onChange="onChangeEventTriggerActionName(this)"/>
	            </td>
	            <td>
	            	<g:each var="field" in="${ bean.parameterList }">
	            		<div class="field-group">
							<label for="${ field.name() }">
								${ field.label() }
								<g:if test="${ field.required() }"><span class="aui-icon icon-required">*</span></g:if>
							</label>
							<g:field type="${ field.type() }" name="triggers[${status}].jsonParameters.${field.name()}"
								value="${bean.jsonParameters[(field.name())]}" class="text medium-field" min="${ field.minValue() }"
								max="${ field.maxValue() }" required="${ field.required() }"/>
						</div>
	            	</g:each>
	            </td>
	            <td class="column-1-buttons command-column">
	            	<a id="delete-trigger-button" class="aui-button aui-button-subtle" title="Suppimer" data-url="${ g.createLink(action: 'deleteTrigger', params: [status: status]) }">
	            		<span class="aui-icon aui-icon-small aui-iconfont-delete">
	            	</a>
	            </td>
	        </tr>
        </g:each>
    </tbody>
</app:datatable>