<g:applyLayout name="dialog">

	<content tag="dialogId">supervision-export-dialog</content>
	<content tag="dialogTitle">Export</content>

	<content tag="dialogBody">
		<g:form name="supervision-export-dialog-form" action="exportAdmin">
			<g:hiddenField name="deviceTypeId" value="${ command.deviceTypeId }"/>
			<g:hiddenField name="profilId" value="${ command.profilId }"/>
			<g:hiddenField name="userSearch" value="${ command.userSearch }"/>
			<g:hiddenField name="ville" value="${ command.ville }"/>
		
			<div class="form-group required">
				<label for="dateDebut">Date début</label>
				<g:field type="date" name="dateDebut" class="form-control aui-date-picker" value="${ app.formatPicker(date: command.dateDebut) }" required="true"/>	
			</div>
			
			<div class="form-group required">
				<label for="dateFin">Date fin</label>
				<g:field type="date" name="dateFin" class="form-control aui-date-picker" value="${ app.formatPicker(date: command.dateFin) }" required="true"/>	
			</div>
			
		</g:form>
	</content>
	
	
	<content tag="dialogFooter">
		<a id="supervision-export-dialog-button" class="btn btn-primary">Exporter</a>
		<a class="btn btn-link" onclick="hideDialog('supervision-export-dialog')">Annuler</a>
	</content>

</g:applyLayout>
