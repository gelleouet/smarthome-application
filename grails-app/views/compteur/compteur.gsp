<html>
<head>
<meta name='layout' content='main' />
</head>

<body onload="onLoadCompteur();">
	<g:applyLayout name="page-default">
	
		<h3 class="mb-3">Mon compteur</h3>
		
		<div class="row">
			<div class="col">
				<g:render template="compteurEau"/>
			</div> <!-- div.col -->
			
		</div> <!-- div.row -->
		
	</g:applyLayout>
</body>
</html>