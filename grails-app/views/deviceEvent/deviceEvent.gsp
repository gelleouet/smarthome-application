<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ deviceEvent.id ? 'Evénément : ' + deviceEvent.libelle : 'Nouvel événement' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="deviceEvent" method="post" class="aui" name="device-event-form">
			<g:hiddenField name="id" value="${deviceEvent.id}" />
	
			<g:render template="form"/>
			
			<h4>Déclencheurs</h4>
			
			<br/>
			<g:submitToRemote class="aui-button" value="Ajouter un déclencheur" url="[action: 'addTrigger']" update="eventTriggers"></g:submitToRemote>
			
			<h6>Pour chaque déclencheur, il est possible d'actionner un autre device et/ou un scénario en même temps.</h6>
			<h6 style="text-transform:none">Le pré-script Groovy permet de modifier l'état du périphérique actionné avant de déclencher l'action. Variables pré-définies :
				<ul>
					<li>device : l'objet device sur lequel est branché l'événement</li>
					<li>triggerDevice : l'objet device qu'on veut déclencher</li>
					<li>devices : tous les devices indexés par leur mac</li>
					<li>Ex : triggerDevice.value = deviceSrc.value</li>
					<li>Ex : triggerDevice.value = devices['gpio4'].value</li>
				</ul>
			</h6>
			
			<div id="eventTriggers" style="margin-top: 10px">
				<g:render template="triggers"/>
			</div>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:if test="${deviceEvent.id }">
						<g:actionSubmit value="Mettre à jour" action="saveEdit" class="aui-button aui-button-primary" />
					</g:if>
					<g:else>
						<g:actionSubmit value="Créer" action="saveCreate" class="aui-button aui-button-primary" />
					</g:else>
					
					<g:link action="deviceEvents" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>