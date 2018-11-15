<g:if test="${info}">
	<g:render template="/templates/messageInfo" model="[message: info]"/>
</g:if>

<g:if test="${message}">
	<g:render template="/templates/messageWarning" model="[message: message]"/>
	<br/>
</g:if>

<g:if test="${error || exception}">
	<g:render template="/templates/messageError" model="[title: error ?: 'Erreur !', errors: errors ?: exception?.errors]"/>
</g:if>

<span id="ajaxError"></span>


