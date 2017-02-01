<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
		<div class="aui-group">
			<div class="aui-item responsive" style="width:350px">
				<div class="filActualite" style="padding:15px;">
					<g:render template="/user/profilPublic" model="[userDeviceCount: userDeviceCount, sharedDeviceCount: sharedDeviceCount]"></g:render>
				</div>
			</div>
			<div class="aui-item responsive">
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
		</div>
	
	</g:applyLayout>
</body>
</html>