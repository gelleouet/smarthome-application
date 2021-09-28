<%@ page import="smarthome.automation.deviceType.AezeoBallonSolaire" %>
<%@ page import="smarthome.automation.DeviceChartCommand" %>

<g:set var="deviceImpl" value="${ device.deviceImpl }"/>
<g:set var="command" value="${ new DeviceChartCommand(device: device, deviceImpl: deviceImpl, dateChart: device.dateValue ?: new Date()) }"/>
<g:set var="chart" value="${ deviceImpl.googleChartLight(command) }"/>

<div class="aui-group">
	<div class="aui-item" style="width:235">
		<svg version="1.1" width="230" height="230" viewBox="0 0 230 230" xmlns="http://www.w3.org/2000/svg">
			<title>BallonSolaire</title>
			
				<rect x="10" y="10" width="100" height="200" rx="5" fill="url(#def1)" stroke="#707070" stroke-width="2"/>
			
				<rect x="109.97" y="19.958" width="10" height="10" fill="#ff0000" stroke="#707070" stroke-width="2"/>
			
				<rect x="109.92" y="189.53" width="10" height="10" fill="#007fff" stroke="#707070" stroke-width="2"/>
			
				<path  fill="none" stroke="#a8a8a8" stroke-width="4.3283" d="m8.6487 201.62h90.045c1.9277 0 3.709-1.0284 4.6729-2.6979s0.96386-3.7263 0-5.3958-2.7452-2.6979-4.6729-2.6979h-74.452c-1.9277 0-3.709-1.0284-4.6729-2.6979-0.96386-1.6695-0.96386-3.7263 0-5.3958 0.96386-1.6694 2.7451-2.6979 4.6729-2.6979h74.452c1.9277 0 3.709-1.0284 4.6729-2.6979s0.96386-3.7263 0-5.3958c-0.96386-1.6695-2.7451-2.6979-4.6729-2.6979h-74.452c-1.9277 0-3.709-1.0284-4.6729-2.6979-0.96386-1.6694-0.96386-3.7263 0-5.3958 0.96386-1.6694 2.7452-2.6979 4.6729-2.6979h74.452c1.9277 0 3.709-1.0284 4.6729-2.6979 0.96386-1.6695 0.96386-3.7263 0-5.3958s-2.7451-2.6979-4.6729-2.6979h-90.045"/>
				<defs>
					<linearGradient id="def1" x1=".5" x2=".5" y2="1">
						<stop stop-color="#ff0000" stop-opacity=".99609" offset="15%"/>
						<stop stop-color="#FF9D00" stop-opacity=".99609" offset="50%"/>
						<stop stop-color="#007fff" stop-opacity=".99609" offset="85%"/>
					</linearGradient>
				</defs>
	
				<circle cx="60" cy="43" r="2" fill-opacity="0" stroke="#707070"/>
				<line x1="60" x2="140" y1="43" y2="43" fill="none" stroke="#707070" stroke-dasharray="5, 5"/>
				<circle cx="140" cy="43" r="2" fill-opacity="0" stroke="#707070"/>
				<text x="145" y="50" fill="#707070" font-family="serif" font-size="20px">${ deviceImpl.temperatureHaut() ?: '-' }°C</text>
	
			
	
				<circle cx="60" cy="109" r="2" fill-opacity="0" stroke="#707070"/>
				<line x1="60" x2="140" y1="109" y2="109" fill="none" stroke="#707070" stroke-dasharray="5, 5"/>
				<circle cx="140" cy="109" r="2" fill-opacity="0" stroke="#707070"/>
				<text x="145" y="116" fill="#707070" font-family="serif" font-size="20px">${ deviceImpl.temperatureMilieu() ?: '-' }°C</text>
	
			
	
				<circle cx="60" cy="175" r="2" fill-opacity="0" stroke="#707070"/>
				<line x1="60" x2="140" y1="175" y2="175" fill="none" stroke="#707070" stroke-dasharray="5, 5"/>
				<circle cx="140" cy="175" r="2" fill-opacity="0" stroke="#707070"/>
				<text x="145" y="182" fill="#707070" font-family="serif" font-size="20px">${ deviceImpl.temperatureBas() ?: '-' }°C</text>
		</svg>
	</div>
	<div class="aui-item">
		<div id="chartDiv-${ device.id }" data-chart-type="${ chart.chartType }">
			<br/>
			<h6 class="h6">Loading chart...</h6>
			<div class="aui-progress-indicator">
			    <span class="aui-progress-indicator-value"></span>
			</div>
			<g:render template="${ deviceImpl.chartDataTemplate() }" model="[label: device.label, datas: chart.values, chart: chart, chartHeight: 230]"/>
		</div>
	</div>
</div>