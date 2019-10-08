<%@ page import="smarthome.core.Config" %>


<div class="form-group required">
	<label>Name</label>
	<g:textField name="name" required="true" value="${config?.name}" class="form-control"/>
</div>

<div class="form-group required">
	<label>Value</label>
	<g:textField name="value" required="true" value="${config?.value}" class="form-control"/>
</div>

