<%@ page import="smarthome.automation.NotificationAccountSender" %>


<div class="field-group">
	<label for="libelle">
		Libellé
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="libelle" required="true" value="${notificationAccountSender?.libelle}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="implClass">
		Implémentation
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="implClass" required="true" value="${notificationAccountSender?.implClass}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="implClass">
		Rôle
	</label>
	<g:textField name="role" value="${notificationAccountSender?.role}" class="text medium-field"/>
</div>

