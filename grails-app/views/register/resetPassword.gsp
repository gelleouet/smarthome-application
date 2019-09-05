<html>
<head>
	<meta name='layout' content='main'/>
</head>

<body>
	<g:applyLayout name="page-signin">
    	<div class="text-center mt-4">
			<h1 class="h2">Réinitialisation du mot de passe</h1>
			<p class="lead">
				Renseignez votre nouveau mot de passe pour le compte ${ command.username }
			</p>
		</div>
		
		<div class="card">
			<div class="card-body">
				<div class="m-sm-4">
					<div class="text-center">
						<asset:image src="apple-touch-icon-retina.png" class="img-fluid rounded-circle" width="132" height="132" />
					</div>
					
					<g:applyLayout name="content-error"/>
		
          
					<g:form controller="register" action="confirmResetPassword" autocomplete='off'>
					
						<g:hiddenField name="token" value="${ command.token }"/>
						<g:hiddenField name="username" value="${ command.username }"/>
					
				        <div class="form-group required">
				            <label for="newPassword">Nouveau mot de passe</label>
				            <g:passwordField name="newPassword" class="form-control form-control-lg" required="true" pattern=".{8,64}"/>
				            <small class="description">Minimum 8 caractères dont 1 chiffre</small>
				        </div>
				        <div class="form-group required">
				            <label for="confirmPassword">Confirmation</label>
				            <g:passwordField name="confirmPassword" class="form-control form-control-lg" required="true" pattern=".{8,64}"/>
				            <small class="form-text text-muted">Minimum 8 caractères dont 1 chiffre</small>
				        </div>
					     
					    <div class="text-center mt-3">
					    	<button type="submit" class="btn btn-lg btn-primary">Confirmer</button>
					    	<g:link controller="login" action="auth">Retourner à la page de connexion</g:link>
					    </div>
					</g:form>
				</div>
			</div>
		</div>
	
	</g:applyLayout>
	
</body>
</html>


