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
 * Sous job du job principal DeviceEventCronMainJob pour exécuter les événements sur une liste paginée
 * Les infos de pagination sont récupérées dans les params du job (offset, max)
 * 
 * @author Gregory
 *
 */
class DeviceEventCronPaginateSubJob implements Job {

	private static final log = LogFactory.getLog(this)
	
	
	@Autowired
	DeviceEventService deviceEventService
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		int offset = jobContext.getJobDetail().getJobDataMap().getInt("offset")
		int max = jobContext.getJobDetail().getJobDataMap().getInt("max")
		
		CronExpression cronExpression
		List<Map> events = deviceEventService.listScheduledEventIds([offset: offset, max: max])
		
		log.info "Scheedule paginate events from ${offset} to ${offset+max} : ${events.size()}"
		
		// pour chaque event, vérifie si le cron correspond à la date déclenchée
		for (Map eventMap : events) {
			cronExpression = new CronExpression(eventMap.cron)
			
			// l'événement doit être déclenché car la date correspond au cron
			if (cronExpression.isSatisfiedBy(jobContext.getScheduledFireTime())) {
				DeviceEvent event = deviceEventService.findById(eventMap.id)
				deviceEventService.executeScheduleDeviceEvent(event, jobContext.getScheduledFireTime())
			}	
		}
	}
	
	
	void setDeviceEventService(DeviceEventService deviceEventService) {
		this.deviceEventService = deviceEventService;
	}
}
