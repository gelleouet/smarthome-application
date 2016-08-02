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
 * Un job exécuté "one-shot" est issu soit d'un timer soit
 * d'un glissement de l'heure programmée
 * 
 * @author Gregory
 *
 */
class DeviceEventTimerJob implements Job {

	private static final log = LogFactory.getLog(this)
	
	@Autowired
	DeviceEventService deviceEventService
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		def deviceEventId = jobContext.getJobDetail().getJobDataMap().getLong("deviceEventId")
		def deviceEvent = deviceEventService.findById(deviceEventId)
		
		log.info "Execute job ${this.class.name} with deviceEvent ${ deviceEvent.libelle}..."
		
		deviceEventService.triggerEvent(deviceEvent, '', null)
	}
	
	
	void setDeviceEventService(DeviceEventService deviceEventService) {
		this.deviceEventService = deviceEventService;
	}
}
