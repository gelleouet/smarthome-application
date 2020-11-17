<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<g:set var="compteur" value="${ command.device.newDeviceImpl() }"/>
	
		<h3 class="mb-3">Validation Index Compteur</h3>
		
		<div class="card">
			
			<div class="card-header">
				<div class="card-actions float-right">
				</div>
				<h4 class="card-title">
					<g:if test="${ compteur instanceof smarthome.automation.deviceType.TeleInformation }">
						<app:icon name="zap"/>
					</g:if>
					<g:elseif test="${ compteur instanceof smarthome.automation.deviceType.CompteurGaz }">
						<app:icon name="fire" lib="awesome"/>
					</g:elseif>
					<g:elseif test="${ compteur instanceof smarthome.automation.deviceType.CompteurEau }">
						<app:icon name="droplet"/>
					</g:elseif>
					 ${ command.device.user.prenomNom } > ${ command.device.label }
					 
					 <sec:ifAnyGranted roles="ROLE_ADMIN">
						<g:link controller="device" action="edit" id="${ command.device.id }" title="Edition"><app:icon name="edit"/></g:link>
					</sec:ifAnyGranted>
				</h4>
			</div> <!-- div.card-header -->
		
			<div class="card-body">
			
				<div class="row">
					<div class="col">
						<g:form action="validIndex" class="enter-as-tab-form">
						
							<g:hiddenField name="id" value="${ command.id }"/>
						
							<div class="row">
								<div class="col">
									<g:link action="compteurIndexImg" id="${ command.id }" target="compteurIndexImg">
										<img class="img-thumbnail rounded" src="${ g.createLink(action: 'compteurIndexImg', id: command.id) }" alt="Aucune image importée"/>
									</g:link>
								</div>
							</div>	
							<div class="row">
								<div class="col">
									<g:render template="formIndex"/>
								</div>
							</div>	
						
							<div class="mt-4 text-center">
								<button class="btn btn-primary">Valider</button>
								<g:link action="compteurIndexs" class="btn btn-link">Annuler</g:link>
							</div>
						</g:form>
					</div> <!-- div.col -->
					
					<div class="col">
						<h5>Historique des index</h5>
						
						<g:formRemote url="[action: 'datatableIndex']" name="deferedForm-index-historique" update="[success: 'ajaxHistoriqueIndex', failure: 'ajaxError']">
						
							<g:hiddenField name="deviceId" value="${ command.device.id }"/>
						
							<div class="btn-toolbar mb-4" role="toolbar">
								<div class="btn-group mr-2" role="group">
									<g:field type="date" name="dateIndex" class="form-control aui-date-picker" value="${ app.formatPicker(date: new Date()) }" required="true"/>
								</div>	
								
								<div class="btn-group mr-2" role="group">
									<button class="btn btn-light"><app:icon name="search"/></button>
								</div>
							</div>
						</g:formRemote>
						
						
						<div id="ajaxHistoriqueIndex" ajax="true">
						</div>
					</div> <!-- div.col -->
				</div> <!-- div.row -->
			</div><!-- div.card-body -->
			
		</div> <!-- div.card -->
		
	</g:applyLayout>
</body>
</html>