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

import smarthome.automation.Event;
import smarthome.automation.EventService;

/**
 * Un job exécuté "one-shot" est issu soit d'un timer soit
 * d'un glissement de l'heure programmée
 * 
 * @author Gregory
 *
 */
class EventSimpleJob implements Job {

	private static final log = LogFactory.getLog(this)
	
	@Autowired
	EventService eventService
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		def eventId = jobContext.getJobDetail().getJobDataMap().getLong("eventId")
		def event = eventService.findById(eventId)
		log.info "Execute job ${this.class.name} with event ${event.libelle}..."
		eventService.execute(event, [:])
	}
	
	
	void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
}
