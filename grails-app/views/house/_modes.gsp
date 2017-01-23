<div class="field-group">
	<label></label>
	<table class="aui">
	    <tbody>
			<g:each var="mode" in="${ modes?.sort{ it.name } }" status="status">
				<tr>
					<td>
						<g:textField name="modes[${ status }].name" value="${ mode.name }" class="text medium-field" required="true"/>
						<g:hiddenField name="modes[${ status }].status" value="${ status }"/>
					</td>
					<td class="column-1-buttons command-column">
		            	<a class="aui-button aui-button-subtle" title="Supprimer" data-url="[action: 'deleteMode', controller: 'house', id: mode.id]">
		            		<span class="aui-icon aui-icon-small aui-iconfont-delete"></span>
		            	</a>
		            </td>
				</tr>	
			</g:each>
		</tbody>
	</table>
	
	<a class="aui-button aui-button-link" title="Ajouter" data-url="[action: 'deleteMode', controller: 'house', id: mode.id]">
   		<span class="aui-icon aui-icon-small aui-iconfont-add">Ajouter</span>
   	</a>
</div>
