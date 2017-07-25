package smarthome.automation.scheduler

import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import smarthome.automation.DeviceAlertService;


/**
 * Sous job du job principal DeviceAlertMonitoringCronMainJob pour détecter les alertes monitoring
 * Les infos de pagination sont récupérées dans les params du job (offset, max)
 * 
 * @see DeviceAlertMonitoringCronMainJob
 * @author Gregory
 *
 */
class DeviceAlertMonitoringCronPaginateSubJob implements Job {

	private static final log = LogFactory.getLog(this)
	
	
	@Autowired
	DeviceAlertService deviceAlertService
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		int offset = jobContext.getJobDetail().getJobDataMap().getInt("offset")
		int max = jobContext.getJobDetail().getJobDataMap().getInt("max")
		
		log.info "Start monitoring from ${offset} to ${offset+max}"
		
		deviceAlertService.processMonitoring(jobContext.getScheduledFireTime(), [offset: offset, max: max])
	}
	
	
	public void setDeviceAlertService(DeviceAlertService deviceAlertService) {
		this.deviceAlertService = deviceAlertService;
	}
	
}
