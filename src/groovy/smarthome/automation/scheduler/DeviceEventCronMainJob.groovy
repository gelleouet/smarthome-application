package smarthome.automation.scheduler

import org.apache.commons.logging.LogFactory;
import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import smarthome.automation.DeviceEvent;
import smarthome.automation.DeviceEventService;

/**
 * Un job exécuté toutes les minutes (le niveau le plus fin)
 * pour scanner toutes les events planifiés et calculés si un event 
 * doit être déclenché
 * 
 * Ce job n'exécute pas les événements mais se charge uniquement de calculer le nombre d'événements
 * et de créer des sous jobs distribués (DeviceEventCronPaginateSubJob) en paginant les event 
 * pour répartir le travail entre plusieurs consumers
 * 
 * Le job créé des pages de 1000 events
 * 
 * @see DeviceEventCronPaginateSubJob
 * @author Gregory
 *
 */
class DeviceEventCronMainJob implements Job {

	private static final log = LogFactory.getLog(this)
	private static final int MAX_PAGE = 1000
	
	
	@Autowired
	SmarthomeScheduler smarthomeScheduler
	
	@Autowired
	DeviceEventService deviceEventService
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		long nbEvent = deviceEventService.countScheduledEvents()
		
		// chaque page est envoyé à un sous job pour traitement
		for (int page=0; page <= nbEvent / MAX_PAGE; page++) {
			smarthomeScheduler.scheduleOneShotJob(DeviceEventCronPaginateSubJob,
				jobContext.getScheduledFireTime(), [offset: page * MAX_PAGE, max: MAX_PAGE])
		}
		
		log.info "Scheeduling ${nbEvent} events."
	}
	
	
	public void setSmarthomeScheduler(SmarthomeScheduler smarthomeScheduler) {
		this.smarthomeScheduler = smarthomeScheduler;
	}
	
	void setDeviceEventService(DeviceEventService deviceEventService) {
		this.deviceEventService = deviceEventService;
	}
}
