<app:datatable datatableId="datatable" recordsTotal="${ indexs?.totalCount ?: 0 }" paginateForm="compteurIndex-form">
    <thead>
        <tr>
            <app:th sortProperty="user.nom,user.prenom" sortProperties="${ command.sortProperties }">Utilisateur</app:th>
            <app:th sortProperty="compteurIndex.dateIndex" sortProperties="${ command.sortProperties }">Date index</app:th>
            <th>Type</th>
            <th>Index 1</th>
            <th>Index 2</th>
            <th>Param 1</th>
            <th class="column-1-buttons"></th>
        </tr>
    </thead>
    <tbody>
    	<g:each var="index" in="${ indexs }">
	        <tr>
	            <td><g:link action="compteurIndex" id="${ index.id }"> ${ index.device.user.nomPrenom } [${ index.device.user.profil?.libelle }]</g:link></td>
	            <td><g:formatDate date="${ index.dateIndex }" format="dd/MM/yyyy"/></td>
	            <td>${ index.device.deviceType.libelle }</td>
	            <td>${ index.index1 as Long }</td>
	            <td>${ index.index2 as Long }</td>
	            <td>${ index.param1 }</td>
	            <td class="column-1<-buttons command-column">
	            	<g:link class="btn btn-light confirm-button" title="Supprimer" action="deleteCompteurIndex" id="${ index.id }">
	            		<app:icon name="trash"/>
	            	</g:link>
	            </td>
	        </tr>
        </g:each>
    </tbody>
</app:datatable>
