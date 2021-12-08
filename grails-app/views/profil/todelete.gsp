<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Suppression compte', navigation: 'Compte']">
	
		<g:applyLayout name="messageError">
			<p><strong>Vous voulez supprimer définitivement votre compte <g:meta name="app.code"/> ?</strong></p>
		</g:applyLayout>

		<br/>
		<p><strong>Cette opération est irréversible et les données suivantes seront supprimées :</strong></p>
		<hr/>
		<p>Données de profil et d'habitation : nom, prénom, mot de passe, email, ville, chauffage, etc.</p>
		<hr/>
		<p>Données des compteurs : index, consommations, configuration des compteurs, etc.</p>
		<hr/>
		<p>Données des défis : consommations, économies, classements, etc.</p>			
			
		<br/>
		<p><strong>En confirmant la suppression de votre compte, celui-ci sera d'abord désactivé et un mail 
		sera envoyé à l'équipe <g:meta name="app.code"/>. Ils procèderont à la suppression réelle du compte
		dans un délai de 30 jours. Durant cette période, vous pourrez prendre contact avec eux pour annuler la suppression.</strong>
		</p>
		
		<p><strong>Si vous êtes sûr de supprimer votre compte, veuillez cliquer sur le bouton "Supprimer mon compte".</p>
		
		
		<div class="mt-4">
			<g:link action="deleteProfil" class="btn btn-danger">Supprimer mon compte</g:link>
		</div>
	</g:applyLayout>
</body>
</html>