<fieldset>
	<div class="field-group">
		<label>Service<span class="aui-icon icon-required">*</span></label>
		<g:select name="notificationAccountSender.id" value="${ notificationAccount?.notificationAccountSender?.id }" from="${ notificationSenders }" class="select"
			optionValue="libelle" optionKey="id" required="true" noSelection="[null: 'Sélectionner un service']"
			onChange="onNotificationAccountSenderChange(this)"
			data-url="${ g.createLink(action: 'formTemplateNotificationSender', controller: 'notificationAccountSender') }" data-immediate="true"/>
	</div>
</fieldset>

<div id="ajaxNotificationSenderForm">
	<g:if test="${ notificationAccount?.id }">
		<g:include action="formTemplateNotificationSender" controller="notificationAccountSender" id="${ notificationAccount.id }" />
	</g:if>
</div>

