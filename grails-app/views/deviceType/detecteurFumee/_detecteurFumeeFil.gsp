<g:if test="${ !value.name }">
	<g:if test="${ value.value == 1 }">
		<span class="aui-lozenge aui-lozenge-error">fumée detectée</span>
	</g:if>
	<g:elseif test="${ value.value == 0 }">
		<span class="label">Fin détection fumée</span>
	</g:elseif>
</g:if>
<g:else>
	<span class="label">Valeur : ${ value.value }</span>
</g:else>