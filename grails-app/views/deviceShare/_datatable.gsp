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
	            <td>
	            	<g:if test="${ !bean.sharedUser }">
	            		Tous mes amis
	            	</g:if>
	            	<g:else>
						${ bean.sharedUser.prenomNom }	            	
	            	</g:else>
	            </td>
	            <td class="column-1-buttons command-column">
	            	<a onclick="deleteDeviceShare(this)" data-url="${ g.createLink(controller: 'deviceShare', action: 'delete', id: bean.id) }" class="aui-button aui-button-subtle" title="Supprimer">
	            		<span class="aui-icon aui-icon-small aui-iconfont-delete">
	            	</a>
	            </td>
	        </tr>
        </g:each>
    </tbody>
</app:datatable>