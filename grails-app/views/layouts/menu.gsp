<nav id="sidebar" class="sidebar">
	<div class="sidebar-content">
		<g:link class="sidebar-brand" uri="/">
			<asset:image src="granddefi/bonhomme.png" height="25px"/>
			<span class="align-middle"><g:meta name="app.code"/></span>
		</g:link>
		
		<g:set var="items" value="${ app.navigationItems(category: 'navbarPrimary')?.subitems }"/>

		<ul class="sidebar-nav">
			<g:each var="menuGroup" in="${ items?.groupBy{ it.header ?: it.label }.sort{ it.key} }" status="menuGroupStatus">
			
				<g:set var="item" value="${ menuGroup.value[0] }"/>
			
				<g:if test="${ menuGroup.value.size() > 1 || item.customMenuView }">
					<li class="sidebar-item">
						<a href="#menuleft-${ menuGroupStatus }" data-toggle="collapse" class="sidebar-link" aria-expanded="true" role="button">
							<g:if test="${ menuGroup.value[0].iconHeader }">
								<app:icon name="${ menuGroup.value[0].iconHeader }" lib="${ menuGroup.value[0].iconLibHeader }"/>
							</g:if>
							<span class="align-middle">${ menuGroup.key }</span>
						</a>
						
						<ul id="menuleft-${ menuGroupStatus }" class="sidebar-dropdown list-unstyled collapse show">
						
							<g:if test="${ item.customMenuView }">
								<g:render template="/${item.controller}/customMenuView" model="[item: item]"/>
							</g:if>
							<g:else>
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
							</g:else>
						</ul>
					</li>
				</g:if>
				
				<g:else>
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


