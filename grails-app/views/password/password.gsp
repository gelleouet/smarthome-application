<html>
<head>
<meta name='layout' content='main' />
</head>

<body>

	<g:applyLayout name="page-settings" model="[titre: 'Mot de passe', navigation: 'user']">

		<g:form method="post">
			<g:hiddenField name="username" value="${command.username}" />
			<g:hiddenField name="nom" value="${command.nom}" />
			<g:hiddenField name="prenom" value="${command.prenom}" />

			<div class="form-group required">
				<label for="oldPassword">Ancien mot de passe</label>
				<g:passwordField name="oldPassword" class="form-control" required="true" value="${command.oldPassword}"/>
			</div>
			<div class="form-group required">
				<label for="newPassword">Nouveau mot de passe</label>
				<g:passwordField name="newPassword" class="form-control" required="true" value="${command.newPassword}" pattern=".{8,64}"/>
				<small class="form-text text-muted">Minimum 8 caractères dont 1 chiffre</small>
			</div>
			<div class="form-group required">
				<label for="confirmPassword">Confirmation mot de passe</label>
				<g:passwordField name="confirmPassword" class="form-control" required="true" value="${command.confirmPassword}" pattern=".{8,64}"/>
				<small class="form-text text-muted">Minimum 8 caractères dont 1 chiffre</small>
			</div>

			<g:actionSubmit value="Enregistrer" action="changePassword" class="btn btn-primary" />
		</g:form>

	</g:applyLayout>

</body>
</html>