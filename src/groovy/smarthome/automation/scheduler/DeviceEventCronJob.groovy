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
 * Les événements ne sont pas excutés dans ce service mais en mode asynchrone
 * dans la queue AMQP pour être répartis entre les différents serveurs
 * 
 * @author Gregory
 *
 */
class DeviceEventCronJob implements Job {

	private static final log = LogFactory.getLog(this)
	
	@Autowired
	DeviceEventService deviceEventService
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		log.info "Scan device scheduled event..."
		
		List<Map> events = deviceEventService.listScheduledEventIds()
		CronExpression cronExpression
		int compteur = 0
		
		// pour chaque event, vérifie si le cron correspond à la date déclenchée
		for (Map eventMap : events) {
			cronExpression = new CronExpression(eventMap.cron)
			
			// l'événement doit être déclenché car la date correspond au cron
			if (cronExpression.isSatisfiedBy(jobContext.getScheduledFireTime())) {
				compteur++
				DeviceEvent event = deviceEventService.findById(eventMap.id)
				deviceEventService.executeScheduleDeviceEvent(event, jobContext.getScheduledFireTime())
			}	
		}
		
		log.info "Scheduling ${compteur} / ${events.size()} events."
	}
	
	
	void setDeviceEventService(DeviceEventService deviceEventService) {
		this.deviceEventService = deviceEventService;
	}
}
