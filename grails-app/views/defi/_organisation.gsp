<div class="w-100">
	<div class="label">
		<span>${ defi.libelle }</span>
		<div>
			<a class="btn btn-light confirm-button" title="Calculer les résultats"><app:icon name="refresh-cw"/></a>
			<a class="btn btn-light confirm-button" title="Effacer les résultats"><app:icon name="delete"/></a>
		</div>
	</div>
	<div class="branch">
		<g:each var="equipe" in="${ defi.equipes.sort { it.libelle } }">
			<div class="entry">
				<span class="label">${ equipe.libelle }</span>
				<g:if test="${ equipe.participants }">
					<div class="branch">
						<g:each var="profilGroup" in="${ equipe.participants.groupBy { it.user.profil.libelle }.sort { it.key} }">
							<div class="entry ${ !profilGroup.value ? 'sole' : '' }">
								<span class="label">${ profilGroup.key }</span>
								<g:if test="${ profilGroup.value }">
									<div class="branch smart-droppable">
										<g:each var="participant" in="${ profilGroup.value.sort { it.user.prenomNom } }">
											<div class="entry sole">
												<div class="label">
													<span>${ participant.user.prenomNom }</span>
													<div>
														<a class="btn btn-light confirm-button" title="Calculer les résultats"><app:icon name="refresh-cw"/></a>
														<a class="btn btn-light confirm-button" title="Effacer les résultats"><app:icon name="delete"/></a>
													</div>
												</div>
											</div>
										</g:each>
									</div>
								</g:if>
							</div>
						</g:each>
					</div>
				</g:if>
			</div>
		</g:each>
	</div>
</div>

<h6 class="mt-4 text-muted">Utilisez le drag&drop pour déplacer un participant dans une autre équipe.</h6>
<h6 class="text-muted">Cliquez sur une équipe pour modifier son nom.</h6>


