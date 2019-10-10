<div class="card mb-4">
	<div class="card-body">
		<ul class="nav nav-pills nav-justified">
			<li class="nav-item">
				<g:link action="mesresultats" class="nav-link ${ viewName == 'mesresultats' ? 'active' : '' }"><app:icon name="award"/> Mes Résultats</g:link>
			</li>
			<li class="nav-item">
				<g:link action="resultatsequipe" class="nav-link ${ viewName == 'resultatsequipe' ? 'active' : '' }"><app:icon name="award"/> Résultats Equipe</g:link>
			</li>
			<li class="nav-item">
				<g:link action="resultatsdefi" class="nav-link ${ viewName == 'resultatsdefi' ? 'active' : '' }"><app:icon name="award"/> Résultats Défi</g:link>
			</li>
		</ul>
	</div>
</div>