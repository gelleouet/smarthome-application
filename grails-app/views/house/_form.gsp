<g:if test="${ house?.id }">
	<g:hiddenField name="house.id" value="${house.id}" />
</g:if>

<fieldset>
	<div class="field-group">
		<label>Ville</label>
		<g:textField name="house.location" value="${house?.location}" class="text long-field" />
		<div class="description">Utilisé pour les prévisions météo sous la forme "ville et/ou code postal, pays". Ex : Lorient, France. Calculé tous les jours à minuit.
			<sec:ifAllGranted roles="ROLE_ADMIN">
				<g:remoteLink action="calculWeather" controller="houseWeather" id="${ house.id }" onSuccess="alert('Calcul terminé')" onFailure="alert('Erreur calcul')">Calcul manuel</g:remoteLink>
			</sec:ifAllGranted>
		</div>
	</div>
	<div class="field-group">
		<label>Surface (en m²)</label>
		<g:field name="house.surface" type="number" value="${house?.surface}" class="text medium-field" />
		<div class="description">Utilisé pour le calcul du classement énergétique</div>
	</div>
	<div class="field-group">
		<label>Chauffage</label>
		<g:select name="house.chauffage.id" value="${house?.chauffage?.id}" from="${ smarthome.automation.Chauffage.list() }" optionKey="id" 
			optionValue="libelle" class="select combobox" noSelection="[null: ' ']"/>
	</div>
	<div class="field-group">
		<label>Compteur gaz</label>
		<g:select name="house.compteurGaz.id" value="${house?.compteurGaz?.id}" from="${ compteursGaz }" optionKey="id" 
			optionValue="label" class="select combobox" noSelection="[null: '']"/>
	</div>
	<div class="field-group">
		<label>Compteur électrique</label>
		<g:select name="house.compteur.id" value="${house?.compteur?.id}" from="${ compteurs }" optionKey="id" 
			optionValue="label" class="select combobox" noSelection="[null: '']"/>
		<div class="description">Utilisé pour le calcul du classement énergétique et la synthèse générale<br/>
		Vos consommations sont calculées tous les soirs à minuit.
		<g:if test="${ house?.id }">
			Vous pouvez lancer un <g:remoteLink action="calculConso" controller="house" id="${ house.id }" onComplete="alert('Calcul terminé')">calcul manuel</g:remoteLink>
		</g:if>
		</div>
	</div>
	<div class="field-group">
		<label>Température principale</label>
		<g:select name="house.temperature.id" value="${house?.temperature?.id}" from="${ temperatures }" optionKey="id" 
			optionValue="label" class="select combobox" noSelection="[null: ' ']"/>
		<div class="description">Utilisé pour la synthèse générale</div>
	</div>
	<div class="field-group">
		<label>Humidité principale</label>
		<g:select name="house.humidite.id" value="${house?.humidite?.id}" from="${ humidites }" optionKey="id" 
			optionValue="label" class="select combobox" noSelection="[null: ' ']"/>
		<div class="description">Utilisé pour la synthèse générale</div>
	</div>
</fieldset>


