<html>
<head>
<meta name='layout' content='main' />
</head>

<body onload="onLoadCompteur()">
	<g:applyLayout name="page-default">
	
		<h3 class="mb-3">Validation des index</h3>
		
		<div class="card">
			
			<div class="card-header">
				<div class="card-actions float-right">
				</div>
			</div> <!-- div.card-header -->
		
			<div class="card-body">
			
				<g:form action="compteurIndexs" name="compteurIndex-form" class="form-inline">
				
					<g:each var="sortProperty" in="${ command.sortProperties }" status="status">
						<g:hiddenField name="sortProperties[${ status }].property" value="${ sortProperty.property }"/>
						<g:hiddenField name="sortProperties[${ status }].order" value="${ sortProperty.order }"/>
					</g:each>
				
					<div class="row w-100">
						<div class="col-8">
							<g:select name="deviceTypeId" value="${ command?.deviceTypeId }" from="${ compteurTypes }"
								optionKey="id" optionValue="libelle" class="form-control" noSelection="['': ' ']"/>
							<g:select name="profilId" value="${ command?.profilId }" from="${ profils }"
								optionKey="id" optionValue="libelle" class="form-control" noSelection="['': ' ']"/>
							<g:textField name="userSearch" value="${ command?.userSearch}" class="form-control" placeholder="Nom prénom"/>
							<button id="compteurIndex-submit-button" class="btn btn-light"><app:icon name="search"/></button>
						</div>
						<div class="col text-right">
							<a id="compteur-index-valid-all-button" class="btn btn-primary" data-url="${ g.createLink(action: 'validAllCompteurIndex') }"><app:icon name="check-circle"/> Tout valider</a>
							<a id="compteur-index-delete-all-button" class="btn btn-danger" data-url="${ g.createLink(action: 'deleteAllCompteurIndex') }"><app:icon name="trash"/> Tout supprimer</a>
						</div>
					</div>	
				</g:form>
		
				<br/>
			
				<div ajax="true" id="compteurIndexAjax" data-form-id="compteurIndex-form">
					<g:render template="compteurIndexDatatable"/>
				</div>
			</div><!-- div.card-body -->
			
		</div> <!-- div.card -->
		
	</g:applyLayout>
</body>
</html>