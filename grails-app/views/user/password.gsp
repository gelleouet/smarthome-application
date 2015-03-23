<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>

	<g:applyLayout name="applicationHeader">
		<div class="aui-page-header-image">
			<div class="aui-avatar aui-avatar-medium">
				<div class="aui-avatar-inner">
					<g:link controller="user" action="profil">
						<asset:image src="ico_add_avatar.png" />
					</g:link>
				</div>
			</div>
		</div>
		<div class="aui-page-header-main">
			<h3>
				<g:link controller="user" action="profil">${command.prenom} ${command.nom}</g:link>
			</h3>
		</div>
		<div class="aui-page-header-actions">
			<div class="aui-buttons"></div>
		</div>
	</g:applyLayout>


	<g:applyLayout name="applicationContent">

		<h3>Changer de mot de passe</h3>

		<g:form controller="user" method="post" class="aui">
			<g:hiddenField name="username" value="${command.username}" />
			<g:hiddenField name="nom" value="${command.nom}" />
			<g:hiddenField name="prenom" value="${command.prenom}" />

			<fieldset>
				<div class="field-group">
					<label for="oldPassword">Ancien mot de passe<span
						class="aui-icon icon-required"> required</span></label>
					<g:passwordField name="oldPassword" class="text" required="true" value="${command.oldPassword}"/>
				</div>
				<div class="field-group">
					<label for="newPassword">Nouveau mot de passe<span
						class="aui-icon icon-required"> required</span></label>
					<g:passwordField name="newPassword" class="text" required="true" value="${command.newPassword}"/>
				</div>
				<div class="field-group">
					<label for="confirmPassword">Confirmation mot de passe<span
						class="aui-icon icon-required"> required</span></label>
					<g:passwordField name="confirmPassword" class="text"
						required="true" value="${command.confirmPassword}"/>
				</div>
			</fieldset>

			<div class="buttons-container">
				<div class="buttons">
				<g:actionSubmit value="Enregistrer" action="changePassword"
					class="aui-button aui-button-primary" />
				<g:link controller="user" action="profil" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>

	</g:applyLayout>

</body>
</html>