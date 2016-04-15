<%@ page import="smarthome.automation.DeviceValue" %>

<g:set var="alarme" value="${ device.metavalueByLabel('alarm type')?.value }"/>
<g:set var="batterie" value="${ device.metavalueByLabel('battery')?.value }"/>

<div style="display:table;">
	<div style="display:table-cell; padding-right:10px;">
		<g:if test="${ device.value?.toDouble() > 0 }">
			<h2><span class="aui-lozenge aui-lozenge-error" style="font-size: large;">FUMEE</span></h2>
		</g:if>
		<g:elseif test="${ device.metavalueByLabel('heat')?.value?.toDouble() > 0 }">
			<h2><span class="aui-lozenge aui-lozenge-error" style="font-size: large;">FEU</span></h2>
		</g:elseif>
		<g:else>
			<h2><span class="aui-lozenge aui-lozenge-subtle" style="font-size: large;">RAS</span></h2>
		</g:else>
	</div>
	<div style="display:table-cell; padding-left:10px;; vertical-align:middle" class="separator-left">
		<span style="font-size:8pt" class="${ batterie?.isDouble() && ((batterie as Double) < 25) ? 'aui-lozenge aui-lozenge-error' : '' }"><strong>Batterie :</strong> ${ batterie }%</span>
		<br/>
		<span style="font-size:8pt" class="${ alarme?.isDouble() && ((alarme as Double) > 0) ? 'aui-lozenge aui-lozenge-current' : '' }"><strong>Alarme :</strong> ${ alarme }</span>
	</div>
</div>
