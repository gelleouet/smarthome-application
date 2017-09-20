package smarthome.automation.scheduler

import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import smarthome.automation.DeviceAlertService;

/**
 * Un job exécuté toutes les minutes (le niveau le plus fin)
 * pour détecter les alertes monitoring
 * 
 * Ce job n'exécute pas les événements mais se charge uniquement de calculer le nombre de devices
 * et de créer des sous jobs distribués (DeviceAlertMonitoringCronPaginateSubJob) en paginant les devices 
 * pour répartir le travail entre plusieurs consumers
 * 
 * Le job créé des pages de 1000 objets
 * 
 * @see DeviceAlertMonitoringCronPaginateSubJob
 * @author Gregory
 *
 */
class DeviceAlertMonitoringCronMainJob implements Job {

	private static final log = LogFactory.getLog(this)
	private static final int MAX_PAGE = 25
	
	
	@Autowired
	SmarthomeScheduler smarthomeScheduler
	
	@Autowired
	DeviceAlertService deviceAlertService
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		long nbDevice = deviceAlertService.countDeviceLevelAlertMonitoring()
		
		// chaque page est envoyé à un sous job pour traitement
		if (nbDevice) {
			for (int page=0; page <= nbDevice / MAX_PAGE; page++) {
				smarthomeScheduler.scheduleOneShotJob(DeviceAlertMonitoringCronPaginateSubJob,
					jobContext.getScheduledFireTime(), [offset: page * MAX_PAGE, max: MAX_PAGE])
			}
		}
		
		log.info "Monitoring ${nbDevice} devices."
	}
	
	
	public void setSmarthomeScheduler(SmarthomeScheduler smarthomeScheduler) {
		this.smarthomeScheduler = smarthomeScheduler;
	}


	public void setDeviceAlertService(DeviceAlertService deviceAlertService) {
		this.deviceAlertService = deviceAlertService;
	}
	
	
}
