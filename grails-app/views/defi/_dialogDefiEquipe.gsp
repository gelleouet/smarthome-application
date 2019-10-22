<g:applyLayout name="dialog">

	<content tag="dialogId">defi-equipe-dialog</content>
	<content tag="dialogTitle">Equipe</content>

	<content tag="dialogBody">
		<g:form name="defi-equipe-dialog-form">
			<g:hiddenField name="defi.id" value="${ defiEquipe?.defi?.id }"/>
			<g:hiddenField name="id" value="${ defiEquipe?.id }"/>
		
			<div class="form-group required">
				<label for="libelle">Libellé</label>
				<g:textField name="libelle" required="true" value="${defiEquipe?.libelle}" class="form-control"/>
			</div>
		</g:form>
	</content>
	
	
	<content tag="dialogFooter">
		<a id="ok-defi-equipe-dialog-button" class="btn btn-primary" data-url="${ g.createLink(action: 'saveDefiEquipe') }">Enregistrer</a>
		<a id="cancel-defi-equipe-dialog-button" class="btn btn-link">Annuler</a>
	</content>

</g:applyLayout>
