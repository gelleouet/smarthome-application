grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.7
grails.project.source.level = 1.7
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.fork = [
	// configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
	//  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

	// configure settings for the test-app JVM, uses the daemon by default
	test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
	// configure settings for the run-app JVM
	run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
	// configure settings for the run-war JVM
	war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
	// configure settings for the Console UI JVM
	console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]


activiti.version = "5.18.0"
camel.version = "2.14.0"
camel.groupId = "org.apache.camel"
quartz.version = "2.2.2"
quartz.groupId = "org.quartz-scheduler"
httpclient.groupId = "org.apache.httpcomponents"
httpclient.version = "4.5.2"
aws.version = "1.11.140"
aws.groupId = "com.amazonaws"


grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
	// inherit Grails' default dependencies
	inherits("global") {
		// specify dependency exclusions here; for example, uncomment this to disable ehcache:
		// excludes 'ehcache'
	}
	log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
	checksums true // Whether to verify checksums on resolve
	legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

	repositories {
		inherits true // Whether to inherit repository definitions from plugins

		grailsPlugins()
		grailsHome()
		mavenLocal()
		grailsCentral()
		mavenCentral()
		// uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
		//mavenRepo "http://repository.codehaus.org"
		//mavenRepo "http://download.java.net/maven/2/"
		//mavenRepo "http://repository.jboss.com/maven2/"
	}

	dependencies {
		// specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
		// runtime 'mysql:mysql-connector-java:5.1.29'
		runtime 'org.postgresql:postgresql:9.3-1101-jdbc41'
		test "org.grails:grails-datastore-test-support:1.0-grails-2.4"
		
		//compile 'javax.websocket:javax.websocket-api:1.1'
		
		bundle('javax.websocket:javax.websocket-api:1.1') {
			// This line is necessary for deployment to Tomcat, since
			// Tomcat comes with its own version of javax.websocket-api.
			export = false
		  }
		
		//compile "com.fasterxml.jackson.core:jackson-core:2.9.5"
		
		compile "$camel.groupId:camel-core:$camel.version"
		compile "$camel.groupId:camel-groovy:$camel.version"
		compile "$camel.groupId:camel-spring:$camel.version"
		compile "$camel.groupId:camel-stream:$camel.version"
		compile "$camel.groupId:camel-velocity:$camel.version"
		compile "$camel.groupId:camel-mail:$camel.version"
		compile "$camel.groupId:camel-rabbitmq:$camel.version"
		compile "$camel.groupId:camel-gson:$camel.version"
		compile "$camel.groupId:camel-script:$camel.version"

		compile "$quartz.groupId:quartz:$quartz.version"
		compile "$quartz.groupId:quartz-jobs:$quartz.version"
		
		compile "$httpclient.groupId:httpclient:$httpclient.version"
		compile "$httpclient.groupId:fluent-hc:$httpclient.version"
		
		compile ("org.activiti:activiti-engine:$activiti.version") {
			excludes "spring-beans", "jackson-core"
		}
		compile ("org.activiti:activiti-spring:$activiti.version") {
			excludes "commons-dbcp", "commons-pool"
		}
		compile ("$aws.groupId:aws-java-sdk-sns:$aws.version") {
		
		}
	}

	plugins {
		// plugins for the build system only
		build ":tomcat:7.0.55"

		// plugins for the compile step
		compile ":scaffolding:2.1.2"
		compile ':cache:1.1.8'
		compile ":asset-pipeline:1.9.9"
		//compile ":platform-core:1.0.0"
		compile ':spring-security-core:2.0-RC4'
		compile "org.grails.plugins:spring-security-acl:2.0.1"
		compile ":rabbitmq:1.0.0"

		// plugins needed at runtime but not for compilation
		runtime ":hibernate4:4.3.5.5" // or ":hibernate:3.6.10.17"
		//runtime ":database-migration:1.4.0"
		runtime ":jquery:1.11.1"

		// Uncomment these to enable additional asset-pipeline capabilities
		//compile ":sass-asset-pipeline:1.9.0"
		//compile ":less-asset-pipeline:1.10.0"
		//compile ":coffee-asset-pipeline:1.8.0"
		//compile ":handlebars-asset-pipeline:1.3.0.3"
	}
}
