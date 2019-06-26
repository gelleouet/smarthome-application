<br/>

<g:if test="${ notificationAccount?.jsonConfig?.usage_point_id }">
	
	<h4>Vos jetons d'accès</h4>
	
	<div class="field-group">
		<label>
			Usage Point ID
		</label>
		<g:textField name="jsonConfig.usage_point_id" value="${ notificationAccount?.jsonConfig?.usage_point_id }" class="text long-field"/>
	</div>
	
	
	<div class="field-group">
		<label>
			Access Token
		</label>
		<g:textField name="jsonConfig.access_token" value="${ notificationAccount?.jsonConfig?.access_token }" class="text long-field"/>
	</div>
	
	<div class="field-group">
		<label>
			Refresh Token
		</label>
		<g:textField name="jsonConfig.refresh_token" value="${ notificationAccount?.jsonConfig?.refresh_token }" class="text long-field"/>
	</div>
	
	<div class="field-group">
		<label>
			Device ID
		</label>
		<g:textField name="jsonConfig.device_id" value="${ notificationAccount?.jsonConfig?.device_id }" class="text long-field"/>
	</div>
	
	<h4>Tester les APIs</h4>
	
	<br/>
	
	<div class="aui-buttons">
		<g:link class="aui-button" action="consumption_load_curve" controller="dataConnect" target="dataconnect">consumption_load_curve</g:link>
	</div>
	
</g:if>
<g:else>
	<h3>DataConnect Enedis</h3>

	<h5>Enedis gère le réseau d’électricité jusqu’au compteur d’électricité.
	Pour consulter vos consommations, autorisez Enedis à nous transmettre vos données Linky.</h5>
	
	<div class="aui-group aui-group-split">
		<div class="aui-item">
			<p>En cliquant sur ce bouton, vous allez accéder à votre compte personnel
			Enedis où vous pourrez donner votre accord pour qu’Enedis nous transmette vos données.</p>
		</div>
		<div class="aui-item">
			<g:link action="authorize" controller="dataConnect" target="dataconnect">
				<asset:image src="vert-enedis.png" width="200px"/>
			</g:link>
		</div>
	</div>
	
	<h6>Pour donner votre autorisation, vous devez créer un compte personnel Enedis. Il vous permet également de suivre et gérer vos données de consommation d’électricité.
	Munissez-vous de votre facture d’électricité pour créer votre espace.</h6>
	
	<h6>Enedis est le gestionnaire du réseau public de distribution d’électricité sur 95% du territoire français continental.</h6>
</g:else>


