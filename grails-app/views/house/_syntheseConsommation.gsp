<%@ page import="smarthome.automation.DeviceValue" %>

<g:set var="currentDate" value="${ new Date() }"/>
<g:set var="currentYear" value="${ currentDate[Calendar.YEAR] }"/>

<h3>Synthèse consommations</h3>

<g:if test="${ house?.compteur }">

	<g:set var="compteurElectrique" value="${ house.compteurElectriqueImpl() }"/>
					
	<g:set var="interpretation" value="${ houseSynthese?.interpretations[house.compteur.id] }"/>
	<g:set var="first_hchp" value="${ DeviceValue.firstValueByDay(house?.compteur, 'hchp') }"/>
	<g:set var="last_hchp" value="${ DeviceValue.lastValueByDay(house?.compteur, 'hchp') }"/>
	<g:set var="first_hchc" value="${ DeviceValue.firstValueByDay(house?.compteur, 'hchc') }"/>
	<g:set var="last_hchc" value="${ DeviceValue.lastValueByDay(house?.compteur, 'hchc') }"/>
	<g:set var="hchp" value="${ first_hchp?.value && last_hchp?.value ? (last_hchp.value - first_hchp.value) / 1000.0 : 0.0 }"/>
	<g:set var="hchc" value="${ first_hchc?.value && last_hchc?.value ? (last_hchc.value - first_hchc.value) / 1000.0 : 0.0 }"/>
	<g:set var="hctotal" value="${ (hchp + hchc as Double).round(1) }"/>
	
	<g:set var="tarifHP" value="${ compteurElectrique.calculTarif('HP', hchp, currentYear) }"/>
	<g:set var="tarifHC" value="${ compteurElectrique.calculTarif('HC', hchc, currentYear) }"/>
	<g:set var="tarifTotal" value="${ (tarifHP != null || tarifHC != null) ? (tarifHP ?: 0.0) + (tarifHC ?: 0.0) : null }"/>


	<div class="aui-group">
		<div class="aui-item responsive" style="width: 33.3%">
			
			<div style="margin-top:20px">
				<div class="separator-bottom">
					<div class="aui-group aui-group-split">
						<div class="aui-item">
							<h4 >${ app.formatUser(date: currentDate) }</h4>
						</div>
						<div class="aui-item">
							<span class="link">${ tarifTotal != null ? (tarifTotal as Double).round(1) : '-' }€</span>
						</div>
					</div>
				</div>	
				
				<div class="synthese-content">
						
					<g:link controller="device" action="deviceChart" params="['device.id': house.compteur.id]">
						<div class="vignette-synthese" style="background: radial-gradient(#3572b0 ${interpretation?.pourcentage == 100 ? '100%' : ''}, orange ${interpretation?.pourcentage < 100 ? interpretation?.pourcentage + '%' : ''});">
							${ hctotal }kWh
						</div>
					</g:link>
					<h6 class="h6">Dernier relevé : ${ app.formatTimeAgo(date: house.compteur.dateValue) }</h6>
					
					<table class="aui datatable" style="margin-bottom:20px;">
						<thead>
							<tr>
								<th></th>
								<th>kWh</th>
								<th>€</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>HP</td>
								<td><span class="link">${ hchp as Integer }</span></td>
								<td><span class="link">${ tarifHP }</span></td>
							</tr>
							<tr>
								<td>HC</td>
								<td><span class="link">${ hchc as Integer }</span></td>
								<td><span class="link">${ tarifHC }</span></td>
							</tr>
						</tbody>
					</table>
					
					<p style="text-align:left"><strong>Interprétation :</strong></p>
				</div>
			</div>
		</div>
		<div class="aui-item responsive">
			<div>
				<g:include action="templateDeviceChart" controller="device" params="[viewMode: 'month', dateChart: app.formatPicker(date: new Date()), 'device.id': house.compteur.id, chartHeight: '350']"/>	
			</div>
		</div>
	</div>

</g:if>