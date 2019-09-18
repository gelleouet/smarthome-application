<div class="card">
	<div class="card-header">
		<h4 class="card-title">Le Grand Défi Energie 2019</h4>
		<h6 class="card-subtitle text-muted">ALEC du pays de Rennes</h6>
	</div>
	
	<div class="card-body">
		<div class="text-center">
			<a href="https://www.alec-rennes.org/legranddefienergie2019/" target="granddefi">
				<asset:image src="grand-defi.gif"/>
				Plus d'informations
			</a>
			
		</div>
		
		<div class="text-center mt-4">
			<asset:image src="grand-defi-partenaire.png"/>
		</div>
		
		<g:if test="${ withAccountButton }">
			<div class="text-center mt-4">
				<g:link action="account" controller="grandDefi" class="btn btn-primary">Je crée mon compte</g:link>
			</div>
		</g:if>
	</div>
</div>