<g:set var="fournisseur" value="${ device?.metadata('fournisseur') }"/>

<g:if test="${ fournisseur?.id }">
	<g:hiddenField name="metadatas[0].id" value="${ fournisseur.id }"/>
</g:if>

<g:hiddenField name="metadatas[0].name" value="fournisseur"/>

<div class="form-group">
	<label title="API : device.metadata('fournisseur')?.value">
		Fournisseur
	</label>
	<g:select name="metadatas[0].value" value="${ fournisseur?.value }" class="form-control" optionKey="libelle" optionValue="libelle"
		from="${smarthome.automation.DeviceTypeProvider.findAllByDeviceType(device?.deviceType)}"
		noSelection="['': '']"/>
</div>



<g:set var="coefConversion" value="${ device?.metadata('coefConversion') }"/>

<g:if test="${ coefConversion?.id }">
	<g:hiddenField name="metadatas[1].id" value="${ coefConversion.id }"/>
</g:if>

<g:hiddenField name="metadatas[1].name" value="coefConversion"/>

<div class="form-group">
	<label title="API : device.metadata('coefConversion')?.value">
		Coefficient de conversion
	</label>
	<g:field type="number decimal" name="metadatas[1].value" value="${coefConversion?.value}" class="form-control"/>
	<small class="text-muted">Ce coefficient sera appliqué sur la consommation intermédiaire et non pas sur l'index total qui lui ne change pas</small>
</div>



<g:set var="modele" value="${ device?.metadata('modele') }"/>

<g:if test="${ modele?.id }">
	<g:hiddenField name="metadatas[2].id" value="${ modele.id }"/>
</g:if>

<g:hiddenField name="metadatas[2].name" value="modele"/>

<div class="form-group">
	<label title="API : device.metadata('modele')?.value">
		Modèle
	</label>
	<g:select name="metadatas[2].value" value="${ modele?.value }" class="form-control"
		from="${['Gaz', 'Gazpar']}"
		noSelection="['': '']"/>
</div>