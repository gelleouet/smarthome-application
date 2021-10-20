<g:if test="${ house?.id }">
	<g:hiddenField name="house.id" value="${house.id}" />
</g:if>

<div class="form-group required">
	<label>Numéro et rue</label>
 	<g:field class="form-control form-control-lg" type="text" name="house.adresse" required="true" value="${ house?.adresse }"/>
</div>

<div class="form-group required">
	<label>Code postal</label>
 	<g:field class="form-control form-control-lg" type="text" name="house.codePostal" required="true" value="${ house?.codePostal }" maxlength="16"/>
</div>
<div class="form-group required">
	<label>Commune</label>
	<g:select name="house.location" value="${ house?.location }" class="form-control form-control-lg combobox" required="true" 
		from="${ smarthome.common.Commune.list() }" optionValue="libelle" optionKey="libelle"/>
</div>
<div class="form-group required">
	<label>Nombre de personnes dans le foyer</label>
	<g:field name="house.nbPersonne" type="number" value="${house?.nbPersonne}" required="true" class="form-control" />
</div>
<div class="form-group">
	<label>Surface (en m²)</label>
	<g:field name="house.surface" type="number" value="${house?.surface}" class="form-control" />
</div>
<div class="form-group required">
	<label>Energie principale de chauffage</label>
	<g:select name="house.chauffage.id" value="${house?.chauffage?.id}" from="${ smarthome.automation.Chauffage.list() }" optionKey="id" 
		optionValue="libelle" class="form-control combobox" noSelection="['': ' ']" required="true"/>
</div>
<div class="form-group">
	<label>Energie secondaire de chauffage</label>
	<g:select name="house.chauffageSecondaire.id" value="${house?.chauffageSecondaire?.id}" from="${ smarthome.automation.Chauffage.list() }" optionKey="id" 
		optionValue="libelle" class="form-control combobox" noSelection="['': ' ']"/>
</div>
<div class="form-group required">
	<label>Eau chaude sanitaire (ECS)</label>
	<g:select name="house.ecs.id" value="${house?.ecs?.id}" from="${ smarthome.automation.ECS.list() }" required="true" optionKey="id" 
		optionValue="libelle" class="form-control combobox" noSelection="['': ' ']"/>
</div>


