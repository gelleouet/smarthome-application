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


<g:set var="modele" value="${ device?.metadata('modele') }"/>

<g:if test="${ modele?.id }">
	<g:hiddenField name="metadatas[1].id" value="${ modele.id }"/>
</g:if>

<g:hiddenField name="metadatas[1].name" value="modele"/>

<div class="form-group">
	<label title="API : device.metadata('modele')?.value">
		Modèle
	</label>
	<g:select name="metadatas[1].value" value="${ modele?.value }" class="form-control"
		from="${['Electronique', 'Mécanique', 'Linky']}"
		noSelection="['': '']"/>
</div>


<g:set var="aggregate" value="${ device?.metadata('aggregate') }"/>

<g:if test="${ aggregate?.id }">
	<g:hiddenField name="metadatas[2].id" value="${ aggregate.id }"/>
</g:if>

<g:hiddenField name="metadatas[2].name" value="aggregate"/>

<div class="form-group">
	<label title="API : device.metadata('aggregate')?.value">
		Calcul des données aggrégées
	</label>
	<g:select name="metadatas[2].value" value="${ aggregate?.value }" class="form-control"
		from="${['diff-index', 'sum-conso']}"
		noSelection="['': ' ']"/>
</div>