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
				<div class="aui-item">
					<g:render template="profilPublic"></g:render>
				</div>
				<div class="aui-item">
					
				</div>
			</g:if>
			
			<g:else>
				<div class="aui-item">
					<g:render template="profilPublic"></g:render>
				</div>
			</g:else>
		</div>	
		
	</g:applyLayout>
</body>
</html>