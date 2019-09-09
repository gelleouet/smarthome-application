<div>
	<a id="add-mode-button" class="btn btn-light" title="Ajouter" data-url="${ g.createLink(action: 'addMode', controller: 'mode') }">
   		<app:icon name="plus"/> Ajouter
   	</a>
   	
   	<table class="table table-hover">
		<thead>
			<tr>
				<th>Nom</th>
				<th class="column-1-buttons"></th>
			</tr>
		</thead>
	    <tbody>
			<g:each var="mode" in="${ modes?.sort{ it.name } }" status="status">
				<tr>
					<td>
						<g:textField name="modes[${ status }].name" value="${ mode.name }" class="form-control" required="true" maxlength="32"/>
						<g:if test="${ mode.id }">
							<g:hiddenField name="modes[${ status }].id" value="${ mode.id }"/>							
						</g:if>
						<g:hiddenField name="modes[${ status }].status" value="${ status }"/>
					</td>
					<td class="command-column">
		            	<a id="delete-mode-button" class="btn btn-light" title="Supprimer" data-url="${ g.createLink(action: 'deleteMode', controller: 'mode', params: [status: status]) }">
		            		<app:icon name="trash"/>
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
