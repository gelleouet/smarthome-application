// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

grails.config.locations = [SmartHomeSecurityDefaultConfig]

if (System.env["smarthome.config.location"]) {
	grails.config.locations << "file:" + System.env["smarthome.config.location"]
	println "Use external configuration from system.env : " + System.env["smarthome.config.location"]
} else if (System.properties["smarthome.config.location"]) {
	grails.config.locations << "file:" + System.properties["smarthome.config.location"]
	println "Use external configuration from system.properties : " + System.properties["smarthome.config.location"]
}

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format

	all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
	atom:          'application/atom+xml',
	css:           'text/css',
	csv:           'text/csv',
	form:          'application/x-www-form-urlencoded',
	html:          ['text/html', 'application/xhtml+xml'],
	js:            'text/javascript',
	json:          ['application/json', 'text/json'],
	multipartForm: 'multipart/form-data',
	rss:           'application/rss+xml',
	text:          'text/plain',
	hal:           ['application/hal+json', 'application/hal+xml'],
	xml:           ['text/xml', 'application/xml']]


// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
	views {
		gsp {
			encoding = 'UTF-8'
			htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
			codecs {
				expression = 'html' // escapes values inside ${}
				scriptlet = 'html' // escapes output from scriptlets in GSPs
				taglib = 'none' // escapes output from taglibs
				staticparts = 'none' // escapes output from static template parts
			}
		}
		// escapes all not-encoded output at final stage of outputting
		// filteringCodecForContentType.'text/html' = 'html'
	}
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password', 'newPassword', 'confirmPassword', 'newPassword']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

environments {

	development {
		grails.logging.jul.usebridge = true
		grails.plugin.springsecurity.debug.useFilter = false

		log4j.main = {
			appenders {
				console name:'stdout', layout:pattern(conversionPattern: "[${appVersion}][%p %d %c{1}] %m%n")
			}

			debug 'smarthome',
				'org.hibernate.SQL'
				//'org.apache.camel.component',
				//'org.springframework.transaction.interceptor'

			//trace 'org.springframework.security.web.authentication.rememberme',
			//		'org.springframework.security.web.authentication'


			info 'grails.app.services',
				'grails.app.controllers',
				'org.hibernate'
				//'net.sf.ehcache.hibernate',
				
			root {
				info()
			}
					
		}
	}


	production {
		grails.logging.jul.usebridge = false

		log4j.main = {
			appenders {
				console name:'stdout', layout:pattern(conversionPattern: "[${appVersion}][%p %d %c{1}] %m%n")
			}

			info 'smarthome', 'grails.app', 'org.apache.camel.component'
		}
	}
}


grails.cache.enabled = true


// ---------------------------------------------------------------------
// 	USER CONFIGURATION (override by grails.config.locations if not empty)
// ---------------------------------------------------------------------

smarthome {
	cluster.serverId = System.properties["smarthome.cluster.serverId"]

	pagination {
		defaultMax = 25
		maxBackend = 500
	}
}

social {
	twitter = "https://twitter.com/ALEC_Rennes"
	facebook = "https://www.facebook.com/groups/legranddefienergieeteau2020/"
	instagram = "https://www.instagram.com/alec_rennes/"
	web = "https://www.alec-rennes.org/gdee/"
}

grails.databinding.dateFormats = ['yyyy-MM-dd', 'dd/MM/yyyy', 'yyyy-MM-dd HH:mm:ss.S', "yyyy-MM-dd'T'hh:mm:ss'Z'"]


quartz.scheduler.instanceName = "SmarthomeQuartzScheduler"
quartz.scheduler.instanceId = "AUTO"
quartz.threadPool.class = "org.quartz.simpl.SimpleThreadPool"
quartz.threadPool.threadCount = 5

quartz.jobStore.isClustered = true
quartz.jobStore.tablePrefix = "quartz.QRTZ_"
quartz.jobStore.class = "org.quartz.impl.jdbcjobstore.JobStoreTX"
quartz.jobStore.driverDelegateClass = "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate"
quartz.jobStore.dataSource = "smarthomeDataSource"
quartz.jobStore.clusterCheckinInterval = 20000

environments {
	development {
		quartz.dataSource.smarthomeDataSource.driver = "org.postgresql.Driver"
		quartz.dataSource.smarthomeDataSource.URL = "jdbc:postgresql://localhost:5432/smarthome"
		quartz.dataSource.smarthomeDataSource.user = "postgres"
		quartz.dataSource.smarthomeDataSource.password = "${System.properties['smarthome.datasource.password']}"
		quartz.dataSource.smarthomeDataSource.maxConnections = 2
	}
	production {
		quartz.dataSource.smarthomeDataSource.jndiURL = "java:comp/env/smartHomeDataSource"
		quartz.scheduler.skipUpdateCheck = true
	}
}

