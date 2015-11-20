<%@ page import="smarthome.automation.Workflow" %>



<div class="field-group">
	<label for="label">
		Nom
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="label" required="true" value="${workflow?.label}" class="text long-field"/>
</div>


<div class="field-group">
	<label for="description">
		Description
	</label>
	<g:textField name="description" value="${workflow?.label}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="script">
		Script
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textArea name="script" required="true" value="${workflow?.script}" class="script textarea text long-field"/>
	<div class="description">Script Groovy</div>
</div>


