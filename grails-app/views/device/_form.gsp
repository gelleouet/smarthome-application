<%@ page import="smarthome.automation.Device" %>

<h4>Modèle</h4>

<div class="form-group required">
	<label for="type">Type</label>
	<g:select id="deviceType.id" name="deviceType.id" from="${deviceTypes}" optionKey="id" optionValue="libelle" required="true" 
		value="${device?.deviceType?.id}" class="form-control" metadata-url="${g.createLink(action: 'templateMetadataForm', controller: 'deviceType', params: [deviceId: device?.id]) }"/>
</div>

<h4>Identification</h4>

<div class="form-group required">
	<label for="label">Nom</span>
	</label>
	<g:textField name="label" required="true" value="${device?.label}" class="form-control"/>
</div>

<div class="form-group required">
	<label for="mac">Mac</span>
	</label>
	<g:textField name="mac" required="true" value="${device?.mac}" class="form-control"/>
	<small class="form-text text-muted">Identifiant unique. Peut-être un port Raspberry (ex : gpio17, gpio02), une adresse MAC, etc.</small>
</div>

<div class="form-group">
	<label for="agent">
		Agent
	</label>
	<g:select name="agent.id" from="${agents}" optionKey="id" optionValue="${{ it.mac + (it.libelle ? (' (' + it.libelle + ')') : '') }}"
		required="true" value="${device?.agent?.id}" class="form-control" noSelection="[null: '']"/>
	<g:if test="${ device?.agent }">
		<div>
			<g:render template="/agent/status" model="[agent: device?.agent]"/>
		</div>
	</g:if>
</div>

<div class="form-group">
	<label for="tableauBord">
		Tableau de bord
	</label>
	<g:textField name="tableauBord" value="${device?.tableauBord}" class="form-control"/>
</div>


<h4>Valeur</h4>

<div class="form-group">
	<label for="formula">
		Formule
	</label>
	<g:textField name="formula" value="${device?.formula}" class="form-control"/>
	<small class="form-text text-muted">Applique la formule sur les valeurs envoyées par l'agent</small>
</div>

<div class="form-group">
	<label for="unite">
		Unité
	</label>
	<g:textField name="unite" value="${device?.unite}" class="form-control"/>
</div>

<div class="form-group">
	<label for="value" title="API : device.value">
		Valeur principale
	</label>
	<g:textField name="value" value="${device?.value}" class="form-control" disabled="true"/>
	<small class="form-text text-muted">Dernière valeur : il y a ${ app.formatTimeAgo(date: device.dateValue) }</small>
</div>








