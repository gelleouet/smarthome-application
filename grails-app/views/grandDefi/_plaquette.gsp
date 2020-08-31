<div class="card">
	<div class="card-header">
		<h4 class="card-title"><g:meta name="app.code"/></h4>
		<h6 class="card-subtitle text-muted">ALEC du pays de Rennes</h6>
	</div>
	
	<div class="card-body">
		<div class="text-center">
			<a href="${ g.meta(name: 'app.urlInfoGde') }" target="granddefi">
				<asset:image src="granddefi/logo.png" height="185px"/>
				<div>Plus d'informations</div>
			</a>
			
		</div>
		
		<div class="text-center mt-4">
			<asset:image src="granddefi/partenaire.png" height="95px"/>
		</div>
		
		<g:if test="${ withAccountButton }">
			<div class="text-center mt-4">
				<g:link action="account" controller="grandDefi" class="btn btn-primary">Je crée mon compte</g:link>
			</div>
		</g:if>
	</div>
</div>