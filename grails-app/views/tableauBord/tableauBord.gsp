<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
		<div class="aui-group">
			<g:if test="${ !mobileAgent }">
				<div class="aui-item">
					<g:render template="profilUser"></g:render>
				</div>
				<div class="aui-item" style="width:500px">
					<g:render template="filActualite"></g:render>
				</div>
				<div class="aui-item">
					<!-- notification / invitation -->
				</div>
			</g:if>
			
			<g:else>
				<div class="aui-item">
					<g:render template="profilUser"></g:render>
					<br/>
					<g:render template="filActualite"></g:render>
				</div>
			</g:else>
		</div>
	
	</g:applyLayout>
</body>
</html>