package smarthome.automation.scheduler

import org.apache.commons.logging.LogFactory
import org.quartz.CronExpression
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.beans.factory.annotation.Autowired
import smarthome.core.SmartHomeException
import smarthome.automation.NotificationAccount
import smarthome.automation.NotificationAccountService
import smarthome.automation.datasource.AbstractDataSourceProvider


/**
 * 
 * @author gregory.elleouet@gmail.com<Grégory Elléoouet>
 *
 */
class DataSourceProviderCronPaginateSubJob implements Job {

	private static final log = LogFactory.getLog(this)

	@Autowired
	NotificationAccountService notificationAccountService


	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		int offset = jobContext.getJobDetail().getJobDataMap().getInt("offset")
		int max = jobContext.getJobDetail().getJobDataMap().getInt("max")

		CronExpression cronExpression
		List<Map> providers = notificationAccountService.listIdsWithCron([offset: offset, max: max])

		log.info "Scheedule datasource provider from ${offset} to ${offset+max} : ${providers.size()}"

		for (Map accountMap : providers) {
			NotificationAccount notificationAccount = notificationAccountService.findById(accountMap.id)
			cronExpression = new CronExpression(notificationAccount.notificationAccountSender.cron)

			// l'événement doit être déclenché car la date correspond au cron
			if (cronExpression.isSatisfiedBy(jobContext.getScheduledFireTime())) {
				try {
					def providerImpl = notificationAccount.notificationAccountSender.newNotificationSender()

					if (providerImpl instanceof AbstractDataSourceProvider) {
						providerImpl.execute(notificationAccount)
					}
				} catch (SmartHomeException ex) {
					log.error("Cannot execute provider ${notificationAccount.id} : ${ex.message}")
				}
			}
		}
	}

	void setNotificationAccountService(NotificationAccountService notificationAccountService) {
		this.notificationAccountService = notificationAccountService
	}
}
