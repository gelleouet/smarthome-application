<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ device.id ? 'Périphérique : ' + device.label : 'Nouveau périphérique' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="device" method="post" class="aui" name="device-form">
			<g:hiddenField name="id" value="${device.id}" />
	
			<g:render template="form"/>
			
			<div id="deviceMetadatas">
				<g:if test="${ device.id }">
					<g:render template="${ device.deviceType.newDeviceType().viewForm() }" model="[device: device]"/>
				</g:if>			
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
					
					<g:link action="devices" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>