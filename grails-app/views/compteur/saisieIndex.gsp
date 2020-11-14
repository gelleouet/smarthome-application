<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<g:set var="compteur" value="${ command.device.newDeviceImpl() }"/>
	
		<div class="card">
			
			<div class="card-header">
				<div class="card-actions float-right">
				</div>
				<h3 >
					<g:if test="${ compteur instanceof smarthome.automation.deviceType.TeleInformation }">
						<app:icon name="zap"/>
					</g:if>
					<g:elseif test="${ compteur instanceof smarthome.automation.deviceType.CompteurGaz }">
						<app:icon name="fire" lib="awesome"/>
					</g:elseif>
					<g:elseif test="${ compteur instanceof smarthome.automation.deviceType.CompteurEau }">
						<app:icon name="droplet"/>
					</g:elseif>
					 ${ command.device.label }
				</h3>
			</div> <!-- div.card-header -->
		
			<div class="card-body">
			
				<div class="row">
					<div class="col">
						<div class="">
							<h4>Veuillez saisir les index de votre compteur pour enregistrer une nouvelle consommation</h4>
						
							<g:uploadForm action="saveIndex" class="enter-as-tab-form">
							
										
								<g:hiddenField name="device.id" value="${ command.device.id }"/>
								
								<g:render template="formIndex"/>
								
								<div class="form-group mt-2">
									<label for="photo-file">Importer une photo du compteur</label>
									<input name="photo" type="file" id="photo-file"/>
									<small class="form-text text-muted">Veuillez à ce que les index soient lisibles sur la photo pour que l'administrateur puisse valider votre saisie.</small>
								</div>
										
							
								<div class="mt-4">
									<button class="btn btn-primary">Enregistrer</button>
								</div>
							</g:uploadForm>
						</div> <!-- div.text-center -->
					</div> <!-- div.col -->
					
					<div class="col">
						<div class="text-center">
							<h4>Historique des index</h4>
							
							<g:formRemote url="[action: 'datatableIndex']" name="deferedForm-index-historique" update="[success: 'ajaxHistoriqueIndex', failure: 'ajaxError']">
							
								<g:hiddenField name="deviceId" value="${ command.device.id }"/>
							
								<div class="btn-toolbar mb-4" role="toolbar">
									<div class="btn-group mr-2" role="group">
										<g:field type="date" name="dateIndex" class="form-control aui-date-picker" value="${ app.formatPicker(date: new Date()) }" required="true"/>
									</div>	
									
									<div class="btn-group mr-2" role="group">
										<button class="btn btn-outline btn-primary">Rafraîchir</button>
									</div>
								</div>
							</g:formRemote>
							
							
							<div id="ajaxHistoriqueIndex" ajax="true">
							</div>
						</div> <!-- div.text-center -->
					</div> <!-- div.col -->
				</div> <!-- div.row -->
			
			</div> <!-- div.card-body -->
			
		</div> <!-- div.card -->
		
	</g:applyLayout>
</body>
</html>