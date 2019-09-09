<%@ page import="smarthome.automation.Notification" %>

<div class="form-group required">
	<label for="notificationAccount">Service</label>
	<g:select id="notificationAccount" name="notificationAccount.id" from="${ notificationAccounts }" optionKey="id" required="true"
		value="${notification?.notificationAccount?.id}" class="form-control" optionValue="${ {it.notificationAccountSender.libelle} }"
		noSelection="[null: 'Sélectionner un service']"
		onChange="onNotificationSenderChange(this)"
		data-url="${ g.createLink(action: 'formSenderParameter', controller: 'notificationAccountSender') }" data-immediate="true"/>
</div>

<div class="form-group required">
	<label for="description">Description</label>
	<g:textField name="description" required="true" value="${notification?.description}" class="form-control"/>
</div>

<div class="form-group required">
	<label for="message">Message</label>
	<g:textArea name="message" required="true" value="${notification?.message}" class="form-control script" style="height:200px;"/>
	<small class="form-text text-muted">Script Groovy renvoyant un String. Variables disponibles : device, event, alert</small>
</div>

<div id="ajaxNotificationParameters">
	<g:if test="${ notification?.id }">
		<g:include action="formSenderParameter" controller="notificationAccountSender" id="${ notification.id }" />
	</g:if>
</div>


