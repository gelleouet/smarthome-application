<div class="form-group required">
	<label for="ruleName">Classe</label>
	<g:textField name="ruleName" value="${scriptRule?.ruleName}" class="form-control" required="true" autofocus="true"/>
</div>

<div class="form-group required">
	<label for="description">Description</label>
	<g:textField name="description" value="${scriptRule?.description}" class="form-control" required="true"/>
</div>

<div class="form-group required">
	<label for="script">Script</label>
	<g:textArea name="script" value="${scriptRule?.script}" class="script form-control" required="true"/>
</div>

