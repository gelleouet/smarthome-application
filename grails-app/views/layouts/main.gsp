<!doctype html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><g:layoutTitle default="${g.meta(name: 'app.code') }"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		
		<link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">		
		
		<asset:stylesheet src="application.css"/>
		<asset:javascript src="application.js"/>
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		<g:layoutHead/>
	</head>
	<body onload="onLoadGoogleChart(); ${pageProperty(name: 'body.onload')}" data-chart-package="${pageProperty(name: 'body.data-chart-package')}">
	
		<main id="content" role="main d-flex w-100" <%= app.stateInsertAttr()  %>>
			<div id="ajaxDialog"></div>
			<g:layoutBody/>
		</main>
		
		<asset:deferredScripts/>
	</body>
</html>
