<g:if test="${ value?.toString()?.isDouble() }">
	<g:if test="${ value > reference }">
		<app:icon name="arrow-up-right"/> +${ value }
	</g:if>
	<g:elseif test="${ value < reference }">
		<app:icon name="arrow-down-right"/> ${ value }
	</g:elseif>
	<g:else>
		${ value }
	</g:else>
</g:if>
<g:else>
	${ value }
</g:else>

<g:layoutBody/>