import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;

import smarthome.automation.scheduler.DeviceEventCronMainJob;
import smarthome.automation.scheduler.SmarthomeScheduler;
import smarthome.plugin.NavigationItemUtils;


// Place your Spring DSL code here
beans = {
	xmlns context:"http://www.springframework.org/schema/context"
	xmlns camel:"http://camel.apache.org/schema/spring"
	
	
	context.'component-scan'('base-package': "smarthome")
	
	
	// Auto détection des routes Camel depuis le contexte Spring
	camel.camelContext(
		id: "camelContext"
	) {
		camel.package "smarthome.esb.routes"
	}
	
	transactionAttributeSource(org.springframework.transaction.annotation.AnnotationTransactionAttributeSource)
	
	"defaultGrailsjava.lang.DoubleConverter"(smarthome.core.DoubleValueConverter)
	
	// gestionnaire de crons
	// seconde | minute | heure | jour du mois (1-31) | mois | jour semaine (1-7) | année 
	smarthomeScheduler(SmarthomeScheduler) {
		jobs = [
			// déclenchement des events planifiés toutes les minutes
			'smarthome.automation.scheduler.DeviceEventCronMainJob' : "0 * * * * ?",
			// calcul des consos maison tous les soirs juste avant minuit
			'smarthome.automation.scheduler.HouseConsoCronMainJob' : "59 59 23 * * ?"
		]
	}
	
	permissionEvaluator(smarthome.security.SmartHomePermissionEvaluator) {
		permissionFactory = ref('aclPermissionFactory')
	}
	
	
	// surchage pour supprimer le cache optimizer
	expressionHandler(DefaultMethodSecurityExpressionHandler) {
		parameterNameDiscoverer = ref('parameterNameDiscoverer')
		expressionParser = ref('expressionParser')
		//permissionCacheOptimizer = ref('aclPermissionCacheOptimizer')
		roleHierarchy = ref('roleHierarchy')
		permissionEvaluator = ref('permissionEvaluator')
	}
}
