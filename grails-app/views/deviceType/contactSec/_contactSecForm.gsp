<g:set var="labelon" value="${ device?.metadata('labelon') }"/>

<g:if test="${ labelon?.id }">
	<g:hiddenField name="metadatas[0].id" value="${ labelon.id }"/>
</g:if>

<g:hiddenField name="metadatas[0].name" value="labelon"/>

<div class="field-group">
	<label title="API : device.metadata('labelon')?.value">
		Libellé ON
	</label>
	<g:field type="text" name="metadatas[0].value" value="${labelon?.value}" class="text medium-field"/>
</div>



<g:set var="labeloff" value="${ device?.metadata('labeloff') }"/>

<g:if test="${ labeloff?.id }">
	<g:hiddenField name="metadatas[1].id" value="${ labeloff.id }"/>
</g:if>

<g:hiddenField name="metadatas[1].name" value="labeloff"/>

<div class="field-group">
	<label title="API : device.metadata('labeloff')?.value">
		Libellé OFF
	</label>
	<g:field type="text" name="metadatas[1].value" value="${labeloff?.value}" class="text medium-field"/>
</div>


<g:set var="labelprior" value="${ device?.metadata('labelprior') }"/>

<g:if test="${ labelprior?.id }">
	<g:hiddenField name="metadatas[2].id" value="${ labelprior.id }"/>
</g:if>

<g:hiddenField name="metadatas[2].name" value="labelprior"/>

<div class="field-group">
	<label title="API : device.metadata('labelprior')?.value">
		Libellé surbrillance
	</label>
	<g:radioGroup values="['on', 'off']" labels="['on', 'off']" name="metadatas[2].value" value="${ labelprior?.value ?: 'on' }" class="radio">
		${ it.radio } ${ it.label }
	</g:radioGroup>
	<div class="description">Utile pour inverser la surbrillance sur l'état OFF pour un contact inversé</div>
</div>
