<%@ page import="smarthome.automation.ProducteurEnergie" %>


<div class="field-group">
	<label>
		Production solaire
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:select name="producteur.id" value="${ producteurEnergieAction?.producteur?.id }" from="${ productions }" 
		optionKey="id" optionValue="libelle" class="select" required="true"/>
</div>

<div class="field-group">
	<label for="nbaction">
		Action
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:field name="nbaction" type="number" value="${ producteurEnergieAction?.nbaction }"
		required="true" class="text medium-field"/>
</div>

<div class="field-group">
	<label>
		Panneau solaire
	</label>
	<g:select name="device.id" value="${ producteurEnergieAction?.device?.id }" from="${ panneaux }" 
		optionKey="id" optionValue="label" class="select" noSelection="[null: '']"/>
</div>


