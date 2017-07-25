<g:if test="${ alert?.isViewed() }">
	<span class="aui-lozenge aui-lozenge-current">${ alert.status }</span>
</g:if>
<g:elseif test="${ alert?.isOpen() }">
	<span class="aui-lozenge aui-lozenge-error">${ alert.status }</span>
</g:elseif>
<g:else>
	<span class="aui-lozenge">${ alert.status }</span>
</g:else>