<div class="w-100">
	<div class="wrapper">
		<div class="label">${ defi.libelle }</div>
		<div class="branch">
			<g:each var="equipe" in="${ defi.equipes.sort { it.libelle } }">
				<div class="entry ${ !equipe.participants ? 'sole' : ''}">
					<div class="label smart-droppable">
						<div>
							${ equipe.libelle }
						</div>
					</div>
					<g:if test="${ equipe.participants }">
						<div class="branch">
							<g:each var="profilGroup" in="${ equipe.participants.groupBy { it.user.profil.libelle }.sort { it.key} }">
								<div class="entry ${ !profilGroup.value ? 'sole' : '' }">
									<div class="label">${ profilGroup.key }</div>
									<g:if test="${ profilGroup.value }">
										<div class="branch">
											<g:each var="participant" in="${ profilGroup.value.sort { it.user.prenomNom } }">
												<div class="entry sole">
													<div class="label smart-draggable">
														<div>
															${ participant.user.prenomNom }
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
</div>


