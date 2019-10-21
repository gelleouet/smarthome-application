<table class="table datatable">
	<thead>
		<tr>
			<th>Principale?</th>
			<th>Nom</th>
			<th>Valeur</th>
			<th>Unité</th>
			<th>Type</th>
			<th>Historisation</th>
			<th>Objet séparé</th>
		</tr>
	</thead>
	<tbody>
		<g:each var="metadata" in="${ device.metavalues?.sort{ (it.type ?: '') + it.label } }" status="status">
			<tr>
				<td class="text-center">
					<g:if test="${ metadata.id }">
						<g:hiddenField name="metavalues[${ status }].id" value="${ metadata.id }"/>
					</g:if>
					<g:checkBox name="metavalues[${ status }].main" value="${ metadata.main }"/>
				</td>
				<td><label title="API : device.metavalue('${ metadata.name }')?.value">${ metadata.label }</label></td>
				<td><g:textField name="metavalues[${ status }].value" value="${ metadata.value }" class="form-control" disabled="true"/></td>
				<td><g:textField name="metavalues[${ status }].unite" value="${ metadata.unite }" class="form-control"/></td>
				<td>
					<g:if test="${ metadata.type }">
						<span class="aui-lozenge aui-lozenge-complete aui-lozenge-subtle">${ metadata.type }</span>
					</g:if>
				</td>	
				<td class="text-center"><g:checkBox name="metavalues[${ status }].trace" value="${ metadata.trace }"/></td>				
				<td class="text-center"><g:checkBox name="metavalues[${ status }].virtualDevice" value="${ metadata.virtualDevice }"/></td>				
			</tr>
		</g:each>	
	</tbody>
</table>

<h6 class="h6">
	<ul>
		<li>Principale : cette valeur sera synchronisée sur la valeur principale de l'objet. Il n'y pas besoin d'activer l'historisation
			car la valeur principale de l'objet est toujours historisée</li>
		<li>Historisation : conserve un historique de toutes les valeurs de l'objet. Sinon seule la dernière valeur est conservée</li>
		<li>Objet séparé : utile pour synchroniser la valeur dans un autre objet (Ex : objet connecté avec plusieurs capteurs)</li>
	</ul>
</h6>

