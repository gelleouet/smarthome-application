<div>
	<g:set var="ptec" value="${  device?.metadata('ptec') }"/>
	<g:set var="isousc" value="${  device?.metadata('isousc') }"/>
	<g:set var="imax" value="${  device?.metadata('imax') }"/>
	<g:set var="hchp" value="${  device?.metadata('hchp') }"/>
	<g:set var="hchc" value="${  device?.metadata('hchc') }"/>
	<g:set var="papp" value="${  device?.metadata('papp') }"/>
	
	<h2>
		<g:if test="${ ptec?.value == 'HC' }">
			<span class="aui-lozenge aui-lozenge-success">${ ptec?.value }</span>
		</g:if>
		<g:else>
			<span class="aui-lozenge">${ ptec?.value }</span>
		</g:else>
		
		Int: ${ device?.value ?: '-' }A / ${ isousc?.value ?: '-' }A (Max: ${ imax?.value ?: '-' }A)
	</h2>
	
	<h2>Puis : ${ papp?.value ?: '-' }VA</h2>
	
	<p>HC: ${ hchc?.value ?: '-' }Wh | HP: ${ hchp?.value ?: '-' }Wh</p>
	
	<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
</div>