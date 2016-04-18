<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
	
		<div class="aui-group">
			<g:if test="${ !mobileAgent }">
				<div class="aui-item">
					
				</div>
				<div class="aui-item filActualiteColumn">
					<div class="filActualite">
						<div class="filActualiteItem">
							<g:render template="deviceView"></g:render>
						</div>
					</div>
					
					<h3>Historique</h3>
					
					<g:render template="deviceValueFil"></g:render>
				</div>
				<div class="aui-item">
					
				</div>
			</g:if>
			
			<g:else>
				<div class="aui-item">
					<div class="filActualite">
						<div class="filActualiteItem">
							<g:render template="deviceView"></g:render>
						</div>
					</div>
					
					<h3>Historique</h3>
					
					<g:render template="deviceValueFil"></g:render>
				</div>
			</g:else>
		</div>	
		
	</g:applyLayout>
</body>
</html>