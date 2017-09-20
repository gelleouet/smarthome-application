<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body onload="onLoadEventEdit()">
	<g:applyLayout name="applicationConfigure">
		<h3>${ event.id ? 'Evénément : ' + event.libelle : 'Nouvel événement' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="event" method="post" class="aui" name="device-event-form">
			<g:hiddenField name="id" value="${event.id}" />
	
			<div class="aui-tabs horizontal-tabs">
			    <ul class="tabs-menu">
			        <li class="menu-item active-tab">
			            <a href="#tabs-event-general">Général</a>
			        </li>
			        <li class="menu-item">
			            <a href="#tabs-event-cron">Planification</a>
			        </li>
			        <li class="menu-item">
			            <a href="#tabs-event-trigger">Actions</a>
			        </li>
			    </ul>
			    <div class="tabs-pane active-pane" id="tabs-event-general">
			    	<div class="smart-tabs-content">
			       		<g:render template="form"/>
			       	</div>
			    </div>
			    <div class="tabs-pane" id="tabs-event-cron">
			        <div class="smart-tabs-content">
			       		<g:render template="eventCron"/>
			       	</div>
			    </div>
			    <div class="tabs-pane" id="tabs-event-trigger">
			        <div class="smart-tabs-content">
			       		<g:render template="eventTrigger"/>
			       	</div>
			    </div>
			</div>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:actionSubmit value="Enregistrer" action="save" class="aui-button aui-button-primary" />
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>