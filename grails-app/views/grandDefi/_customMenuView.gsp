<%@ page import="smarthome.application.DefiCommand" %>

<g:set var="defiService" bean="defiService"/>


<g:each var="defi" in="${ defiService.listByAuthenticatedUser([max: 5]) }">
	<li class="sidebar-item">
		<g:link class="sidebar-link" action="mesresultats" controller="grandDefi" params="['defi.id': defi.id]">
			<app:icon name="award"/> ${ defi.libelle }
		</g:link>
	</li>
</g:each>


<li class="sidebar-item">
	<g:link class="sidebar-link" action="catalogue" controller="defi">
		<app:icon name="grid"/> Catalogue
	</g:link>
</li>