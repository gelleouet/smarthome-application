<%@ page import="smarthome.automation.DeviceType" %>


<fieldset>
	<div class="field-group">
		<label for="libelle">
			Libellé
			<span class="aui-icon icon-required">*</span>
		</label>
		<g:textField name="libelle" required="true" value="${deviceType?.libelle}" class="text long-field"/>
	</div>
	
	<div class="field-group">
		<label for="libelle">
			Capteur
			<span class="aui-icon icon-required">*</span>
		</label>
		<g:checkBox name="capteur" required="true" value="${deviceType?.capteur}"/>
	</div>
</fieldset>

