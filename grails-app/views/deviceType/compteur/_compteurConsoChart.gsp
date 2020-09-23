<%@ page import="smarthome.automation.ChartViewEnum" %>


<div id="chartDivConso" data-chart-type="${ command.deviceImpl.defaultChartType().factory }">
	<h6>Loading chart...</h6>
	<div class="aui-progress-indicator">
	    <span class="aui-progress-indicator-value"></span>
	</div>
	<g:render template="/chart/datas/chartDatas" model="[command: command,
		datas: chart.values, chart: chart, compareChart: compareChart]"/>
</div> 

<g:if test="${ !mobileAgent }">
	<form class="form-inline">
		<div class="form-group">
			<label for="label">
				Consommation sur la sélection
			</label>
			<g:textField name="selectionConso" class="form-control" readonly="true"/> ${ command.deviceImpl.uniteByView(command.viewMode) }
			<a id="selectionConso-clear-button" class="btn btn-light ml-2" title="Effacer"><app:icon name="x"/></a>
			&nbsp;<span class="text-muted" id="selectionConso-label"></span>
		</div>
	</form>
	
	<small class="form-text text-muted">Vous pouvez zoomer sur les graphiques en glissant la souris avec le clic gauche sur une zone. Pour revenir en arrière, faites un clic droit.
		Vous pouvez aussi calculer la consommation totale sur une période en sélectionnant 2 points sur une série. Cette option ne fonctionne que sur une seule série 
		à la fois.</small>
</g:if>
