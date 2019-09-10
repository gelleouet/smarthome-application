<div class="form-group">
	<label>
		Destinataires
	</label>
	<g:textField name="jsonParameters.to" value="${ notification?.jsonParameters?.to }" class="form-control"/>
	<small class="form-text text-muted">Par défaut votre numéro mobile. Sinon plusieurs numéros possibles séparés par une virgule. Format des numéros : +33...</small>
</div>
