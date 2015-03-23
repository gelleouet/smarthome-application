<g:each var="header" in="${items?.sort({ it.label })?.groupBy({ it.header}) }">
	<div class="aui-dropdown2-section">
		<g:if test="${header.key}">
			<div class="aui-dropdown2-heading">
				<g:if test="${header.key == '_secusername' }">
					<sec:username/>
				</g:if>
				<g:else>
					${header.key }
				</g:else>
			</div>
		</g:if>
		<ul>
			<g:each var="item" in="${header.value.sort({it.label}) }">
				<li>
					<g:link controller="${item.controller }" action="${item.action }">${item.label }</g:link>
				</li>
			</g:each>
		</ul>
	</div>
</g:each>
