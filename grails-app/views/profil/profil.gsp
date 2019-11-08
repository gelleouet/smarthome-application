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
			<div class="form-group">
				<label >N° téléphone mobile</label>
				<g:textField name="user.telephoneMobile" value="${user.telephoneMobile}" class="form-control" />
				<small class="form-text">Utile pour l'envoi de notifications SMS. Au format +336...</small>
			</div>
			
			<div class="custom-control custom-checkbox">
	        	<g:checkBox name="user.profilPublic" value="${ user.profilPublic }" class="custom-control-input"/>
	            <label for="user.profilPublic" class="custom-control-label">J'autorise les autres utilisateurs <g:meta name="app.code"/> à pouvoir m'envoyer des invitations
	            dans le but de partager les statistiques de ma maison. Vous pouvez ainsi suivre d'autres utilisateurs et comparer vos consommations.
	            <br/>
	            Dans un souci de confidentialité, vos données ne seront visibles à vos amis que si vous acceptez leurs invitations.
	            </label>
		    </div>

			<h4 class="mt-4">Maison principale</h4>
			
			<g:include action="templateEditByUser" controller="house" params="[user: user]"/>
			
			<!-- 
			<h4 class="mt-4">Modes</h4>
			
			<div id="ajaxModes">
				<g:include action="templateEditByUser" controller="mode" params="[user: user]"/> 
			</div> 

			<h4>Applications</h4>
			
			<div class="form-group required">
				<label>Application ID</label>
				<h5>${ user.applicationKey }</h5>
				<small class="form-text text-muted">Utilisez cet ID pour connecter des agents à l'application <g:meta name="app.code"/>. Il vous sera demandé dans les identifiants de connexion.</small>
			</div>
			 -->
	
			<g:actionSubmit value="Enregistrer" action="saveProfil" class="btn btn-primary" />
			
		</g:form>
	</g:applyLayout>
</body>
</html>