<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Profil', navigation: 'Compte']">
	
		<g:form name="profil-form" controller="profil" method="post" action="saveProfil">
			
			<g:hiddenField name="user.id" value="${user.id}" />
			<g:hiddenField name="house.id" value="${house.id}" />
	
			<h4>Général</h4>
			
			<div class="form-group required">
				<label>Email</label>
				<g:textField name="user.username" value="${user.username}" type="email"
					class="form-control" required="true" disabled="true" />
			</div>
			<div class="form-group required">
				<label>Prénom</label>
				<g:textField name="user.prenom" value="${user.prenom}" required="true" 
					class="form-control"/>
			</div>
			<div class="form-group required">
				<label>Nom</label>
				<g:textField name="user.nom" value="${user.nom}" class="form-control"
					required="true" />
			</div>
			
			 <h4 class="mt-4">Informations du foyer</h4>

			<div class="form-group required">
	        	<label for="nbPersonne">Nombre de personnes</label>
          			<g:field class="form-control" type="number" name="house.nbPersonne" required="true" value="${ house.nbPersonne }"/>
	        </div>
	        <div class="form-group required">
	        	<label for="adresse">Adresse</label>
          			<g:field class="form-control" type="text" name="house.adresse" required="true" value="${ house.adresse }"/>
	        </div>
	        <div class="form-group required">
	        	<label for="codePostal">Code postal</label>
          			<g:field class="form-control" type="text" name="house.codePostal" required="true" value="${ house.codePostal }"/>
	        </div>
	        <div class="form-group required">
	        	<label for="ville">Ville</label>
          			<g:select class="form-control combobox" name="house.location" required="true" from="${ communes }"
          				optionKey="libelle" optionValue="libelle" value="${ house.location }" noSelection="['': ' ']"/>
	        </div>
			
			<button class="btn btn-primary">Enregistrer</button>
			
		</g:form>
	</g:applyLayout>
</body>
</html>