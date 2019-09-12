<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<h3 class="mb-3">Mes compteurs</h3>
		
		<div class="row">
			<div class="col">
				<div class="card">
					<div class="card-header">
						<div class="card-actions float-right">
							<div class="dropdown show">
								<a href="#" data-toggle="dropdown" data-display="static">
									<app:icon name="more-horizontal"/>
								</a>
								<div class="dropdown-menu dropdown-menu-right">
									<g:if test="${ dataConnect }">
										<g:link class="dropdown-item" action="edit" controller="notificationAccount" id="${ dataConnect.id }">Service</g:link>
										<g:if test="${ dataConnectDevice }">
											<g:link class="dropdown-item" action="edit" controller="device" id="${ dataConnectDevice.id }">Compteur</g:link>
										</g:if>
									</g:if>
									<g:elseif test="${ compteurElecDevice }">
										<g:link class="dropdown-item" action="edit" controller="device" id="${ compteurElecDevice.id }">Compteur</g:link>
									</g:elseif>
								</div>
							</div>
						</div>
						<h4 class="card-title"><app:icon name="zap"/> Electricité</h4>
					</div>
					<div class="card-body">
						<div>
							<g:if test="${ dataConnect }">
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
							
							<g:elseif test="${ compteurElecDevice }">
								<div class="row mb-4 text-center">
									<div class="col">
										<g:if test="${ compteurElecDevice.metadata('modele')?.value == 'Mécanique' }">
											<asset:image src="compteur-elec-mecanique.png"/>
										</g:if>
										<g:else>
											<asset:image src="compteur-elec-electronique.png"/>
										</g:else>
									</div>
									<div class="col">
										<div class="btn-group-vertical">
											<g:link class="btn btn-primary mb-2" action="compteur"><app:icon name="edit"/> Saisie index</g:link>
											<g:link class="btn btn-primary mb-2" action="deviceChart" controller="device" params="['device.id': compteurElecDevice.id]"><app:icon name="bar-chart"/> Consommations</g:link>
										</div>
									</div>
								</div>
								
								<g:render template="/templates/messageInfo" model="[message: compteurElecDevice.label, icon: 'check-circle']"/>
							</g:elseif>
							
							<g:else>
								
								<h5 class="mb-4 font-weight-bold">Votre compteur électrique n'est pas encore configuré. Veuillez choisir le type de compteur installé chez vous et cliquez sur suivant :</h5>
								
								<g:form action="registerCompteur">
									<g:hiddenField name="compteurType" value="elec"/>
									
									<label class="custom-control custom-radio">
										<g:radio name="compteurModel" value="Linky" class="custom-control-input" checked="true"/>
										<span class="custom-control-label">Compteur Linky</span>
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
										<span class="custom-control-label">Compteur non communicant</span>
									</label>
									<div class="row">
										<div class="col">
											<asset:image src="compteur-elec-electronique.png" class="ml-4" style="height:75px;"/>
											<asset:image src="compteur-elec-mecanique.png" class="ml-1" style="height:75px;"/>
										</div>
										<div class="col-8">
											<small class="font-text text-muted">Les index de consommation seront saisis manuellement</small>
										</div>
									</div>
									
									<div class="mt-4 text-right">
										<button class="btn btn-primary"><app:icon name="chevron-right"/> Suivant</button>
									</div>
								</g:form>
							</g:else>
							
							<g:if test="${ dataConnectDevice?.dateValue || compteurElecDevice?.dateValue }">
								<h6 class="text-center">Dernière valeur : il y a <app:formatTimeAgo date="${ dataConnectDevice?.dateValue ?: compteurElecDevice?.dateValue }"/></h6>
							</g:if>
						</div>
					</div>
				</div>
			</div><!-- div.col -->
			<div class="col">
				<div class="card">
					<div class="card-header">
						<div class="card-actions float-right">
							<app:icon name="more-horizontal"/>
						</div>
						<h4 class="card-title"><app:icon name="burn" lib="awesome"/> Gaz</h4>
					</div>
					<div class="card-body">
						
					</div>
				</div>
			</div> <!-- div.col -->
		</div> <!-- div.row -->
		
		
	</g:applyLayout>
</body>
</html>