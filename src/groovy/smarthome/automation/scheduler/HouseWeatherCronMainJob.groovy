package smarthome.automation.scheduler

import groovy.time.TimeCategory;

import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import smarthome.automation.HouseWeatherService;


/**
 * Un job exécuté tous les soirs à minuit pour calculer les prévisions météo de chaque maison
 * 
 * Ce job ne fait pas le calcul mais se charge de répartir tous les calculs entre plusieurs jobs
 * pour distribuer la charge entre plusieurs consumers
 * 
 * Il gère aussi plusieurs providers de service météo. Généralement, il y a des quotas sur les appels
 * des services, donc on gère une config avec le nom du provider (et des paramètres) pour chaque tranche de maisons
 * 
 * @author Gregory
 *
 */
class HouseWeatherCronMainJob implements Job {

	private static final log = LogFactory.getLog(this)
	private static final int MAX_PAGE = 25
	
	@Autowired
	HouseWeatherService houseWeatherService
	
	@Autowired
	SmarthomeScheduler smarthomeScheduler
	
	@Autowired
	GrailsApplication grailsApplication
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		long nbHouse = houseWeatherService.countHouseForWeather()
		long idx = 0
		Date dateExecution = jobContext.getScheduledFireTime()
		
		def providers = grailsApplication.config.weather.providers.iterator()
		
		if (!providers.hasNext()) {
			throw new JobExecutionException("No weather provider !")
		}
		
		def provider = providers.next()
		
		// chaque page est envoyé à un sous job pour traitement
		for (int page=0; page <= nbHouse / MAX_PAGE; page++) {
			idx += MAX_PAGE
			
			// si le nombre d'appels dépasse la limite, on change de provider
			if (idx > provider.maxQuery) {
				if (providers.hasNext()) {
					provider = providers.next()
					idx = 0
				} else {
					throw new JobExecutionException("No more weather provider !")
				}
			}
			
			// l'exécution sera décalée d'une minute pour chaque sous-job
			// cela permet d'éviter de surcharger le webservice météo et d'éviter d'atteindre des quotas 
			// en nombre d'appel / sec
			use (TimeCategory) {
				dateExecution = dateExecution + (page).minute	
			}
			
			smarthomeScheduler.scheduleOneShotJob(HouseWeatherCronPaginateSubJob, dateExecution,
				[offset: page * MAX_PAGE, max: MAX_PAGE, providerImpl: provider.impl, providerParams: provider.params])
		}
		
		log.info "Scheeduling ${nbHouse} house weather."
	}
	
	
	void setSmarthomeScheduler(SmarthomeScheduler smarthomeScheduler) {
		this.smarthomeScheduler = smarthomeScheduler;
	}

	void setHouseWeatherService(HouseWeatherService houseWeatherService) {
		this.houseWeatherService = houseWeatherService;
	}

	void setGrailsApplication(GrailsApplication grailsApplication) {
		this.grailsApplication = grailsApplication;
	}
	
}
