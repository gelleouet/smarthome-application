<div>
	<g:form controller="device">
		<g:hiddenField name="actionName" value="send"/>
		<g:hiddenField name="id" value="${ device.id }"/>
		
		<g:textField name="value" class="text short-field" placeholder="Valeur"/>
		<g:textField name="command" class="text short-field" placeholder="Commande"/>
		<g:actionSubmit value="Envoyer" class="aui-button confirm-button" action="invokeAction"/>
		
		<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
	</g:form>
</div>