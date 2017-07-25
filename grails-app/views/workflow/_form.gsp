<%@ page import="smarthome.core.Workflow" %>


<div class="field-group">
	<label for="libelle">
		Libellé
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="libelle" required="true" value="${workflow?.libelle}" class="text long-field" autofocus="true"/>
</div>

<div class="field-group">
	<label for="description">
		Description
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="description" required="true" value="${workflow?.description }" class="text long-field"/>
</div>

<div class="field-group">
	<label for="bpmnFile">
		Diagramme BPMN 2.0
		<span class="aui-icon icon-required">*</span>
	</label>
	<label class="ffi" data-ffi-button-text="Parcourir">
       <input type="file" id="bpmnFile" name="bpmnFile"/>
    </label>
</div>


