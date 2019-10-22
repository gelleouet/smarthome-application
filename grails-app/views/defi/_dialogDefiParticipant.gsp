<g:applyLayout name="dialog">

	<content tag="dialogId">defi-participant-dialog</content>
	<content tag="dialogTitle">Participant</content>

	<content tag="dialogBody">
		<g:form name="defi-participant-dialog-form">
			<g:hiddenField name="defi.id" value="${ command.defi.id }"/>
			<g:hiddenField name="defiParticipant.id" value="${ command.defiParticipant?.id }"/>
		
			<g:if test="${ command.defiParticipant?.id }">
				<h4>${ command.defiParticipant.user.prenomNom }</h4>
			</g:if>
			<g:else>
				<div class="form-group required">
					<label for="participants">Participants</label>
					<g:select name="participants" value="${ command.participants }" from="${ participants }"
						optionKey="id" optionValue="${ { it.prenomNom + ' [' + it.profil.libelle + ']' } }" required="true" class="form-control"/>
				</div>
			</g:else>
			
			<div class="form-group required">
				<label for="defiEquipeName">Equipe</label>
				<g:select name="defiEquipeName" value="${ command.defiEquipeName }" from="${ equipes }"
					optionKey="libelle" optionValue="libelle" required="true" class="form-control combobox" data-tags="true"/>
			</div>
		</g:form>
	</content>
	
	
	<content tag="dialogFooter">
		<a id="ok-defi-participant-dialog-button" class="btn btn-primary" data-url="${ g.createLink(action: 'saveDefiParticipant') }">Enregistrer</a>
		<a id="cancel-defi-participant-dialog-button" class="btn btn-link">Annuler</a>
	</content>

</g:applyLayout>
