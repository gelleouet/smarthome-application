import smarthome.automation.scheduler.DeviceEventCronJob;
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
	smarthomeScheduler(SmarthomeScheduler) {
		jobs = [
			deviceEventCronJob(DeviceEventCronJob) {
				// toutes les minutes
				cron = "0 * * * * *"
			}
		]
	}
}
