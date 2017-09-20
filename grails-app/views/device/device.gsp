<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ device.id ? 'Objet : ' + device.label : 'Nouvel objet' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="device" method="post" class="aui" name="device-form">
			<g:hiddenField name="id" value="${device.id}" />
	
	
			<div class="aui-tabs horizontal-tabs">
			    <ul class="tabs-menu">
			        <li class="menu-item active-tab">
			            <a href="#tabs-device-general">Général</a>
			        </li>
			        <li class="menu-item">
			            <a href="#tabs-device-configuration">Configuration</a>
			        </li>
			        <li class="menu-item">
			            <a href="#tabs-device-valeurs">Valeurs</a>
			        </li>
			        <li class="menu-item">
			            <a href="#tabs-device-alerts">Alertes</a>
			        </li>
			    </ul>
			    <div class="tabs-pane active-pane" id="tabs-device-general">
			    	<div class="smart-tabs-content">
			       		<g:render template="form"/>
			       	</div>
			    </div>
			    <div class="tabs-pane" id="tabs-device-configuration">
			        <div id="deviceMetadatas" class="smart-tabs-content">
						<g:if test="${ device.id }">
							<g:render template="${ device.deviceType.newDeviceType().viewForm() }" model="[device: device]"/>
						</g:if>			
					</div>
			    </div>
			    <div class="tabs-pane" id="tabs-device-valeurs">
			        <div id="deviceMetavalues" class="smart-tabs-content">
						<g:if test="${ device.id }">
							<g:render template="/deviceType/generic/metavaluesForm" model="[device: device]"/>
						</g:if>			
					</div>
			    </div>
			    <div class="tabs-pane" id="tabs-device-alerts">
			        <div id="deviceLevelAlerts" class="smart-tabs-content">
						<g:if test="${ device.id }">
							<g:render template="/deviceAlert/deviceLevelAlert" model="[device: device, deviceEvents: deviceEvents]"/>
						</g:if>				
					</div>
			    </div>
			</div>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:if test="${device.id }">
						<g:actionSubmit value="Mettre à jour" action="saveEdit" class="aui-button aui-button-primary" />
					</g:if>
					<g:else>
						<g:actionSubmit value="Créer" action="saveCreate" class="aui-button aui-button-primary" />
					</g:else>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>