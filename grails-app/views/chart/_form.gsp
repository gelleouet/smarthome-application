<%@ page import="smarthome.automation.Chart" %>
<%@ page import="smarthome.automation.ChartTypeEnum" %>


<div class="field-group">
	<label for="label">
		Libellé
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="label" required="true" value="${chart?.label}"class="text long-field"/>
</div>

<%--<div class="field-group">
	<label for="chartType">
		Graphique
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:select name="chartType" from="${ chartTypes }" required="true" value="${chart?.chartType}" class="select"/>
</div>--%>
<g:hiddenField name="chartType" value="${ ChartTypeEnum.Combo }"/>

<div class="field-group">
	<label for="chartType">
		Groupe <span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="groupe" value="${chart?.groupe}"class="text long-field" required="true"/>
</div>

