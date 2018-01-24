<g:each var="header" in="${items?.sort({ it.label })?.groupBy({ it.header}).sort({it.key}) }">
	<div class="aui-sidebar-group aui-sidebar-group-actions">
		<g:if test="${header.key}">
			<div class="aui-nav-heading" title="${header.key }">
				<strong>${header.key }</strong>
			</div>
		</g:if>
		<ul class="aui-nav" title="${header.key }">
			<g:each var="item" in="${header.value.sort({it.label}) }">
				<li class="${app.isCurrentItem(item: item) ? 'aui-nav-selected' : '' }"><g:link controller="${item.controller }"
						action="${item.action }">
						${item.label }
					</g:link></li>
			</g:each>
		</ul>
	</div>
</g:each>
