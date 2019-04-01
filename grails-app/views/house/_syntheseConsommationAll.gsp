<%@ page import="smarthome.automation.DeviceValue" %>
<%@ page import="smarthome.core.DateUtils" %>

<g:set var="currentDate" value="${ new Date() }"/>

<h3>Synthèse consommations</h3>

<g:if test="${ house?.compteur }">
	<g:set var="compteurElectrique" value="${ house.compteurElectriqueImpl() }"/>
	<g:set var="consoJour" value="${ compteurElectrique.consosJour() }"/>
	<g:set var="consoMois" value="${ compteurElectrique.consosMois() }"/>
	<g:set var="consoAnnee" value="${ compteurElectrique.consosAnnee() }"/>
	<g:set var="interpretation" value="${ [pourcentage:100] }"/>
	

	<div class="aui-group">
		<div class="aui-item responsive">
			<div style="margin-top:20px">
				<div class="separator-bottom">
					<div class="aui-group aui-group-split">
						<div class="aui-item" style="width:30%">
							<h4>${ app.formatUser(date: currentDate) }</h4>
						</div>
						<div class="aui-item">
							<h4><span class="link">${ consoJour.tarifTotal != null ? (consoJour.tarifTotal as Double).round(1) : '-' }€</span>
								<g:if test="${ params.compare }">
									<g:link class="aui-button" style="float:right; margin-left:10px;" action="compareHouseDeviceChart" controller="device" params="['device.id': house.compteur.id]">Comparer</g:link>
								</g:if>
							</h4>
						</div>
					</div>
				</div>	
				
				<div class="synthese-content">
						
					<g:link controller="device" action="deviceChart" params="['device.id': house.compteur.id, viewMode: 'year']">
						<div class="vignette-synthese" style="background: radial-gradient(#0747a6 ${interpretation?.pourcentage == 100 ? '100%' : ''}, orange ${interpretation?.pourcentage < 100 ? interpretation?.pourcentage + '%' : ''});">
							${ consoJour.total }
							<span class="aui-icon aui-icon-small aui-iconfont-priority-low"></span>
						</div>
					</g:link>
					<h6 class="h6">(kWh) - Dernier relevé : ${ app.formatTimeAgo(date: house.compteur.dateValue) }</h6>
					
					<table class="aui datatable" style="margin-bottom:20px;">
						<thead>
							<tr>
								<th>${ consoJour.optTarif }</th>
								<th>kWh</th>
								<th>€</th>
							</tr>
						</thead>
						<tbody>
							<g:if test="${ consoJour.optTarif in ['HC', 'EJP'] }">
								<tr>
									<td>${ consoJour.optTarif == 'HC' ? 'Heures creuses' : 'Heures normales' }</td>
									<td><span class="link">${ (consoJour.hchc as Double)?.round(1) }</span></td>
									<td><span class="link">${ (consoJour.tarifHC as Double)?.round(1) }</span></td>
								</tr>
								<tr>
									<td>${ consoJour.optTarif == 'HC' ? 'Heures pleines' : 'Heures pointe mobile' }</td>
									<td><span class="link">${ (consoJour.hchp as Double)?.round(1) }</span></td>
									<td><span class="link">${ (consoJour.tarifHP as Double)?.round(1) }</span></td>
								</tr>
							</g:if>
							<g:else>
								<tr>
									<td>Toutes heures</td>
									<td><span class="link">${ (consoJour.base as Double)?.round(1) }</span></td>
									<td><span class="link">${ (consoJour.tarifBASE as Double)?.round(1) }</span></td>
								</tr>
							</g:else>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		
		<div class="aui-item responsive">
			<div style="margin-top:20px">
				<div class="separator-bottom">
					<div class="aui-group aui-group-split">
						<div class="aui-item" style="width:30%">
							<h4><g:formatDate date="${ currentDate }" format="MMMM yyyy"/></h4>
						</div>
						<div class="aui-item">
							<h4><span class="link">${ consoMois.tarifTotal != null ? (consoMois.tarifTotal as Double).round(1) : '-' }€</span>
								<g:if test="${ params.compare }">
									<g:link class="aui-button" style="float:right; margin-left:10px;" action="compareHouseDeviceChart" controller="device" params="['device.id': house.compteur.id]">Comparer</g:link>
								</g:if>
							</h4>
						</div>
					</div>
				</div>	
				
				<div class="synthese-content">
						
					<g:link controller="device" action="deviceChart" params="['device.id': house.compteur.id, viewMode: 'year']">
						<div class="vignette-synthese" style="background: radial-gradient(#0747a6 ${interpretation?.pourcentage == 100 ? '100%' : ''}, orange ${interpretation?.pourcentage < 100 ? interpretation?.pourcentage + '%' : ''});">
							${ consoMois.total }
							<span class="aui-icon aui-icon-small aui-iconfont-priority-low"></span>
						</div>
					</g:link>
					<h6 class="h6">(kWh) - Dernier relevé : ${ app.formatTimeAgo(date: house.compteur.dateValue) }</h6>
					
					<table class="aui datatable" style="margin-bottom:20px;">
						<thead>
							<tr>
								<th>${ consoMois.optTarif }</th>
								<th>kWh</th>
								<th>€</th>
							</tr>
						</thead>
						<tbody>
							<g:if test="${ consoMois.optTarif in ['HC', 'EJP'] }">
								<tr>
									<td>${ consoMois.optTarif == 'HC' ? 'Heures creuses' : 'Heures normales' }</td>
									<td><span class="link">${ (consoMois.hchc as Double)?.round(1) }</span></td>
									<td><span class="link">${ (consoMois.tarifHC as Double)?.round(1) }</span></td>
								</tr>
								<tr>
									<td>${ consoMois.optTarif == 'HC' ? 'Heures pleines' : 'Heures pointe mobile' }</td>
									<td><span class="link">${ (consoMois.hchp as Double)?.round(1) }</span></td>
									<td><span class="link">${ (consoMois.tarifHP as Double)?.round(1) }</span></td>
								</tr>
							</g:if>
							<g:else>
								<tr>
									<td>Toutes heures</td>
									<td><span class="link">${ (consoMois.base as Double)?.round(1) }</span></td>
									<td><span class="link">${ (consoMois.tarifBASE as Double)?.round(1) }</span></td>
								</tr>
							</g:else>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		
		<div class="aui-item responsive">
			<div style="margin-top:20px">
				<div class="separator-bottom">
					<div class="aui-group aui-group-split">
						<div class="aui-item" style="width:30%">
							<h4><g:formatDate date="${ currentDate }" format="yyyy"/></h4>
						</div>
						<div class="aui-item">
							<h4><span class="link">${ consoAnnee.tarifTotal != null ? (consoAnnee.tarifTotal as Double).round(1) : '-' }€</span>
								<g:if test="${ params.compare }">
									<g:link class="aui-button" style="float:right; margin-left:10px;" action="compareHouseDeviceChart" controller="device" params="['device.id': house.compteur.id]">Comparer</g:link>
								</g:if>
							</h4>
						</div>
					</div>
				</div>	
				
				<div class="synthese-content">
						
					<g:link controller="device" action="deviceChart" params="['device.id': house.compteur.id, viewMode: 'year']">
						<div class="vignette-synthese" style="background: radial-gradient(#0747a6 ${interpretation?.pourcentage == 100 ? '100%' : ''}, orange ${interpretation?.pourcentage < 100 ? interpretation?.pourcentage + '%' : ''});">
							${ consoAnnee.total }
							<span class="aui-icon aui-icon-small aui-iconfont-priority-low"></span>
						</div>
					</g:link>
					<h6 class="h6">(kWh) - Dernier relevé : ${ app.formatTimeAgo(date: house.compteur.dateValue) }</h6>
					
					<table class="aui datatable" style="margin-bottom:20px;">
						<thead>
							<tr>
								<th>${ consoAnnee.optTarif }</th>
								<th>kWh</th>
								<th>€</th>
							</tr>
						</thead>
						<tbody>
							<g:if test="${ consoAnnee.optTarif in ['HC', 'EJP'] }">
								<tr>
									<td>${ consoAnnee.optTarif == 'HC' ? 'Heures creuses' : 'Heures normales' }</td>
									<td><span class="link">${ (consoAnnee.hchc as Double)?.round(1) }</span></td>
									<td><span class="link">${ (consoAnnee.tarifHC as Double)?.round(1) }</span></td>
								</tr>
								<tr>
									<td>${ consoMois.optTarif == 'HC' ? 'Heures pleines' : 'Heures pointe mobile' }</td>
									<td><span class="link">${ (consoAnnee.hchp as Double)?.round(1) }</span></td>
									<td><span class="link">${ (consoAnnee.tarifHP as Double)?.round(1) }</span></td>
								</tr>
							</g:if>
							<g:else>
								<tr>
									<td>Toutes heures</td>
									<td><span class="link">${ (consoAnnee.base as Double)?.round(1) }</span></td>
									<td><span class="link">${ (consoAnnee.tarifBASE as Double)?.round(1) }</span></td>
								</tr>
							</g:else>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		
	</div>
</g:if>
<g:else>
	<p class="label">Profil incomplet : les objets par défaut ne sont pas renseignés</p>
</g:else>