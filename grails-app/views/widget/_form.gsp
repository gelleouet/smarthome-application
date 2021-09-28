<%@ page import="smarthome.core.Widget" %>

<div class="field-group">
	<label for="libelle">
		Libellé
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="libelle" required="true" value="${widget?.libelle}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="description">
		Description
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="description" required="true" value="${widget?.description}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="controllerName">
		Controlleur
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="controllerName" required="true" value="${widget?.controllerName}" class="text medium-field"/>

</div>

<div class="field-group">
	<label for="actionName">
		Action
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="actionName" required="true" value="${widget?.actionName}" class="text medium-field"/>

</div>


<div class="field-group">
	<label for="refreshPeriod">
		Rafraichissement auto
	</label>
	<g:field name="refreshPeriod" type="number" value="${widget.refreshPeriod}" class="text short-field"/>
	<div class="description">En minute</div>
</div>


<div class="field-group">
	<label for="configName">
		Action configuration
	</label>
	<g:textField name="configName" value="${widget?.configName}" class="text medium-field"/>
</div>

<div class="field-group">
	<label for="contentView">
		Vue spécifique
	</label>
	<g:textField name="contentView" value="${widget?.contentView}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="style">
		Style HTML
	</label>
	<g:textField name="style" value="${widget?.style}" class="text long-field"/>
	<div class="description">Ex : height:400px;width:300px</div>
</div>





