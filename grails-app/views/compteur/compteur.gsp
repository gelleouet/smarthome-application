<html>
<head>
<meta name='layout' content='main' />
</head>

<body onload="onLoadCompteur();">
	<g:applyLayout name="page-default">
	
		<h3 class="mb-3">Mes compteurs</h3>
		
		<div class="row">
			<div class="col">
				<g:render template="compteurElec"/>
			</div><!-- div.col -->
				
			<div class="col">
				<g:render template="compteurGaz"/>
			</div> <!-- div.col -->
			
			<div class="col">
				<g:render template="compteurEau"/>
			</div> <!-- div.col -->
			
		</div> <!-- div.row -->
		
	</g:applyLayout>
</body>
</html>