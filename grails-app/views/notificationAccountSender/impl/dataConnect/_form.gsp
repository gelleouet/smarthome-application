<br/>

<g:if test="${ notificationAccount?.jsonConfig?.usage_point_id }">
	
	<h4>Vos jetons d'accès</h4>
	
	<div class="form-group">
		<label>
			Usage Point ID
		</label>
		<g:textField name="jsonConfig.usage_point_id" value="${ notificationAccount?.jsonConfig?.usage_point_id }" class="form-control"/>
	</div>
	
	
	<div class="form-group">
		<label>
			Access Token
		</label>
		<g:textField name="jsonConfig.access_token" value="${ notificationAccount?.jsonConfig?.access_token }" class="form-control"/>
	</div>
	
	<div class="form-group">
		<label>
			Refresh Token
		</label>
		<g:textField name="jsonConfig.refresh_token" value="${ notificationAccount?.jsonConfig?.refresh_token }" class="form-control"/>
	</div>
	
	<div class="form-group">
		<label>
			Last token
		</label>
		<g:textField name="jsonConfig.last_token" value="${ notificationAccount?.jsonConfig?.last_token }" class="form-control"/>
		<g:if test="${ notificationAccount?.jsonConfig?.last_token }">
			<small class="form-text text-muted">ie : <g:formatDate date="${ new Date(notificationAccount?.jsonConfig?.last_token as Long) }" format="dd/MM/yyyy HH:mm"/></small>
		</g:if>
	</div>
	
	<div class="form-group">
		<label>
			Last consumption_load_curve
		</label>
		<g:textField name="jsonConfig.last_consumption_load_curve" value="${ notificationAccount?.jsonConfig?.last_consumption_load_curve }" class="form-control"/>
		<g:if test="${ notificationAccount?.jsonConfig?.last_consumption_load_curve }">
			<small class="form-text text-muted">ie : <g:formatDate date="${ new Date(notificationAccount?.jsonConfig?.last_consumption_load_curve as Long) }" format="dd/MM/yyyy HH:mm"/></small>
		</g:if>
	</div>
	
	<div class="form-group">
		<label>
			Last daily_consumption
		</label>
		<g:textField name="jsonConfig.last_daily_consumption" value="${ notificationAccount?.jsonConfig?.last_daily_consumption }" class="form-control"/>
		<g:if test="${ notificationAccount?.jsonConfig?.last_daily_consumption }">
			<small class="form-text text-muted">ie : <g:formatDate date="${ new Date(notificationAccount?.jsonConfig?.last_daily_consumption as Long) }" format="dd/MM/yyyy"/></small>
		</g:if>
	</div>
	
	<div class="form-group">
		<label>
			Last consumption_max_power
		</label>
		<g:textField name="jsonConfig.last_consumption_max_power" value="${ notificationAccount?.jsonConfig?.last_consumption_max_power }" class="form-control"/>
		<g:if test="${ notificationAccount?.jsonConfig?.last_consumption_max_power }">
			<small class="form-text text-muted">ie : <g:formatDate date="${ new Date(notificationAccount?.jsonConfig?.last_consumption_max_power as Long) }" format="dd/MM/yyyy"/></small>
		</g:if>
	</div>
	
	<h4 class="mt-4">Tester les APIs</h4>
	
	<div class="btn-group mb-4">
		<g:link class="btn btn-light" action="authorize" controller="dataConnect" target="dataconnect">authorize</g:link>
		<g:link class="btn btn-light" action="refresh_token" controller="dataConnect" target="dataconnect">refresh_token</g:link>
		<g:link class="btn btn-light" action="consumption_load_curve" controller="dataConnect" target="dataconnect">consumption_load_curve</g:link>
		<g:link class="btn btn-light" action="daily_consumption" controller="dataConnect" target="dataconnect">daily_consumption</g:link>
		<g:link class="btn btn-light" action="consumption_max_power" controller="dataConnect" target="dataconnect">consumption_max_power</g:link>
	</div>
	
</g:if>
<g:else>
	<h4>Enedis DataConnect</h4>

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


