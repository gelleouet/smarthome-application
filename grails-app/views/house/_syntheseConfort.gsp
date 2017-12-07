<%@ page import="smarthome.automation.DeviceValue" %>

<h3>Synthèse confort habitat</h3>

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
			<h4 class="separator-bottom">Qualité air</h4>
			
			<div class="synthese-content">
				<div class="vignette-synthese" style="background: radial-gradient(#3572b0, orange 100%);">
					-	
				</div>
				<h6 class="h6">Dernier relevé :</h6>
				<p style="text-align:left"><strong>Interprétation :</strong></p>
			</div>
		</div>
	</div>
</div>
