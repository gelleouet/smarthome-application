<%@ page import="smarthome.automation.Device" %>


<div class="field-group">
	<label for="type">
		Type
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:select name="deviceType.id" from="${deviceTypes}" optionKey="id" optionValue="libelle" required="true" value="${device?.deviceType?.id}" class="select"/>
</div>

<div class="field-group">
	<label for="agent">
		Agent
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:select name="agent.id" from="${agents}" optionKey="id" optionValue="mac" required="true" value="${device?.agent?.id}" class="select"/>
</div>

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
	<div class="description">Identifiant unique. Peut-être un port GPIO (Raspberry), une adresse MAC, un pin Arduino, etc.</div>
</div>

<div class="field-group">
	<label for="groupe">
		Groupe
	</label>
	<g:textField name="groupe" value="${device?.groupe}" class="text long-field"/>

</div>

<div class="field-group">
	<label for="value">
		Valeur courante
	</label>
	<g:textField name="value" value="${device?.value}" class="text medium-field" disabled="true"/>
</div>

<div class="field-group">
	<label for="dateValue">
		Dernière valeur
	</label>
	<g:field name="dateValue" type="datetime"  value="${device?.dateValue}" class="text medium-field" disabled="true"/>
</div>








