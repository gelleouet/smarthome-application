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
	            <td>${ index.name }</td>
	            <td>${ index.value }</td>
	        </tr>
        </g:each>
    </tbody>
</app:datatable>