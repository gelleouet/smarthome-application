<g:each var="axis" in="${ chart.vAxis }" status="status">
	${ status }: {title: '${ axis.title }', maxValue: '${ axis.maxValue != null ? axis.maxValue : 'automatic' }',
		minValue: '${ axis.minValue != null ? axis.minValue : 'automatic' }'},
</g:each>