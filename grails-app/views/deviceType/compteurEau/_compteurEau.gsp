<%@ page import="smarthome.automation.DeviceValue" %>

<g:set var="consoInst" value="${  device?.metavalue('conso')?.value as Double }"/>
<g:set var="coefConversion" value="${  device?.metadata('coefConversion') }"/>
<g:set var="first_index" value="${ DeviceValue.firstValueByDay(device) }"/>
<g:set var="last_index" value="${ DeviceValue.lastValueByDay(device) }"/>
<g:set var="consoJour" value="${ 0.0 }"/>

<g:if test="${ first_index && last_index }">
	<g:set var="consoJour" value="${ last_index.value - first_index.value }"/>	
</g:if>

<g:if test="${ coefConversion?.value }">
	<g:set var="consoJour" value="${ consoJour * coefConversion.value.toDouble() }"/>
	
	<g:if test="${ consoInst != null  }">
		<g:set var="consoInst" value="${ consoInst * coefConversion.value.toDouble() }"/>
	</g:if>
</g:if>


<div style="display:table;">
	<div style="display:table-cell; padding-right:10px;">
		<h2>${ consoInst != null ? consoInst :  '-' } ${ device?.metadata('unite')?.value }</h2>
	</div>
	
	<div style="display:table-cell; padding-left:10px;; vertical-align:top" class="separator-left">
		<p style="font-size:8pt">
		<span style="color:#707070;"><strong>Index total :</strong> ${ (device?.value as Double)?.longValue() }</span>
		<br/>
		<span style="color:#707070;"><strong>Conso journalière :</strong> ${ consoJour.round(2) } ${ device?.metadata('unite')?.value }</span>
		</p>
	</div>
</div>
