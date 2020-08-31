<%@ page import="smarthome.automation.DeviceType" %>


<div class="form-group required">
	<label for="libelle">
		Libellé
	</label>
	<g:textField name="libelle" required="true" value="${deviceType?.libelle}" class="form-control" autofocus="true"/>
</div>

<div class="form-group required">
	<label for="implClass">
		Implémentation
	</label>
	<g:textField name="implClass" required="true" value="${deviceType?.implClass}" class="form-control"/>
</div>

<label class="custom-control custom-checkbox">
    <g:checkBox class="custom-control-input" name="qualitatif" value="${deviceType?.qualitatif}"/>
    <span class="custom-control-label" for="qualitatif">Qualitatif ?</span>
</label>
  
<label class="custom-control custom-checkbox">
    <g:checkBox class="custom-control-input" name="planning" value="${deviceType?.planning}"/>
    <span class="custom-control-label" for="planning">Planning ?</span>
</label>  

<div class="form-group">
	<label for="configuration">
		Configuration
	</label>
	<g:textArea name="configuration" value="${deviceTypeConfig?.data}" class="medium-script form-control"/>
</div>

	

