<%@ page import="smarthome.automation.ProducteurEnergie" %>


<div class="field-group">
	<label for="libelle">
		Nom
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="libelle" required="true" value="${producteurEnergie?.libelle}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="action">
		Action
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:field name="nbaction" type="number" value="${producteurEnergie.nbaction}" required="true" class="text medium-field"/>
</div>

<div class="field-group">
	<label for="investissement">
		Investissement (€)
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:field name="investissement" type="number decimal" value="${ producteurEnergie.investissement }" required="true" class="text medium-field"/>
</div>

<div class="field-group">
	<label for="surface">
		Surface (m²)
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:field name="surface" type="number decimal" value="${ producteurEnergie.surface }" required="true" class="text medium-field"/>
</div>

