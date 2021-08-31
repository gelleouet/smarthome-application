<div class="card" style="min-height:440px">
	<div class="card-body">
	
		<h4 class="card-title">Je n'ai pas encore de compte</h4>
		<hr/>
	
		<div class="text-center">
			<a href="${ g.meta(name: 'app.urlInfoGde') }" target="granddefi">
				<asset:image src="granddefi/plaquette.jpg" width="400px"/>
				<div>Plus d'informations</div>
			</a>
			
		</div>
		
		<g:if test="${ withAccountButton }">
			<div class="text-center mt-4">
				<g:link action="account" controller="grandDefi" class="btn btn-primary">Je crée mon compte</g:link>
			</div>
		</g:if>
	</div>
</div>