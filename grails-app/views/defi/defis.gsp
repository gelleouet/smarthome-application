<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Défis', navigation: 'Compte']">
	
		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="defis">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Rechercher ..." name="search" value="${ command.search }"/>
						<button class="btn btn-light"><app:icon name="search"/></button>
					</fieldset>
				</g:form>
			</div>
			<div class="col-4 text-right">
				<g:link action="edit" class="btn btn-light"><app:icon name="plus"/> Ajouter</g:link>
			</div>
		</div>
	
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Nom</th>
		            <th>Période référence</th>
		            <th>Période action</th>
		            <th>Statut</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ defiInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.libelle }</g:link></td>
			            <td>du <app:formatUser date="${ bean.referenceDebut }"/> au <app:formatUser date="${ bean.referenceFin }"/></td>
			            <td>du <app:formatUser date="${ bean.actionDebut }"/> au <app:formatUser date="${ bean.actionFin }"/></td>
			            <td>
			            	<g:if test="${ bean.actif }">
			            		<span class="badge badge-primary">OUVERT</span>
			            	</g:if>
			            	<g:else>
			            		<span class="badge badge-dark">FERME</span>
			            	</g:else>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>