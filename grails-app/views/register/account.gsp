<html>
<head>
	<meta name='layout' content='main'/>
</head>

<body>
	<g:applyLayout name="page-focus">
    
	    <div class="text-center mt-4">
			<h1 class="h2">Création d'un nouveau compte</h1>
			<p class="lead">
				Compléter le formulaire suivant pour démarrer avec <g:meta name="app.code"/>
			</p>
		</div>
    
		<div class="card">
			<div class="card-body">
				<div class="m-sm-4">
					<div class="text-center">
						<asset:image src="logo.png" class="img-fluid rounded-circle" width="132" height="132" />
					</div>
					
					<g:applyLayout name="content-error"/>
				
					<g:form action="createAccount" autocomplete='off'>
				        <div class="form-group required">
				        	<label for="prenom">Prénom</label>
	            			<g:field class="form-control form-control-lg" type="text" name="prenom" required="true" value="${ command.prenom }" />
				        </div>
				        <div class="form-group required">
				        	<label for="nom">Nom</label>
	            			<g:field class="form-control form-control-lg" type="text" name="nom" required="true" value="${ command.nom }"/>
				        </div>
				        <div class="form-group required">
				        	<label for="username">Email</label>
	            			<g:field class="form-control form-control-lg" type="email" name="username" required="true" value="${ command.username }"/>
				        </div>
				        <div class="form-group required">
				        	<label for="newPassword">Mot de passe</label>
	            			<g:passwordField name="newPassword" class="form-control form-control-lg" required="true" pattern=".{8,64}"/>
	            			<small class="form-text text-muted">Minimum 8 caractères dont 1 chiffre</small>
				        </div>
				        <div class="form-group required">
				        	<label for="confirmPassword">Confirmation</label>
	            			<g:passwordField name="confirmPassword" class="form-control form-control-lg" required="true" pattern=".{8,64}"/>
	            			<small class="form-text text-muted">Minimum 8 caractères dont 1 chiffre</small>
				        </div>
				        
				        <h4 class="mt-4">Informations du foyer</h4>
				        
				        
				        <div class="form-group required">
				        	<label for="nbPersonne">Nombre de personnes</label>
	            			<g:field class="form-control form-control-lg" type="number" name="nbPersonne" required="true" value="${ command.nbPersonne }"/>
				        </div>
				        <div class="form-group required">
				        	<label for="adresse">Adresse</label>
	            			<g:field class="form-control form-control-lg" type="text" name="adresse" required="true" value="${ command.adresse }"/>
				        </div>
				        <div class="form-group required">
				        	<label for="codePostal">Code postal</label>
	            			<g:field class="form-control form-control-lg" type="text" name="codePostal" required="true" value="${ command.codePostal }"/>
				        </div>
				        <div class="form-group required">
				        	<label for="ville">Ville</label>
	            			<g:select class="form-control form-control-lg combobox" name="ville" required="true" from="${ communes }"
	            				optionKey="libelle" optionValue="libelle" value="${ command.ville }" noSelection="['': ' ']"/>
				        </div>
				        
				        <label class="custom-control custom-checkbox required">
				        	<g:checkBox name="acceptUseData" class="custom-control-input" value="${ command.acceptUseData }" required="true"/>
				        	<span class="custom-control-label">
				        		Autorisation d'exploitation des données de consommation dans le cadre de l'expérimentation "Foyers Volontaires ECODO".
				        	</span>
				        </label>
				        <label class="custom-control custom-checkbox required">
				        	<g:checkBox name="acceptPublishData" class="custom-control-input" value="${ command.acceptPublishData }" required="true"/>
				        	<span class="custom-control-label">
				        		Autorisation de publication des résultats de l'expérimentation "Foyers Volontaires ECODO" (analyse sur données de consommation anonymisées).
				        	</span>
				        </label>
	     
	     				<br/>
	     				
	     				<h6>Dans le cadre du RGPD, la Collectivité Eau du Bassin Rennais est responsable de la confidentialité des données de consommation. Aucune donnée nominative ne sera transmise à un tiers.</h6>
	     				
	     				<br/>
	     				
					    <h6>
					     	Après avoir cliqué sur "Démarrer", vous allez recevoir un mail contenant un lien vers une page Web.
	     					Ce lien permettra d'activer votre compte. Cette action permet aussi de vérifier que vous êtes bien le propriétaire de l'adresse email.
	     				</h6>

	     				<h6><span class="required">*</span> : champs obligatoires</h6>

	     				<br/>
	     
	     				<div class="text-center mt-3">
							<button type="submit" class="btn btn-lg btn-primary">Inscription</button>
							<g:link class="btn btn-link" controller="login" action="auth">Retourner à la page de connexion</g:link>
						</div>
					</g:form>
				</div>
			</div>
		</div> <!-- div.card -->
          
	</g:applyLayout>
	
</body>
</html>


