<nav id="sidebar" class="sidebar">
	<div class="sidebar-content">
		<g:link class="sidebar-brand" uri="/">
			<asset:image src="granddefi/bonhomme.png" height="25px"/>
			<!-- <i class="align-middle" data-feather="home"></i> -->
			<span class="align-middle"><g:meta name="app.code"/></span>
		</g:link>
		
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
		</ul>
	</div>
</nav>


