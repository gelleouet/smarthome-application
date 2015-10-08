<%@ page import="smarthome.automation.Workflow" %>



<div class="field-group">
	<label for="label">
		Label
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="label" required="" value="${workflowInstance?.label}"class="text medium-field"/>

</div>

<div class="field-group">
	<label for="script">
		Script
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="script" required="" value="${workflowInstance?.script}"class="text medium-field"/>

</div>

<div class="field-group">
	<label for="user">
		User
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:select id="user" name="user.id" from="${smarthome.security.User.list()}" optionKey="id" required="" value="${workflowInstance?.user?.id}" class="select"/>

</div>

