<g:if test="${ alert?.isViewed() }">
	<g:link controller="deviceAlert" action="deviceAlerts" id="${ alert.id }">
		<span class="aui-lozenge aui-lozenge-current">${ alert.level }</span>
	</g:link>
</g:if>
<g:elseif test="${ alert?.isOpen() }">
	<g:link controller="deviceAlert" action="deviceAlerts" id="${ alert.id }">
		<span class="aui-lozenge aui-lozenge-error">${ alert.level }</span>
	</g:link>
</g:elseif>
