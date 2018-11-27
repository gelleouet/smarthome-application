<%@ page import="smarthome.automation.DeviceValue" %>

<g:set var="batterie" value="${ device.metavalueByLabel('battery')?.value }"/>

<div style="display:table;">
	<div style="display:table-cell; padding-right:10px;">
		<!--  gestion d'une valeur numérique ou boolean -->
		<g:if test="${ device.deviceImpl.isMovement() }">
			<h2><span class="aui-lozenge aui-lozenge-complete" style="font-size: large;">MOUVEMENT</span></h2>
		</g:if>
		<g:else>
			<h2><span class="aui-lozenge aui-lozenge-subtle" style="font-size: large;">RAS</span></h2>
		</g:else>
	</div>
	<div style="display:table-cell; padding-left:10px;; vertical-align:middle" class="separator-left">
		<span style="font-size:8pt" class="${ batterie?.isDouble() && ((batterie as Double) < 25) ? 'aui-lozenge aui-lozenge-error' : '' }"><strong>Batterie :</strong> ${ batterie }%</span>
	</div>
</div>
