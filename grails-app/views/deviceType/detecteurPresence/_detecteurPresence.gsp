<%@ page import="smarthome.automation.DeviceValue" %>

<div>
	<g:if test="${ device.value == 'true' }">
		<h2><span class="aui-lozenge aui-lozenge-complete" style="font-size: large;">MOUVEMENT !</span></h2>
	</g:if>
	<g:else>
		<h2><span class="aui-lozenge aui-lozenge-subtle" style="font-size: large;">RAS</span></h2>
	</g:else>
</div>

<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
