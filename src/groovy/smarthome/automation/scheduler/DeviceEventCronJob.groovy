package smarthome.automation.scheduler

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.springframework.beans.factory.InitializingBean;

/**
 * Un job exécuté toutes les minutes (le niveau le plus fin)
 * pour scanner toutes les events planifiés et calculés si un event 
 * doit être déclenché
 * 
 * @author Gregory
 *
 */
class DeviceEventCronJob extends SmarthomeCronJob implements InitializingBean, Job {

	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		println "Scan device event cron"
		
	}

}
