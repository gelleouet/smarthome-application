<div class="row mb-3">
	<div class="col-8">
		<h3>${ currentDefi?.libelle ?: 'Mes défis' }
		</h3>
	</div>
	<div class="col-4">
		<div class="btn-toolbar justify-content-end">
			<div class="btn-group">
				<button class="btn btn-primary dropdown-toggle" data-toggle="dropdown">Autres défis</button>
				<div class="dropdown-menu">
					<g:each var="defi" in="${ defis?.sort { it.libelle } }">
						<g:link class="dropdown-item" action="defis" params="['defi.id': defi.id]">${ defi.libelle }</g:link>
					</g:each>
				</div>
			</div>
		</div>
	</div>
</div>


<div class="card flex-fill w-100">
	<div class="card-body">
		<ul class="nav nav-pills nav-justified">
			<li class="nav-item">
				<g:link action="mesresultats" params="['defi.id': currentDefi?.id]" class="nav-link ${ viewName == 'mesresultats' ? 'active' : '' }"><app:icon name="award"/> Mes Résultats</g:link>
			</li>
			<li class="nav-item">
				<g:link action="resultatsequipe" params="['defi.id': currentDefi?.id]" class="nav-link ${ viewName == 'resultatsequipe' ? 'active' : '' }"><app:icon name="award"/> Résultats Equipe</g:link>
			</li>
			<li class="nav-item">
				<g:link action="resultatsdefi" params="['defi.id': currentDefi?.id]" class="nav-link ${ viewName == 'resultatsdefi' ? 'active' : '' }"><app:icon name="award"/> Résultats Défi</g:link>
			</li>
		</ul>
	</div>
</div>


<g:if test="${ !currentDefi }">
	<div class="card flex-fill w-100">
		<div class="card-body">
			<g:applyLayout name="messageWarning">
				Vous n'êtes enregistrés sur aucun défi.
			</g:applyLayout>
		</div>
	</div>
</g:if>
<g:elseif test="${ ! currentDefi?.canDisplay() }">
	<div class="card flex-fill w-100">
		<div class="card-body">
			<g:applyLayout name="messageWarning">
				Les résultats ne sont pas encore calculés.
			</g:applyLayout>
		</div>
	</div>
</g:elseif>