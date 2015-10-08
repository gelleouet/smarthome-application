<div>
	<g:form controller="device">
		<g:hiddenField name="actionName" value="onOff"/>
		<g:hiddenField name="id" value="${ device.id }"/>
		
		<g:set var="value" value="${  device.value as Double }"/>
		
		<g:if test="${ value == 1 }">
			<g:actionSubmit value="OFF" class="aui-button confirm-button" action="invokeAction"/>
			<span class="aui-lozenge aui-lozenge-success" style="font-size: large;">ON</span>
		</g:if>
		<g:else>
			<g:actionSubmit value="ON" class="aui-button confirm-button" action="invokeAction"/>
			<span class="aui-lozenge" style="font-size: large;">OFF</span>
		</g:else>
		
		<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
	</g:form>
</div>