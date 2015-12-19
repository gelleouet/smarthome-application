<%@ page import="smarthome.automation.DeviceValue" %>

<div style="display:table;">
	<div style="display:table-cell; padding-right:10px;">
		<h2>${ device.value }°C</h2>
	</div>
	
	<g:set var="values" value="${ DeviceValue.doubleValueAggregategByDay(device) }"/>
	
	<div style="display:table-cell; padding-left:10px;; vertical-align:top" class="separator-left">
		<p style="font-size:8pt">
		<span style="color:#3b73af"><strong>Min :</strong> ${ values?.min != null ? values.min : '-' }°C</span>
		<br/>
		<span style="color:#d04437"><strong>Max :</strong> ${ values?.max != null ? values.max : '-' }°C</span>
		</p>
	</div>
</div>

<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
