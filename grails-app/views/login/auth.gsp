<html>
<head>
	<meta name='layout' content='anonymous'/>
</head>

<body>
	<g:applyLayout name="applicationContent">
    
    <g:if test="${ mobileAgent }">
    	<h1><g:meta name="app.code"/></h1>
    </g:if>
    <g:else>
    	<h1>Bienvenue sur l'application <g:meta name="app.code"/></h1>
    </g:else>
      
    <h3 class="separator">Veuillez saisir vos identifiants</h3>
          
	<form action="${postUrl}" method="post" id="d" class="aui ${ mobileAgent ? 'top-label' : '' }" autocomplete='off'>
		<fieldset>
	        <div class="field-group">
	            <label for="username">Adresse mail<span class="aui-icon icon-required"> required</span></label>
	            <input class="text" type="email" id="username" name="j_username" placeholder="yourmail@example.com" autofocus="true">
	        </div>
	        <div class="field-group">
	            <label for="password1" accesskey="p">Mot de passe<span class="aui-icon icon-required"> required</span></label>
	            <input class="password" type="password" id="password" name="j_password">
	        </div>
	     </fieldset>
	     
	     <fieldset class="group">
	        <div class="checkbox">
	            <input class="checkbox" type="checkbox" name="${rememberMeParameter}" id="remember_me" checked='checked'>
	            <label for="remember_me">Mémoriser mes identifiants</label>
	        </div>                                
	    </fieldset>
	    
	    <div class="buttons-container">
	        <div class="buttons">
	            <input class="aui-button aui-button-primary" type="submit" value="S'authentifier" id="connexion">
	            <g:link class="cancel" controller="register" action="forgotPassword">J'ai oublié mon mot de passe</g:link>
	        </div>
	    </div>
	</form>
	
	
	<h2 class="separator"></h2>
	
	<g:if test="${ mobileAgent }">
    	<h3>Autres applications</h3>
    </g:if>
    <g:else>
    	<h3>Ou se connecter avec d'autres applications</h3>
    </g:else>
	
	<div class="buttons-container" style="padding-top:20px">
        <div class="buttons">
            <button class="aui-button" aria-disabled="true" disabled><span class="aui-icon aui-icon-small aui-iconfont-user">View </span> Google</button>
            <button class="aui-button" aria-disabled="true" disabled><span class="aui-icon aui-icon-small aui-iconfont-user">View </span> Facebook</button>
            <button class="aui-button" aria-disabled="true" disabled><span class="aui-icon aui-icon-small aui-iconfont-user">View </span> Twitter</button>
        </div>
    </div>
	</g:applyLayout>
	
</body>
</html>