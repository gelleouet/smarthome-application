<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Utilisateurs', navigation: 'Système']">
	
	
		<div class="row mb-4">
			<div class="col-6">
				<h4>${ user.id ? user.nom + ' ' + user.prenom: 'Nouvel utilisateur' }</h4>
			</div>
			<div class="col-6 text-right">
				<div class="btn-toolbar">
					<div class="btn-group">
						<g:remoteLink url="[action: 'geocodeDefaultHouse', controller: 'house', id: user.id]" class="btn btn-light">GPS</g:remoteLink>
		        		<g:remoteLink url="[action: 'calculDefaultWeather', controller: 'houseWeather', id: user.id]" class="btn btn-light">Météo</g:remoteLink>
		        		<g:remoteLink url="[action: 'calculConsoForUser', controller: 'house', id: user.id]" class="btn btn-light">Consos</g:remoteLink>
		        		<g:link class="btn btn-light" action="switchUser" id="${ user.id }">
		            		<app:icon name="log-in"/> Switch
		            	</g:link>
					</div>
				</div>
			</div>
		</div>
	
		
		<div class="nav nav-tabs" role="tablist">
			<a class="nav-item nav-link active" data-toggle="tab" role="tab" href="#tabs-user-general">Général</a>
	        <a class="nav-item nav-link" data-toggle="tab" role="tab" href="#tabs-user-acl">Liste de contrôles d'accès (ACL)</a>
		</div>
		
		<div class="tab-content">
			<div class="tab-pane active" id="tabs-user-general" role="tabpanel">
		    	<div class="smart-tabs-content">
					<g:include view="/user/userForm.gsp"/>
				</div>
			</div>

			<g:if test="${ user.id }">
				<div class="tab-pane" id="tabs-user-acl" role="tabpanel">
		    		<div class="smart-tabs-content">
						<g:include view="/user/userAclForm.gsp"/>
					</div>
				</div>
			</g:if>
		</div>
	
		
	</g:applyLayout>
</body>
</html>