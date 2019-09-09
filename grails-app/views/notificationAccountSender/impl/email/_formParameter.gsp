<div class="form-group">
	<label>
		Objet
	</label>
	<g:textField name="jsonParameters.subject" value="${ notification?.jsonParameters?.subject }" class="form-control"/>
	<small class="form-text text-muted">Par défaut la description de la notification. Script Groovy renvoyant un String. Variables disponibles : device, event, alert, alertLevel, devices</small>
</div>

<div class="form-group">
	<label>
		Destinataires
	</label>
	<g:textField name="jsonParameters.to" value="${ notification?.jsonParameters?.to }" class="form-control"/>
	<small class="form-text text-muted">Par défaut votre email. Sinon plusieurs emails possibles séparés par une virgule.</small>
</div>

<div class="form-group">
	<label>
		Copies
	</label>
	<g:textField name="jsonParameters.cc" value="${ notification?.jsonParameters?.cc }" class="form-control"/>
	<small class="form-text text-muted">Plusieurs emails possibles séparés par une virgule.</small>
</div>

<div class="form-group">
	<label>
		Copies cachées
	</label>
	<g:textField name="jsonParameters.bcc" value="${ notification?.jsonParameters?.bcc }" class="form-control"/>
	<small class="form-text text-muted">Plusieurs emails possibles séparés par une virgule.</small>
</div>
