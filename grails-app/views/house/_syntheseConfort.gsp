<%@ page import="smarthome.automation.DeviceValue" %>

<h3>Confort habitat</h3>

<g:if test="${ house?.temperature || house?.humidite }">
	<div class="aui-group" style="margin-top:20px">
		<div class="aui-item responsive">
			<div>
				<div class="separator-bottom">
					<div class="aui-group aui-group-split">
						<div class="aui-item">
							<h4>Température</h4>
						</div>
						<div class="aui-item">
						</div>
					</div>	
				</div>
				
				<div class="synthese-content">
					<g:if test="${ house?.temperature }">
						<g:set var="interpretation" value="${ houseSynthese?.interpretations[house.temperature.id] }"/>
					
						<g:link controller="device" action="deviceChart" params="['device.id': house.temperature.id]">
							<div class="vignette-synthese" style="background: radial-gradient(#0747a6 ${interpretation?.pourcentage == 100 ? '100%' : ''}, orange ${interpretation?.pourcentage < 100 ? interpretation?.pourcentage + '%' : ''});">
								${ house.temperature.value }<span class="vignette-unite">°C</span>
							</div>
						</g:link>
						<h6 class="h6">Dernier relevé : ${ app.formatTimeAgo(date: house.temperature.dateValue) }</h6>
						<p style="text-align:left"><strong>Interprétation :</strong> ${ interpretation?.commentaire }</p>
					</g:if>
					<g:elseif test="${ house?.user?.id == secUser?.id }">
						<p class="h6"><span class="aui-icon aui-icon-small aui-iconfont-warning"></span> 
						L'objet <g:link controller="profil" action="profil">Température principale</g:link> n'est pas configuré sur votre profil !</p>
					</g:elseif>
					
					<div style="text-align:right; font-weight:bold; margin-top:5px;">
						<g:link class="link" controller="device" action="deviceChart" params="['device.id': house.temperature.id]">
							<span class="aui-icon aui-icon-small aui-iconfont-arrows-right"></span> Voir le détail
						</g:link>
					</div>
				</div>
			</div>
		</div>
		<div class="aui-item responsive">
			<div>
				<div class="separator-bottom">
					<div class="aui-group aui-group-split">
						<div class="aui-item">
							<h4>Humidité</h4>
						</div>
						<div class="aui-item">
						</div>
					</div>	
				</div>
				
				<div class="synthese-content">
					<g:if test="${ house?.humidite }">
						<g:set var="interpretation" value="${ houseSynthese?.interpretations[house.humidite.id] }"/>
						
						<g:link controller="device" action="deviceChart" params="['device.id': house.humidite.id]">
							<div class="vignette-synthese" style="background: radial-gradient(#0747a6 ${interpretation?.pourcentage == 100 ? '100%' : ''}, orange ${interpretation?.pourcentage < 100 ? interpretation?.pourcentage + '%' : ''});">
								${ house.humidite.value }<span class="vignette-unite">%</span>
							</div>
						</g:link>
						<h6 class="h6">Dernier relevé : ${ app.formatTimeAgo(date: house.humidite.dateValue) }</h6>
						<p style="text-align:left"><strong>Interprétation :</strong> ${ interpretation?.commentaire }</p>
					</g:if>
					<g:elseif test="${ house?.user?.id == secUser?.id }">
						<h6 class="h6"><span class="aui-icon aui-icon-small aui-iconfont-warning"></span> 
						L'objet <g:link controller="profil" action="profil">Humidité principale</g:link> n'est pas configuré sur votre profil !</h6>
					</g:elseif>
					
					<div style="text-align:right; font-weight:bold; margin-top:5px;">
						<g:link class="link" controller="device" action="deviceChart" params="['device.id': house.humidite.id]">
							<span class="aui-icon aui-icon-small aui-iconfont-arrows-right"></span> Voir le détail
						</g:link>
					</div>
				</div>
			</div>
		</div>
		<div class="aui-item responsive">
			<div>
				<div class="separator-bottom">
					<div class="aui-group aui-group-split">
						<div class="aui-item">
							<h4>Qualité air</h4>
						</div>
						<div class="aui-item">
						</div>
					</div>	
				</div>
				
				<div class="synthese-content">
					<div class="vignette-synthese" style="background: radial-gradient(#0747a6 '100%', orange '');">
						-	
					</div>
					<h6 class="h6">Dernier relevé :</h6>
					<p style="text-align:left"><strong>Interprétation :</strong></p>
				</div>
			</div>
		</div>
	</div>
</g:if>
<g:else>
	<p class="label">Profil incomplet : les objets par défaut ne sont pas renseignés</p>
</g:else>