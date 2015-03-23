<!-- iteration sur le 1er niveau qui passe en header -->
<g:each var="level1" in="${items?.sort({ it.label }) }">
	<div class="aui-dropdown2-section">
		<div class="aui-dropdown2-heading">
			${ level1.label }
		</div>

		<!-- itère sur le 2e niveau, si enfant, on prend l'élément par défaut ou le 1er élément comme controller du menu -->
		<ul>
			<g:each var="level2" in="${ level1.subitems.sort({ it.label }) }">
				<!-- l'item a son propre controller -->
				<g:if test="${level2.action }">
					<li><g:link controller="${level2.controller }" action="${level2.action }"> ${level2.label }</g:link></li>
				</g:if>
				<g:else>
					<!-- il y a un item par défaut dans le groupe niveau 3 -->
					<g:set var="defaultItem" value="${ level2.subitems.find { it.defaultGroup && it.action } }"/>
					
					<g:if test="${ defaultItem }">
						<li><g:link controller="${defaultItem.controller }" action="${defaultItem.action }"> ${level2.label }</g:link></li>
					</g:if>
					<g:else>
						<!-- on prend le 1er item du groupe niveau 3 -->
						<g:set var="fistItem" value="${ level2.subitems.find { it.action} }"/>
						
						<g:if test="${ fistItem }">
							<li><g:link controller="${fistItem.controller }" action="${fistItem.action }"> ${level2.label }</g:link></li>
						</g:if>
					</g:else>
				</g:else>
			</g:each>
		</ul>
	</div>
</g:each>
