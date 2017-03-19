<g:each var="metadata" in="${ device.metadatas?.findAll({ !(it.name in (exclude ?: [])) })?.sort{ (it.type ?: '') + it.label } }" status="status">

	<g:set var="index" value="${ status + (startStatus ?: 0) }"/>

	<g:if test="${ metadata.id }">
		<g:hiddenField name="metadatas[${ index }].id" value="${ metadata.id }"/>
	</g:if>
	
	<div class="field-group">
		<label title="API : device.metadata('${ metadata.name }')?.value">
			${ metadata.label}
		</label>
		<g:textField name="metadatas[${ index }].value" value="${ metadata.value }" class="text long-field"/>
		
		<g:if test="${ metadata.type }">
			<span class="aui-lozenge aui-lozenge-complete aui-lozenge-subtle">${ metadata.type }</span>
		</g:if>
		
		<g:if test="${ commitButton && device.agent }">
			<a id="commit-metadata-button" class="aui-button aui-button-subtle" title="Envoyer au périphérique"
				data-url="${ g.createLink(action: 'changeMetadata', params: [metadataName: metadata.name]) }">
           		<span class="aui-icon aui-icon-small aui-iconfont-share-list">
           	</a>
		</g:if>
		
		<g:if test="${ metadata.values }">
			<div class="description"><strong><ul>
				<g:each var="value" in="${ metadata.values.split('##')  }" status="valueStatus">
					<li>${ valueStatus } : ${ value }</li>
				</g:each>
			</ul></strong></div>
		</g:if>
	</div>

</g:each>