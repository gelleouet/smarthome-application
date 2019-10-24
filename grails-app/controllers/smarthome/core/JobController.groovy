package smarthome.core



import org.springframework.security.access.annotation.Secured

import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.SmartHomeException
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum


@Secured("hasRole('ROLE_ADMIN')")
class JobController extends AbstractController {

	JobService jobService


	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	//@NavigableAction(label = "Jobs", navigation = NavigationEnum.configuration,
	//header = "Système")
	def jobs() {
		def jobs = jobService.list()
		def recordsTotal = jobs.size()
		render(view: 'jobs', model: [recordsTotal: recordsTotal, jobs: jobs])
	}


	/**
	 * Exécution manuelle d'un job
	 * 
	 * @param jobInstance
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "jobs")
	def execute(String jobInstance) {
		jobService.execute(jobInstance)
	}
}
