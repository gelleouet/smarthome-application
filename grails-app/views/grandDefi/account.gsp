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
					
					<div class="text-center">
						<asset:image src="logo.png" class="img-fluid rounded-circle" width="132px" />
					</div>
					
					<g:applyLayout name="content-error"/>
				
					<g:form action="createAccount" autocomplete='off'>
				        
				        <h3 class="mt-4">Votre profil</h3>
				        
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
							<label>Numéro et rue</label>
						 	<g:field class="form-control form-control-lg" type="text" name="adresse" required="true" value="${ command?.adresse }"/>
						</div>
						
						<div class="form-group required">
							<label>Code postal</label>
						 	<g:field class="form-control form-control-lg" type="text" name="codePostal" required="true" value="${ command?.codePostal }" maxlength="16"/>
						</div>
						
						<div class="form-group required">
				        	<label>Commune</label>
	            			<g:select class="form-control form-control-lg combobox" name="commune.id" required="true" from="${ communes }"
	            				optionKey="id" optionValue="libelle" value="${ command?.commune?.id }" noSelection="['': ' ']"/>
				        </div>
				        
				        <div class="form-group required">
				        	<label>Adresse courriel</label>
	            			<g:field class="form-control form-control-lg" type="email" name="username" required="true" value="${ command?.username }"/>
				        </div>
				        
				        <div class="form-group required">
							<label>Numéro téléphone</label>
						 	<g:field class="form-control form-control-lg" type="text" name="telephone" required="true" value="${ command?.telephone }"/>
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
				        
				        <h3 class="mt-4">Vos données</h3>
				        
				        <label class="custom-control custom-checkbox">
				        	<g:checkBox name="autorise_user_data" class="custom-control-input" value="${ command?.autorise_user_data }" readonly="true"/>
				        	<span class="custom-control-label">
				        		J'autorise l'ALEC du Pays de Rennes à utiliser mon adresse e-mail et mon numéro de téléphone pour me contacter dans le cadre du <g:meta name="app.code"/> (nécessaire pour le bon déroulement du défi).
				        	</span>
				        </label>
				        
				        <label class="custom-control custom-checkbox">
				        	<g:checkBox name="autorise_conso_data" class="custom-control-input" value="${ command?.autorise_conso_data }" readonly="true"/>
				        	<span class="custom-control-label">
				        		J'autorise l'ALEC du Pays de Rennes à récupérer mes données de consommation d’énergie et d’eau à usage exclusif du <g:meta name="app.code"/> (nécessaire pour le bon déroulement du défi).
				        	</span>
				        </label>
				        
				        <label class="custom-control custom-checkbox">
				        	<g:checkBox name="profilPublic" class="custom-control-input" value="${ command?.profilPublic }"/>
				        	<span class="custom-control-label">
				        		J'autorise l'ALEC du Pays de Rennes à me faire apparaître dans la liste des participants de mon équipe.
				        	</span>
				        </label>
				        
				        <label class="custom-control custom-checkbox">
				        	<g:checkBox name="autorise_share_data" class="custom-control-input" value="${ command?.autorise_share_data }"/>
				        	<span class="custom-control-label">
				        		J'autorise l'ALEC du Pays de Rennes à transmettre mon adresse e-mail et mon numéro de téléphone à ma commune/ma mairie pour me contacter dans le cadre du <g:meta name="app.code"/>.
				        	</span>
				        </label>
				        
				         <label class="custom-control custom-checkbox">
				        	<g:checkBox name="engage_enedis_account" class="custom-control-input" value="${ command?.engage_enedis_account }" readonly="true"/>
				        	<span class="custom-control-label">
				        		Je m’engage à créer mon compte client Enedis <a href="https://mon-compte-particulier.enedis.fr" target="enedis">https://mon-compte-particulier.enedis.fr</a> et à transmettre mes relevés de compteurs d’énergie et d’eau avant et pendant le <g:meta name="app.code"/> (nécessaire pour le bon déroulement du défi).
				        	</span>
				        </label>
	     
	     				<h3 class="mt-4">Votre habitation/bâtiment</h3>
	     				
	     				<div class="form-group required">
				        	<label>Nombre de personnes dans le foyer (vous compris)</label>
	            			<g:field class="form-control form-control-lg" type="number" name="nbPersonne" required="true" value="${ command?.nbPersonne }"/>
				        </div>
	     				
				        <div class="form-group">
				        	<label>Surface (m²)</label>
	            			<g:field class="form-control form-control-lg" type="number" name="surface" value="${ command?.surface }"/>
				        </div>
				        
				        <div class="form-group required">
				        	<label>Energie principale de chauffage</label>
	            			<g:select class="form-control form-control-lg combobox" name="chauffage.id" required="true" from="${ chauffages }"
	            				optionKey="id" optionValue="libelle" value="${ command?.chauffage?.id }"/>
				        </div>
				        
				        <div class="form-group">
				        	<label>Energie secondaire de chauffage (= appoint)</label>
	            			<g:select class="form-control form-control-lg combobox" name="chauffageSecondaire.id" from="${ chauffages }"
	            				optionKey="id" optionValue="libelle" value="${ command?.chauffageSecondaire?.id }" noSelection="['': ' ']"/>
				        </div>
				        
				        <div class="form-group required">
				        	<label>Eau Chaude Sanitaire (ECS)</label>
	            			<g:select class="form-control form-control-lg combobox" name="ecs.id" required="true" from="${ ecs }"
	            				optionKey="id" optionValue="libelle" value="${ command?.ecs?.id }" noSelection="['': ' ']"/>
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


