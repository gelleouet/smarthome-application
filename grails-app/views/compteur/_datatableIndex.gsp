<app:datatable datatableId="datatableIndex" recordsTotal="${ recordsTotal }">
    <thead>
        <tr>
            <th>Date</th>
            <th>Index</th>
            <th>Valeur</th>
        </tr>
    </thead>
    <tbody>
    	<g:each var="index" in="${ indexList }">
	        <tr>
	        	<td><app:formatUserDateTime date="${ index.dateValue }"/></td>
	            <td>${ index.name ?: 'base' }</td>
	            <td><g:formatNumber number="${ index.value }" format="### ### ### ### ##0"/></td>
	        </tr>
        </g:each>
    </tbody>
</app:datatable>