<g:set var="items" value="${ app.navigationItems(category: 'navbarPrimary')?.subitems?.sort({ it.label }) }"/>

<ul class="sidebar-nav">
	<g:each var="header" in="${ items?.groupBy({ it.header}).sort({it.key}) }">
		<li class="sidebar-header">
			${header.key }
		</li>
		
		<g:each var="item" in="${ header.value.sort({it.label}) }">
			<li class="sidebar-item ${app.isCurrentItem(item: item) ? 'active' : '' }">
				<g:link class="sidebar-link" action="${ item.action }" controller="${ item.controller }">
					<g:if test="${ item.icon }">
						<app:icon name="${ item.icon }"/>
					</g:if>
					${item.label }
				</g:link>
			</li>
		</g:each>
	</g:each>
	<!-- 
	<li class="sidebar-header">
		Je surveille
	</li>
	
	<li class="sidebar-item">
		<g:link class="sidebar-link" action="consommationElec" controller="house"><i class="align-middle" data-feather="zap"></i> Consommations Ã©lectricitÃ©</g:link>
	</li>
	<li class="sidebar-item">
		<g:link class="sidebar-link"><i class="align-middle" data-feather="compass"></i> Consommations gaz</g:link>
	</li>
	<li class="sidebar-item">
		<g:link class="sidebar-link"><i class="align-middle" data-feather="droplet"></i> Consommations eau</g:link>
	</li>
	<li class="sidebar-item">
		<g:link class="sidebar-link" action="chartsGrid" controller="chart"><i class="align-middle" data-feather="bar-chart"></i> Graphiques personnalisÃ©s</g:link>
	</li>
	 -->
</ul>