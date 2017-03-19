<h4>Configuration bouton</h4>

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


<h4>Configuration périphérique</h4>
<g:render template="/deviceType/generic/metadatasForm" 
	model="[device: device, exclude: ['labelon', 'labeloff'], startStatus: 2, commitButton: true]"/>

