import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.activiti.spring.SpringProcessEngineConfiguration
import org.activiti.spring.ProcessEngineFactoryBean
import org.activiti.engine.impl.history.HistoryLevel
import smarthome.automation.scheduler.SmarthomeScheduler
import smarthome.plugin.NavigationItemUtils
import smarthome.security.SmartHomeUserDetailsService


// Place your Spring DSL code here
beans = {
	xmlns context:"http://www.springframework.org/schema/context"
	xmlns camel:"http://camel.apache.org/schema/spring"


	context.'component-scan'('base-package': "smarthome")


	// Auto détection des routes Camel depuis le contexte Spring
	camel.camelContext(id: "camelContext") {
		camel.package "smarthome.esb.routes"
	}

	transactionAttributeSource(org.springframework.transaction.annotation.AnnotationTransactionAttributeSource)

	"defaultGrailsjava.lang.DoubleConverter"(smarthome.core.DoubleValueConverter)

	// gestionnaire de crons
	// seconde | minute | heure | jour du mois (1-31) | mois | jour semaine (1-7) | année
	smarthomeScheduler(SmarthomeScheduler) {
		jobs = [
			// monitoring des devices toutes les minutes
			'smarthome.automation.scheduler.DeviceAlertMonitoringCronMainJob' : "0 * * * * ?",
			// déclenchement des events planifiés toutes les minutes
			'smarthome.automation.scheduler.EventCronMainJob' : "0 * * * * ?",
			// calcul des consos maison tous les soirs juste avant minuit
			'smarthome.automation.scheduler.HouseConsoCronMainJob' : "59 59 23 * * ?",
			// calcul des prévisions météo tous les soirs juste après minuit
			'smarthome.automation.scheduler.HouseWeatherCronMainJob' : "0 1 0 * * ?"
		]
	}

	userDetailsService(SmartHomeUserDetailsService)

	permissionEvaluator(smarthome.security.SmartHomePermissionEvaluator) {
		permissionFactory = ref('aclPermissionFactory')
	}

	// Démarre une instance Activiti Workflow
	processEngineConfiguration(SpringProcessEngineConfiguration) {
		transactionManager = ref("transactionManager")
		dataSource = ref("dataSource")
		history = HistoryLevel.NONE.getKey()
		dbHistoryUsed = false
		createDiagramOnDeploy = true
		asyncExecutorActivate = true
		asyncExecutorEnabled = true
		jobExecutorActivate = false

		// configuration SMTP
		mailServerHost = application.config.smtp.hostname
		mailServerPort = application.config.smtp.port
		mailServerDefaultFrom = application.config.smtp.from
		mailServerUsername = application.config.smtp.username
		mailServerPassword = application.config.smtp.password
		mailServerUseTLS = true

		//databaseTablePrefix = application.config.activiti.schemaName
		//tablePrefixIsSchema = true
	}

	processEngine(ProcessEngineFactoryBean) {
		processEngineConfiguration = ref("processEngineConfiguration")
	}

	repositoryService(processEngine: "getRepositoryService")
	runtimeService(processEngine: "getRuntimeService")


	// surchage pour supprimer le cache optimizer
	expressionHandler(DefaultMethodSecurityExpressionHandler) {
		parameterNameDiscoverer = ref('parameterNameDiscoverer')
		expressionParser = ref('expressionParser')
		//permissionCacheOptimizer = ref('aclPermissionCacheOptimizer')
		roleHierarchy = ref('roleHierarchy')
		permissionEvaluator = ref('permissionEvaluator')
	}

	// juste utilisé pour injecter le context dans variable static accessible depuis n'importe où
	applicationUtils(smarthome.core.ApplicationUtils)


	dataConnectApi(smarthome.api.DataConnectApi) {
		grailsApplication = ref('grailsApplication')
	}
}
