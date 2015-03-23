<html>
<head>
	<meta name='layout' content='anonymous'/>
</head>

<body>
	<g:applyLayout name="applicationContent">
    
     <h3 class="separator">Réinitialisation du mot de passe</h3>
          
	<g:form controller="register" action="confirmResetPassword" class="aui" autocomplete='off'>
	
		<g:hiddenField name="token" value="${ command.token }"/>
		<g:hiddenField name="username" value="${ command.username }"/>
	
		<h5>Utilisateur : ${ command.username }</h5>
	
		<br/>
	
		<fieldset>
	        <div class="field-group">
	            <label for="username">Nouveau mot de passe<span class="aui-icon icon-required"> required</span></label>
	            <g:passwordField name="newPassword" class="text" required="true"/>
	        </div>
	        <div class="field-group">
	            <label for="username">Confirmation<span class="aui-icon icon-required"> required</span></label>
	            <g:passwordField name="confirmPassword" class="text" required="true"/>
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


