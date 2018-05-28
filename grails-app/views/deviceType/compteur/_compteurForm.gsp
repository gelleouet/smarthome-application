<g:set var="fournisseur" value="${ device?.metadata('fournisseur') }"/>

<g:if test="${ fournisseur?.id }">
	<g:hiddenField name="metadatas[0].id" value="${ fournisseur.id }"/>
</g:if>

<g:hiddenField name="metadatas[0].name" value="fournisseur"/>

<div class="field-group">
	<label title="API : device.metadata('fournisseur')?.value">
		Fournisseur
	</label>
	<g:select name="metadatas[0].value" value="${ fournisseur?.value }" class="select" optionKey="libelle" optionValue="libelle"
		from="${smarthome.automation.DeviceTypeProvider.findAllByDeviceType(device?.deviceType)}"
		noSelection="['': '']"/>
</div>


<g:set var="unite" value="${ device?.metadata('unite') }"/>

<g:if test="${ unite?.id }">
	<g:hiddenField name="metadatas[1].id" value="${ unite.id }"/>
</g:if>

<g:hiddenField name="metadatas[1].name" value="unite"/>

<div class="field-group">
	<label title="API : device.metadata('unite')?.value">
		Unité
	</label>
	<g:field type="text" name="metadatas[1].value" value="${unite?.value}" class="text medium-field"/>
</div>


<g:set var="coefConversion" value="${ device?.metadata('coefConversion') }"/>

<g:if test="${ coefConversion?.id }">
	<g:hiddenField name="metadatas[2].id" value="${ coefConversion.id }"/>
</g:if>

<g:hiddenField name="metadatas[2].name" value="coefConversion"/>

<div class="field-group">
	<label title="API : device.metadata('coefConversion')?.value">
		Coefficient de conversion
	</label>
	<g:field type="number decimal" name="metadatas[2].value" value="${coefConversion?.value}" class="text medium-field"/>
	<div class="description">Ce coefficient sera appliqué sur la consommation intermédiaire et non pas sur l'index total qui lui ne change pas</div>
</div>