<g:if test="${ house?.id }">
	<g:hiddenField name="house.id" value="${house.id}" />
</g:if>

<fieldset>
	<div class="field-group">
		<label>Surface (en m²)</label>
		<g:field name="house.surface" type="number" value="${house?.surface}" class="text medium-field" />
		<div class="description">Utilisé pour le calcul du classement énergétique</div>
	</div>
	<div class="field-group">
		<label>Compteur principal</label>
		<g:select name="house.compteur.id" value="${house?.compteur?.id}" from="${ compteurs }" optionKey="id" 
			optionValue="label" class="select combobox" noSelection="[null: '']"/>
		<div class="description">Utilisé pour le calcul du classement énergétique et la synthèse générale</div>
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


