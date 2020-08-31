<html>
<head>
	<meta name='layout' content='main'/>
</head>

<body>
	<g:applyLayout name="page-focus">
    
	    <div class="text-center mt-4">
			<h1 class="h2">Création d'un nouveau compte</h1>
			<p class="lead">
				Compléter le formulaire suivant pour démarrer le <g:meta name="app.code"/>
			</p>
		</div>
    
		<div class="card">
			<div class="card-body">
				<div class="m-sm-4">
					<h6 class="text-muted text-center">ALEC du pays de Rennes</h6>
					<div class="row mb-5">
						<div class="col-5">
							<asset:image src="granddefi/logo.png" height="132"/>
						</div>
						<div class="col-7">
							<asset:image src="granddefi/partenaire.png"/>
						</div>
					</div>
					
					<g:applyLayout name="content-error"/>
				
					<g:form action="createAccount" autocomplete='off'>
				        
				        <div class="form-group required">
				        	<label>Profil</label>
	            			<g:select class="form-control form-control-lg" name="profil.id" required="true" from="${ profils }"
	            				optionKey="id" optionValue="libelle" onchange="onChangeProfil(this)"
	            				data-url="${ g.createLink(action: 'formProfil', controller: 'profil') }"
	            				value="${ command?.profil?.id }"/>
				        </div>
				        
				        <div id="ajaxProfilForm">
				        	<g:if test="${ command?.profil }">
				        		<g:render template="/profil/${ command.profil.view }"/>
				        	</g:if>
				        	<g:elseif test="${ profils }">
				        		<g:render template="/profil/${ profils[0].view }"/>
				        	</g:elseif>
				        	<g:else>
				        		<g:render template="/profil/particulier"/>
				        	</g:else>
				        </div>
				        
				        <div class="form-group required">
				        	<label>Email</label>
	            			<g:field class="form-control form-control-lg" type="email" name="username" required="true" value="${ command?.username }"/>
				        </div>
				        <div class="form-group required">
				        	<label>Mot de passe</label>
	            			<g:passwordField name="newPassword" class="form-control form-control-lg" required="true" pattern=".{8,64}"/>
	            			<small class="form-text text-muted">Minimum 8 caractères dont 1 chiffre</small>
				        </div>
				        <div class="form-group required">
				        	<label>Confirmation</label>
	            			<g:passwordField name="confirmPassword" class="form-control form-control-lg" required="true" pattern=".{8,64}"/>
	            			<small class="form-text text-muted">Minimum 8 caractères dont 1 chiffre</small>
				        </div>
				        
				        <label class="custom-control custom-checkbox">
				        	<g:checkBox name="profilPublic" class="custom-control-input" value="${ command?.profilPublic }"/>
				        	<span class="custom-control-label">
				        		J'autorise le <g:meta name="app.code"/> à me faire apparaître dans la liste des participants de mon équipe.
				        	</span>
				        </label>
	     
	     				<h4 class="mt-4">Habitation</h4>
	     				
	     				<div class="form-group required">
				        	<label>Commune</label>
	            			<g:select class="form-control form-control-lg combobox" name="commune.id" required="true" from="${ communes }"
	            				optionKey="id" optionValue="libelle" value="${ command?.commune?.id }"/>
				        </div>
				        
				        <div class="form-group required">
				        	<label>Surface (m²)</label>
	            			<g:field class="form-control form-control-lg" type="number" name="surface" required="true" value="${ command?.surface }"/>
				        </div>
				        
				        <div class="form-group required">
				        	<label>Energie principale chauffage</label>
	            			<g:select class="form-control form-control-lg combobox" name="chauffage.id" required="true" from="${ chauffages }"
	            				optionKey="id" optionValue="libelle" value="${ command?.chauffage?.id }"/>
				        </div>
				        
				        <div class="form-group required">
				        	<label>Eau Chaude Sanitaire (ECS)</label>
	            			<g:select class="form-control form-control-lg combobox" name="ecs.id" required="true" from="${ ecs }"
	            				optionKey="id" optionValue="libelle" value="${ command?.ecs?.id }"/>
				        </div>
	     				
					    <h6>
					     	Après avoir cliqué sur "Démarrer", vous allez recevoir un mail contenant un lien vers une page Web.
	     					Ce lien permettra d'activer votre compte. Cette action permet aussi de vérifier que vous êtes bien le propriétaire de l'adresse email.
	     				</h6>
	     				
	     				<h6><span class="required">*</span> : champs obligatoires</h6>

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


