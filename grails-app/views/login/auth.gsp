<html>
<head>
	<meta name='layout' content="main"/>
</head>

<body>
	<g:applyLayout name="page-signin">
    
	    <div class="text-center mt-4">
			<h1 class="h2">Bienvenue sur l'application <g:meta name="app.code"/></h1>
			<p class="lead">
				Connectez-vous pour continuer
			</p>
		</div>
	    
	    
	    <div class="row">
	    	<div class="col">
	    		
	    	</div>
	    	
	    	<div class="col-6">
			    <div class="card">
					<div class="card-body">
						<div class="m-sm-4">
							<div class="text-center">
								<asset:image src="logo.png" class="img-fluid rounded-circle" width="132" height="132" />
							</div>
							
							<g:applyLayout name="content-error"/>
							
							<form action="${postUrl}" method="post" id="d" autocomplete='off'>
								<div class="form-group required">
									<label>Email</label>
									<input class="form-control form-control-lg" type="email" id="username" name="j_username" placeholder="Entrez votre email" autofocus="true" required="true">
								</div>
								<div class="form-group required">
									<label>Mot de passe</label>
									<input class="form-control form-control-lg" type="password" id="password" name="j_password" placeholder="Entrez votre mot de passe" required="true">
									<small>
										<g:link controller="register" action="forgotPassword">Mot de passe oublié ?</g:link></small>
								</div>
								<div>
									<div class="custom-control custom-checkbox align-items-center">
										<input class="custom-control-input" type="checkbox" name="${rememberMeParameter}" id="remember_me" checked='checked'>
				            			<label for="remember_me" class="custom-control-label text-small">Mémoriser mes identifiants</label>
									</div>
								</div>
								<div class="text-center mt-3">
									<button type="submit" class="btn btn-lg btn-primary">Connexion</button>
									<!-- <g:link class="btn btn-link" controller="register" action="account">Je n'ai pas encore de compte</g:link> -->
								</div>
							</form>
						</div>
					</div>
				</div> <!-- div.card  -->
			</div> <!-- div.col -->
			
			<div class="col">
			</div> <!-- div.col -->
			
		</div> <!-- div.row -->
          
	</g:applyLayout>
	
</body>
</html>