<%@ page import="grails.plugin.springsecurity.SpringSecurityUtils" %>

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
	            <td>
	            	<g:if test="${ SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN') }">
	            		<a id="show-compteur-index-dialog-button" data-url="${ g.createLink(action: 'dialogIndex', id: index.id) }">
							${ raw(deviceImpl.formatHtmlIndex(index.value)) }	            		
	            		</a>
	            	</g:if>
	            	<g:else>
	            		${ raw(deviceImpl.formatHtmlIndex(index.value)) }
	            	</g:else>
				</td>
	        </tr>
        </g:each>
    </tbody>
</app:datatable>