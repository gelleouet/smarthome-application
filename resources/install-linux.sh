#!/bin/sh

# Installation des scripts de buid/deploy de l'application
# Les scripts sont copiés dans le dossier /usr/local/bin et sont accessibles
# directement car présent dans PATH
#
# Demande les chemins aux repertoires necessaires (grails, java, template,
# instance tomcat) et construit les scripts relatifs à cette config
#
# Installe aussi le service systemd pour cette application
# le service n'est pas activé par le script
# il faut lancer manuellement systemctl enable ....
#
# @author Gregory Elleouet <gregory.elleouet@gmail.com>
#


CONFIG_FILE="/root/.smarthome-application.build"


# charge la dernière conf build

if [ -f "$CONFIG_FILE" ]; then
	DEFAULT_JAVA_HOME=`grep "JAVA_HOME" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_GRAILS_HOME=`grep "GRAILS_HOME" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_CATALINA_HOME=`grep "CATALINA_HOME" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_CATALINA_BASE=`grep "CATALINA_BASE" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_PROJECT_PATH=`grep "PROJECT_PATH" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_DEPLOY_PATH=`grep "DEPLOY_PATH" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_DEPLOY_CONTEXT=`grep "DEPLOY_CONTEXT" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_HTTP_PORT=`grep "HTTP_PORT" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_HTTPS_PORT=`grep "HTTPS_PORT" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_AJP_PORT=`grep "AJP_PORT" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_SHUTDOWN_PORT=`grep "SHUTDOWN_PORT" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_JDBC_HOST=`grep "JDBC_HOST" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_JDBC_PORT=`grep "JDBC_PORT" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_JDBC_DATABASE=`grep "JDBC_DATABASE" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_JDBC_USER=`grep "JDBC_USER" $CONFIG_FILE | awk -F "=" '{print $2}'`
	DEFAULT_JDBC_PASSWORD=`grep "JDBC_PASSWORD" $CONFIG_FILE | awk -F "=" '{print $2}'`
fi

# Quelques valeurs par défaut

if [ -z "$DEFAULT_HTTP_PORT" ]; then
	DEFAULT_HTTP_PORT=8080
fi
if [ -z "$DEFAULT_HTTPS_PORT" ]; then
	DEFAULT_HTTPS_PORT=8443
fi
if [ -z "$DEFAULT_AJP_PORT" ]; then
	DEFAULT_AJP_PORT=8009
fi
if [ -z "$DEFAULT_SHUTDOWN_PORT" ]; then
	DEFAULT_SHUTDOWN_PORT=8005
fi
if [ -z "$DEFAULT_JDBC_HOST" ]; then
	DEFAULT_JDBC_HOST="localhost"
fi
if [ -z "$DEFAULT_JDBC_PORT" ]; then
	DEFAULT_JDBC_PORT=5432
fi


# Config du user

echo "-------------------"
echo "Java environnement"
echo "------------------"
read -p "Java home [default=$DEFAULT_JAVA_HOME]: " JAVA_HOME
read -p "Grails home [default=$DEFAULT_GRAILS_HOME]: " GRAILS_HOME
read -p "Catalina home [default=$DEFAULT_CATALINA_HOME]: " CATALINA_HOME
read -p "Catalina base [default=$DEFAULT_CATALINA_BASE]: " CATALINA_BASE

echo "-------------------"
echo "Build environnement"
echo "-------------------"
read -p "Project path [default=$DEFAULT_PROJECT_PATH]: " PROJECT_PATH

echo "-------------------"
echo "Deploy environnement"
echo "--------------------"
read -p "Deploy path [default=$DEFAULT_DEPLOY_PATH]: " DEPLOY_PATH
read -p "Deploy context [default=$DEFAULT_DEPLOY_CONTEXT]: " DEPLOY_CONTEXT
read -p "HTTP port [default=$DEFAULT_HTTP_PORT]: " HTTP_PORT
read -p "HTTPS port [default=$DEFAULT_HTTPS_PORT]: " HTTPS_PORT
read -p "AJP port [default=$DEFAULT_AJP_PORT]: " AJP_PORT
read -p "SHUTDOWN port [default=$DEFAULT_SHUTDOWN_PORT]: " SHUTDOWN_PORT
read -p "JDBC host [default=$DEFAULT_JDBC_HOST]: " JDBC_HOST
read -p "JDBC port [default=$DEFAULT_JDBC_PORT]: " JDBC_PORT
read -p "JDBC database [default=$DEFAULT_JDBC_DATABASE]: " JDBC_DATABASE
read -p "JDBC user  [default=$DEFAULT_JDBC_USER]: " JDBC_USER
read -p "JDBC password  [default=$DEFAULT_JDBC_PASSWORD]: " JDBC_PASSWORD

if [ -z "$JAVA_HOME" ] && [ -z "$DEFAULT_JAVA_HOME" ]; then
  echo "Java home is required !"
  exit 1
elif [ -z "$JAVA_HOME" ]; then
	JAVA_HOME="$DEFAULT_JAVA_HOME"
fi

if [ -z "$GRAILS_HOME" ] && [ -z "$DEFAULT_GRAILS_HOME" ]; then
  echo "Grails home is required !"
  exit 1
elif [ -z "$GRAILS_HOME" ]; then
	GRAILS_HOME="$DEFAULT_GRAILS_HOME"
fi

if [ -z "$CATALINA_HOME" ] && [ -z "$DEFAULT_CATALINA_HOME" ]; then
  echo "Catalina home is required !"
  exit 1
elif [ -z "$CATALINA_HOME" ]; then
	CATALINA_HOME="$DEFAULT_CATALINA_HOME"
fi

if [ -z "$CATALINA_BASE" ] && [ -z "$DEFAULT_CATALINA_BASE" ]; then
  echo "Catalina base is required !"
  exit 1
elif [ -z "$CATALINA_BASE" ]; then
	CATALINA_BASE="$DEFAULT_CATALINA_BASE"
fi

if [ -z "$PROJECT_PATH" ] && [ -z "$DEFAULT_PROJECT_PATH" ]; then
  echo "Project path is required !"
  exit 1
elif [ -z "$PROJECT_PATH" ]; then
	PROJECT_PATH="$DEFAULT_PROJECT_PATH"
fi

if [ -z "$DEPLOY_PATH" ] && [ -z "$DEFAULT_DEPLOY_PATH" ]; then
  echo "Deploy path is required !"
  exit 1
elif [ -z "$DEPLOY_PATH" ]; then
	DEPLOY_PATH="$DEFAULT_DEPLOY_PATH"
fi

if [ -z "$DEPLOY_CONTEXT" ] && [ -z "$DEFAULT_DEPLOY_CONTEXT" ]; then
  echo "Deploy context is required !"
  exit 1
elif [ -z "$DEPLOY_CONTEXT" ]; then
	DEPLOY_CONTEXT="$DEFAULT_DEPLOY_CONTEXT"
fi

if [ -z "$HTTP_PORT" ]; then
	HTTP_PORT="$DEFAULT_HTTP_PORT"
fi

if [ -z "$HTTPS_PORT" ]; then
	HTTPS_PORT="$DEFAULT_HTTPS_PORT"
fi

if [ -z "$AJP_PORT" ]; then
	AJP_PORT="$DEFAULT_AJP_PORT"
fi

if [ -z "$SHUTDOWN_PORT" ]; then
	SHUTDOWN_PORT="$DEFAULT_SHUTDOWN_PORT"
fi

if [ -z "$JDBC_HOST" ]; then
	JDBC_HOST="$DEFAULT_JDBC_HOST"
fi

if [ -z "$JDBC_PORT" ]; then
	JDBC_PORT="$DEFAULT_JDBC_PORT"
fi

if [ -z "$JDBC_DATABASE" ] && [ -z "$DEFAULT_JDBC_DATABASE" ]; then
  echo "JDBC database is required !"
  exit 1
elif [ -z "$JDBC_DATABASE" ]; then
	JDBC_DATABASE="$DEFAULT_JDBC_DATABASE"
fi

if [ -z "$JDBC_USER" ] && [ -z "$DEFAULT_JDBC_USER" ]; then
  echo "JDBC user is required !"
  exit 1
elif [ -z "$JDBC_USER" ]; then
	JDBC_USER="$DEFAULT_JDBC_USER"
fi

if [ -z "$JDBC_PASSWORD" ] && [ -z "$DEFAULT_JDBC_PASSWORD" ]; then
  echo "JDBC password is required !"
  exit 1
elif [ -z "$JDBC_PASSWORD" ]; then
	JDBC_PASSWORD="$DEFAULT_JDBC_PASSWORD"
fi


# Sauvegarde la config

echo "JAVA_HOME=$JAVA_HOME" > $CONFIG_FILE
echo "GRAILS_HOME=$GRAILS_HOME" >> $CONFIG_FILE
echo "CATALINA_HOME=$CATALINA_HOME" >> $CONFIG_FILE
echo "CATALINA_BASE=$CATALINA_BASE" >> $CONFIG_FILE
echo "PROJECT_PATH=$PROJECT_PATH" >> $CONFIG_FILE
echo "DEPLOY_PATH=$DEPLOY_PATH" >> $CONFIG_FILE
echo "DEPLOY_CONTEXT=$DEPLOY_CONTEXT" >> $CONFIG_FILE
echo "HTTP_PORT=$HTTP_PORT" >> $CONFIG_FILE
echo "HTTPS_PORT=$HTTPS_PORT" >> $CONFIG_FILE
echo "AJP_PORT=$AJP_PORT" >> $CONFIG_FILE
echo "SHUTDOWN_PORT=$SHUTDOWN_PORT" >> $CONFIG_FILE
echo "JDBC_HOST=$JDBC_HOST" >> $CONFIG_FILE
echo "JDBC_PORT=$JDBC_PORT" >> $CONFIG_FILE
echo "JDBC_DATABASE=$JDBC_DATABASE" >> $CONFIG_FILE
echo "JDBC_USER=$JDBC_USER" >> $CONFIG_FILE
echo "JDBC_PASSWORD=$JDBC_PASSWORD" >> $CONFIG_FILE


# Init variable

PROJECT_NAME=`basename $PROJECT_PATH`
PATH_SCRIPT="/usr/local/bin"

# Sauvegarde les infos


# Construction du script build "[proxy-name]-build.sh"
# Execute la commande "war" de grails dans le dossier du proxy
# synchronise d'abord le projet avec le depot git

cat <<EOF > ${PATH_SCRIPT}/${PROJECT_NAME}-build.sh
#!/bin/sh
export JAVA_HOME="$JAVA_HOME"
export GRAILS_HOME="$GRAILS_HOME"
cd $PROJECT_PATH
git pull origin master
\${GRAILS_HOME}/bin/grails war
EOF

chmod +x ${PATH_SCRIPT}/${PROJECT_NAME}-build.sh


# Construction du script deploy "[proxy-name]-deploy.sh"
# Cree une instance tomcat à partir d'un template
# il est possible de creer plusieurs instances en specifiant un id (numero) dans le contexte d'un cluster HA
# Installe le service systemd associé à l'instance

SYSTEMD_PATH="/etc/systemd/system"

cat <<EOF > ${PATH_SCRIPT}/${PROJECT_NAME}-deploy.sh
#!/bin/sh
INSTANCE_ID=\$1

if [ -z "\$INSTANCE_ID" ]; then
  read -p "Instance ID [1-9]: " INSTANCE_ID
fi

read -p "Project version [x.y.z]: " PROJECT_VERSION
INSTANCE_NAME="${PROJECT_NAME}-\${INSTANCE_ID}"
INSTANCE="$DEPLOY_PATH/\$INSTANCE_NAME"
WAR_FILE="$PROJECT_PATH/target/${PROJECT_NAME}-\${PROJECT_VERSION}.war"

if [ -z "\$INSTANCE_ID" ]; then
  echo "Instance ID is required !"
  exit 1
fi

if [ -z "\$PROJECT_VERSION" ]; then
  echo "Project version is required !"
  exit 1
fi

if [ ! -f "\$WAR_FILE" ]; then
  echo "War not exist ! (\$WAR_FILE)"
  exit 1
fi

rm -r \$INSTANCE
cp -r $CATALINA_BASE \$INSTANCE
mkdir \$INSTANCE/logs
mkdir \$INSTANCE/temp
mkdir \$INSTANCE/work
mkdir \$INSTANCE/webapps
cp \$WAR_FILE \$INSTANCE/webapps/${DEPLOY_CONTEXT}.war

sed -i -e "s/serverId.pid/\${INSTANCE_NAME}.pid/g" \$INSTANCE/bin/setenv.sh
sed -i -e "s/serverId=serverId/serverId=\${INSTANCE_NAME}/g" \$INSTANCE/bin/setenv.sh

sed -i -e "s/port=\"8005\"/port=\"\${INSTANCE_ID}${SHUTDOWN_PORT}\"/g" \$INSTANCE/conf/server.xml
sed -i -e "s/port=\"8080\"/port=\"\${INSTANCE_ID}${HTTP_PORT}\"/g" \$INSTANCE/conf/server.xml
sed -i -e "s/redirectPort=\"8443\"/redirectPort=\"\${INSTANCE_ID}${HTTPS_PORT}\"/g" \$INSTANCE/conf/server.xml
sed -i -e "s/port=\"8009\"/port=\"\${INSTANCE_ID}${AJP_PORT}\"/g" \$INSTANCE/conf/server.xml

sed -i -e "s/#jdbc-host#/${JDBC_HOST}/g" \$INSTANCE/conf/context.xml
sed -i -e "s/#jdbc-port#/${JDBC_PORT}/g" \$INSTANCE/conf/context.xml
sed -i -e "s/#jdbc-database#/${JDBC_DATABASE}/g" \$INSTANCE/conf/context.xml
sed -i -e "s/#jdbc-user#/${JDBC_USER}/g" \$INSTANCE/conf/context.xml
sed -i -e "s/#jdbc-password#/${JDBC_PASSWORD}/g" \$INSTANCE/conf/context.xml


cat <<EOF1 > ${SYSTEMD_PATH}/\${INSTANCE_NAME}.service
[Unit]
Description=Smarthome Application
After=syslog.target network.target

[Service]
Type=forking
Environment="JAVA_HOME=$JAVA_HOME"
Environment="CATALINA_HOME=$CATALINA_HOME"
Environment="CATALINA_BASE=\$INSTANCE"
PIDFile=/var/run/\${INSTANCE_NAME}.pid
ExecStart=$CATALINA_HOME/bin/startup.sh
ExecStop=/bin/kill -15 \$MAINPID
Restart=on-failure
RestartSec=5s

[Install]
WantedBy=multi-user.target

EOF1


EOF

chmod +x ${PATH_SCRIPT}/${PROJECT_NAME}-deploy.sh

