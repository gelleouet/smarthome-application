<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<g:set var="compteur" value="${ device.newDeviceImpl() }"/>
	
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
					 ${ device.user.prenomNom } > ${ device.label }
				</h4>
			</div> <!-- div.card-header -->
		
			<div class="card-body">
				<g:form action="validIndex">
				
					<g:hiddenField name="id" value="${ command.id }"/>
				
					<div class="row">
						<div class="col-6">
							<g:render template="formIndex"/>
						</div>
						<div class="col-6">
							<g:link action="compteurIndexImg" id="${ command.id }" target="compteurIndexImg">
								<img class="img-thumbnail rounded" src="${ g.createLink(action: 'compteurIndexImg', id: command.id) }" />
							</g:link>
						</div>
					</div>	
				
					<div class="mt-4 text-center">
						<button class="btn btn-primary">Valider</button>
						<g:link action="compteurIndexs" class="btn btn-link">Annuler</g:link>
					</div>
				</g:form>
			</div><!-- div.card-body -->
			
		</div> <!-- div.card -->
		
	</g:applyLayout>
</body>
</html>