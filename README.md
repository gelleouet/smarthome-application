# BeMyHomeSmart

## 1. Présentation

BeMyHomeSmart est une application libre et gratuite pour surveiller et contrôler
votre maison connectée. Les fonctionnalités développées répondent aux besoins classiques
de la domotisation d'une maison (pilotage volets roulants, prises, portails, etc)
et d'analyses des consommations. Les fonctions automatiques sont aussi implémentées
avec les scénarios ou déclenchements d'événements.  

Vous pouvez rapidement vous [créer un compte](https://www.jdevops.com/smarthome/register/account)
depuis l'application web, ou même de part son caractère opensource et sa licence EUPL,
l'installer sur votre propre serveur avec cette [procédure](https://github.com/gelleouet/smarthome-application/wiki/Installation-serveur). L'application Web n'est qu'un composant de
la solution, car pour connecter les différents objets connectés, il faudra installer
un ou plusieurs "agents". BeMyHomeSmart s'appuie sur des [Raspberry](https://www.raspberrypi.org/)
sur lesquels il faut installer un [programme spécial](https://github.com/gelleouet/smarthome-raspberry).
Celui-ci est aussi diffusé en opensource sous licence EUPL. Afin de faciliter son
installation, des [images sous Raspbian](https://github.com/gelleouet/smarthome-raspberry/wiki/Installation-simple-depuis-image-pr%C3%A9-configur%C3%A9e)
ont été pré-configurés.

## 2. Développement

### 2.1 Configuration

L'application Web est développée en Java/Groovy avec le framework Web [Grails](http://www.grails.org).
Il faut donc télécharger une JDK (uniquement testé avec la version 8 max) et le
framework [Grails](https://grails.org/download.html).  

Configurer les variables d'environnements :  

    - JAVA_HOME = ...  
    - GRAILS_HOME = ...  
    
Démarrer la console Grails en mode interactif depuis le répertoire du projet 
(ceci permet de ne charger qu'une seule fois l'env Grails et d'exécuter ensuite
les commandes grails. Cela est plus rapide car les dépendances ne sont checkées
qu'une seule fois et cela prend du temps) :  
 
     bash> grails  
     grails> run-app  
     grails> war  

Exécuter une commande Grails en dehors de la console (depuis le répertoire du projet) :  

     bash> grails war  
     bash> grails run-app  

[Voir la liste des commandes disponibles](http://docs.grails.org/latest/ref/Command%20Line/Usage.html)

Si vous utilisez l'environnement de développement Eclipse, un [script](https://github.com/gelleouet/smarthome-application/blob/master/scripts/Eclipse.groovy)
a été développé
pour créer les fichiers _.project_ et _.classpath_ en fonction des dépendances
du projet. A la création du projet ou à chaque ajout de plugins ou librairies,
exécuter la commande :  

     bash> grails eclipse  

Pour exécuter le projet, il faudra installer dans votre environnement :  

- Service PostgreSQL. Le schéma peut être créé avec le script SQL fourni dans le projet
[ddl.sql](https://github.com/gelleouet/smarthome-application/blob/master/grails-app/migrations/ddl.sql)
- Service RabbitMQ. Les exchanges et queues sont créés dynamiquement

Démarrer le projet dans l'environnement de développement :  

     bash> grails run-app -Dsmarthome.cluster.serverId=.. -Dsmarthome.datasource.password=.. -Dsmarthome.config.location=..

Variables d'environnement :  

- _smarthome.cluster.serverId_ : nom unique d'une instance dans un cluster.
Permet d'envoyer des messages entre instances  
- _smarthome.datasource.password_ : mot de passe du PostgreSQL  
- _smarthome.config.location_ : le fichier de config avec les credentials et
paramètres de l'application

### 2.2 Conventions Grails

Pour plus de détails, consulter la [documentation officielle](http://docs.grails.org/latest/)

Le framework Grails repose principalement sur les frameworks Spring/Hibernate et
sur le principe [Convention Over Configuration](http://docs.grails.org/latest/guide/gettingStarted.html#conventionOverConfiguration)
pour la configuration du projet. Les noms et les emplacements des fichiers sont
utilisés plutôt qu'une configuration explicite (même si cela est autorisé aussi).

La structure des répertoires est très importante :

- grails-app
    - conf : fichier build, config, datasource, resources, url mappings...
    - controllers : web controllers. C in MVC model
    - domain : entités mappés en base
    - services : services métier
    - taglib
    - utils
    - views : V in MVC model
- src/java
- src/groovy
- src/templates/scaffolding : templates pour générer les artefacts en ligne de commande
- test/unit : tests unitaires
- test/integration : tests d'intégration

Le principe d'injection de dépendances (IOC) est géré automatiquement par le framework.
Le simple fait de déclarer une propriété dans un bean (controller, service) dont le nom
ou type est géré par le contexte Spring, suffit à injecter cette dépendance. Pour
les cas manuels, il est toujours possible d'utiliser une configuration classique
par annotation ou en déclarant le bean dans le fichier _conf/spring/resources.groovy_

Pour faciliter certaines tâches au sein de chaque artefact (controller, service), des
classes de base abstraites sont disponibles :

- _smarthome.core.AbstractController_ : handler des erreurs, pagination, gestion des
messages info, error, warning dans les request, retour par défaut des response
- _smarthome.core.AbstractService_ : gestion par défaut des transactions, des envois
de message asynchrones AMQP, et méthodes de base sur les domain
- _smarthome.core.AbstractRuleService_ : service de base pour les règles métier

## 3. Architecture

### 3.1 Objectifs

L'application Web est développée pour être scalable le plus simplement et le plus
efficacement. Plusieurs instances peuvent être déployées pour augmenter les capacités de traitement
des requêtes. Les performances sont aussi un point très important sur les choix de
développement. Un traitement particulier est appliqué dès lors qu'une tâche peut 
être complexe ou nécessiter plus de ressources. 

L'infrastructure en place répond à des contraintes de haute disponiblité pour
assurer le service si un noeud s'arrête (problème ou simple maintenance). 2 instances
de l'application sont démarrées à minima et le traffic est réparti entre ces instances
par un load-balancer [Apache HTTP Server](https://httpd.apache.org/).

![Architecture]()

La sécurité est un point très important. Tous les échanges avec l'application Web
sont sécurisés avec un certificat SSL (chiffrement des données).

### 3.2 Cluster Webapp / Stateless

La webapp est configurée pour être déployée dans un cluster de containers Web.
Même si l'utilisation de la session n'est pas recommandé car cela ajoute des contraintes
sur le cluster, certains cas peuvent le justifier. Les objets en session sont répliqués
sur l'ensemble des noeuds du cluster. Cela permet de s'afranchir du mode *sticky session*
du proxy (les requêtes d'un user sont toujours envoyées sur le même noeud) et d'utiliser
le mode *round robin* (envoi des requêtes de manière équitable (ou géré par coef) à
l'ensemble des noeuds)

La réplication des session au niveau Webapp est gérée dans le fichier _web.xml_. Une
configuration est aussi nécessaire sur les container (tomcat, jetty, etc.)

     <distributable/>  

Chaque objet en session doit implémenter _java.io.Serializable_  
Il faut quand même toujours privilégier d'autres moyens de partager/persistée une
information que d'utiliser la session (ex : base de données, etc.)

Pour faciliter le déploiement de nouvelles instances Web, les fonctions de lecture/écriture
de fichiers sont à *proscrire*. Cela permet à l'application de ne pas être dépendante
d'un système de fichiers local. Si l'utilisation de ressources (au sens général)
est nécessaire, l'application doit obligatoirement passer par un service tiers
accessible à tous les noeuds du cluster. Ce principe accentue le caractère stateless
de l'application et améliore sa scalabilité.

### 3.3 Bus AMQP
Ce bus AMQP géré par le service [RabbitMQ](https://www.rabbitmq.com/) offre 2 avantages :

- gérer des tâches de manière asynchrone. Les traitements longs (ex : mail, génération pdf)
peuvent être envoyés sur le bus de manière à libérer une requête. Cela fluidifie
l'exécution des requêtes et améliore la navigation et l'utilisation de l'application
web
- diviser/paralléliser des tâches dans un contexte multithread et à un plus haut
niveau dans le conexte du cluster car tous les noeuds d'un cluster sont à l'écoute
de ces messages

[Apache Camel](https://camel.apache.org/) gère le routing et la transformation
des messages. Les routes disponibles sont implémentées dans le package _smarthome.esb.routes_
(ex : envoi des mails, exécution des workflows, réception des messages des agents, etc.)

Le service RabbitMQ est aussi configuré en cluster et les messages reçus par un
un noeud sont répliqués sur les autres noeuds.

### 3.4 Pagination

Pour des raisons de performance, tous les services chargeant des données en base
doivent être paginés. Ceci et d'autant plus vrai pour les services liés à l'API
publique afin de ne pas écrouler le serveur en cas de mauvaise utilisation de celle-ci.
C'est un détail très simple, mais à lui tout seul, il peut déclencher de nombreuses
erreurs en production liés à des dépassements de mémoire.

Depuis les controllers, la classe _smarthome.core.AbstractController_ permet de gérer
facilement l'envoi des données liées à la pagination (offset, max)

### 3.5 Workflow / Business Process Management

L'utilisation du moteur de workflow [Activiti](https://www.activiti.org/)
au sein de l'application permet de s'adapter
le plus précisément aux process des utilisateurs tout en gardant un code simple.
Les services développés doivent rester le plus simple possible et ne pas dépendre
d'autres services. Ensuite toute la logique et les enchainements de services sont
gérés par les process. La complexité et la logique métier sont extraites du code et
sont modélisés en processus métier. Ces process peuvent en plus être modifiés et
redéployés à "chaud". Ils sont créés dans des éditeurs _wysiwyg_ et peuvent être
modélisés par des profils non développeurs.

En terme de développement, c'est un excellement moyen de garder du code simple et
de ne pas être obligé d'écrire des "usines à gaz" pour répondre aux problématiques
utilisateur.

Dans le cas de l'application, ces workflows sont exécutés de manière asynchrone
depuis l'exécution d'un service "point d'entrée". Il suffit simplement d'annoter
une méthode de service avec _smarthome.core.AsynchronousWorkflow_ et de préciser
le nom du workflow à exécuter. Tout le contexte de la méthode (arguments en entrée
et résultat de sortie) est passé au workflow. Il peut y avoir des déclenchements
en cascade de workflows si un service du workflow est lui-même annoté pour en 
exécuter un. 

### 3.6 Scheduler Cluster

Un gestionnaire de tâches planifiées est utilisé dans l'application et géré avec
la librairie [Quartz](http://www.quartz-scheduler.org/). Ce gestionnaire est
configuré en cluser. Les tâches sont réparties entre les noeuds les moins occupés
et un système de verrou s'assure qu'une tâche n'est exécutée qu'une seule fois.
En découpant les tâches en sous-tâches, on peut ainsi dans certains cas paralléliser
et accéler le traitement de celles-ci. Les jobs sont développées aevc le modèle suivant :
un job parent calcule le nombre total de tâches et créé des paquets de tâches
(en se basant sur les configurations de pagination). Chaque paquet de tâches est
ensuite créé dans le gestionnaire de tâche en tant que tâche unique et planifiée ASAP.
Chaque noeud/thread libre vient prendre une tâche et la traite.

Les jobs sont disponibles dans le paquet _smarthome.automation.scheduler_ et sont
référencés dans le fichier _conf/spring/resources.groovy_
