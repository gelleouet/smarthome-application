<html>
<head>
<meta name='layout' content='anonymous' />
</head>

<body>
	<g:applyLayout name="applicationContent">
	
		<h2 class="separator">Découvrir l'application <g:meta name="app.code"/></h2>
		
		<h3>Présentation</h3>
		
		<p>SmartHome est une application serveur centralisant toute la gestion de vos périphériques domotique. Elle référence chaque périphérique et permet de les
		piloter à distance par l'intermédiaire d'agents installés dans votre maison. Chaque agent est connecté en permanence sur le serveur, ce qui
		permet d'envoyer et recevoir des informations en temps réel (mode push).</p>
		
		<p>L'application SmartHome est open-source : chacun peut y contribuer en y développant de nouvelles fonctionnalités. 
		Les codes sources <a href="https://github.com/gelleouet/smarthome-application">smarthome-application</a> sont hébergés sur le dépôt github. 
		L'application est développée avec le framework <a href="https://grails.org/">Grails (Java / Groovy)</a></p>
		
		
		<h6 >SECURITE : l'application est hébergée sur un serveur disposant d'un certificat SSL, ce qui garantit la protection et la confidentialité des données
		échangées entre votre maison et le serveur. De plus, les agents doivent d'abord s'authentifier sur le serveur avec des identifiants délivrés
		lors de votre inscription avant de pouvoir établir une connexion permanente. Une activation manuelle de votre part est nécessaire pour débloquer un agent</h6>
		
		<asset:image src="/gandi-ssl.png" />
		
		<h6>L'infrastructure hébergeant l'application est entièrement redondante : 2 serveurs Web et 2 serveurs base de données sont configurés pour faire 
		de la répartition de charge au niveau des serveurs Web (Apache + Tomcat) et de la réplication temps réel au niveau des serveurs bases de données (PostgreSQL)</h6>
		
		<h3>Les agents</h3>
		
		<p>Un programme en <a href="https://nodejs.org/">Node.JS</a> a été développé pour être installé spécifiquement sur un <a href="http://www.raspberrypi.org/">Raspberry</a>.
		Il est donc possible de connecter tout Raspberry sur le serveur SmartHome en installant le programme <a href="https://github.com/gelleouet/smarthome-raspberry">smarthome-raspberry</a>. 
		Ce programme est disponible sur le <a href="https://github.com/gelleouet/smarthome-raspberry">dépôt github</a> avec sa procédure d'installation.</p>
		
		<p>Plusieurs agents peuvent être connectés au même compte.</p>
		
		<div>
			<asset:image src="/site/raspberry.jpeg" style="width: 256px;"/>
			<h6>A l'avenir, des Raspberry pré-configurés pourront être commandés sur le store SmartHome.</h6>
		</div>
		
		<p><a href="https://github.com/gelleouet/smarthome-raspberry/wiki">Procédure pour l'installation d'un agent Raspberry et la connexion de vos premiers périphériques.</a></p>
		
		<h3>Fonctionnalités</h3>
		
		<p>L'application SmartHome permet pour l'instant de piloter et d'enregistrer les valeurs de vos périphériques. Il est possible de générer des graphiques
		à partir des historiques des valeurs. Liste des périphériques reconnus :</p>
		<ul>
			<li>Tout périphérique filaire pouvant être branché sur le port GPIO du Raspberry (Ex : contact sec, interrupteur, etc.)</li>
			<li>Sondes de température Dallas (protocole One-Wire)</li>
			<li>Périphériques HomeEasy (433Mhz) (à venir...)</li>
			<li>Périphériques Z-Wave (à venir...)</li>
		</ul>
		
		<asset:image src="/site/devices-grid.png" style="width: 256px;"/>
		<asset:image src="/site/graphique.png" style="width: 256px;"/>
		
		<p>Résumé des fonctionnalités :</p>
		
		<ul>
			<li>Des programmes (ou scénarios) permettent de piloter plusieurs périphériques en même temps et de répondre à des événements déclenchés depuis les agents. (à venir...)</li>
			<li>Graphique à partir des historiques des valeurs</li>
			<li>Construction de graphiques personnalisés pour combiner plusieurs périphériques et comparer des valeurs (à venir...)</li>
		</ul>	
		
		
		<h3>Tarification</h3>
		
		<p>L'application SmartHome est entièrement gratuite. Seul le coût du Raspberry vous incombe mais il n'y a pas de frais pour l'utilisation de la partie serveur.</p>
		
		<p><a href="http://www.raspberrypi.org/raspberry-pi-2-on-sale/">Commande d'un Raspberry sur le site officiel.</a></p>	
	</g:applyLayout>
</body>
</html>