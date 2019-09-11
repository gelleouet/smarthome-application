<%@ page import="smarthome.automation.DevicePlanning" %>

<html>
<head>
<meta name='layout' content='main' />
</head>

<body onload="onLoadDeviceEdit()">
	
	<g:applyLayout name="page-settings" model="[titre: 'Objets connectés', navigation: 'automation']">
		
		<div class="row mb-4">
			<div class="col-8">
				<h4>${ device.id ? 'Objet : ' + device.label : 'Nouvel objet' }</h4>
			</div>
			<div class="col-4 text-right">
				<div class="btn-toolbar">
					<div class="btn-group">
						<g:if test="${ device.id }">
							<g:link class="btn btn-light" action="deviceChart" params="['device.id': device.id]">
								<app:icon name="bar-chart"/> Graphique
							</g:link>
							<sec:ifAnyGranted roles="ROLE_ADMIN">
								<g:link class="btn btn-light" action="aggregateValues" params="['device.id': device.id]">
									<app:icon name="database"/> Aggrégation
								</g:link>
							</sec:ifAnyGranted>
						</g:if>	
					</div>
				</div>
			</div>
		</div>
		
		<g:form controller="device" method="post" name="device-form">
			<g:hiddenField name="id" value="${device.id}" />
		
		    <div class="nav nav-tabs" role="tablist">
	            <a class="nav-item nav-link active" data-toggle="tab" role="tab" href="#tabs-device-general">Général</a>
	            <a class="nav-item nav-link" data-toggle="tab" role="tab" href="#tabs-device-configuration">Configuration</a>
	            <a class="nav-item nav-link" data-toggle="tab" role="tab" href="#tabs-device-valeurs">Valeurs</a>
	            <a class="nav-item nav-link" data-toggle="tab" role="tab" href="#tabs-device-alerts">Alertes</a>
		        <g:if test="${ device.deviceType?.planning }">
			    	<a class="nav-item nav-link" data-toggle="tab" role="tab" href="#tabs-device-planning">Plannings</a>
		        </g:if>
		    </div>
			
		    <div class="tab-content">
			    <div class="tab-pane active" id="tabs-device-general" role="tabpanel">
			    	<div class="smart-tabs-content">
			       		<g:render template="form"/>
			       	</div>
			    </div>
			    <div class="tab-pane" id="tabs-device-configuration" role="tabpanel">
			        <div id="deviceMetadatas" class="smart-tabs-content">
			        	<div class="form-group">
							<label for="extras">
								Extras
							</label>
							<g:textArea name="extras" value="${device?.extras}" class="short-script form-control"/>
							<small class="form-text text-muted">Options de configuration au format JSON</small>
						</div>
						
						<g:if test="${ device.id }">
							<g:render template="${ device.deviceType.newDeviceType().viewForm() }" model="[device: device]"/>
						</g:if>			
					</div>
			    </div>
			    <div class="tab-pane" id="tabs-device-valeurs" role="tabpanel">
			        <div id="deviceMetavalues" class="smart-tabs-content">
						<g:if test="${ device.id }">
							<g:render template="/deviceType/generic/metavaluesForm" model="[device: device]"/>
						</g:if>			
					</div>
			    </div>
			    <div class="tab-pane" id="tabs-device-alerts" role="tabpanel">
			        <div id="deviceLevelAlerts" class="smart-tabs-content">
						<g:if test="${ device.id }">
							<g:render template="/deviceAlert/deviceLevelAlert" model="[device: device, deviceEvents: deviceEvents]"/>
						</g:if>				
					</div>
			    </div>
			    <g:if test="${ device.deviceType?.planning }">
			    	<div class="tab-pane" id="tabs-device-planning" role="tabpanel">
				        <div id="plannings" class="smart-tabs-content">
				        	<div id="ajaxDevicePlannings">
								<g:render template="/devicePlanning/devicePlannings" model="[device: device, devicePlannings: devicePlannings]"/>
							</div>
						</div>
				    </div>
			    </g:if>
		    </div> <!-- div.tab-content -->
			
			<br/>
	
			<g:if test="${device.id }">
				<g:actionSubmit value="Enregistrer" action="saveEdit" class="btn btn-primary" />
			</g:if>
			<g:else>
				<g:actionSubmit value="Créer" action="saveCreate" class="btn btn-primary" />
			</g:else>
			<g:link action="devices" class="btn btn-link">Annuler</g:link>
		</g:form>
		
	</g:applyLayout>
</body>
</html>