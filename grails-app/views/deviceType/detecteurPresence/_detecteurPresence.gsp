<%@ page import="smarthome.automation.DeviceValue" %>

<g:set var="batterie" value="${ device.metavalueByLabel('battery')?.value }"/>
<g:set var="tamper" value="${ device.metavalueByLabel('general')?.value }"/>

<div style="display:table;">
	<div style="display:table-cell; padding-right:10px;">
		<g:if test="${ device.value == 'true' }">
			<h2><span class="aui-lozenge aui-lozenge-complete" style="font-size: large;">MOUVEMENT</span></h2>
		</g:if>
		<g:else>
			<h2><span class="aui-lozenge aui-lozenge-subtle" style="font-size: large;">RAS</span></h2>
		</g:else>
	</div>
	<div style="display:table-cell; padding-left:10px;; vertical-align:middle" class="separator-left">
		<span style="font-size:8pt" class="${ batterie?.isDouble() && ((batterie as Double) < 25) ? 'aui-lozenge aui-lozenge-error' : '' }"><strong>Batterie :</strong> ${ batterie }%</span>
		<br/>
		<span style="font-size:8pt" class="${ tamper?.isDouble() && ((tamper as Double) > 0) ? 'aui-lozenge aui-lozenge-current' : '' }"><strong>Secousse :</strong> ${ tamper }</span>
	</div>
</div>

<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
