<%@ page import="smarthome.automation.DeviceValue" %>

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
		<p style="font-size:8pt">
		<strong>Battery :</strong> ${ device.metavalueByLabel('battery')?.value }%
		<br/>
		<strong>Alarm :</strong> ${ device.metavalueByLabel('alarm type')?.value }
		</p>
	</div>
</div>

<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
