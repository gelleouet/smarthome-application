<%@ page import="smarthome.automation.DeviceValue" %>


<div class="device-grid-body-content-large">
	<g:set var="ptec" value="${  device?.metavalue('ptec') }"/>
	<g:set var="isousc" value="${  device?.metavalue('isousc') }"/>
	<g:set var="imax" value="${  device?.metavalue('imax') }"/>
	<g:set var="papp" value="${  device?.metavalue('papp') }"/>
	<g:set var="hcinst" value="${  device?.metavalue('hcinst') }"/>
	<g:set var="hpinst" value="${  device?.metavalue('hpinst') }"/>
	
	<div style="float:left;display:block;">
		<p style="font-size:11pt; font-weight:bold;padding-bottom:5px">${ papp?.value ?: '-' } VA
			<br/>
			
			<g:if test="${ ptec?.value?.startsWith('HC') }">
				<span style="font-size:12pt; font-weight:bold;" class="aui-lozenge aui-lozenge-subtle aui-lozenge-success">${ hcinst?.value ?: '-' } Wh ${ ptec?.value }</span>
			</g:if>
			<g:else>
				<span style="font-size:12pt; font-weight:bold;" class="aui-lozenge aui-lozenge-subtle aui-lozenge-error">${ hpinst?.value ?: '-' } Wh ${ ptec?.value ?: '-' }</span>
			</g:else>
		</p>
		
		<span class="h6">${ app.formatTimeAgo(date: device.dateValue) }</span>
	</div>
	
	<g:set var="first_hchp" value="${ DeviceValue.firstValueByDay(device, 'hchp') }"/>
	<g:set var="last_hchp" value="${ DeviceValue.lastValueByDay(device, 'hchp') }"/>
	<g:set var="first_hchc" value="${ DeviceValue.firstValueByDay(device, 'hchc') }"/>
	<g:set var="last_hchc" value="${ DeviceValue.lastValueByDay(device, 'hchc') }"/>
	
	<g:set var="hchp" value="${ first_hchp?.value && last_hchp?.value ? (last_hchp.value.longValue() - first_hchp.value.longValue()) / 1000 : 0 }"/>
	<g:set var="hchc" value="${ first_hchc?.value && last_hchc?.value ? (last_hchc.value.longValue() - first_hchc.value.longValue()) / 1000 : 0 }"/>
	
	<div style="float:right;font-size:8pt;display:block;" class="separator-left">
		<span class="aui-lozenge aui-lozenge-subtle">${ device?.value ?: '-' }A / ${ isousc?.value ?: '--' }A</span> 
		<span class="aui-lozenge aui-lozenge-subtle aui-lozenge-error desktop-only">Max: ${ imax?.value ?: '---' }A</span>
		<p>
			<strong>HC : </strong> ${ hchc.toDouble().round(1) } kWh
			<br/>
			<strong>HP : </strong> ${ hchp.toDouble().round(1) } kWh
			</p>
	</div>
	
</div>