<fieldset>
<div class="field-group">
	<label for="ruleName">
		Classe
		<span class="aui-icon icon-required"> required</span>
	</label>
	<g:textField name="ruleName" value="${scriptRule?.ruleName}" class="text long-field" required="true" autofocus="true"/>
</div>

<div class="field-group">
	<label for="description">
		Description
		<span class="aui-icon icon-required"> required</span>
	</label>
	<g:textField name="description" value="${scriptRule?.description}" class="text long-field" required="true"/>
</div>

<div class="field-group">
	<label for="script">
		Script
		<span class="aui-icon icon-required"> required</span>
	</label>
	<g:textArea name="script" value="${scriptRule?.script}" class="script textarea text long-field" required="true"/>
</div>
</fieldset>

