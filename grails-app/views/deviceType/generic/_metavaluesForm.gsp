<g:each var="metadata" in="${ device.metavalues?.sort{ (it.type ?: '') + it.label } }" status="status">

	<g:if test="${ metadata.id }">
		<g:hiddenField name="metavalues[${ status }].id" value="${ metadata.id }"/>
	</g:if>
	
	<div class="field-group">
		<label title="API : device.metavalue('${ metadata.name }')?.value">
			${ metadata.label}
		</label>
		<g:textField name="metavalues[${ status }].value" value="${ metadata.value }" class="text long-field" disabled="true"/>
		
		<g:if test="${ metadata.type }">
			<span class="aui-lozenge aui-lozenge-complete aui-lozenge-subtle">${ metadata.type }</span>
		</g:if>
	</div>

</g:each>