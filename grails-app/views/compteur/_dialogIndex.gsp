<g:applyLayout name="dialog">

	<content tag="dialogId">compteur-index-dialog</content>
	<content tag="dialogTitle">Modification index</content>

	<content tag="dialogBody">
		<h4>Index ${ deviceValue.name?.toUpperCase() } du <app:formatUser date="${ deviceValue.dateValue }"/></h4>
	
	
		<g:form name="compteur-index-dialog-form">
			<g:hiddenField name="id" value="${ deviceValue.id }"/>
		
			<div class="form-group required">
				<label>Date</label>
				<g:field type="date" name="dateValue" value="${ app.formatPicker(date: deviceValue.dateValue) }" class="form-control aui-date-picker" required="true"/>
			</div>
		
			<div class="form-group required">
				<label>Valeur</label>
				<g:textField name="value" value="${ deviceValue.value as Long }" required="true" class="form-control"/>
			</div>
		</g:form>
	</content>
	
	
	<content tag="dialogFooter">
		<a id="ok-compteur-index-dialog-button" class="btn btn-primary" data-url="${ g.createLink(action: 'updateIndex') }">Modifier</a>
		<a id="cancel-compteur-index-dialog-button" class="btn btn-link">Annuler</a>
	</content>

</g:applyLayout>
