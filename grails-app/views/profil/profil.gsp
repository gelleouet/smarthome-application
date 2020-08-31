<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Profil', navigation: 'Compte']">
	
		<g:form name="profil-form" controller="profil" method="post">
			
			<g:hiddenField name="user.id" value="${user.id}" />
	
			<h4>Général</h4>
			
			<div class="form-group">
				<label>Profil</label>
				<g:select class="form-control" name="user.profil.id" from="${ profils }"
           				optionKey="id" optionValue="libelle"  value="${ user.profil?.id }"/>
			</div>
			<div class="form-group required">
				<label>Email</label>
				<g:textField name="user.username" value="${user.username}" type="email"
					class="form-control" required="true" disabled="true" />
			</div>
			<div class="form-group required">
				<label>Prénom</label>
				<g:textField name="user.prenom" value="${user.prenom}" required="true" 
					class="form-control"/>
			</div>
			<div class="form-group required">
				<label>Nom</label>
				<g:textField name="user.nom" value="${user.nom}" class="form-control"
					required="true" />
			</div>
			
			<div class="custom-control custom-checkbox">
	        	<g:checkBox name="user.profilPublic" value="${ user.profilPublic }" class="custom-control-input"/>
	            <label for="user.profilPublic" class="custom-control-label">J'autorise le <g:meta name="app.code"/> à me faire apparaître dans la liste des participants de mon équipe.
	            </label>
		    </div>

			<h4 class="mt-4">Habitation</h4>
			
			<g:include action="templateEditByUser" controller="house" params="[user: user]"/>
			
			<g:actionSubmit value="Enregistrer" action="saveProfil" class="btn btn-primary" />
			
		</g:form>
	</g:applyLayout>
</body>
</html>