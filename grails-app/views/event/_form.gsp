<%@ page import="smarthome.automation.Event" %>

<div class="field-group">
	<label for="libelle">
		Description
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="libelle" required="true" value="${event?.libelle}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="actif">
		Actif		
	</label>
	<g:checkBox name="actif" value="${event?.actif}" class="checkbox"/>
	<div class="description">Si désactivé, suspend temporairement un événement</div>
</div>


<div class="field-group">
	<label for="modes">
		Modes
	</label>
	<g:select name="modeList" from="${ modes }" value="${ event?.modes*.mode }" 
		optionKey="id" optionValue="name" class="select combobox long-field" multiple="true"/>
	<div class="description">
		<ul>
			<g:radioGroup name="inverseMode" value="${ event?.inverseMode }" values="[false,true]"
				labels="['Déclenche l\'événement si au moins un mode sélectionné est activé', 'Déclenche l\'événement si aucun mode sélectionné n\'est activé']">
				<li>${ it.radio} ${ it.label } </li>
			</g:radioGroup>
		</ul>
	</div>
</div>

<div class="field-group">
	<label for="device">
		Objets associés
	</label>
	<g:select name="deviceList" from="${devices}" value="${event?.devices*.device}"
		optionKey="id" optionValue="label" class="select combobox long-field" multiple="true"/>
	<div class="description">Déclenche automatiquement l'événement sur chaque changement d'un objet associé</div>
</div>

<div class="field-group">
	<label for="condition">
		Condition
	</label>
	<g:textArea name="condition" value="${event?.condition}" class="short-script textarea text long-field"/>
	<div class="description">Expression Groovy renvoyant en dernière instruction un Boolean ou CronExpression.
		Variables pré-définies : device, event, alert, alertLevel, devices
	</div>
</div>



