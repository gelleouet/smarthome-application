<%@ page import="smarthome.automation.DeviceValue" %>

<g:set var="noeud" value="${ device.newDeviceImpl().convertToNoeud() }"/>
<g:set var="beaufort" value="${ device.newDeviceImpl().convertToBeaufort() }"/>

<div style="display:table;">
	<div style="display:table-cell; padding-right:10px;">
		<h2>${ device.value != null ? device.value : '-' }km/h</h2>
	</div>
	
	<g:set var="values" value="${ DeviceValue.doubleValueAggregategByDay(device) }"/>
	
	<div style="display:table-cell; padding-left:10px;; vertical-align:top" class="separator-left">
		<p style="font-size:8pt">
		<span style="color:#707070"><strong>Min :</strong> ${ values?.min != null ? values.min : '-' }km/h</span>
		<br/>
		<span style="color:#707070"><strong>Max :</strong> ${ values?.max != null ? values.max : '-' }km/h</span>
		</p>
		
		<p style="font-size:8pt">
		<span style="color:#707070"><strong>Noeud :</strong> ${ noeud != null ? noeud : '-' }kt</span>
		<br/>
		<span style="color:#707070"><strong>Beaufort :</strong> ${ beaufort?.force } - ${ beaufort?.desc}</span>
		</p>
	</div>
</div>
<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
