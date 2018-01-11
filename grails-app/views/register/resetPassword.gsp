<html>
<head>
	<meta name='layout' content='anonymous'/>
</head>

<body>
	<g:applyLayout name="applicationContent">
    
     <h3 class="separator">Réinitialisation du mot de passe</h3>
          
	<g:form controller="register" action="confirmResetPassword" class="aui ${ mobileAgent ? 'top-label' : '' }" autocomplete='off'>
	
		<g:hiddenField name="token" value="${ command.token }"/>
		<g:hiddenField name="username" value="${ command.username }"/>
	
		<h5>Utilisateur : ${ command.username }</h5>
	
		<br/>
	
		<fieldset>
	        <div class="field-group">
	            <label for="username">Nouveau mot de passe<span class="aui-icon icon-required"> required</span></label>
	            <g:passwordField name="newPassword" class="text" required="true" pattern=".{8,64}"/>
	            <div class="description">Minimum 8 caractères dont 1 chiffre</div>
	        </div>
	        <div class="field-group">
	            <label for="username">Confirmation<span class="aui-icon icon-required"> required</span></label>
	            <g:passwordField name="confirmPassword" class="text" required="true" pattern=".{8,64}"/>
	            <div class="description">Minimum 8 caractères dont 1 chiffre</div>
	        </div>
	     </fieldset>
	     
	     <br/>
	     
	     
	    <div class="buttons-container">
	        <div class="buttons">
	            <input class="aui-button aui-button-primary" type="submit" value="Réinitialiser">
	        </div>
	    </div>
	</g:form>
	
	</g:applyLayout>
	
</body>
</html>


