<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Profil', navigation: 'Compte']">
	
		<g:form name="profil-form" controller="profil" action="saveProfil" method="post">
			
			<g:hiddenField name="user.id" value="${user.id}" />
	
			<h4>Mon profil</h4>
			
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
			<div class="form-group required">
				<label>Numéro téléphone</label>
			 	<g:field class="form-control form-control-lg" type="text" name="user.telephoneMobile" required="true" value="${ user?.telephoneMobile }"/>
			</div>
			
			<h4 class="mt-4">Mes données</h4>
		    
		    <label class="custom-control custom-checkbox">
	        	<g:checkBox name="user.autorise_user_data" class="custom-control-input" value="${ user.autorise_user_data }"/>
	        	<span class="custom-control-label">
	        		J'autorise l'ALEC du Pays de Rennes à utiliser mon adresse e-mail et mon numéro de téléphone pour me contacter dans le cadre du <g:meta name="app.code"/> (nécessaire pour le bon déroulement du défi).
	        	</span>
	        </label>
	        
	        <label class="custom-control custom-checkbox">
	        	<g:checkBox name="user.autorise_conso_data" class="custom-control-input" value="${ user.autorise_conso_data }"/>
	        	<span class="custom-control-label">
	        		J'autorise l'ALEC du Pays de Rennes à récupérer mes données de consommation d’énergie et d’eau à usage exclusif du <g:meta name="app.code"/> (nécessaire pour le bon déroulement du défi).
	        	</span>
	        </label>
	        
	        <label class="custom-control custom-checkbox">
	        	<g:checkBox name="user.profilPublic" class="custom-control-input" value="${ user.profilPublic }"/>
	        	<span class="custom-control-label">
	        		J'autorise l'ALEC du Pays de Rennes à me faire apparaître dans la liste des participants de mon équipe.
	        	</span>
	        </label>
	        
	        <label class="custom-control custom-checkbox">
	        	<g:checkBox name="user.autorise_share_data" class="custom-control-input" value="${ user.autorise_share_data }"/>
	        	<span class="custom-control-label">
	        		J'autorise l'ALEC du Pays de Rennes à transmettre mon adresse e-mail et mon numéro de téléphone à ma commune/ma mairie pour me contacter dans le cadre du <g:meta name="app.code"/>.
	        	</span>
	        </label>
	        
	         <label class="custom-control custom-checkbox">
	        	<g:checkBox name="user.engage_enedis_account" class="custom-control-input" value="${ user.engage_enedis_account }"/>
	        	<span class="custom-control-label">
	        		Je m’engage à créer mon compte client Enedis <a href="https://mon-compte-particulier.enedis.fr" target="enedis">https://mon-compte-particulier.enedis.fr</a> et à transmettre mes relevés de compteurs d’énergie et d’eau avant et pendant le <g:meta name="app.code"/> (nécessaire pour le bon déroulement du défi).
	        	</span>
	        </label>

			<h4 class="mt-4">Mon habitation</h4>
			
			<g:include action="templateEditByUser" controller="house" params="[user: user]"/>
			
			<div class="row">
				<div class="col">
					<button class="btn btn-primary">Enregistrer</button>
				</div>
				<div class="col text-right">
					<g:link action="todelete" controller="profil" class="btn btn-danger"><app:icon name="user-x"/> Supprimer mon compte</g:link>
				</div>
			</div>
		</g:form>
	</g:applyLayout>
</body>
</html>