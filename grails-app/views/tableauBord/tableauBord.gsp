<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
		<div class="aui-group">
			<g:if test="${ !mobileAgent }">
				<div class="aui-item">
					<g:render template="/user/profilPublic"></g:render>
				</div>
				<div class="aui-item filActualiteColumn">
					<g:render template="/device/deviceValueFil" model="[filActualite: filActualite]"></g:render>
				</div>
				<div class="aui-item">
					<!-- notification / invitation -->
				</div>
			</g:if>
			
			<g:else>
				<div class="aui-item">
					<g:render template="/user/profilPublic"></g:render>
					<br/>
					<g:render template="/device/deviceValueFil" model="[filActualite: filActualite]"></g:render>
				</div>
			</g:else>
		</div>
	
	</g:applyLayout>
</body>
</html>