<%@ page import="smarthome.automation.DeviceTypeProvider" %>


<div class="field-group">
	<label for="libelle">
		Libellé
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="libelle" required="true" value="${deviceTypeProvider?.libelle}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="deviceType">
		Type
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:select id="deviceType" name="deviceType.id" from="${ deviceTypes }" optionKey="id" optionValue="libelle" required="true" value="${deviceTypeProvider?.deviceType?.id}" class="select"/>
</div>

