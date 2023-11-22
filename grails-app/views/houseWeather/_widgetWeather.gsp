<g:if test="${ house?.latitude && house?.longitude  }">
	<g:if test="${ forecast }">
		<h3>${ house.location }</h3>
		
		<div>
			<span class="h6"><g:formatDate date="${ forecast.date }" format="EEEE dd/MM/yyyy H'h'"/></span>
		</div>
		
		<div class="aui-buttons" style="margin-top:10px;">
			<g:link class="aui-button" action="hourlyForecast" controller="houseWeather" id="${ house.id }">24 heures</g:link>
			<g:link class="aui-button" action="dailyForecast" controller="houseWeather" id="${ house.id }">7 jours</g:link>
		</div>
		
		<div class="aui-group separator-bottom" style="margin:10px 0px; padding:10px 0px">
			<div class="aui-item" style="text-align:left; width:60%">
				<p style="font-size:30px; color:#707070"><i class="wi ${ forecast.style }"></i> ${ forecast.text }</p>
				
				<p class="h6-nomargin" style="text-transform:none;">Couverture nuageuse ${ forecast.cloudCover }% <i class="wi wi-cloud"></i></p>
				
				<p style="margin-top:20px; color:#707070"><i style="font-size:14pt;" class="wi wi-sunrise"></i> <g:formatDate date="${ dailyForecast.sunrise }" format="HH:mm"/>
			    	<i style="font-size:14pt;" class="wi wi-sunset"></i> <g:formatDate date="${ dailyForecast.sunset }" format="HH:mm"/></p>
			</div>
			<div class="aui-item separator-left" style="text-align:right">
				<p style="font-size:18px; color:#707070">${ forecast.pressure }<span style="font-size:small;">hPa</span> <i style="font-size:14pt;" class="wi wi-barometer"></i></p>
				<p style="font-size:18px; color:#707070">${ forecast.humidity }<span style="font-size:small;">%</span> <i style="font-size:14pt;" class="wi wi-raindrop"></i></p>
				<p style="font-size:18px; color:#707070">${ forecast.uvIndex }<span style="font-size:small;">UV</span> <i style="font-size:14pt;" class="wi wi-hot"></i></p>
			</div>
		</div>
		
		<div class="aui-group" style="margin-top:5px">
			<div class="aui-item" style="text-align:center; width:25%">
				<h2>${ forecast.temperature }° <i class="wi wi-thermometer"></i></h2>
				<h6>Ressenti : ${ forecast.apparentTemperature }°</h6>	
			</div>
			<div class="aui-item" style="text-align:center; width:35%">
				<h2>${ forecast.precipProbability }% <i class="wi wi-rain"></i></h2>
				<h6>${ forecast.precipType }</h6>		
			</div>
			<div class="aui-item" style="text-align:center; width:40%">
				<h2>${ forecast.windSpeed }<span style="font-size:small;">km/h</span> <i style="font-size:30pt;" class="wi wi-wind from-${ forecast.windBearing }-deg"></i>	</h2>
				<h6>Rafale : ${ forecast.windGust }km/h</h6>	
			</div>
		</div>
	</g:if>
	<g:else>
		<p class="label">Aucune donnée météo trouvée.</p>
	</g:else>
</g:if>
<g:else>
	<h3>Prévisions météo</h3>
	
	<p class="label">Veuillez renseigner <g:link action="profil" controller="profil">latitude et longitude <span class="aui-icon aui-icon-small aui-iconfont-weblink"></span></g:link> de votre habitation principale pour avoir les prévisions météo</p>
</g:else>

<div style="text-align:right; margin-top:10px;">
	<a style="font-size:x-small; font-weight:bold;" href="https://developer.apple.com/weatherkit/get-started/" target="weatherkit">Powered by Apple WeatherKit</a>
</div>