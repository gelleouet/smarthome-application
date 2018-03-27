<%@ page import="smarthome.automation.DeviceType" %>


<div class="field-group">
	<label for="libelle">
		Libellé
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="libelle" required="true" value="${deviceType?.libelle}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="libelle">
		Implémentation
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="implClass" required="true" value="${deviceType?.implClass}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="actif">
		Qualitatif ?
	</label>
	<g:checkBox name="qualitatif" value="${deviceType?.qualitatif}" class="checkbox"/>
</div>

<div class="field-group">
	<label for="script">
		Configuration
	</label>
	<g:textArea name="configuration" value="${deviceTypeConfig?.data}" class="script textarea text long-field"/>
</div>
	

