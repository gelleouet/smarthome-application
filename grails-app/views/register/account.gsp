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
						<asset:image src="logo.png" class="img-fluid rounded-circle" width="132px" />
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
				        <label class="custom-control custom-checkbox">
				        	<g:checkBox name="profilPublic" class="custom-control-input" value="${ command.profilPublic }"/>
				        	<span class="custom-control-label">
				        		J'autorise les autres utilisateurs <g:meta name="app.code"/> à pouvoir m'envoyer des invitations
	            				dans le but de partager les statistiques de ma maison.
	            				Vous pouvez ainsi suivre d'autres utilisateurs et comparer vos consommations.
								Dans un souci de confidentialité, vos données ne seront visibles à vos amis que si vous acceptez leurs invitations.
				        	</span>
				        </label>
	     
	     				<br/>
	     				
					    <h6>
					     	Après avoir cliqué sur "Démarrer", vous allez recevoir un mail contenant un lien vers une page Web.
	     					Ce lien permettra d'activer votre compte. Cette action permet aussi de vérifier que vous êtes bien le propriétaire de l'adresse email.
	     				</h6>

	     				<br/>
	     
	     				<div class="text-center mt-3">
							<button type="submit" class="btn btn-lg btn-primary">Démarrer</button>
							<g:link class="btn btn-link" controller="login" action="auth">Retourner à la page de connexion</g:link>
						</div>
					</g:form>
				</div>
			</div>
		</div> <!-- div.card -->
          
	</g:applyLayout>
	
</body>
</html>


