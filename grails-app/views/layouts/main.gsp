<!doctype html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><g:layoutTitle default="${g.meta(name: 'app.code') }"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		
		<link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">		
		<link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">
		
		<asset:stylesheet src="application.css"/>
		<asset:javascript src="application.js"/>
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		<g:layoutHead/>
	</head>
	<body onload="${pageProperty(name: 'body.onload')}" data-chart-package="${pageProperty(name: 'body.data-chart-package')}">
	
		<main id="content" role="main d-flex w-100" <%= app.stateInsertAttr()  %>>
			<div id="ajaxDialog"></div>
			<g:layoutBody/>
		</main>
		
		<asset:script type="text/javascript">
			onLoadGoogleChart()
		</asset:script>
		
		<asset:deferredScripts/>
	</body>
</html>
