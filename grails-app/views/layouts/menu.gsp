<nav id="sidebar" class="sidebar">
	<div class="sidebar-content">
		<g:link class="sidebar-brand" uri="/">
			<asset:image src="granddefi/bonhomme.png" height="25px"/>
			<span class="align-middle"><g:meta name="app.code"/></span>
		</g:link>
		
		<g:set var="items" value="${ app.navigationItems(category: 'navbarPrimary')?.subitems }"/>

		<ul class="sidebar-nav">
			<g:each var="menuGroup" in="${ items?.groupBy{ it.header ?: it.label }.sort{ it.key} }" status="menuGroupStatus">
			
				<g:if test="${ menuGroup.value.size() > 1 }">
					<li class="sidebar-item">
						<a href="#menuleft-${ menuGroupStatus }" data-toggle="collapse" class="sidebar-link" aria-expanded="true" role="button">
							<g:if test="${ menuGroup.value[0].iconHeader }">
								<app:icon name="${ menuGroup.value[0].iconHeader }" lib="${ menuGroup.value[0].iconLibHeader }"/>
							</g:if>
							<span class="align-middle">${ menuGroup.key }</span>
						</a>
						
						<ul id="menuleft-${ menuGroupStatus }" class="sidebar-dropdown list-unstyled collapse show">
							<g:each var="item" in="${ menuGroup.value.sort({it.label}) }">
								<li class="sidebar-item ${app.isCurrentItem(item: item) ? 'active' : '' }">
									<g:link class="sidebar-link" action="${ item.action }" controller="${ item.controller }">
										<g:if test="${ item.icon }">
											<app:icon name="${ item.icon }" lib="${ item.iconLib }"/>
										</g:if>
										${item.label }
									</g:link>
								</li>
							</g:each>
						</ul>
					</li>
				</g:if>
				
				<g:else>
					<g:set var="item" value="${ menuGroup.value[0] }"/>
				
					<li class="sidebar-item ${app.isCurrentItem(item: item) ? 'active' : '' }">
						<g:link class="sidebar-link" action="${ item.action }" controller="${ item.controller }">
							<g:if test="${ item.icon }">
								<app:icon name="${ item.icon }" lib="${ item.iconLib }"/>
							</g:if>
							${item.label }
						</g:link>
					</li>
				</g:else>
			
			</g:each>
		</ul>
	</div>
</nav>


