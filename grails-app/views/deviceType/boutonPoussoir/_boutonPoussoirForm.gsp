<h4>Configuration</h4>

<g:set var="timeout" value="${ device?.metadata('timeout') }"/>

<g:if test="${ timeout?.id }">
	<g:hiddenField name="metadatas[0].id" value="${ timeout.id }"/>
</g:if>

<g:hiddenField name="metadatas[0].name" value="timeout"/>

<div class="field-group">
	<label>
		Timeout (en millisecondes)
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:field type="number" name="metadatas[0].value" required="true" value="${timeout?.value}" class="text medium-field"/>
</div>