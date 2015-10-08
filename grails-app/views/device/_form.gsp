<%@ page import="smarthome.automation.Device" %>


<h4>Modèle</h4>

<div class="field-group">
	<label for="type">
		Type
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:select id="deviceType.id" name="deviceType.id" from="${deviceTypes}" optionKey="id" optionValue="libelle" required="true" 
		value="${device?.deviceType?.id}" class="select" metadata-url="${g.createLink(action: 'templateMetadataForm', controller: 'deviceType', params: [deviceId: device?.id]) }"/>
</div>

<h4>Identification</h4>

<div class="field-group">
	<label for="label">
		Nom
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="label" required="true" value="${device?.label}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="mac">
		Mac
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="mac" required="true" value="${device?.mac}" class="text long-field"/>
	<div class="description">Identifiant unique. Peut-être un port Raspberry (ex : gpio17, gpio02), une adresse MAC, etc.</div>
</div>

<div class="field-group">
	<label for="agent">
		Agent
	</label>
	<g:select name="agent.id" from="${agents}" optionKey="id" optionValue="${{ it.mac + (it.libelle ? (' (' + it.libelle + ')') : '') }}" required="true" value="${device?.agent?.id}" class="select" noSelection="[null: '']"/>
</div>

<div class="field-group">
	<label for="groupe">
		Groupe
	</label>
	<g:textField name="groupe" value="${device?.groupe}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="show">
		Visible sur tableau de bord
	</label>
	<g:checkBox name="show" value="${device?.show}"/>
</div>








