package smarthome.automation.scheduler

import org.apache.commons.logging.LogFactory;
import org.hibernate.service.spi.InjectService;
import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

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
		
		List<DeviceEvent> events = deviceEventService.listScheduledEvent()
		CronExpression cronExpression
		
		// pour chaque event, vérifie si le cron correspond à la date déclenchée
		for (DeviceEvent event : events) {
			cronExpression = new CronExpression(event.cron)
			
			// l'événement doit être déclenché car la date correspond au cron
			if (cronExpression.isSatisfiedBy(jobContext.getScheduledFireTime())) {
				deviceEventService.executeScheduleDeviceEvent(event)
			}	
		}
		
		log.info "Triggered ${events.size()} events."
	}
	
	
	void setDeviceEventService(DeviceEventService deviceEventService) {
		this.deviceEventService = deviceEventService;
	}
}
