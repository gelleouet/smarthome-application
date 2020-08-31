<div class="card">

	<div class="card-header">
		<div class="card-actions float-right">
			<div class="dropdown show">
				<a href="#" data-toggle="dropdown" data-display="static">
					<app:icon name="more-horizontal"/>
				</a>
				<div class="dropdown-menu dropdown-menu-right">
					<g:link class="dropdown-item" action="resetCompteurEau">Réinitialiser</g:link>
				</div>
			</div>
		</div>
		<h4 class="card-title"><app:icon name="droplet"/> Eau</h4>
	</div> <!-- div.card-header -->
	
	<div class="card-body">
		<div>
			<g:if test="${ house?.compteurEau }">
				<div class="row mb-4 text-center">
					<div class="col">
						<asset:image src="compteur-eau.jpg" height="200px"/>
					</div>
					<div class="col">
						<div class="btn-group-vertical">
							<g:link class="btn btn-primary mb-2" action="saisieIndex" params="[deviceId: house.compteurEau.id]"><app:icon name="edit"/> Saisie index</g:link>
							<g:link class="btn btn-primary mb-2" action="deviceChart" controller="device" params="['device.id': house.compteurEau.id, dateChart: app.formatPicker(date: house.compteurEau.dateValue ?: new Date())]"><app:icon name="bar-chart"/> Consommations</g:link>
						</div>
						
						<g:if test="${ house.compteurEau.metadata('fournisseur') }">
							<div class="mt-4">
								<span class="text-muted">Votre fournisseur d'eau : </span>
								<br/>
								<span class="badge badge-primary">${ house.compteurEau.metadata('fournisseur').value }</span>
							</div>
						</g:if>
					</div>
				</div>
				
				<g:render template="/templates/messageInfo" model="[message: house.compteurEau.label, icon: 'check-circle']"/>
			</g:if>
			
			<g:else>
				
				<h5 class="mt-1 mb-4 font-weight-bold">
					Votre compteur d'eau n'est pas encore configuré. Veuillez choisir le type de compteur installé chez vous et cliquez sur suivant.					
				</h5>
				
				<g:form action="registerCompteur">
					<g:hiddenField name="compteurType" value="eau"/>
					
					<label class="custom-control custom-radio mt-4">
						<g:radio id="radioCompteurEauNew" name="compteurModel" value="Eau" class="custom-control-input"/>
						<span class="custom-control-label">Compteur non communicant</span>
					</label>
					<div class="row">
						<div class="col">
							<asset:image src="compteur-eau.jpg" class="ml-4"/>
						</div>
						<div class="col-8">
							<small class="font-text text-muted">Les index de consommation seront saisis manuellement.</small>
						</div>
					</div>
					
					<label class="custom-control custom-radio mt-4">
						<g:radio id="radioCompteurEauExist" name="compteurModel" value="_exist" class="custom-control-input"/>
						<span class="custom-control-label">Compteur existant</span>
					</label>
					<div class="row">
						<div class="col">
						</div>
						<div class="col-8">
							<small class="font-text text-muted">Sélectionnez un compteur déjà associé à votre compte</small>
							
							<g:select class="form-control" name="compteurEau.id" from="${ compteurEaux }"
         								optionKey="id" optionValue="label" noSelection="[null: ' ']"
         								onchange="selectRadio('radioCompteurEauExist', true)"/>
						</div>
					</div>
					
					<h5 class="mt-4 font-weight-bold">Vous pouvez également sélectionner votre fournisseur d'eau
						pour visualiser les coûts de consommation <span class="text-muted">(hors abonnement)</span> :</h5>
					
					<g:select class="form-control" name="fournisseur" from="${ fournisseurEau }"
         				optionKey="libelle" optionValue="libelle" noSelection="['': ' ']"/>
					
					<div class="mt-4 text-right">
						<button class="btn btn-primary"><app:icon name="chevron-right"/> Suivant</button>
					</div>
				</g:form>
			</g:else>
			
			<g:if test="${ house?.compteurEau }">
				<h6 class="text-center">Dernière valeur : il y a <app:formatTimeAgo date="${ house.compteurEau.dateValue }"/></h6>
			</g:if>
		</div>
	</div> <!-- div.card-body -->
	
</div> <!-- div.card -->