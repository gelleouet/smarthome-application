<div class="device-grid-body-content-large">
	<g:set var="ptec" value="${  device?.metavalue('ptec') }"/>
	<g:set var="isousc" value="${  device?.metavalue('isousc') }"/>
	<g:set var="imax" value="${  device?.metavalue('imax') }"/>
	<g:set var="hchp" value="${  device?.metavalue('hchp') }"/>
	<g:set var="hchc" value="${  device?.metavalue('hchc') }"/>
	<g:set var="papp" value="${  device?.metavalue('papp') }"/>
	
	<div style="float:left;display:block;">
		<h2>${ papp?.value ?: '-----' } VA
			<g:if test="${ ptec?.value == 'HC' }">
				<span class="aui-lozenge aui-lozenge-success">${ ptec?.value }</span>
			</g:if>
			<g:else>
				<span class="aui-lozenge">${ ptec?.value ?: '----' }</span>
			</g:else>
		</h2>
			
		<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
	</div>
	
	
	<div style="float:right;font-size:8pt;display:block;" class="separator-left">
		<span class="aui-lozenge aui-lozenge-subtle">${ device?.value ?: '---' }A / ${ isousc?.value ?: '--' }A</span> 
		<span class="aui-lozenge aui-lozenge-subtle aui-lozenge-error">Max: ${ imax?.value ?: '---' }A</span>
		<p><strong>HC : </strong> ${ hchc?.value ?: '---------' } Wh<br/>
		<strong>HP : </strong> ${ hchp?.value ?: '---------' } Wh</p>
	</div>
	
</div>