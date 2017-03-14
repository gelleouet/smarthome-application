<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
		<div class="aui-group">
			<div class="aui-item" style="width:350px">
				<div class="filActualite" style="padding:15px;">
					<g:render template="/user/profilPublic" model="[userDeviceCount: userDeviceCount, sharedDeviceCount: sharedDeviceCount]"></g:render>
				</div>
				<br/>
				<div class="filActualite" style="padding:15px;">
					<g:render template="/houseMode/changeMode" model="[house: house]"></g:render>
				</div>
			</div>
			<div class="aui-item">
				<div class="filActualite" style="padding:15px;">
					<g:render template="/house/synthese"/>
				</div>
				<br/>
				<div class="filActualite" style="padding:15px;">
					<g:render template="/device/deviceActivite"/>
				</div>
				<br/>
				<div class="filActualite" style="padding:15px;">
					<g:render template="/deviceEvent/deviceEventActivite"/>
				</div>
				
			</div>
		</div>
	
	</g:applyLayout>
</body>
</html>