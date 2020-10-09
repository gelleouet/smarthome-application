<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<g:set var="compteur" value="${ device.newDeviceImpl() }"/>
	
		<h3 class="mb-3">Saisie Index Compteur</h3>
		
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
					 ${ device.label }
				</h4>
			</div> <!-- div.card-header -->
		
			<div class="card-body">
				<div class="text-center">
				
					<h5>Veuillez saisir les index de votre compteur pour enregistrer une nouvelle consommation</h5>
				
					<g:uploadForm action="saveIndex">
					
						<div class="row justify-content-center mt-4">
							<div class="col-6 text-left">
								
								<g:hiddenField name="deviceId" value="${ command.deviceId }"/>
								
								<g:render template="formIndex"/>
								
								<div class="form-group mt-2">
									<label for="photo-file">Importer une photo du compteur</label>
									<input name="photo" type="file" id="photo-file"/>
									<small class="form-text text-muted">Veuillez à ce que les index soient lisibles sur la photo pour que l'administrateur puisse valider votre saisie.</small>
								</div>
								
							</div>
						</div>	
					
						<div class="mt-4">
							<button class="btn btn-primary">Enregistrer</button>
						</div>
					</g:uploadForm>
				</div>
			</div><!-- div.card-body -->
			
		</div> <!-- div.card -->
		
	</g:applyLayout>
</body>
</html>