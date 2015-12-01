<%@ page import="smarthome.automation.DeviceValue" %>

<div style="display:table;">
	<div style="display:table-cell; padding-right:10px;">
		<h2>${ device.value }Lux</h2>
	</div>
	
	<g:set var="values" value="${ DeviceValue.doubleValueAggregategByDay(device) }"/>
	
	<div style="display:table-cell; padding-left:10px;; vertical-align:top" class="separator-left">
		<p style="font-size:8pt">
		<span style="color:#815b3a"><strong>Min :</strong> ${ values?.min ?: '-' }Lux</span>
		<br/>
		<span style="color:#f6c342"><strong>Max :</strong> ${ values?.max ?: '-' }Lux</span>
		</p>
	</div>
</div>

<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
