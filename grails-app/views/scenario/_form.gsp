<%@ page import="smarthome.automation.Scenario" %>



<div class="field-group">
	<label for="label">
		Nom
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="label" required="true" value="${scenario?.label}" class="text long-field"/>
</div>


<div class="field-group">
	<label for="description">
		Description
	</label>
	<g:textField name="description" value="${scenario?.label}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="script">
		Script
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textArea name="script" required="true" value="${scenario?.script}" class="script textarea text long-field"/>
	<div class="description">Script Groovy</div>
</div>


