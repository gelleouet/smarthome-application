chartDatas.setColumns([0,
	<g:each var="serie" in="${ series }" status="status">
		${status+1},
		<g:if test="${ serie.annotation }">
		{ calc: "stringify",
        sourceColumn: ${status+1},
        type: "string",
        role: "annotation" },
        </g:if>
    </g:each>
])