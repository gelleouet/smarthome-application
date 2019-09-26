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
				
					<g:form action="saveIndex">
					
						<g:hiddenField name="device.id" value="${ device.id }"/>
					
						<div class="row justify-content-center mt-4">
							<div class="col-6 text-left">
								<g:if test="${ compteur instanceof smarthome.automation.deviceType.TeleInformation }">
									<g:if test="${ compteur.optTarif in ['HC', 'EJP'] }">
										<div class="form-group required">
											<label>Index Heures Creuses</label>
											<g:field type="number decimal" name="metavalues.hchc" value="${ command.metavalues.hchc }" class="form-control" required="true"/>
										</div>
										<div class="form-group required">
											<label>Index Heures Pleines</label>
											<g:field type="number decimal" name="metavalues.hchp" value="${ command.metavalues.hchp }" class="form-control" required="true"/>
										</div>
									</g:if>
									<g:else>
										<div class="form-group required">
											<label>Index Heures Base</label>
											<g:field type="number decimal" name="metavalues.base" value="${ command.metavalues.base }" class="form-control" required="true"/>
										</div>
									</g:else>
								</g:if>
								
								<g:elseif test="${ compteur instanceof smarthome.automation.deviceType.CompteurGaz }">
									<div class="form-group required">
										<label>Index</label>
										<g:field type="number decimal" name="value" value="${ command.value }" class="form-control" required="true"/>
									</div>
									<div class="form-group required">
										<label>Coefficient de conversion</label>
										<g:field type="number decimal" name="metadatas.coefConversion" value="${ command.metadatas.coefConversion ?: device.metadata('coefConversion')?.value }" class="form-control" required="true"/>
										<small class="text-muted">Ce coefficient sert à convertir les volumes en kWh. Il sera conservé dans la configuration du compteur</small>
									</div>
								</g:elseif>
								
								<div class="form-group required">
									<label>Date Index</label>
									<g:field name="dateValue" class="form-control small" value="${ app.formatPicker(date: new Date()) }" type="date" required="true"/>
									<small class="text-muted">Après calcul avec le dernier index enregistré, la consommation sera enregistrée à cette date</small>
								</div>
							</div>
						</div>	
					
						<div class="mt-4">
							<button class="btn btn-primary">Enregistrer</button>
							<g:link action="compteur" class="btn btn-link">Annuler</g:link>
						</div>
					</g:form>
				</div>
			</div><!-- div.card-body -->
			
		</div> <!-- div.card -->
		
	</g:applyLayout>
</body>
</html>