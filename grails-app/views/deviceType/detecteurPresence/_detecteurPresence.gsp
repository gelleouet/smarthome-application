<%@ page import="smarthome.automation.DeviceValue" %>

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
		<p style="font-size:8pt">
		<strong>Battery :</strong> ${ device.metavalueByLabel('battery')?.value }%
		</p>
	</div>
</div>

<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>