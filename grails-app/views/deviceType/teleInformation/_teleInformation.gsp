<%@ page import="smarthome.automation.DeviceValue" %>


<g:set var="compteurElectrique" value="${ device.newDeviceImpl() }"/>
<g:set var="consos" value="${ compteurElectrique.consosJour() }"/>

<div class="device-grid-body-content-large">
	<g:set var="ptec" value="${  device?.metavalue('ptec') }"/>
	<g:set var="isousc" value="${  device?.metavalue('isousc') }"/>
	<g:set var="imax" value="${  device?.metavalue('imax') }"/>
	<g:set var="papp" value="${  device?.metavalue('papp') }"/>
	<g:set var="hcinst" value="${  device?.metavalue('hcinst') }"/>
	<g:set var="hpinst" value="${  device?.metavalue('hpinst') }"/>
	<g:set var="baseinst" value="${  device?.metavalue('baseinst') }"/>
	
	<div class="aui-group" >
		<div class="aui-item">
			<div style="text-align:center; margin-top:10px">
				<g:link controller="device" action="deviceChart" params="['device.id': device.id]">
					<span style="font-size:24px" class="aui-lozenge aui-lozenge-subtle">${ papp?.value ? papp?.value as Integer : '-' }W</span>
				</g:link>
			</div>
		</div>
		<div class="aui-item separator-left">
			<ul class="label" style="padding-inline-start:5px;">
				<li>Contrat : ${ consos.optTarif } ${ isousc?.value ?: '--' }A</li>
				<li>Période :
					<g:if test="${ ptec?.value in ['HP', 'HPM'] }">
						<span class="aui-lozenge aui-lozenge-subtle aui-lozenge-error">${ ptec?.value ?: '-' }</span>
					</g:if>
					<g:else>
						<span class="aui-lozenge aui-lozenge-subtle aui-lozenge-success">${ ptec?.value }</span>	
					</g:else> 
				</li>
				<g:if test="${ consos.optTarif == 'HC' }">
					<li>HC : ${ (consos.hchc as Double)?.round(1) } kWh</li>
					<li>HP : ${ (consos.hchp as Double)?.round(1) } kWh</li>
				</g:if>
				<g:elseif test="${ consos.optTarif == 'EJP' }">
					<li>HN : ${ (consos.hchc as Double)?.round(1) } kWh</li>
					<li>HPM : ${ (consos.hchp as Double)?.round(1) } kWh</li>
				</g:elseif>
				<g:else>
					<li>BASE : ${ (consos.base as Double)?.round(1) } kWh</li>
				</g:else>
			</ul>
			
		</div>
	</div>
	
</div>