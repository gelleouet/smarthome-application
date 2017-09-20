<div class="field-group">
	<label>
		Objet
	</label>
	<g:textField name="jsonParameters.subject" value="${ notification?.jsonParameters?.subject }" class="text long-field"/>
	<div class="description">Par défaut la description de la notification. Script Groovy renvoyant un String. Variables disponibles : device, event, alert, alertLevel, devices</div>
</div>

<div class="field-group">
	<label>
		Destinataires
	</label>
	<g:textField name="jsonParameters.to" value="${ notification?.jsonParameters?.to }" class="text long-field"/>
	<div class="description">Par défaut votre email. Sinon plusieurs emails possibles séparés par une virgule.</div>
</div>

<div class="field-group">
	<label>
		Copies
	</label>
	<g:textField name="jsonParameters.cc" value="${ notification?.jsonParameters?.cc }" class="text long-field"/>
	<div class="description">Plusieurs emails possibles séparés par une virgule.</div>
</div>

<div class="field-group">
	<label>
		Copies cachées
	</label>
	<g:textField name="jsonParameters.bcc" value="${ notification?.jsonParameters?.bcc }" class="text long-field"/>
	<div class="description">Plusieurs emails possibles séparés par une virgule.</div>
</div>
