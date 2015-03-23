<html>
<head>
	<meta name='layout' content='anonymous'/>
</head>

<body>
	<g:applyLayout name="applicationContent">
    
     <h3 class="separator">Mot de passe oublié</h3>
          
	<g:form controller="register" action="confirmForgotPassword" class="aui" autocomplete='off'>
		<fieldset>
	        <div class="field-group">
	            <label for="username">Adresse mail<span class="aui-icon icon-required"> required</span></label>
	            <input class="text" type="text" id="username" name="username" placeholder="you@example.com" required="true" value="${ username }">
	        </div>
	     </fieldset>
	     
	     
	     <h6>Après avoir cliqué sur "Envoyer", vous allez recevoir un email contenant un lien vers une page Web.
	     	<br/>
	     	En cliquant dessus, vous serez alors redirigé vers une page vous demandant un nouveau mot de passe.
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


