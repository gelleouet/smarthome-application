<nav class="navbar navbar-expand-lg mb-4 bg-dark">
	<g:link class="navbar-brand" uri="/">
		<app:icon name="home" class="d-inline-block align-top" style="width:30px; height:30px;"/>
		<g:meta name="app.code"/>
	</g:link>
	
	<div class="navbar-nav ml-auto">
			<g:link controller="register" action="account" class="btn btn-outline-primary">Inscription</g:link>
			<g:link controller="login" action="auth" class="btn btn-outline-primary ml-2">Connexion</g:link>
	</div>
</nav>