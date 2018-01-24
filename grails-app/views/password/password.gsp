<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>

	<g:applyLayout name="applicationConfigure">

		<div class="aui-toolbar2">
		    <div class="aui-toolbar2-inner">
		        <div class="aui-toolbar2-primary">
		            <div>
		                <h3>Mot de passe</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form >
		            <div class="aui-buttons">
						
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		<g:form method="post" class="aui ${ mobileAgent ? 'top-label' : '' }">
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
					<g:passwordField name="newPassword" class="text" required="true" value="${command.newPassword}" pattern=".{8,64}"/>
					<div class="description">Minimum 8 caractères dont 1 chiffre</div>
				</div>
				<div class="field-group">
					<label for="confirmPassword">Confirmation mot de passe<span
						class="aui-icon icon-required"> required</span></label>
					<g:passwordField name="confirmPassword" class="text" required="true" value="${command.confirmPassword}" pattern=".{8,64}"/>
					<div class="description">Minimum 8 caractères dont 1 chiffre</div>
				</div>
			</fieldset>

			<div class="buttons-container">
				<div class="buttons">
				<g:actionSubmit value="Enregistrer" action="changePassword"
					class="aui-button aui-button-primary" />
				</div>
			</div>
		</g:form>

	</g:applyLayout>

</body>
</html>