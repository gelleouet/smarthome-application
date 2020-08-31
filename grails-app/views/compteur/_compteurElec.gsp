<div class="card">

	<div class="card-header">
		<div class="card-actions float-right">
			<div class="dropdown show">
				<a href="#" data-toggle="dropdown" data-display="static">
					<app:icon name="more-horizontal"/>
				</a>
				<div class="dropdown-menu dropdown-menu-right">
					<g:link class="dropdown-item" action="resetCompteurElec">Réinitialiser</g:link>
				</div>
			</div>
		</div>
		<h4 class="card-title"><app:icon name="zap"/> Electricité</h4>
	</div> <!-- div.card-header -->
	
	<div class="card-body">
		<div>
			<g:if test="${ house?.compteur && house.compteur.id == dataConnectDevice?.id }">
				<div class="row mb-4 text-center">
					<div class="col">
						<g:render template="/compteur/modeleElec" model="[compteur: house?.compteur]"/>
					</div>
					<div class="col">
						<div class="btn-group-vertical">
							<g:link class="btn btn-primary mb-2 ${ !dataConnectDevice ? 'disabled' : ''}" action="deviceChart" controller="device" params="['device.id': dataConnectDevice?.id, dateChart: app.formatPicker(date: house.compteur.dateValue ?: new Date())]" disabled="${ dataConnectDevice ? 'false' : 'true'}"><app:icon name="bar-chart"/> Consommations</g:link>
						</div>
						
						<g:if test="${ house.compteur.metadata('fournisseur') }">
							<div class="mt-4">
								<span class="text-muted">Votre fournisseur d'électricité : </span>
								<br/>
								<span class="badge badge-primary">${ house.compteur.metadata('fournisseur').value }</span>
							</div>
						</g:if>
					</div>
				</div>
				
				<g:render template="/templates/messageInfo" model="[message: 'Linky with Enedis DataConnect', icon: 'check-circle']"/>
			</g:if>
			
			<g:elseif test="${ house?.compteur }">
				<div class="row mb-4 text-center">
					<div class="col">
						<g:render template="/compteur/modeleElec" model="[compteur: house?.compteur]"/>
					</div>
					<div class="col">
						<div class="btn-group-vertical">
							<g:link class="btn btn-primary mb-2" action="saisieIndex" controller="compteur" params="[deviceId: house.compteur.id]"><app:icon name="edit"/> Saisie index</g:link>
							<g:link class="btn btn-primary mb-2" action="deviceChart" controller="device" params="['device.id': house.compteur.id, dateChart: app.formatPicker(date: house.compteur.dateValue ?: new Date())]"><app:icon name="bar-chart"/> Consommations</g:link>
						</div>
						
						<g:if test="${ house.compteur.metadata('fournisseur') }">
							<div class="mt-4">
								<span class="text-muted">Votre fournisseur d'électricité : </span>
								<br/>
								<span class="badge badge-primary">${ house.compteur.metadata('fournisseur').value }</span>
							</div>
						</g:if>
					</div>
				</div>
				
				<g:render template="/templates/messageInfo" model="[message: house.compteur.label, icon: 'check-circle']"/>
			</g:elseif>
			
			<g:else>
				
				<h5 class="mb-4 font-weight-bold">Votre compteur électrique n'est pas encore configuré. Veuillez choisir le type de compteur installé chez vous et cliquez sur suivant :</h5>
				
				<g:form action="registerCompteur">
					<g:hiddenField name="compteurType" value="elec"/>
					
					<label class="custom-control custom-radio">
						<g:radio name="compteurModel" value="Linky" class="custom-control-input" checked="true"/>
						<span class="custom-control-label">Linky - Enedis DataConnect</span>
					</label>
					<div class="row">
						<div class="col">
							<asset:image src="linky.png" class="ml-4 compteur-model-img"/>
						</div>
						<div class="col-8">
							<small class="font-text text-muted">Les consommations seront téléchargées automatiquement par le service Enedis DataConnect</small>
						</div>
					</div>
					
					<label class="custom-control custom-radio mt-4">
						<g:radio id="radioCompteurElecNew" name="compteurModel" value="Electronique" class="custom-control-input"/>
						<span class="custom-control-label">Compteur non communicant</span>
					</label>
					<div class="row">
						<div class="col">
							<asset:image src="compteur-elec-electronique.png" class="ml-4 compteur-model-img"/>
							<asset:image src="compteur-elec-mecanique.png" class="ml-1 compteur-model-img"/>
						</div>
						<div class="col-8">
							<small class="font-text text-muted">Les index de consommation seront saisis manuellement. Veuillez indiquer votre contrat tarifaire :</small>
							
							<g:select class="form-control" name="contrat" from="${ contratElecs }"
         								optionKey="key" optionValue="value" onchange="selectRadio('radioCompteurElecNew', true)"/>
						</div>
					</div>
					
					<label class="custom-control custom-radio mt-4">
						<g:radio id="radioCompteurElecExist" name="compteurModel" value="_exist" class="custom-control-input"/>
						<span class="custom-control-label">Compteur existant</span>
					</label>
					<div class="row">
						<div class="col">
							
						</div>
						<div class="col-8">
							<small class="font-text text-muted">Sélectionnez un compteur déjà associé à votre compte</small>
							
							<g:select class="form-control" name="compteurElec.id" from="${ compteurElecs }"
         								optionKey="id" optionValue="label" noSelection="[null: ' ']"
         								onchange="selectRadio('radioCompteurElecExist', true)"/>
						</div>
					</div>
					
					
					<h5 class="mt-4 font-weight-bold">Vous pouvez également sélectionner votre fournisseur d'électricité
						pour visualiser les coûts de consommation <span class="text-muted">(hors abonnement)</span> :</h5>
					
					<g:select class="form-control" name="fournisseur" from="${ fournisseurElecs }"
         				optionKey="libelle" optionValue="libelle" noSelection="['': ' ']"/>
					
					<div class="mt-4 text-right">
						<button class="btn btn-primary"><app:icon name="chevron-right"/> Suivant</button>
					</div>
				</g:form>
			</g:else>
			
			<g:if test="${ house?.compteur }">
				<h6 class="text-center">Dernière valeur : il y a <app:formatTimeAgo date="${ house.compteur.dateValue }"/></h6>
			</g:if>
		</div>
	</div> <!-- div.card-body -->
	
</div> <!-- div.card -->