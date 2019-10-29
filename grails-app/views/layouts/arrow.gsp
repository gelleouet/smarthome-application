<g:if test="${ value?.toString()?.isDouble() }">
	<g:if test="${ value > reference }">
		<app:icon name="arrow-up-right"/> +<g:formatNumber number="${ value }" format="###,###,###,###.##"/>
	</g:if>
	<g:elseif test="${ value < reference }">
		<app:icon name="arrow-down-right"/> <g:formatNumber number="${ value }" format="###,###,###,###.##"/>
	</g:elseif>
	<g:else>
		<g:formatNumber number="${ value }" format="###,###,###,###.##"/>
	</g:else>
</g:if>
<g:else>
	${ value }
</g:else>

<g:layoutBody/>