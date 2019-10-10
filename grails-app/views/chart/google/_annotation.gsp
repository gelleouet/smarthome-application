<g:if test="${ chart.series.find{ it.annotation } }">

chartDatas = new google.visualization.DataView(chartDatas)

chartDatas.setColumns([0,
	<g:each var="serie" in="${ chart.series }" status="status">
		${status+1},
		<g:if test="${ serie.annotation }">
		{ calc: "stringify",
        sourceColumn: ${status+1},
        type: "string",
        role: "annotation" },
        </g:if>
    </g:each>
])

</g:if>