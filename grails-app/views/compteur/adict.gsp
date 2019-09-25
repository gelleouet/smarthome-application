<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<h3 class="mb-3">GRDF ADICT</h3>
		
		<div class="card">
			<div class="card-body">
				<div class="text-center">
					<h5 class="font-weight-bold">Pour consulter vos consommations, autorisez GRDF à nous transmettre vos données.</h5>
			
					<asset:image src="gazpar.png" height="150px"/>
			
					<p>Compléter le formulaire suivant pour la demande d'autorisation auprès du service GRDF ADICT</p>
					
					<g:form action="accreditation">
						<div class="row justify-content-center mt-4">
							<div class="col-6 text-left">
								<div class="form-group required">
									<label>PCE</label>
									<g:textField name="pce" value="${ accreditation?.pce }" class="form-control" required="true"/>
								</div>
								<div class="form-group required">
									<label>Rôle</label>
									<g:select name="role" value="${ accreditation?.role ?: 'AUTORISE_CONTRAT_FOURNITURE' }" from="${ ['AUTORISE_CONTRAT_FOURNITURE', 'DETENTEUR_CONTRAT_FOURNITURE'] }" class="form-control" required="true"/>
								</div>
								<div class="form-group required">
									<label>Code postal</label>
									<g:textField name="codePostal" value="${ accreditation?.codePostal }" class="form-control" required="true"/>
								</div>
								<div class="form-group required">
									<label>Type titulaire</label>
									<g:select name="titulaireType" value="${ accreditation?.titulaireType }" from="${ ['nomTitulaire', 'raisonSociale'] }" class="form-control" required="true"/>
								</div>
								<div class="form-group required">
									<label>Titulaire</label>
									<g:textField name="titulaireValeur" value="${ accreditation?.titulaireValeur }" class="form-control" required="true"/>
								</div>
								<div class="form-group required">
									<label>Email</label>
									<g:textField name="emailTitulaire" value="${ accreditation?.emailTitulaire }" class="form-control" required="true"/>
								</div>
							</div>
						</div>
						
						<div class="mt-4">
							<button class="btn btn-primary">Demande autorisation</button>
							<g:link action="compteur" controller="compteur" class="btn btn-link">Annuler</g:link>
						</div>
					</g:form>
							
				</div>
			</div> <!-- div.card-body -->
		</div> <!-- div.card -->
		
	</g:applyLayout>
</body>
</html>