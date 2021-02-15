<!doctype html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><g:layoutTitle default="${g.meta(name: 'app.code') }"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		
		<link rel="apple-touch-icon" sizes="180x180" href="${assetPath(src: 'apple-touch-icon.png')}" type="image/png">		
		<link rel="icon" sizes="32x32" href="${assetPath(src: 'favicon-32x32.png')}" type="image/png">		
		<link rel="icon" sizes="16x16" href="${assetPath(src: 'favicon-16x16.png')}" type="image/png">		
		
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
