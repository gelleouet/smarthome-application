<%@ page import="smarthome.automation.DeviceValue" %>

<g:set var="currentDate" value="${ currentDate ?: new Date() }"/>

<h4><g:if test="${ icon }"><app:icon name="${ icon }" lib="${ iconLib }"/></g:if> ${ title ?: 'Consommations jour' }</h4>

<g:if test="${ compteur }">
	<g:set var="compteurImpl" value="${ compteur.newDeviceImpl() }"/>
	<g:set var="consos" value="${ compteurImpl.consosJour(currentDate) }"/>
	<g:set var="defaultUnite" value="${ compteurImpl.defaultUnite() }"/>
	<g:set var="consoMoyenne" value="${ compteurImpl.consoMoyenneJour(currentDate-7, currentDate) }"/>
	<g:set var="interpretation" value="${ [pourcentage:100] }"/>
	

	<div class="container">
		<div class="row">
			<div class="col-4">
				
				<div style="margin-top:20px">
					<div class="separator-bottom">
						<div class="row">
							<div class="col-8">
								<h5>${ currentDate.format('EEEE dd MMMM yyyy') }</h5>
							</div>
							<div class="col text-right">
								<h5><span class="link">${ consos.tarifTotal != null ? (consos.tarifTotal as Double).round(1) : '-' }€</span>
								</h5>
							</div>
						</div>
					</div>	
					
					<div class="synthese-content">
							
						<g:link controller="device" action="deviceChart" params="['device.id': compteur.id]">
							<div class="vignette-synthese" style="background: radial-gradient(#47bac1 ${interpretation?.pourcentage == 100 ? '100%' : ''}, orange ${interpretation?.pourcentage < 100 ? interpretation?.pourcentage + '%' : ''});">
								${ consos.total }<span class="vignette-unite">${ defaultUnite }</span>
							</div>
						</g:link>
						
						<h6>Dernier relevé : ${ app.formatTimeAgo(date: compteur.dateValue) }</h6>
						
						<table class="table table-hover" style="margin-bottom:20px;">
							<thead>
								<tr>
									<th>${ consos.optTarif }</th>
									<th>${ defaultUnite }</th>
									<th>€</th>
								</tr>
							</thead>
							<tbody>
								<g:if test="${ consos.optTarif in ['HC', 'EJP'] }">
									<tr>
										<td>${ consos.optTarif == 'HC' ? 'Heures creuses' : 'Heures normales' }</td>
										<td><span class="link">${ (consos.hchc as Double)?.round(1) }</span></td>
										<td><span class="link">${ (consos.tarifHC as Double)?.round(1) }</span></td>
									</tr>
									<tr>
										<td>${ consos.optTarif == 'HC' ? 'Heures pleines' : 'Heures pointe mobile' }</td>
										<td><span class="link">${ (consos.hchp as Double)?.round(1) }</span></td>
										<td><span class="link">${ (consos.tarifHP as Double)?.round(1) }</span></td>
									</tr>
								</g:if>
								<g:else>
									<tr>
										<td>Heures base</td>
										<td><span class="link">${ (consos.base as Double)?.round(1) }</span></td>
										<td><span class="link">${ (consos.tarifBASE as Double)?.round(1) }</span></td>
									</tr>
								</g:else>
							</tbody>
						</table>
						
						<div style="mt-2">
							<div class="row">
								<div class="col-7">
									<strong>Consommation moyenne sur les 7 derniers jours</strong>
								</div>
								<div class="col">
									<strong>${ consoMoyenne }${ defaultUnite }</strong>
								</div>
							</div>
							
						</div>
					</div>
				</div>
			</div>
			<div class="col">
				<div>
					<g:include action="templateDeviceChart" controller="device" params="[viewMode: 'day',
						dateChart: app.formatPicker(date: currentDate), dateDebutUser: app.formatPicker(date: currentDate.clearTime()),
						'device.id': compteur.id, chartHeight: '350', suffixId: 'synthese-day']"/>	
				</div>
			</div>
		</div>
	</div>
	
	<div style="text-align:right; font-weight:bold;">
		<g:link class="btn btn-primary" controller="device" action="deviceChart" params="['device.id': compteur.id]">
			<app:icon name="arrow-right-circle"/> Voir le détail
		</g:link>
	</div>
</g:if>
<g:else>
	<p class="label">Profil incomplet : le compteur n'est pas renseigné</p>
</g:else>