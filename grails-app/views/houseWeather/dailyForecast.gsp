<%@page import="smarthome.core.DateUtils" %>

<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>

	<nav class="aui-navgroup aui-navgroup-horizontal">
	    <div class="aui-navgroup-inner">
	        <div class="aui-navgroup-primary">
	            <ul class="aui-nav">
	                <li><g:link action="devicesGrid" controller="device" params="[favori: true]">Favoris</g:link></li>
	                <g:each var="tableauBord" in="${ tableauBords }">
						<li>
							<g:link action="devicesGrid" controller="device" params="[tableauBord: tableauBord]">${ tableauBord }</g:link>
						</li>	                
	                </g:each>
	            </ul>
	        </div><!-- .aui-navgroup-primary -->
	    </div><!-- .aui-navgroup-inner -->
	</nav>


	<g:applyLayout name="applicationHeader">
		<div class="aui-group aui-group-split">
			<div class="aui-item responsive">
				<h3>Prévisions à 7j, ${ house.location }</h3>
			</div>
			<div class="aui-item responsive">
				<div class="aui-buttons" style="margin-top:0px;">
					<g:link class="aui-button" action="hourlyForecast" controller="houseWeather" id="${ house.id }">Prévisions à 24h</g:link>
				</div>
			</div>
		</div>
	</g:applyLayout>


	<g:applyLayout name="applicationContent">
		<div style="text-align:right;">
			<a style="font-size:x-small; font-weight:bold;" href="https://developer.apple.com/weatherkit/get-started/" target="weatherkit">Powered by Apple WeatherKit</a>
		</div>
		
		<div style="overflow-x:auto; margin-top:10px;">
		<table class="aui datatable">
		    <tbody>
		    	<g:set var="currentDate" value="${ new Date().clearTime() }"/>
		    
		    	<g:each var="forecast" in="${ dailyForecasts }">
		    		<g:set var="rowStyle" value="${ currentDate == forecast.date ? 'font-weight:bold; color:#0747a6;' : '' }"/>
		    		
			        <tr style="${ rowStyle }">
			            <td>
			            	<p><g:formatDate date="${ forecast.date }" format="EEEE dd/MM/yyyy"/></p>
			            	
			            	<p><i style="font-size:14pt;" class="wi wi-sunrise"></i> <g:formatDate date="${ forecast.sunrise }" format="HH:mm"/>
			            	<i style="font-size:14pt;" class="wi wi-sunset"></i> <g:formatDate date="${ forecast.sunset }" format="HH:mm"/></p>
			            </td>
			            <td>
			            	<span style="font-size:18px;"><i class="wi ${ forecast.style }"></i></span> ${ forecast.text }
			            	<span class="h6-nomargin" title="Couverture nuageuse">${ forecast.cloudCover }% <i class="wi wi-cloud"></i></span>
			            </td>
			            <td style="text-align:center; min-width:75px;">
			            	<p><i style="font-size:14pt;" class="wi wi-direction-up"></i> ${ forecast.temperatureHigh }° <i style="font-size:14pt;" class="wi wi-thermometer"></i>
			            	<span class="h6-nomargin" title="Ressentie">${ forecast.apparentTemperatureHigh }°</span></p>
			            	
			            	<p class="h6-nomargin"><g:formatDate date="${ forecast.temperatureHighTime }" format="HH:mm"/></p>
			            </td>
			            <td style="text-align:center; min-width:75px;">
			            	<p><i style="font-size:14pt;" class="wi wi-direction-down"></i> ${ forecast.temperatureLow }° <i style="font-size:14pt;" class="wi wi-thermometer"></i>
			            	<span class="h6-nomargin" title="Ressentie">${ forecast.apparentTemperatureLow }°</span></p>
			            	
			            	<p class="h6-nomargin"><g:formatDate date="${ forecast.temperatureLowTime }" format="HH:mm"/></p>
			            </td>
			            <td style="text-align:center;">
			            	${ forecast.precipProbability }% <i style="font-size:14pt;" class="wi wi-rain"></i>
			            	<span class="h6-nomargin">${ forecast.precipType }</span>
			            	
			            	<p class="h6-nomargin"><g:formatDate date="${ forecast.precipIntensityMaxTime }" format="HH:mm"/></p>
			            </td>
			            <td style="text-align:center;">
			            	${ forecast.windSpeed }km/h <i style="font-size:18pt;" class="wi wi-wind from-${ forecast.windBearing }-deg"></i>
			            	<span class="h6-nomargin" title="Rafale">${ forecast.windGust }km/h</span>
			            	
			            	<p class="h6-nomargin"><g:formatDate date="${ forecast.windGustTime }" format="HH:mm"/></p>
			            </td>
			            <td style="min-width:75px;">
			            	${ forecast.pressure }hPa <i style="font-size:14pt;" class="wi wi-barometer"></i>
			            	<g:if test="${ mobileAgent }"><br/></g:if><g:else>&nbsp;</g:else>
			            	${ forecast.humidity }% <i style="font-size:14pt;" class="wi wi-raindrop"></i>
			            	<g:if test="${ mobileAgent }"><br/></g:if><g:else>&nbsp;</g:else>
			            	${ forecast.uvIndex }uv <i style="font-size:14pt;" class="wi wi-hot"></i>
			            	
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</table>
		</div>
	</g:applyLayout>
	
</body>
</html>