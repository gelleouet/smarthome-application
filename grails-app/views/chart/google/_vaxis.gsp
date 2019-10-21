<g:each var="axis" in="${ chart.vAxis }" status="status">
	${ status }: {title: '${ axis.title }', maxValue: '${ axis.maxValue ?: 'automatic' }',
		minValue: '${ axis.minValue ?: 'automatic' }'},
</g:each>