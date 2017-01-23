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
			optionValue="label" class="select" noSelection="[null: '']"/>
		<div class="description">Utilisé pour le calcul du classement énergétique</div>
	</div>
</fieldset>

