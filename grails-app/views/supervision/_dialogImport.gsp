<g:applyLayout name="dialog">

	<content tag="dialogId">supervision-import-dialog</content>
	<content tag="dialogTitle">Import</content>

	<content tag="dialogBody">
		<g:uploadForm name="supervision-import-dialog-form" action="importData">
		
			<div class="form-group required">
				<label>Import</label>
				<g:select name="importImpl" class="form-control" required="true" from="${ importImpls }"
					optionKey="key" optionValue="value"/>	
			</div>
			<div class="form-group required">
				<g:field type="file" name="importFile" required="true"/>	
			</div>
			
			<button id="supervision-import-submit-dialog-button" class="d-none"></button>
		</g:uploadForm>
	</content>
	
	
	<content tag="dialogFooter">
		<a id="supervision-import-dialog-button" class="btn btn-primary">Importer</a>
		<a class="btn btn-link" onclick="hideDialog('supervision-import-dialog')">Annuler</a>
	</content>

</g:applyLayout>
