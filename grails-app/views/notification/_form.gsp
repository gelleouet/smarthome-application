<%@ page import="smarthome.automation.Notification" %>

<div class="field-group">
	<label for="notificationAccount">
		Service
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:select id="notificationAccount" name="notificationAccount.id" from="${ notificationAccounts }" optionKey="id" required="true"
		value="${notification?.notificationAccount?.id}" class="select" optionValue="${ {it.notificationAccountSender.libelle} }"
		noSelection="[null: 'Sélectionner un service']"
		onChange="onNotificationSenderChange(this)"
		data-url="${ g.createLink(action: 'formSenderParameter', controller: 'notificationAccountSender') }" data-immediate="true"/>
</div>

<div class="field-group">
	<label for="description">
		Description
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="description" required="true" value="${notification?.description}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="message">
		Message
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textArea name="message" required="true" value="${notification?.message}" class="text textarea long-field script" style="height:200px;"/>
	<div class="description">Script Groovy renvoyant un String. Variables disponibles : device, event, alert</div>
</div>

<div id="ajaxNotificationParameters">
	<g:if test="${ notification?.id }">
		<g:include action="formSenderParameter" controller="notificationAccountSender" id="${ notification.id }" />
	</g:if>
</div>


