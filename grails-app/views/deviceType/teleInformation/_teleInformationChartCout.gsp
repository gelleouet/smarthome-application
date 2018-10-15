<%@ page import="smarthome.automation.ChartViewEnum" %>

<g:set var="googleChartTarif" value="${ command.deviceImpl.googleChartTarif(command, chart.values) }" scope="request"/>

<g:if test="${ !command.deviceImpl.fournisseur }">
	<g:applyLayout name="messageWarning">
		Pour visualiser les coûts des consommations, veuillez sélectionner
			<g:link action="edit" controller="device" id="${ command.device.id }" fragment="tabs-device-configuration">un gestionnaire d'énergie</g:link>	
	</g:applyLayout>
</g:if>

<g:set var="googleChartTarif" value="${ command.deviceImpl.googleChartTarif(command, chart.values) }" scope="request"/>

<g:if test="${ !mobileAgent }">
	<h6>
		Vous pouvez zoomer sur les graphiques en glissant la souris avec le clic gauche sur une zone. Pour revenir en arrière, faites un clic droit.
		Vous pouvez aussi calculer le coût total sur une période en sélectionnant 2 points sur une série. Cette option ne fonctionne que sur une seule série 
		à la fois.
	</h6>
	
	<form class="aui">
		<div class="field-group">
			<label for="label">
				Coût sur la sélection
			</label>
			<g:textField name="selectionCout" class="text medium-field" readonly="true"/> €
		</div>
	</form>
</g:if>

<div id="chartDivTarif" data-chart-type="ColumnChart">
	<br/>
	<h6 class="h6">Loading chart...</h6>
	<div class="aui-progress-indicator">
	    <span class="aui-progress-indicator-value"></span>
	</div>
	
	<g:render template="/chart/datas/chartTarifDatas" model="[chart: googleChartTarif]"/>
</div>  