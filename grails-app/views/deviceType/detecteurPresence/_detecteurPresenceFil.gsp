<g:if test="${ !value.name }">
	<g:if test="${ value.value == 1 }">
		<span class="aui-lozenge aui-lozenge-complete">mouvement detecté</span>
	</g:if>
	<g:elseif test="${ value.value == 0 }">
		<span class="label">Fin détection mouvement</span>
	</g:elseif>
</g:if>
<g:else>
	<span class="label">Valeur : ${ value.value }</span>
</g:else>
