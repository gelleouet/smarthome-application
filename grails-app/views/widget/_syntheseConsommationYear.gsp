<%@ page import="smarthome.automation.DeviceValue" %>
<%@ page import="smarthome.core.DateUtils" %>

<g:set var="currentDate" value="${ new Date() }"/>

<h4>
<g:render template="/widget/syntheseConsommationIcon"/> ${ title ?: 'Consommations année' }
</h4>

<g:if test="${ house?.compteur }">
	<g:set var="compteurElectrique" value="${ house.compteurElectriqueImpl() }"/>
	<g:set var="consos" value="${ compteurElectrique.consosAnnee() }"/>
	<g:set var="interpretation" value="${ [pourcentage:100] }"/>
	

	<div class="container">
		<div class="row">
			<div class="col-4">
				
				<div style="margin-top:20px">
					<div class="separator-bottom">
						<div class="row">
							<div class="col-8">
								<h5><g:formatDate date="${ currentDate }" format="yyyy"/></h5>
							</div>
							<div class="col text-right">
								<h5><span class="link">${ consos.tarifTotal != null ? (consos.tarifTotal as Double).round(1) : '-' }€</span>
								</h5>
							</div>
						</div>
					</div>	
					
					<div class="synthese-content">
							
						<g:link controller="device" action="deviceChart" params="['device.id': house.compteur.id, viewMode: 'year']">
							<div class="vignette-synthese" style="background: radial-gradient(#47bac1 ${interpretation?.pourcentage == 100 ? '100%' : ''}, orange ${interpretation?.pourcentage < 100 ? interpretation?.pourcentage + '%' : ''});">
								${ consos.total as Integer }<span class="vignette-unite"> kWh</span>
							</div>
						</g:link>
						
						<h6 class="mt-2">Dernier relevé : ${ app.formatTimeAgo(date: house.compteur.dateValue) }</h6>
						
						<table class="table table-hover" style="margin-bottom:20px;">
							<thead>
								<tr>
									<th>${ consos.optTarif }</th>
									<th>kWh</th>
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
					</div>
				</div>
			</div>
			<div class="col">
				<div>
					<g:include action="templateDeviceChart" controller="device" params="[viewMode: 'year',
						dateChart: app.formatPicker(date: new Date()), dateDebutUser: app.formatPicker(date: DateUtils.firstDayInYear(currentDate)),
						'device.id': house.compteur.id, chartHeight: '350', suffixId: 'synthese-year']"/>	
				</div>
			</div>
		</div>
	</div>
	
	<div style="text-align:right; font-weight:bold;">
		<g:link class="btn btn-primary" controller="device" action="deviceChart" params="['device.id': house.compteur.id, viewMode: 'year']">
			<app:icon name="arrow-right-circle"/> Voir le détail
		</g:link>
	</div>
</g:if>
<g:else>
	<g:applyLayout name="messageWarning">
		Profil incomplet : le compteur n'est pas renseigné
		<g:link controller="compteur" action="compteur" class="btn btn-link"><app:icon name="settings"/> Configurer votre compteur</g:link>
	</g:applyLayout>
</g:else>