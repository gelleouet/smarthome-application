<g:if test="${ house?.id }">
	<g:hiddenField name="house.id" value="${house.id}" />
</g:if>

<div class="form-group">
	<label>Commune</label>
	<g:select class="form-control form-control-lg combobox" name="location" from="${ smarthome.common.Commune.list() }"
		optionValue="libelle" value="${ house?.location }"/>
</div>
<div class="form-group">
	<label>Surface (en m²)</label>
	<g:field name="house.surface" type="number" value="${house?.surface}" class="form-control" />
</div>
<div class="form-group">
	<label>Energie principale chauffage</label>
	<g:select name="house.chauffage.id" value="${house?.chauffage?.id}" from="${ smarthome.automation.Chauffage.list() }" optionKey="id" 
		optionValue="libelle" class="form-control combobox" noSelection="[null: ' ']"/>
</div>
<div class="form-group">
	<label>Eau chaude sanitaire (ECS)</label>
	<g:select name="house.ecs.id" value="${house?.ecs?.id}" from="${ smarthome.automation.ECS.list() }" optionKey="id" 
		optionValue="libelle" class="form-control combobox" noSelection="[null: ' ']"/>
</div>


