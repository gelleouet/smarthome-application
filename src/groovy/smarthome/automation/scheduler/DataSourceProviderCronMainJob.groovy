package smarthome.automation.scheduler

import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import smarthome.automation.NotificationAccountService;


/**
 * 
 * @author Gregory
 *
 */
class DataSourceProviderCronMainJob implements Job {

	private static final log = LogFactory.getLog(this)
	private static final int MAX_PAGE = 25
	
	@Autowired
	NotificationAccountService notificationAccountService
	
	@Autowired
	SmarthomeScheduler smarthomeScheduler
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		long nbProvider = notificationAccountService.countWithCron()
		
		// chaque page est envoyé à un sous job pour traitement
		for (int page=0; page <= nbProvider / MAX_PAGE; page++) {
			smarthomeScheduler.scheduleOneShotJob(DataSourceProviderCronPaginateSubJob,
				jobContext.getScheduledFireTime(), [offset: page * MAX_PAGE, max: MAX_PAGE])
		}
		
		log.info "Scheeduling ${nbProvider} datasource provider."
	}
	
	
	public void setSmarthomeScheduler(SmarthomeScheduler smarthomeScheduler) {
		this.smarthomeScheduler = smarthomeScheduler;
	}
	
	void setNotificationAccountService(NotificationAccountService notificationAccountService) {
		this.notificationAccountService = notificationAccountService;
	}
}
