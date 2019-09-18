<div class="card">

	<div class="card-header">
		<div class="card-actions float-right">
			<div class="dropdown show">
				<a href="#" data-toggle="dropdown" data-display="static">
					<app:icon name="more-horizontal"/>
				</a>
				<div class="dropdown-menu dropdown-menu-right">
					<g:if test="${ dataConnect }">
						<g:link class="dropdown-item" action="edit" controller="notificationAccount" id="${ dataConnect.id }">Service DataConnect</g:link>
					</g:if>
					<g:if test="${ house?.compteur }">
						<g:link class="dropdown-item" action="edit" controller="device" id="${ house.compteur.id }">Compteur</g:link>
					</g:if>
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
						<asset:image src="linky.png"/>
					</div>
					<div class="col">
						<div class="btn-group-vertical">
							<g:link class="btn btn-primary mb-2" action="dataconnect"><app:icon name="user-check"/> DataConnect</g:link>
							<g:link class="btn btn-primary mb-2 ${ !dataConnectDevice ? 'disabled' : ''}" action="deviceChart" controller="device" params="['device.id': dataConnectDevice?.id]" disabled="${ dataConnectDevice ? 'false' : 'true'}"><app:icon name="bar-chart"/> Consommations</g:link>
						</div>
					</div>
				</div>
				
				<g:render template="/templates/messageInfo" model="[message: 'Linky with Enedis DataConnect', icon: 'check-circle']"/>
			</g:if>
			
			<g:elseif test="${ house?.compteur }">
				<div class="row mb-4 text-center">
					<div class="col">
						<g:render template="/compteur/modele" model="[compteur: house?.compteur]"/>
					</div>
					<div class="col">
						<div class="btn-group-vertical">
							<g:link class="btn btn-primary mb-2" action="compteur"><app:icon name="edit"/> Saisie index</g:link>
							<g:link class="btn btn-primary mb-2" action="deviceChart" controller="device" params="['device.id': house.compteur.id]"><app:icon name="bar-chart"/> Consommations</g:link>
						</div>
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
							<asset:image src="linky.png" class="ml-4" style="height:75px;"/>
						</div>
						<div class="col-8">
							<small class="font-text text-muted">Les consommations seront téléchargées automatiquement par le service Enedis DataConnect</small>
						</div>
					</div>
					
					<label class="custom-control custom-radio mt-4">
						<g:radio name="compteurModel" value="Electronique" class="custom-control-input"/>
						<span class="custom-control-label">Compteur non communicant ou existant</span>
					</label>
					<div class="row">
						<div class="col">
							<asset:image src="compteur-elec-electronique.png" class="ml-4" style="height:75px;"/>
							<asset:image src="compteur-elec-mecanique.png" class="ml-1" style="height:75px;"/>
						</div>
						<div class="col-8">
							<small class="font-text text-muted">Les index de consommation seront saisis manuellement ou envoyés automatiquement par le bus téléinformation</small>
							
							<g:select class="form-control" name="compteurElec.id" from="${ compteurElecs }"
         								optionKey="id" optionValue="label" placeholder="Select compteur" noSelection="[null: '']"/>
						</div>
					</div>
					
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