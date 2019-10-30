<%@ page import="smarthome.automation.ChartViewEnum" %>

<g:set var="googleChartTarif" value="${ command.deviceImpl.googleChartTarif(command, chart.values) }" scope="request"/>

<g:if test="${ googleChartTarif }">
	<g:if test="${ !command.deviceImpl.fournisseur }">
		<g:applyLayout name="messageWarning">
			Pour visualiser les coûts des consommations, veuillez sélectionner
				<g:link action="edit" controller="device" id="${ command.device.id }" fragment="tabs-device-configuration">un gestionnaire d'énergie</g:link>	
		</g:applyLayout>
	</g:if>
	
	<div id="chartDivTarif" data-chart-type="ColumnChart">
		<br/>
		<h6>Loading chart...</h6>
		<div class="aui-progress-indicator">
		    <span class="aui-progress-indicator-value"></span>
		</div>
		
		<g:render template="/chart/datas/chartDatas" model="[chart: googleChartTarif]"/>
	</div>  
	
	<small class="form-text text-muted">L’estimation financière en € de votre consommation se base sur le prix du kWh heures pleines du tarif réglementé, hors abonnement.</small>
	<br/>
	
	<g:if test="${ !mobileAgent }">
		<form class="form-inline">
			<div class="form-group">
				<label for="label">
					Coût sur la sélection
				</label>
				<g:textField name="selectionCout" class="form-control" readonly="true"/> €
				<a id="selectionCout-clear-button" class="btn btn-light ml-2" title="Effacer"><app:icon name="x"/></a>
				&nbsp;<span class="text-muted" id="selectionCout-label"></span>
			</div>
		</form>
		
		<small class="form-text text-muted">Vous pouvez zoomer sur les graphiques en glissant la souris avec le clic gauche sur une zone. Pour revenir en arrière, faites un clic droit.
			Vous pouvez aussi calculer la consommation totale sur une période en sélectionnant 2 points sur une série. Cette option ne fonctionne que sur une seule série 
			à la fois.</small>
	</g:if>
</g:if>
<g:else>
	<g:applyLayout name="messageWarning">
		Le graphique <strong>Tarif</strong> n'est pas géré par cet objet.
	</g:applyLayout>
</g:else>