<%@ page import="smarthome.core.Workflow" %>


<div class="form-group required">
	<label for="libelle">Libellé</label>
	<g:textField name="libelle" required="true" value="${workflow?.libelle}" class="form-control" autofocus="true"/>
</div>

<div class="form-group required">
	<label for="description">Description</label>
	<g:textField name="description" required="true" value="${workflow?.description }" class="form-control"/>
</div>

<div class="form-group">
	<label class="form-label w-100">Diagramme BPMN 2.0</label>
    <input type="file" id="bpmnFile" name="bpmnFile"/>
</div>


