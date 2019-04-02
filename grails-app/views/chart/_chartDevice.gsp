<%@ page import="smarthome.automation.SeriesTypeEnum" %>

<app:datatable datatableId="datatableDevice" recordsTotal="${ chart?.devices?.size() ?: 0 }">
    <thead>
        <tr>
            <th></th>
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
		            
		            <fieldset>
		            	<div class="field-group">
							<label>
								Objet
								<span class="aui-icon icon-required">*</span>
							</label>
							<g:select name="devices[${ status }].device.id" from="${ devices }" required="true" value="${ device.device?.id }" 
			            		optionKey="id" optionValue="label" class="select" data-url="${ g.createLink(action: 'refreshDevice') }"/>
						</div>
						
						<div class="field-group">
							<label>
								Donnée
								<span class="aui-icon icon-required">*</span>
							</label>
							<g:select name="devices[${ status }].metavalue" from="${ device?.device?.metavalues?.sort{ it.label } }" value="${ device.metavalue }" 
			            	optionKey="name" optionValue="label" class="select" noSelection="['': 'Valeur par défaut']"/>
						</div>
						
						<div class="field-group">
							<label>
								Représentation
								<span class="aui-icon icon-required">*</span>
							</label>
							<g:select name="devices[${ status }].chartType" from="${ SeriesTypeEnum.values() }" required="true" value="${ device.chartType }" class="select"/>
						</div>
						
		            	<div class="field-group">
							<label>
								Fonction
								<span class="aui-icon icon-required">*</span>
							</label>
							<g:select name="devices[${ status }].function" from="${ functions }" value="${ device.function }" class="select" required="true"/>
							<div class="description">Utilisée pour les vues mois/année</div>
						</div>
						
						<div class="field-group">
							<label>
								Légende
							</label>
							<g:textField name="devices[${ status }].legend" value="${ device.legend }" class="text"/>
						</div>
						
						<div class="field-group">
							<label>
								Couleur
							</label>
							<g:textField name="devices[${ status }].color" value="${ device.color }" class="text"/>
						</div>
						
		            	<div class="field-group">
							<label>
								Transformation
							</label>
							<g:textArea name="devices[${ status }].transformer" value="${ device.transformer }" class="short-script textarea"/>
						</div>
		            </fieldset>
	            </td>
	            
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

