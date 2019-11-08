<div class="row mb-3">
	<div class="col-8">
		<h3>
			${ currentDefi?.libelle ?: 'Mes défis' }
			<g:if test="${ equipe }">
				&gt; ${ equipe.libelle }
			</g:if>
			<g:if test="${ participant && viewName == 'mesresultats' }">
				&gt; ${ participant.user.prenomNom }
			</g:if>
			
			<g:if test="${ currentDefi }">
				<sec:ifAnyGranted roles="ROLE_ADMIN_GRAND_DEFI,ROLE_ADMIN">
					<g:link controller="defi" action="edit" id="${ currentDefi.id }" title="Edition"><app:icon name="edit"/></g:link>
				</sec:ifAnyGranted>
			</g:if>
		</h3>
		<g:if test="${ currentDefi }">
			<h5 class="text-muted">Référence du <app:formatUser date="${ currentDefi.referenceDebut }"/> au <app:formatUser date="${ currentDefi.referenceFin }"/>
			- Action du <app:formatUser date="${ currentDefi.actionDebut }"/> au <app:formatUser date="${ currentDefi.actionFin }"/></h5>
		</g:if>
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
				<g:link action="mesresultats" params="['defi.id': currentDefi?.id, 'defiEquipeParticipant.id': participant?.id]" class="nav-link ${ viewName == 'mesresultats' ? 'active' : '' }">
					<app:icon name="award"/> Mes Résultats
					<g:if test="${ participant?.canDisplay() }">
						&nbsp;<span class="badge badge-pill bg-menu">${ participant.classement_global() } / ${ participant.total_global() }</span>
					</g:if>
				</g:link>
			</li>
			<li class="nav-item">
				<g:link action="resultatsequipe" params="['defi.id': currentDefi?.id, 'defiEquipe.id': equipe?.id]" class="nav-link ${ viewName == 'resultatsequipe' ? 'active' : '' }">
					<app:icon name="award"/> Résultats Equipe
					<g:if test="${ equipe?.canDisplay() }">
						&nbsp;<span class="badge badge-pill bg-menu">${ equipe.classement_global() } / ${ equipe.total_global() }</span>
					</g:if>
				</g:link>
			</li>
			<li class="nav-item">
				<g:link action="resultatsdefi" params="['defi.id': currentDefi?.id, 'defiEquipe.id': equipe?.id]" class="nav-link ${ viewName == 'resultatsdefi' ? 'active' : '' }">
					<app:icon name="award"/> Résultats Défi
					<g:if test="${ equipe?.canDisplay() }">
						&nbsp;<span class="badge badge-pill bg-menu">${ equipe.classement_global() } / ${ equipe.total_global() }</span>
					</g:if>
				</g:link>
			</li>
			<li class="nav-item">
				<g:link action="participants" params="['defi.id': currentDefi?.id]" class="nav-link ${ viewName == 'participants' ? 'active' : '' }">
					<app:icon name="users"/> Les équipes
					<g:if test="${ currentDefi }">
						&nbsp;<span class="badge badge-pill bg-menu">${ currentDefi.equipes.size() }</span>
					</g:if>
				</g:link>
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
<g:elseif test="${ ! equipe?.canDisplay() }">
	<div class="card flex-fill w-100">
		<div class="card-body">
			<g:applyLayout name="messageWarning">
				Le défi n'est pas encore terminé. Les résultats seront calculés à la fin de la période d'action.
			</g:applyLayout>
		</div>
	</div>
</g:elseif>