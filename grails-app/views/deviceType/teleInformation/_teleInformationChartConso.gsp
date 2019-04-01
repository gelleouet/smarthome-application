<%@ page import="smarthome.automation.ChartViewEnum" %>


<div id="chartDivConso" data-chart-type="${ command.deviceImpl.defaultChartType().factory }">
	<h6 class="h6">Loading chart...</h6>
	<div class="aui-progress-indicator">
	    <span class="aui-progress-indicator-value"></span>
	</div>
	<g:render template="${ command.deviceImpl.chartDataTemplate() }" model="[command: command,
		datas: chart.values, chart: chart, compareChart: compareChart]"/>
</div> 

<g:if test="${ !mobileAgent }">
	<form class="aui">
		<div class="field-group">
			<label for="label">
				Consommation sur la sélection
			</label>
			<g:textField name="selectionConso" class="text medium-field" readonly="true"/> ${ command.viewMode == ChartViewEnum.day ? 'Wh' : 'kWh' }
			<div class="description">Vous pouvez zoomer sur les graphiques en glissant la souris avec le clic gauche sur une zone. Pour revenir en arrière, faites un clic droit.
		Vous pouvez aussi calculer la consommation totale sur une période en sélectionnant 2 points sur une série. Cette option ne fonctionne que sur une seule série 
		à la fois.</div>
		</div>
	</form>
</g:if>
