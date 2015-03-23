<html>
<head>
	<meta name='layout' content='anonymous'/>
</head>

<body>
	<g:applyLayout name="applicationContent">
    
     <h3 class="separator">Création d'un compte <g:meta name="app.code"/></h3>
          
	<g:form action="createAccount" class="aui" autocomplete='off'>
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
	            <g:passwordField name="newPassword" class="text medim-field" required="true"/>
	        </div>
	        <div class="field-group">
	            <label for="username">Confirmation<span class="aui-icon icon-required"> required</span></label>
	            <g:passwordField name="confirmPassword" class="text medim-field" required="true"/>
	        </div>
	     </fieldset>
	     
	     
	     <h6>Après avoir cliqué sur "Envoyer", vous allez recevoir un email contenant un lien vers une page Web.
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


