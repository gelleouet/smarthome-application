<%@ page import="smarthome.automation.Event" %>

<div class="form-group required">
	<label for="libelle">Description</label>
	<g:textField name="libelle" required="true" value="${event?.libelle}" class="form-control"/>
</div>

<label class="custom-control custom-checkbox">
	<g:checkBox name="actif" value="${event?.actif}" class="custom-control-input"/>
	<span class="custom-control-label">Actif</span>
</label>
<small class="text-muted">Si désactivé, suspend temporairement un événement</small>


<div class="form-group">
	<label for="modes">
		Modes
	</label>
	<g:select name="modeList" from="${ modes }" value="${ event?.modes*.mode }" 
		optionKey="id" optionValue="name" class="form-control combobox" multiple="true"/>
	<small class="text-muted mt-2">
		<g:radioGroup name="inverseMode" value="${ event?.inverseMode }" values="[false,true]"
			labels="['Déclenche l\'événement si au moins un mode sélectionné est activé', 'Déclenche l\'événement si aucun mode sélectionné n\'est activé']">
			<div class="form-check">
				${ it.radio}
				<label class="form-check-label">${ it.label }</label>
			</div>
		</g:radioGroup>
	</small>
</div>

<div class="form-group">
	<label for="device">
		Objets associés
	</label>
	<g:select name="deviceList" from="${devices}" value="${event?.devices*.device}"
		optionKey="id" optionValue="label" class="combobox form-control" multiple="true"/>
	<small class="text-muted">Déclenche automatiquement l'événement sur chaque changement d'un objet associé</small>
</div>

<div class="form-group">
	<label for="condition">
		Condition
	</label>
	<g:textArea name="condition" value="${event?.condition}" class="short-script form-control"/>
	<small class="text-muted">Expression Groovy renvoyant en dernière instruction un Boolean ou CronExpression.
		Variables pré-définies : device, event, alert, alertLevel, devices
	</small>
</div>



