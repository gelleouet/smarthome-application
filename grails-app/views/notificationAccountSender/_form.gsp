<%@ page import="smarthome.automation.NotificationAccountSender" %>


<div class="form-group required">
	<label for="libelle">Libellé</label>
	<g:textField name="libelle" required="true" value="${notificationAccountSender?.libelle}" class="form-control"/>
</div>

<div class="form-group required">
	<label for="implClass">Implémentation</label>
	<g:textField name="implClass" required="true" value="${notificationAccountSender?.implClass}" class="form-control"/>
</div>

<div class="form-group">
	<label for="role">Rôle</label>
	<g:textField name="role" value="${notificationAccountSender?.role}" class="form-control"/>
</div>

<div class="">
	<label for="cron">Cron</label>
	<g:textField name="cron" value="${notificationAccountSender?.cron}" class="form-control"/>
</div>

