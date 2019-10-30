<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<g:render template="defiMenu"/>
		
		<div class="row">
			<g:each var="equipe" in="${ currentDefi?.equipes?.sort { it.libelle } }">
				<div class="col-4">
					
					<div class="card">
						<div class="card-body px-4">
							<div class="row">
						  		<div class="col-6">
						  			<h4>${ equipe.libelle }</h4>
						  		</div>
						  		<div class="col-6 text-right">
						  			<g:link action="resultatsequipe" params="['defi.id': currentDefi.id, 'defiEquipe.id': equipe.id]" class="btn btn-light">Résultats</g:link>
						  		</div>
						  	</div>
							<div class="row pt-2">
						  		<div class="col-6">
						  			<span>Référence</span>
						  		</div>
						  		<div class="col-6 text-right">
						  			<span class="text-black-50">
						  				<g:render template="formatConsommation" model="[value: equipe.reference_global()]"/>
						  			</span>
						  		</div>
						  	</div>
						  	<div class="row">
						  		<div class="col-6">
						  			<span>Action</span>
						  		</div>
						  		<div class="col-6 text-right">
						  			<span class="text-primary">
						  				<g:render template="formatConsommation" model="[value: equipe.action_global()]"/>
						  			</span>
						  		</div>
						  	</div>
						  	<div class="row">
						  		<div class="col-6">
						  			<span>Economie</span>
						  		</div>
						  		<div class="col-6 text-right">
						  			<span class="font-weight-bold">
						  				${ equipe.economie_global() }%
						  			</span>
						  		</div>
						  	</div>
						</div>
						
						<g:set var="members" value="${ participants.findAll { it.defiEquipe == equipe } }"/>
						
						<ul class="list-group list-group-flush">
							<g:each var="profil" in="${ members.groupBy { it.user.profil } }">
								<li class="list-group-item">
									<div>
										<asset:image src="${profil.key.icon }" style="width:30px"/> <span class="font-weight-bold">${ profil.key.libelle }</span>
										: <g:each var="member" in="${ profil.value }">
											<g:link action="mesresultats" params="['defi.id': currentDefi.id, 'defiEquipeParticipant.id': member.id]">${ member.user.prenomNom }</g:link>
										</g:each>
									</div>
								</li>
							</g:each>
						</ul>
					</div>
					
				</div> <!-- div.col -->
			</g:each>
		</div> <!-- div.row -->
		
	</g:applyLayout>
</body>
</html>