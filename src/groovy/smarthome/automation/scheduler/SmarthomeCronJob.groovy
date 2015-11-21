package smarthome.automation.scheduler

import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.JobBuilder.*;
import org.quartz.JobDetail;
import org.quartz.Trigger;


/**
 * Un job de type cron 
 * 
 * @author Gregory
 *
 */
class SmarthomeCronJob {

	String cron
	
	JobDetail getJobDetail() {
		return newJob(this.getClass())
             .withIdentity(this.getClass().getSimpleName())
             .build();
	}
	
	Trigger getTrigger() {
		return newTrigger()
			.withIdentity(this.getClass().getSimpleName() + "Trigger")
			.withSchedule(cronSchedule(cron))
			.build()
	}
}
