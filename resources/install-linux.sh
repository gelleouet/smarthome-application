#!/bin/sh

# Installation des scripts de buid/deploy/exec de l'application
# Les scripts sont copiés dans le dossier /usr/local/bin et sont accessibles
# directement car présent dans PATH
#
# Demande les chemins aux repertoires necessaires (grails, java, template,
# instance tomcat) et construit les scripts relatifs à cette config

# @author Gregory Elleouet <gregory.elleouet@gmail.com>
#

# Config du user

echo "Java environnement"
echo "------------------"
read -p "Java home: " JAVA_HOME
read -p "Grails home: " GRAILS_HOME
read -p "Catalina home: " CATALINA_HOME
read -p "Catalina base: " CATALINA_BASE

echo "Build environnement"
echo "-------------------"
read -p "Project path: " PROJECT_PATH

echo "Deploy environnement"
echo "--------------------"
read -p "Deploy path: " DEPLOY_PATH
read -p "HTTP port [default=8080]: " HTTP_PORT
read -p "HTTPS port [default=8443]: " HTTPS_PORT
read -p "AJP port [default=8009]: " AJP_PORT
read -p "SHUTDOWN port [default=8005]: " SHUTDOWN_PORT
read -p "JDBC host [default=localhost]: " JDBC_HOST
read -p "JDBC port [default=5432]: " JDBC_PORT
read -p "JDBC database: " JDBC_DATABASE
read -p "JDBC user [default=postgres]: " JDBC_USER
read -p "JDBC password: " JDBC_PASSWORD

if [ -z "$JAVA_HOME" ]; then
  echo "Java home is required !"
  exit 1
fi

if [ -z "$GRAILS_HOME" ]; then
  echo "Grails home is required !"
  exit 1
fi

if [ -z "$CATALINA_HOME" ]; then
  echo "Catalina home is required !"
  exit 1
fi

if [ -z "$CATALINA_BASE" ]; then
  echo "Catalina base is required !"
  exit 1
fi

if [ -z "$PROJECT_PATH" ]; then
  echo "Project path is required !"
  exit 1
fi

if [ -z "$DEPLOY_PATH" ]; then
  echo "Deploy path is required !"
  exit 1
fi

if [ -z "$JDBC_DATABASE" ]; then
  echo "JDBC database is required !"
  exit 1
fi

if [ -z "$JDBC_PASSWORD" ]; then
  echo "JDBC password is required !"
  exit 1
fi


# Init variable

PROJECT_NAME=`basename $PROJECT_PATH`
PATH_SCRIPT="/usr/local/bin"


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

cat <<EOF > ${PATH_SCRIPT}/${PROJECT_NAME}-deploy.sh
#!/bin/sh
read -p "Instance ID [1-9]: " INSTANCE_ID
read -p "Project version [x.y.z]: " PROJECT_VERSION
INSTANCE_NAME="${PROJECT_NAME}-\${INSTANCE_ID}"
INSTANCE="$DEPLOY_PATH/\$INSTANCE_NAME"
WAR_FILE="$PROJECT_PATH/build/libs/${PROJECT_NAME}-\${PROJECT_VERSION}.war"

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
cp \$WAR_FILE \$INSTANCE/webapps/${PROJECT_NAME}.war

sed -i -e "s/serverId.pid/\${INSTANCE_NAME}.pid/g" \$INSTANCE/bin/setenv.sh
sed -i -e "s/serverId=serverId/serverId=\${INSTANCE_NAME}/g" \$INSTANCE/bin/setenv.sh

sed -i -e "s/port=\"8005\"/port=\"${INSTANCE_ID}${SHUTDOWN_PORT:-8005}\"/g" \$INSTANCE/conf/server.xml
sed -i -e "s/port=\"8080\"/port=\"${INSTANCE_ID}${HTTP_PORT:-8080}\"/g" \$INSTANCE/conf/server.xml
sed -i -e "s/redirectPort=\"8443\"/redirectPort=\"${INSTANCE_ID}${HTTPS_PORT:-8443}\"/g" \$INSTANCE/conf/server.xml
sed -i -e "s/port=\"8009\"/port=\"${INSTANCE_ID}${AJP_PORT:-8009}\"/g" \$INSTANCE/conf/server.xml

sed -i -e "s/#jdbc-host#/${JDBC_HOST:-localhost}/g" \$INSTANCE/conf/context.xml
sed -i -e "s/#jdbc-port#/${JDBC_PORT:-5432}/g" \$INSTANCE/conf/context.xml
sed -i -e "s/#jdbc-database#/${JDBC_DATABASE}/g" \$INSTANCE/conf/context.xml
sed -i -e "s/#jdbc-user#/${JDBC_USER:-postgres}/g" \$INSTANCE/conf/context.xml
sed -i -e "s/#jdbc-password#/${JDBC_PASSWORD}/g" \$INSTANCE/conf/context.xml
EOF

chmod +x ${PATH_SCRIPT}/${PROJECT_NAME}-deploy.sh


# Construction des  fichiers [proxy-name]-start et [proxy-name]-stop 
# Ces scripts prennent en paramètre l'ID de l'instance à commander

cat <<EOF > ${PATH_SCRIPT}/${PROJECT_NAME}-start.sh
#!/bin/sh
read -p "Instance ID [1-9]: " INSTANCE_ID
INSTANCE_NAME="${PROJECT_NAME}-\${INSTANCE_ID}"
INSTANCE="$DEPLOY_PATH/\$INSTANCE_NAME"
export JAVA_HOME="$JAVA_HOME"
export CATALINA_HOME="$CATALINA_HOME"
export CATALINA_BASE="\$INSTANCE"
cd \$CATALINA_BASE
\$CATALINA_HOME/bin/startup.sh
EOF


cat << EOF > ${PATH_SCRIPT}/${PROJECT_NAME}-stop.sh
#!/bin/sh
read -p "Instance ID [1-9]: " INSTANCE_ID
INSTANCE_NAME="${PROJECT_NAME}-\${INSTANCE_ID}"
INSTANCE="$DEPLOY_PATH/\$INSTANCE_NAME"
export JAVA_HOME="$JAVA_HOME"
export CATALINA_HOME="$CATALINA_HOME"
export CATALINA_BASE="\$INSTANCE"
\$CATALINA_HOME/bin/shutdown.sh
EOF

chmod +x ${PATH_SCRIPT}/${PROJECT_NAME}-start.sh
chmod +x ${PATH_SCRIPT}/${PROJECT_NAME}-stop.sh
