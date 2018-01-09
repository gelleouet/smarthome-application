<g:set var="timeout" value="${ device?.metadata('timeout') }"/>

<g:if test="${ timeout?.id }">
	<g:hiddenField name="metadatas[0].id" value="${ timeout.id }"/>
</g:if>

<g:hiddenField name="metadatas[0].name" value="timeout"/>

<div class="field-group">
	<label title="API : device.metadata('timeout')?.value">
		Timeout bouton poussoir (en millisecondes)
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:field type="number" name="metadatas[0].value" required="true" value="${timeout?.value}" class="text medium-field"/>
</div>


<g:set var="defaulttimer" value="${ device?.metadata('defaulttimer') }"/>

<g:if test="${ defaulttimer?.id }">
	<g:hiddenField name="metadatas[1].id" value="${ defaulttimer.id }"/>
</g:if>

<g:hiddenField name="metadatas[1].name" value="defaulttimer"/>

<div class="field-group">
	<label title="API : device.metadata('defaulttimer')?.value">
		Minuterie par défaut (en minutes)
	</label>
	<g:field type="text" name="metadatas[1].value" value="${defaulttimer?.value}" class="text medium-field"/>
</div>


<h4>Configuration périphérique</h4>
<g:render template="/deviceType/generic/metadatasForm" 
	model="[device: device, exclude: ['timeout', 'defaulttimer'], startStatus: 2, commitButton: true]"/>