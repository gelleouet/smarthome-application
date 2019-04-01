<%@ page import="smarthome.automation.ChartViewEnum" %>

<g:set var="compteur" value="${ command.device.newDeviceImpl() }"/>

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

<div style="overflow-x:auto;">
	<table class="aui app-datatable" style="margin-top:20px;" data-client-pagination="true">
		<thead>
			<tr>
				<th></th>
				<g:if test="${ compteur.optTarif == 'HC' }">
					<th colspan="2" style="text-align:center">HC</th>
					<th colspan="2" style="text-align:center">HP</th>
				</g:if>
				<g:elseif test="${ compteur.optTarif == 'EJP' }">
					<th colspan="2" style="text-align:center">HN</th>
					<th colspan="2" style="text-align:center">HPM</th>
				</g:elseif>
				<th colspan="2" style="text-align:center">TOTAL</th>
			</tr>
			<tr>
				<th></th>
				<g:if test="${ compteur.optTarif in ['HC', 'EJP'] }">
					<th style="text-align:center">Consommation</th>
					<th style="text-align:center">Tarif</th>
					<th style="text-align:center">Consommation</th>
					<th style="text-align:center">Tarif</th>
				</g:if>
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
							<g:formatDate date="${ data.key }" format="EEEE dd/MM/yyyy"/>
						</g:elseif>
						<g:else>
							<g:formatDate date="${ data.key }" format="MMMM yyyy"/>
						</g:else>	
					</td>
					<g:if test="${ compteur.optTarif in ['HC', 'EJP'] }">
						<td style="text-align:center"><span class="link">${ data.value['kwhHC']?.round(1) }kWh</span></td>
						<td style="text-align:center"><span class="link">${ data.value['prixHC']?.round(3) }€</span></td>
						<td style="text-align:center"><span class="link">${ data.value['kwhHP']?.round(1) }kWh</span></td>
						<td style="text-align:center"><span class="link">${ data.value['prixHP']?.round(3) }€</span></td>
					</g:if>
					<td style="text-align:center; font-weight:bold;"><span class="link">${ data.value['kwh']?.round(1) }kWh</span></td>
					<td style="text-align:center; font-weight:bold;"><span class="link">${ data.value['prix']?.round(3) }€</span></td>
				</tr>
			</g:each>
		</tbody>
		
		<g:set var="googleChartTarifValues" value="${ googleChartTarif?.values?.values() }"/>
		
		<tfoot>
			<td><strong>TOTAL</strong></td>
			<g:if test="${ compteur.optTarif in ['HC', 'EJP'] }">
				<td style="text-align:center; font-weight:bold;">
					<g:formatNumber number="${ googleChartTarifValues?.sum { it.kwhHC ?: 0d } }" maxFractionDigits="1"/>kWh
				</td>
				<td style="text-align:center; font-weight:bold;">
					<g:formatNumber number="${ googleChartTarifValues?.sum { it.prixHC ?: 0d } }" maxFractionDigits="3"/>€
				</td>
				<td style="text-align:center; font-weight:bold;">
					<g:formatNumber number="${ googleChartTarifValues?.sum { it.kwhHP ?: 0d } }" maxFractionDigits="1"/>kWh
				</td>
				<td style="text-align:center; font-weight:bold;">
					<g:formatNumber number="${ googleChartTarifValues?.sum { it.prixHP ?: 0d } }" maxFractionDigits="3"/>€
				</td>
			</g:if>
			<td style="text-align:center; font-weight:bold;">
				<g:formatNumber number="${ googleChartTarifValues?.sum{ it.kwh ?: 0d } }" maxFractionDigits="1"/>kWh
			</td>
			<td style="text-align:center; font-weight:bold;">
				<g:formatNumber number="${ googleChartTarifValues?.sum{ it.prix ?: 0d } }" maxFractionDigits="3"/>€
			</td>
		</tfoot>
	</table>
</div>    

