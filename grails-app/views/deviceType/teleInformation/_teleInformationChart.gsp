<%@ page import="smarthome.automation.ChartViewEnum" %>

<g:if test="${ !command.deviceImpl.fournisseur }">
	<g:applyLayout name="messageWarning">
		Pour visualiser les coûts des consommations, veuillez sélectionner
			<g:link action="edit" controller="device" id="${ command.device.id }" fragment="tabs-device-configuration">un gestionnaire d'énergie</g:link>	
	</g:applyLayout>
</g:if>


<p class="separator" style="text-align:center; font-weight:bold; font-size: medium;">Consommations (Wh)</p>

<div id="chartDivConso" data-chart-type="${ command.deviceImpl.defaultChartType().factory }">
	<br/>
	<h6 class="h6">Loading chart...</h6>
	<div class="aui-progress-indicator">
	    <span class="aui-progress-indicator-value"></span>
	</div>
	<g:render template="${ command.deviceImpl.chartDataTemplate() }" model="[command: command, datas: datas]"/>
</div>


<p class="separator" style="text-align:center; font-weight:bold; font-size: medium;">Tarifs consommations (€)</p>

<g:set var="googleChartTarif" value="${ command.deviceImpl.googleChartTarif(command, datas) }"/>

<div id="chartDivTarif" data-chart-type="ColumnChart">
	<br/>
	<h6 class="h6">Loading chart...</h6>
	<div class="aui-progress-indicator">
	    <span class="aui-progress-indicator-value"></span>
	</div>
	
	<g:render template="/chart/datas/chartTarifDatas" model="[chart: googleChartTarif]"/>
</div>


<p class="separator" style="text-align:center; font-weight:bold; font-size: medium;">Synthèse
	<g:if test="${ command.viewMode == ChartViewEnum.day }">
		<g:formatDate date="${ command.dateChart }" format="dd/MM/yyyy"/>
	</g:if>
	<g:elseif test="${ command.viewMode == ChartViewEnum.month }">
		<g:formatDate date="${ command.dateChart }" format="MMMM yyyy"/>
	</g:elseif>
	<g:else>
		<g:formatDate date="${ command.dateChart }" format="yyyy"/>
	</g:else>
</p>


<table class="aui app-datatable" style="margin-top:20px;" data-client-pagination="true">
	<thead>
		<tr>
			<th></th>
			<th colspan="2" style="text-align:center">HC</th>
			<th colspan="2" style="text-align:center">HP</th>
			<th colspan="2" style="text-align:center">TOTAL</th>
		</tr>
		<tr>
			<th></th>
			<th style="text-align:center">Consommation</th>
			<th style="text-align:center">Tarif</th>
			<th style="text-align:center">Consommation</th>
			<th style="text-align:center">Tarif</th>
			<th style="text-align:center">Consommation</th>
			<th style="text-align:center">Tarif</th>
		</tr>
	</thead>
	<tbody>
		<g:each var="data" in="${ googleChartTarif?.values?.sort { it.key } }">
			<tr>
				<td>
					<g:if test="${ command.viewMode == ChartViewEnum.day }">
						<g:formatDate date="${ data.key }" format="HH:mm"/>
					</g:if>
					<g:elseif test="${ command.viewMode == ChartViewEnum.month }">
						<g:formatDate date="${ data.key }" format="dd/MM/yyyy"/>
					</g:elseif>
					<g:else>
						<g:formatDate date="${ data.key }" format="MMMM yyyy"/>
					</g:else>	
				</td>
				<td style="text-align:center"><span class="link">${ data.value.find{ it.name == 'HC'}?.kwh?.round(1) }kWh</span></td>
				<td style="text-align:center"><span class="link">${ data.value.find{ it.name == 'HC'}?.prix?.round(3) }€</span></td>
				<td style="text-align:center"><span class="link">${ data.value.find{ it.name == 'HP'}?.kwh?.round(1) }kWh</span></td>
				<td style="text-align:center"><span class="link">${ data.value.find{ it.name == 'HP'}?.prix?.round(3) }€</span></td>
				<td style="text-align:center; font-weight:bold;"><span class="link">${ data.value.sum{ it.kwh ?: 0d }?.round(1) }kWh</span></td>
				<td style="text-align:center; font-weight:bold;"><span class="link">${ data.value.sum{ it.prix ?: 0d }?.round(3) }€</span></td>
			</tr>
		</g:each>
	</tbody>
	
	<g:set var="googleChartTarifValues" value="${ googleChartTarif?.aggregateValues }"/>
	
	<tfoot>
		<td><strong>TOTAL</strong></td>
		<td style="text-align:center; font-weight:bold;">${ googleChartTarifValues?.findAll { it.name == 'HC'}?.sum { it.kwh ?: 0d }?.round(1) }kWh</td>
		<td style="text-align:center; font-weight:bold;">${ googleChartTarifValues?.findAll { it.name == 'HC'}?.sum { it.prix ?: 0d }?.round(3) }€</td>
		<td style="text-align:center; font-weight:bold;">${ googleChartTarifValues?.findAll { it.name == 'HP'}?.sum { it.kwh ?: 0d }?.round(1) }kWh</td>
		<td style="text-align:center; font-weight:bold;">${ googleChartTarifValues?.findAll { it.name == 'HP'}?.sum { it.prix ?: 0d }?.round(3) }€</td>
		<td style="text-align:center; font-weight:bold;">${ googleChartTarifValues?.sum{ it.kwh ?: 0d }?.round(1) }kWh</span></td>
		<td style="text-align:center; font-weight:bold;">${ googleChartTarifValues?.sum{ it.prix ?: 0d }?.round(3) }€</span></td>
	</tfoot>
</table>


