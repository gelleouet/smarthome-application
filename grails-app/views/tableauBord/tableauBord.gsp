<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
		<div class="aui-group">
			<g:if test="${ !mobileAgent }">
				<div class="aui-item" style="width:350px">
					<div class="filActualite" style="padding:15px;">
						<g:render template="/user/profilPublic" model="[userDeviceCount: userDeviceCount, sharedDeviceCount: sharedDeviceCount]"></g:render>
					</div>
				</div>
				<div class="aui-item">
					<div class="filActualite" style="padding:15px;">
						<g:render template="/device/deviceActivite"/>
					</div>
					<br/>
					<div class="filActualite" style="padding:15px;">
						<g:render template="/deviceEvent/deviceEventActivite"/>
					</div>
					<br/>
					<div class="filActualite" style="padding:15px;">
						<g:render template="/house/synthese"/>
					</div>
				</div>
			</g:if>
			
			<g:else>
				<div class="aui-item">
					<div class="filActualite" style="padding:15px;">
						<g:render template="/user/profilPublic" model="[userDeviceCount: userDeviceCount, sharedDeviceCount: sharedDeviceCount]"></g:render>
					</div>
					<br/>
					<div class="filActualite" style="padding:15px;">
						<g:render template="/device/deviceActivite"/>
					</div>
					<br/>
					<div class="filActualite" style="padding:15px;">
						<g:render template="/deviceEvent/deviceEventActivite"/>
					</div>
					<br/>
					<div class="filActualite" style="padding:15px;">
						<g:render template="/house/synthese"/>
					</div>
				</div>
			</g:else>
		</div>
	
	</g:applyLayout>
</body>
</html>