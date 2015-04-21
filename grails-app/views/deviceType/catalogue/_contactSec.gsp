<div>
	<g:set var="value" value="${  device.value as Double }"/>
	<h2>
		<g:if test="${ value == 1 }">
			<span class="aui-lozenge aui-lozenge-success" style="font-size: large;">ON</span>
		</g:if>
		<g:else>
			<span class="aui-lozenge" style="font-size: large;">OFF</span>
		</g:else>
	</h2>
	
	<p style="padding-top:8px" class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
</div>