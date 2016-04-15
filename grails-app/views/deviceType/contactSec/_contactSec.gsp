<div>
	<g:set var="value" value="${  device.value as Double }"/>
	<h2>
		<g:if test="${ value == 1 }">
			<span class="aui-lozenge aui-lozenge-complete" style="font-size: large;">ON</span><span class="aui-lozenge aui-lozenge-subtle" style="font-size: large;">OFF</span>
		</g:if>
		<g:else>
			<span class="aui-lozenge aui-lozenge-subtle" style="font-size: large;">ON</span><span class="aui-lozenge aui-lozenge-complete" style="font-size: large;">OFF</span>
		</g:else>
	</h2>
</div>