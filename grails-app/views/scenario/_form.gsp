<%@ page import="smarthome.automation.Scenario" %>


<div class="form-group required">
	<label for="label">Nom</label>
	<g:textField name="label" required="true" value="${scenario?.label}" class="form-control"/>
</div>


<div class="form-group">
	<label for="description">
		Description
	</label>
	<g:textField name="description" value="${scenario?.label}" class="form-control"/>
</div>

<div class="form-group required">
	<label for="script">Script</label>
	<g:textArea name="script" required="true" value="${scenario?.script}" class="script form-control"/>
	<small class="text-muted">Script Groovy</small>
</div>


