<%@ page import="smarthome.automation.DeviceValue" %>

<h3>Synthèse confort habitat et consommation</h3>

<div class="aui-group" style="margin-top:20px">
	<div class="aui-item responsive">
		<div>
			<h4 class="separator-bottom">Température</h4>
			
			<div class="synthese-content">
				<g:if test="${ house?.temperature }">
					<g:set var="interpretation" value="${ houseSynthese?.interpretations[house.temperature.id] }"/>
				
					<g:link controller="device" action="deviceChart" params="['device.id': house.temperature.id]">
						<div class="vignette-synthese" style="background: radial-gradient(#3572b0 ${interpretation?.pourcentage == 100 ? '100%' : ''}, orange ${interpretation?.pourcentage < 100 ? interpretation?.pourcentage + '%' : ''});">
							${ house.temperature.value }°C
						</div>
					</g:link>
					<h6 class="h6">Dernier relevé : ${ app.formatTimeAgo(date: house.temperature.dateValue) }</h6>
					<p style="text-align:left"><strong>Interprétation :</strong> ${ interpretation?.commentaire }</p>
				</g:if>
				<g:else>
					<p class="h6"><span class="aui-icon aui-icon-small aui-iconfont-warning"></span> 
					L'objet <g:link controller="profil" action="profil">Température principale</g:link> n'est pas configuré sur votre profil !</p>
				</g:else>
			</div>
		</div>
	</div>
	<div class="aui-item responsive">
		<div>
			<h4 class="separator-bottom">Humidité </h4>
			
			<div class="synthese-content">
				<g:if test="${ house?.humidite }">
					<g:set var="interpretation" value="${ houseSynthese?.interpretations[house.humidite.id] }"/>
					
					<g:link controller="device" action="deviceChart" params="['device.id': house.humidite.id]">
						<div class="vignette-synthese" style="background: radial-gradient(#3572b0 ${interpretation?.pourcentage == 100 ? '100%' : ''}, orange ${interpretation?.pourcentage < 100 ? interpretation?.pourcentage + '%' : ''});">
							${ house.humidite.value }%
						</div>
					</g:link>
					<h6 class="h6">Dernier relevé : ${ app.formatTimeAgo(date: house.humidite.dateValue) }</h6>
					<p style="text-align:left"><strong>Interprétation :</strong> ${ interpretation?.commentaire }</p>
				</g:if>
				<g:else>
					<h6 class="h6"><span class="aui-icon aui-icon-small aui-iconfont-warning"></span> 
					L'objet <g:link controller="profil" action="profil">Humidité principale</g:link> n'est pas configuré sur votre profil !</h6>
				</g:else>
			</div>
		</div>
	</div>
	<div class="aui-item responsive">
		<div>
			<h4 class="separator-bottom">Consommation</h4>
			
			<div class="synthese-content">
				<g:if test="${ house?.compteur }">
					<g:set var="interpretation" value="${ houseSynthese?.interpretations[house.compteur.id] }"/>
					<g:set var="first_hchp" value="${ DeviceValue.firstValueByDay(house?.compteur, 'hchp') }"/>
					<g:set var="last_hchp" value="${ DeviceValue.lastValueByDay(house?.compteur, 'hchp') }"/>
					<g:set var="first_hchc" value="${ DeviceValue.firstValueByDay(house?.compteur, 'hchc') }"/>
					<g:set var="last_hchc" value="${ DeviceValue.lastValueByDay(house?.compteur, 'hchc') }"/>
					<g:set var="hchp" value="${ first_hchp?.value && last_hchp?.value ? (last_hchp.value - first_hchp.value) / 1000.0 : 0.0 }"/>
					<g:set var="hchc" value="${ first_hchc?.value && last_hchc?.value ? (last_hchc.value - first_hchc.value) / 1000.0 : 0.0 }"/>
					
					<g:link controller="device" action="deviceChart" params="['device.id': house.compteur.id]">
						<div class="vignette-synthese" style="background: radial-gradient(#3572b0 ${interpretation?.pourcentage == 100 ? '100%' : ''}, orange ${interpretation?.pourcentage < 100 ? interpretation?.pourcentage + '%' : ''});">
								${ (hchp + hchc as Double).round(1) }kWh
							</div>
					</g:link>
					<h6 class="h6">Dernier relevé : ${ app.formatTimeAgo(date: house.compteur.dateValue) }</h6>
					<p style="text-align:left"><strong>Interprétation :</strong> ${ interpretation?.commentaire }</p>
				</g:if>
				<g:else>
					<h6 class="h6"><span class="aui-icon aui-icon-small aui-iconfont-warning"></span> 
					L'objet <g:link controller="profil" action="profil">Compteur principal</g:link> n'est pas configuré sur votre profil !</h6>
				</g:else>
			</div>
		</div>
	</div>
</div>
