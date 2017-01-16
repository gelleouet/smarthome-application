<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
		<div class="aui-group">
			<g:if test="${ !mobileAgent }">
				<div class="aui-item">
					<div class="filActualite" style="padding:15px;">
						<g:render template="/user/profilPublic" model="[userDeviceCount: filActualite.userDevices.size(), sharedDeviceCount: filActualite.sharedDevices.size()]"></g:render>
					</div>
				</div>
				<div class="aui-item filActualiteColumn">
					<g:render template="/device/deviceValueFil" model="[filActualite: filActualite.values]"></g:render>
				</div>
				<div class="aui-item">
					<!-- notification / invitation -->
				</div>
			</g:if>
			
			<g:else>
				<div class="aui-item">
					<div class="filActualite" style="padding:15px;">
						<g:render template="/user/profilPublic" model="[userDeviceCount: filActualite.userDevices.size(), sharedDeviceCount: filActualite.sharedDevices.size()]"></g:render>
					</div>
					<br/>
					<g:render template="/device/deviceValueFil" model="[filActualite: filActualite.values]"></g:render>
				</div>
			</g:else>
		</div>
	
	</g:applyLayout>
</body>
</html>