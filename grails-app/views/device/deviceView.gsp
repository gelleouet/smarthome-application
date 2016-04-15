<html>
<head>
<meta name='layout' content='authenticated-focus' />
</head>

<body>
	<g:applyLayout name="applicationContent">
		<h4>${ device.label }</h4>
		<br/>
		<g:render template="${ device.newDeviceImpl().viewGrid() }" model="[device: device]"></g:render>
	</g:applyLayout>
</body>
</html>