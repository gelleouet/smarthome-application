<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ deviceEvent.id ? 'Evénément : ' + deviceEvent.libelle : 'Nouvel événement' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="deviceEvent" method="post" class="aui" name="device-event-form">
			<g:hiddenField name="id" value="${deviceEvent.id}" />
	
			<div class="aui-tabs horizontal-tabs">
			    <ul class="tabs-menu">
			        <li class="menu-item active-tab">
			            <a href="#tabs-event-general">Général</a>
			        </li>
			        <li class="menu-item">
			            <a href="#tabs-event-cron">Planification</a>
			        </li>
			        <li class="menu-item">
			            <a href="#tabs-event-trigger">Déclencheurs</a>
			        </li>
			    </ul>
			    <div class="tabs-pane active-pane" id="tabs-event-general">
			    	<div class="smart-tabs-content">
			       		<g:render template="form"/>
			       	</div>
			    </div>
			    <div class="tabs-pane" id="tabs-event-cron">
			        <div class="smart-tabs-content">
			       		<g:render template="deviceCron"/>
			       	</div>
			    </div>
			    <div class="tabs-pane" id="tabs-event-trigger">
			        <div class="smart-tabs-content">
			       		<g:render template="deviceTrigger"/>
			       	</div>
			    </div>
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
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>