<g:if test="${flash.info}">
	<g:render template="/templates/messageInfo" model="[message: flash.info]"/>
</g:if>

<g:if test="${flash.message}">
	<g:render template="/templates/messageWarning" model="[message: flash.message]"/>
	<br/>
</g:if>

<g:if test="${flash.error || error || exception}">
	<g:render template="/templates/messageError" model="[title: flash.error ?: (error ?: 'Erreur !'), message: exception?.message, errors: flash.errors]"/>
</g:if>

<span id="ajaxError"></span>


