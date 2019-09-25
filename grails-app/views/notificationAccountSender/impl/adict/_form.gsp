<br/>

<g:if test="${ notificationAccount?.jsonConfig?.pce }">
	
	<h4>Votre accréditation</h4>
	
	<div class="form-group">
		<label>
			PCE
		</label>
		<g:textField name="jsonConfig.pce" value="${ notificationAccount?.jsonConfig?.pce }" class="form-control"/>
	</div>
	
	
	<div class="form-group">
		<label>
			Rôle
		</label>
		<g:textField name="jsonConfig.role" value="${ notificationAccount?.jsonConfig?.role }" class="form-control"/>
	</div>
	
	<div class="form-group">
		<label>
			Code postal
		</label>
		<g:textField name="jsonConfig.codePostal" value="${ notificationAccount?.jsonConfig?.codePostal }" class="form-control"/>
	</div>
	
	<div class="form-group">
		<label>
			Type titulaire
		</label>
		<g:textField name="jsonConfig.titulaireType" value="${ notificationAccount?.jsonConfig?.titulaireType }" class="form-control"/>
	</div>
	
	<div class="form-group">
		<label>
			Titulaire
		</label>
		<g:textField name="jsonConfig.titulaireValeur" value="${ notificationAccount?.jsonConfig?.titulaireValeur }" class="form-control"/>
	</div>
	
	<div class="form-group">
		<label>
			Email
		</label>
		<g:textField name="jsonConfig.emailTitulaire" value="${ notificationAccount?.jsonConfig?.emailTitulaire }" class="form-control"/>
	</div>
	
	<div class="form-group">
		<label>
			Last consommationInformative
		</label>
		<g:textField name="jsonConfig.last_consommationInformative" value="${ notificationAccount?.jsonConfig?.last_consommationInformative }" class="form-control"/>
		<g:if test="${ notificationAccount?.jsonConfig?.last_consommationInformative }">
			<small class="form-text text-muted">ie : <g:formatDate date="${ new Date(notificationAccount?.jsonConfig?.last_consommationInformative as Long) }" format="dd/MM/yyyy HH:mm"/></small>
		</g:if>
	</div>
	
	<h4 class="mt-4">Tester l'API</h4>
	
	<div class="btn-group mb-4">
		<g:link class="btn btn-light" action="token" controller="adict" target="adict">token</g:link>
		<g:link class="btn btn-light" action="consommationInformative" controller="adict" target="adict">consommationInformative</g:link>
	</div>
	
</g:if>
<g:else>
	<h4>GRDF ADICT</h4>

	<h5>Enedis gère le réseau d’électricité jusqu’au compteur d’électricité.
	Pour consulter vos consommations, autorisez Enedis à nous transmettre vos données Linky.</h5>
	
	<div class="row">
		<div class="col">
			<p>En cliquant sur ce bouton, vous allez accéder à votre compte personnel
			Enedis où vous pourrez donner votre accord pour qu’Enedis nous transmette vos données.</p>
		</div>
		<div class="col">
			<g:link action="authorize" controller="dataConnect" target="dataconnect">
				<asset:image src="vert-enedis.png" width="200px"/>
			</g:link>
		</div>
	</div>
	
	<h6>Pour donner votre autorisation, vous devez créer un compte personnel Enedis. Il vous permet également de suivre et gérer vos données de consommation d’électricité.
	Munissez-vous de votre facture d’électricité pour créer votre espace.</h6>
	
	<h6>Enedis est le gestionnaire du réseau public de distribution d’électricité sur 95% du territoire français continental.</h6>
</g:else>


