<%@ page import="smarthome.automation.DeviceValue" %>

<g:set var="compteurElectrique" value="${ house.compteurElectriqueImpl() }"/>
<g:set var="consos" value="${ compteurElectrique.consosJour() }"/>
<g:set var="device" value="${ house.compteur }"/>

<g:set var="ptec" value="${  device?.metavalue('ptec') }"/>
<g:set var="isousc" value="${  device?.metavalue('isousc') }"/>
<g:set var="imax" value="${  device?.metavalue('imax') }"/>
<g:set var="papp" value="${  device?.metavalue('papp') }"/>
<g:set var="hcinst" value="${  device?.metavalue('hcinst') }"/>
<g:set var="hpinst" value="${  device?.metavalue('hpinst') }"/>
<g:set var="baseinst" value="${  device?.metavalue('baseinst') }"/>

<h3>${ device.label }</h3>
	
<div class="aui-buttons" style="margin-top:10px;">
	<g:link class="aui-button" action="watmetre" controller="energie" id="${ device.id }">Watmètre</g:link>
</div>
	
<div class="aui-group" style="margin:10px 0px; padding:10px 0px">
	<div class="aui-item">
		<div style="text-align:center;">
			<g:set var="pourcentage" value="${ 80 }"/>
			
			<g:link controller="device" action="deviceChart" params="['device.id': device.id]">
				<div class="vignette-synthese" style="background: radial-gradient(#0747a6 ${pourcentage == 100 ? '100%' : ''}, orange ${pourcentage < 100 ? pourcentage + '%' : ''});">
					${ papp?.value ? papp?.value as Integer : '-' } 
					<span class="aui-icon aui-icon-small aui-iconfont-priority-low"></span>
				</div>
			</g:link>
			
			<h6 class="h6">(W) - Il y a ${ app.formatTimeAgo(date: device.dateValue) }</h6>
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
		</ul>
		
		<h5 style="margin-top:10px">Consommations jour</h5>
		
		<ul class="label" style="padding-inline-start:5px;">
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



	
