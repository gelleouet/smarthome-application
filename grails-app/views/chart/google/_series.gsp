<g:each var="serie" in="${ series }" status="status">
	${status}: {
		<g:if test="${ serie.targetAxisIndex }">targetAxisIndex: ${ serie.targetAxisIndex },</g:if>
		<g:if test="${ serie.type }">type: '${ serie.type }',</g:if>
		<g:if test="${ !serie.pointsVisible }">pointsVisible: false,</g:if>
		<g:if test="${ serie.color }">color: '${ serie.color }',</g:if>
	},
</g:each>