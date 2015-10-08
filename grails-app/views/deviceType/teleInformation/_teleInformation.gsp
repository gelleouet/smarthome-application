<div class="device-grid-body-content-large">
	<g:set var="ptec" value="${  device?.metavalue('ptec') }"/>
	<g:set var="isousc" value="${  device?.metavalue('isousc') }"/>
	<g:set var="imax" value="${  device?.metavalue('imax') }"/>
	<g:set var="hchp" value="${  device?.metavalue('hchp') }"/>
	<g:set var="hchc" value="${  device?.metavalue('hchc') }"/>
	<g:set var="papp" value="${  device?.metavalue('papp') }"/>
	<g:set var="hcinst" value="${  device?.metavalue('hcinst') }"/>
	<g:set var="hpinst" value="${  device?.metavalue('hpinst') }"/>
	
	<div style="float:left;display:block;">
		<p style="font-size:11pt; font-weight:bold;padding-bottom:5px">${ papp?.value ?: '-----' } VA
			<br/>
			
			<g:if test="${ ptec?.value?.startsWith('HC') }">
				<span style="font-size:12pt; font-weight:bold;" class="aui-lozenge aui-lozenge-subtle aui-lozenge-success">${ device?.value ?: '---' }A ${ ptec?.value }</span>
			</g:if>
			<g:else>
				<span style="font-size:12pt; font-weight:bold;" class="aui-lozenge aui-lozenge-subtle aui-lozenge-error">${ device?.value ?: '---' }A ${ ptec?.value ?: '----' }</span>
			</g:else>
		</p>
		
		<span class="h6">${ app.formatTimeAgo(date: device.dateValue) }</span>
	</div>
	
	
	<div style="float:right;font-size:8pt;display:block;" class="separator-left">
		<span class="aui-lozenge aui-lozenge-subtle">${ isousc?.value ?: '--' }A</span> 
		<span class="aui-lozenge aui-lozenge-subtle aui-lozenge-error">Max: ${ imax?.value ?: '---' }A</span>
		<p>
			<strong>HC : </strong> ${ hchc?.value ?: '---------' } Wh
			<br/>
			<strong>HP : </strong> ${ hchp?.value ?: '---------' } Wh
			</p>
	</div>
	
</div>