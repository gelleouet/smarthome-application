<html>
<head>
<meta name='layout' content='main' />
</head>

<body onload="onLoadEventEdit()">
	<g:applyLayout name="page-settings" model="[titre: 'Evénements', navigation: 'Smarthome']">
	
		<div class="row mb-4">
			<div class="col-8">
				<h4>${ event.id ? 'Evénément : ' + event.libelle : 'Nouvel événement' }</h4>
			</div>
			<div class="col-4 text-right">
				<div class="btn-toolbar">
					<div class="btn-group">
					</div>
				</div>
			</div>
		</div>
		
		<g:form controller="event" method="post" class="aui ${ mobileAgent ? 'top-label' : '' }" name="device-event-form">
			<g:hiddenField name="id" value="${event.id}" />
	
			<div class="nav nav-tabs" role="tablist">
	        	<a class="nav-item nav-link active" data-toggle="tab" role="tab" href="#tabs-event-general">Général</a>
	            <a class="nav-item nav-link" data-toggle="tab" role="tab" href="#tabs-event-cron">Planification</a>
	            <a class="nav-item nav-link" data-toggle="tab" role="tab" href="#tabs-event-trigger">Actions</a>
			</div>
			
			<div class="tab-content">
			    <div class="tab-pane active" id="tabs-event-general" role="tabpanel">
			    	<div class="smart-tabs-content">
			       		<g:render template="form"/>
			       	</div>
			    </div>
			    <div class="tab-pane" id="tabs-event-cron" role="tabpanel">
			        <div class="smart-tabs-content">
			       		<g:render template="eventCron"/>
			       	</div>
			    </div>
			    <div class="tab-pane" id="tabs-event-trigger" role="tabpanel">
			        <div class="smart-tabs-content">
			       		<g:render template="eventTrigger"/>
			       	</div>
			    </div>
		    </div>
			
			<br/>
	
			<g:actionSubmit value="Enregistrer" action="save" class="btn btn-primary" />
			<g:link action="events" class="btn btn-link">Annuler</g:link>
		</g:form>
		
	</g:applyLayout>
</body>
</html>