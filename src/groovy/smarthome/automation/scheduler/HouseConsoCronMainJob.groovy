package smarthome.automation.scheduler

import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import smarthome.automation.HouseService;


/**
 * Un job exécuté tous les soirs à minuit pour calculer la conso de l'année
 * Si l'année est incomplète, ca sera une estimation
 * 
 * Ce job ne fait pas le calcul mais se charge de répartir tous les calculs entre plusieurs jobs
 * pour distribuer la charge entre plusieurs consumers
 * 
 * @author Gregory
 *
 */
class HouseConsoCronMainJob implements Job {

	private static final log = LogFactory.getLog(this)
	private static final int MAX_PAGE = 250
	
	@Autowired
	HouseService houseService
	
	@Autowired
	SmarthomeScheduler smarthomeScheduler
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		long nbHouse = houseService.countHouseForCalculConso()
		
		// chaque page est envoyé à un sous job pour traitement
		for (int page=0; page <= nbHouse / MAX_PAGE; page++) {
			smarthomeScheduler.scheduleOneShotJob(HouseConsoCronPaginateSubJob,
				jobContext.getScheduledFireTime(), [offset: page * MAX_PAGE, max: MAX_PAGE])
		}
		
		log.info "Scheeduling ${nbHouse} house conso."
	}
	
	
	public void setSmarthomeScheduler(SmarthomeScheduler smarthomeScheduler) {
		this.smarthomeScheduler = smarthomeScheduler;
	}
	
	void setHouseService(HouseService houseService) {
		this.houseService = houseService;
	}
}
