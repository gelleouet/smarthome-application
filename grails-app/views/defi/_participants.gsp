<table class="table mt-2 app-datatable">
	<thead>
		<tr>
			<th></th>
			<th class="text-right">Référence (kWh)</th>
			<th class="text-right">Action (kWh)</th>
			<th class="text-right">Economie (%)</th>
			<th class="text-right">Classement</th>
			<th class="column-2-buttons"></th>
		</tr>
	</thead>
	
	<tbody>
	
		<!-- Niveau 1 : données du défi -->
		
		<tr class="">
			<td><h4 class="font-weight-bold">${ defi.libelle }</h4></td>
			<td class="text-right">${ defi.reference_global() }</td>
			<td class="text-right">${ defi.action_global() }</td>
			<td class="text-right">${ defi.economie_global }</td>
			<td class="text-right">${ defi.classement_global }</td>
			<td class="command-column column-2-buttons">
				<g:link action="calculerResultat" params="['defi.id': defi.id]" class="btn btn-light confirm-button" title="Calculer"><app:icon name="cpu"/></g:link>
			</td>
		</tr>
	
		<!-- Niveau 2: données des équipes -->
		
		<g:set var="equipes" value="${ participants.groupBy { it.defiEquipe }.sort { it.key.libelle } }"/>

		<g:each var="equipe" in="${ equipes }">
		
			<tr class="bg-primary text-white">
				<td><h5 class="font-weight-bold text-white"><app:icon name="users"/> ${ equipe.key.libelle }</h5></td>
				<td class="text-right">${ equipe.key.reference_global() }</td>
				<td class="text-right">${ equipe.key.action_global() }</td>
				<td class="text-right">${ equipe.key.economie_global }</td>
				<td class="text-right">${ equipe.key.classement_global }</td>
				<td class="command-column column-2-buttons">
					<g:link action="effacerResultat" params="['defiEquipe.id': equipe.key.id]" class="btn btn-light ml-1 confirm-button" title="Effacer"><app:icon name="delete"/></g:link>
				</td>
			</tr>
			
			<g:set var="profils" value="${ equipe.value.groupBy { it.user.profil }.sort { it.key.libelle } }"/>
			
			<g:each var="profil" in="${ profils }">
			
				<g:set var="equipeProfil" value="${ equipeProfils.find{ it.defiEquipe == equipe.key && it.profil == profil.key } }"/>
			
				<!-- Niveau 3 : données par profil de l'équipe -->
				
				<tr class="bg-light">
					<td><asset:image src="${profil.key.icon }" style="width:30px"/> ${ profil.key.libelle }</td>
					<td class="text-right">${ equipeProfil?.reference_global() }</td>
					<td class="text-right">${ equipeProfil?.action_global() }</td>
					<td class="text-right">${ equipeProfil?.economie_global }</td>
					<td class="text-right">${ equipeProfil?.classement_global }</td>
					<td class="command-column column-2-buttons">
					</td>
				</tr>
			
				<g:each var="participant" in="${ profil.value }">
				
					<!-- Niveau 4 : données par participants -->
				
					<tr>
						<td class="pl-6"><app:icon name="user"/> ${ participant.user.prenomNom }</td>
						<td class="text-right">${ participant.reference_global() }</td>
						<td class="text-right">${ participant.action_global() }</td>
						<td class="text-right">${ participant.economie_global }</td>
						<td class="text-right">${ participant.classement_global }</td>
						<td class="command-column column-2-buttons">
							<g:link action="effacerResultat" params="['defiEquipeParticipant.id': participant.id]" class="btn btn-light ml-1 confirm-button" title="Effacer"><app:icon name="delete"/></g:link>
						</td>
					</tr>
				</g:each>
			</g:each>
		</g:each>
		
	</tbody>
</table>
