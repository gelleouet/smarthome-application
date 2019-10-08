<g:each var="serie" in="${ chart.series }" status="status">
	${status}: {
		<g:if test="${ serie.targetAxisIndex || serie.axisIndex }">targetAxisIndex: ${ serie.targetAxisIndex ?: (serie.axisIndex ?: 0) },</g:if>
		<g:if test="${ serie.type }">type: '${ serie.type }',</g:if>
		<g:if test="${ !serie.pointsVisible }">pointsVisible: false,</g:if>
		<g:if test="${ serie.color }">color: '${ serie.color }',</g:if>
		lineDashStyle: ${ chart.lineDashStyle(serie) }
	},
</g:each>