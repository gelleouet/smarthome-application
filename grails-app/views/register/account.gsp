<html>
<head>
	<meta name='layout' content='anonymous'/>
</head>

<body>
	<g:applyLayout name="applicationContent">
    
    <h3 class="separator">Création d'un compte <g:meta name="app.code"/></h3>
          
	<g:form action="createAccount" class="aui ${ mobileAgent ? 'top-label' : '' }" autocomplete='off'>
		<fieldset>
	        <div class="field-group">
	            <label for="prenom">Prénom<span class="aui-icon icon-required"> required</span></label>
	            <g:field class="text medium-field" type="text" name="prenom" required="true" value="${ command.prenom }" />
	        </div>
	        <div class="field-group">
	            <label for="prenom">Nom<span class="aui-icon icon-required"> required</span></label>
	            <g:field class="text medium-field" type="text" name="nom" required="true" value="${ command.nom }"/>
	        </div>
	        <div class="field-group">
	            <label for="username">Adresse mail<span class="aui-icon icon-required"> required</span></label>
	            <g:field class="text long-field" type="email" name="username" required="true" value="${ command.username }"/>
	        </div>
	        <div class="field-group">
	            <label for="username">Mot de passe<span class="aui-icon icon-required"> required</span></label>
	            <g:passwordField name="newPassword" class="text medim-field" required="true" pattern=".{8,64}"/>
	            <div class="description">Minimum 8 caractères dont 1 chiffre</div>
	        </div>
	        <div class="field-group">
	            <label for="username">Confirmation<span class="aui-icon icon-required"> required</span></label>
	            <g:passwordField name="confirmPassword" class="text medim-field" required="true" pattern=".{8,64}"/>
	            <div class="description">Minimum 8 caractères dont 1 chiffre</div>
	        </div>
	     </fieldset>
	     <fieldset class="group">
	        <legend><span>Social</span></legend>
	        <div class="checkbox">
	        	<g:checkBox name="profilPublic" class="checkbox" value="${ command.profilPublic }"/>
	            <label for="profilPublic" class="label">J'autorise les autres utilisateurs <g:meta name="app.code"/> à pouvoir m'envoyer des invitations
	            dans le but de partager les statistiques de ma maison. Vous pouvez ainsi suivre d'autres utilisateurs et comparer vos consommations.
	            <br/>
	            Dans un souci de confidentialité, vos données ne seront visibles à vos amis que si vous acceptez leurs invitations.
	            </label>
	        </div>
	    </fieldset>
	     
	     <br/>
	     <h6 class="h6">Après avoir cliqué sur "Envoyer", vous allez recevoir un email contenant un lien vers une page Web.
	     	<br/>
	     	Ce lien permettra d'activer votre compte. Cette action permet aussi de vérifier que vous êtes bien le propriétaire de l'adresse mail.
	     </h6>

	     <br/>
	     
	     
	    <div class="buttons-container">
	        <div class="buttons">
	            <input class="aui-button aui-button-primary" type="submit" value="Envoyer">
	            <g:link uri="/" class="cancel">Retourner à la page de connexion</g:link>
	        </div>
	    </div>
	</g:form>
	
	</g:applyLayout>
	
</body>
</html>


