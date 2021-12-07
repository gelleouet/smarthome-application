<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<h3>Catalogue</h3>
	
		<g:form class="form-inline" action="catalogue" name="catalogue-defi-form">
			<fieldset>
				<input autofocus="true" class="form-control medium" type="text" placeholder="Nom..." name="search" value="${ command.search }"/>
				<input class="form-control medium" type="text" placeholder="Organisation ..." name="organisation" value="${ command.organisation }"/>
				<button class="btn btn-light"><app:icon name="search"/></button>
			</fieldset>
		</g:form>
	
		<!-- layout grille 3 colonnes -->
		<div class="mt-4">
			<div class="row">
				
				<g:each var="defi" in="${ defis }">
					<div class="col-4">
						<div class="card">
							<div class="card-body px-4">
								<h5 class="card-title mb-2"><app:icon name="award"/> ${ defi.libelle }</h5>
								
								<div class="row">
							  		<div class="col">
							  			<label class="font-weight-bold">Organisateur</label> : ${ defi.organisation ?: defi.user.prenomNom }
							  		</div>
							  	</div>
								<div class="row">
							  		<div class="col">
							  			<label class="font-weight-bold">Référence</label> : du <app:formatUser date="${ defi.referenceDebut }"/> au <app:formatUser date="${ defi.referenceFin }"/>
							  		</div>
							  	</div>
								<div class="row">
							  		<div class="col">
							  			<label class="font-weight-bold">Action</label> : du <app:formatUser date="${ defi.actionDebut }"/> au <app:formatUser date="${ defi.actionFin }"/>
							  		</div>
							  	</div>
							  	
							  	<div class="mt-2">
							  		<div class="btn-group">
							  			<g:if test="${ defi.actif }">
							  				<g:link action="inscription" controller="grandDefi" class="btn btn-primary" params="[defiId: defi.id]"><app:icon name="check-square"/> Je m'inscris</g:link>
							  			</g:if>
							  			<sec:ifAnyGranted roles="ROLE_ADMIN">
							  				<g:link action="edit" controller="defi" class="btn btn-primary ml-1" id="${ defi.id }"><app:icon name="edit"/> Modifier</g:link>
							  			</sec:ifAnyGranted>
							  		</div>
							  	</div>
							</div>
							
						</div> <!-- div.card -->
					</div> <!-- div.col -->
				</g:each>
				
			</div> <!-- div.row -->
		</div>
		
		<div class="mt-4 text-right">
			<ul class="pagination" data-form-id="catalogue-defi-form">
				<g:paginate total="${ recordsTotal }"/>
			</ul>
		</div>
		
		
	</g:applyLayout>
</body>
</html>