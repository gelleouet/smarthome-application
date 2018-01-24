<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<div class="aui-group aui-group-split">
			<div class="aui-item">
				<h3>${ device.id ? 'Objet : ' + device.label : 'Nouvel objet' } <span id="ajaxSpinner" class="spinner"/></h3>			
			</div>
			<div class="aui-item">
				<div class="aui-buttons">
					<g:if test="${ device.id }">
						<g:link class="aui-button" action="deviceChart" params="['device.id': device.id]">
							<span class="aui-icon aui-icon-small aui-iconfont-macro-gallery"></span> Graphique
						</g:link>
					</g:if>
				</div>
			</div>
		</div>
		
		<g:form controller="device" method="post" class="aui ${ mobileAgent ? 'top-label' : '' }" name="device-form">
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
			        	<div class="field-group">
							<label for="extras">
								Extras
							</label>
							<g:textArea name="extras" value="${device?.extras}" class="short-script textarea text long-field"/>
							<div class="description">Options de configuration au format JSON</div>
						</div>
						
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