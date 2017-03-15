<div class="field-group">
	<label>Modes</label>
	
	<div>
		<a id="add-mode-button" class="aui-button" title="Ajouter" data-url="${ g.createLink(action: 'addMode', controller: 'mode') }">
	   		<span class="aui-icon aui-icon-small aui-iconfont-add">Ajouter</span> Ajouter mode
	   	</a>
	   	
	   	<table class="aui datatable">
			<thead>
				<tr>
					<th>Nom</th>
					<th></th>
				</tr>
			</thead>
		    <tbody>
				<g:each var="mode" in="${ modes?.sort{ it.name } }" status="status">
					<tr>
						<td>
							<g:textField name="modes[${ status }].name" value="${ mode.name }" class="text long-field" required="true" maxlength="32"/>
							<g:if test="${ mode.id }">
								<g:hiddenField name="modes[${ status }].id" value="${ mode.id }"/>							
							</g:if>
							<g:hiddenField name="modes[${ status }].status" value="${ status }"/>
						</td>
						<td class="column-1-buttons command-column">
			            	<a id="delete-mode-button" class="aui-button aui-button-subtle" title="Supprimer" data-url="${ g.createLink(action: 'deleteMode', controller: 'mode', params: [status: status]) }">
			            		<span class="aui-icon aui-icon-small aui-iconfont-delete"></span>
			            	</a>
			            </td>
					</tr>	
				</g:each>
			</tbody>
		</table>
	</div>
	
	<%--<div class="description">Vous pouvez ajouter les modes prédéfinis <span class="aui-label">FERIE</span> <span class="aui-label">WEEKEND</span> <span class="aui-label">SEMAINE</span>. 
	Ils seront automatiquement activés au moment voulu.
	</div>--%>
</div>
