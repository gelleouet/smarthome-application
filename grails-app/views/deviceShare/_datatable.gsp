<app:datatable datatableId="shareDatatable" recordsTotal="${ shares.size() }" noPaginate="true">
    <thead>
        <tr>
            <th>Partages</th>
            <th class="column-1-buttons"></th>
        </tr>
    </thead>
    <tbody>
    	<g:each var="bean" in="${ shares }">
	        <tr>
	            <td>${ bean.libelle }</td>
	            <td class="column-1-buttons command-column">
	            	<g:link class="aui-button aui-button-subtle confirm-button" title="Suppimer" action="delete" id="${ bean.id }">
	            		<span class="aui-icon aui-icon-small aui-iconfont-delete">
	            	</g:link>
	            </td>
	        </tr>
        </g:each>
    </tbody>
</app:datatable>