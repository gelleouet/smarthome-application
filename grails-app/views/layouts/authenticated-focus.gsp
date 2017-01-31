<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="${g.meta(name: 'app.code') }"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">		
		<link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">
		
		<asset:stylesheet src="application.css"/>
		<link rel="stylesheet" href="//aui-cdn.atlassian.com/aui-adg/${g.meta(name: 'aui.version') }/css/aui.min.css" media="all">
		<link rel="stylesheet" href="//aui-cdn.atlassian.com/aui-adg/${g.meta(name: 'aui.version') }/css/aui-experimental.min.css" media="all">
		
		<asset:javascript src="application.js"/>
		<script src="//aui-cdn.atlassian.com/aui-adg/${g.meta(name: 'aui.version') }/js/aui.min.js"></script>
		<script src="//aui-cdn.atlassian.com/aui-adg/${g.meta(name: 'aui.version') }/js/aui-experimental.min.js"></script>
		
		<g:layoutHead/>
	</head>
	<body class="aui-page-focused aui-page-size-large">
		<g:include view="/layouts/headerAuthenticated.gsp"/>
		<section id="content" role="main" <%= app.stateInsertAttr() %>>
			<g:layoutBody/>
		</section>
		
		<div id="ajaxDialog"></div>
		
		<g:include view="/layouts/footer.gsp"/>
		
		<asset:deferredScripts/>
	</body>
</html>
