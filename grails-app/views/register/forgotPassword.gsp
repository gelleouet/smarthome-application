<html>
<head>
	<meta name='layout' content='main'/>
</head>

<body>
	<g:applyLayout name="page-focus">
    
	    <div class="text-center mt-4">
				<h1 class="h2">Mot de passe oublié</h1>
				<p class="lead">
					Entrez votre email pour réinitialiser votre mot de passe
				</p>
		</div>
         
        <div class="card">
			<div class="card-body">
				<div class="m-sm-4">
					<div class="text-center">
						<asset:image src="logo.png" class="img-fluid rounded-circle" width="132" height="132" />
					</div>
					
					<g:applyLayout name="content-error"/>
				
					<g:form controller="register" action="confirmForgotPassword" autocomplete='off'>
				        <div class="form-group required">
				            <label for="username">Email</label>
				            <input class="form-control form-control-lg" type="email" id="username" name="username" placeholder="Entrez votre email" required="true" value="${ username }">
				        </div>
	     
					     <h6>Après cette opération, vous allez recevoir un mail contenant un lien pour vous demander un nouveau mot de passe.
					     </h6>

	     				<br/>
	     
	     				<div class="text-center mt-3">
							<button type="submit" class="btn btn-lg btn-primary">Réinitialiser</button>
							<g:link class="btn btn-link" controller="login" action="auth">Retourner à  la page de connexion</g:link>
						</div>
					</g:form>
				</div>
			</div>
		</div>
	
	</g:applyLayout>
	
</body>
</html>


