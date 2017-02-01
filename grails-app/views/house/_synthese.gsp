<h3>Synthèse confort habitat et consommation</h3>

<div class="aui-group" style="margin-top:20px">
	<div class="aui-item responsive">
		<div>
			<h4 class="separator-bottom">Température</h4>
			
			<div class="synthese-content">
				<g:if test="${ house?.temperature }">
					<g:set var="interpretation" value="${ houseSynthese?.interpretations[house.temperature.id] }"/>
				
					<div class="vignette-synthese" style="background: radial-gradient(#3572b0 ${interpretation?.pourcentage == 100 ? '100%' : ''}, orange ${interpretation?.pourcentage < 100 ? interpretation?.pourcentage + '%' : ''});">
						${ house.temperature.value }°C
					</div>
					<h6 class="h6">Dernier relevé : ${ app.formatTimeAgo(date: house.temperature.dateValue) }</h6>
					<p style="text-align:left"><strong>Interprétation :</strong> ${ interpretation?.commentaire }</p>
				</g:if>
				<g:else>
					<p class="h6"><span class="aui-icon aui-icon-small aui-iconfont-warning"></span> 
					L'objet <g:link controller="user" action="profil">Température principale</g:link> n'est pas configuré sur votre profil !</p>
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
					
					<div class="vignette-synthese" style="background: radial-gradient(#3572b0 ${interpretation?.pourcentage == 100 ? '100%' : ''}, orange ${interpretation?.pourcentage < 100 ? interpretation?.pourcentage + '%' : ''});">
						${ house.humidite.value }%
					</div>
					<h6 class="h6">Dernier relevé : ${ app.formatTimeAgo(date: house.humidite.dateValue) }</h6>
					<p style="text-align:left"><strong>Interprétation :</strong> ${ interpretation?.commentaire }</p>
				</g:if>
				<g:else>
					<h6 class="h6"><span class="aui-icon aui-icon-small aui-iconfont-warning"></span> 
					L'objet <g:link controller="user" action="profil">Humidité principale</g:link> n'est pas configuré sur votre profil !</h6>
				</g:else>
			</div>
		</div>
	</div>
	<div class="aui-item responsive">
		<div>
			<h4 class="separator-bottom">Consommation</h4>
			
			<g:if test="${ house?.compteur }">
			
			</g:if>
			<g:else>
				<h6 class="h6"><span class="aui-icon aui-icon-small aui-iconfont-warning"></span> 
				L'objet <g:link controller="user" action="profil">Compteur principal</g:link> n'est pas configuré sur votre profil !</h6>
			</g:else>
		</div>
	</div>
</div>
