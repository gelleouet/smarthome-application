<html>
<head>
<meta name='layout' content='authenticated-focus' />
</head>

<body>
	<g:applyLayout name="applicationContent">
		<h4>${ device.label }</h4>
		<br/>
		<g:render template="${ device.newDeviceImpl().viewGrid() }" model="[device: device]"></g:render>
		
		<div style="padding-top:4px;">
			<g:render template="deviceToolbar" model="[device: device]"></g:render>
		</div>
	</g:applyLayout>
</body>
</html>