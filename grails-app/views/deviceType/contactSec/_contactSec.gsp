<g:set var="labelon" value="${ device?.metadata('labelon') }"/>
<g:set var="labeloff" value="${ device?.metadata('labeloff') }"/>
<g:set var="labelprior" value="${ device?.metadata('labelprior') }"/>

<g:set var="value" value="${  device.value as Double }"/>
<g:set var="lozengeClass" value="${ (value == 1 && labelprior?.value == 'on') || (value == 0 && labelprior?.value == 'off') ? 'aui-lozenge-complete' : 'aui-lozenge-subtle' }"/>

<div>
	<h2>
		<g:if test="${ value == 1 }">
			<span class="aui-lozenge ${ lozengeClass }" style="font-size: large;">${ labelon?.value ?: 'ON' }</span>
		</g:if>
		<g:else>
			<span class="aui-lozenge ${ lozengeClass }" style="font-size: large;">${ labeloff?.value ?: 'OFF' }</span>
		</g:else>
	</h2>
</div>