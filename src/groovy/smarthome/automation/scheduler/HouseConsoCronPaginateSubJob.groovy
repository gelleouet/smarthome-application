package smarthome.automation.scheduler

import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import smarthome.automation.HouseService;


/**
 * Sous job du job principal HouseConsoCronMainJob pour calculer les consos sur une liste paginée
 * Les infos de pagination sont récupérées dans les params du job (offset, max)
 * 
 * @author Gregory
 *
 */
class HouseConsoCronPaginateSubJob implements Job {

	private static final log = LogFactory.getLog(this)
	
	@Autowired
	HouseService houseService
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		int offset = jobContext.getJobDetail().getJobDataMap().getInt("offset")
		int max = jobContext.getJobDetail().getJobDataMap().getInt("max")
		
		List<Map> houses = houseService.listHouseIdsForCalculConso([offset: offset, max: max])
		
		log.info "Scheedule house conso from ${offset} to ${offset+max} : ${houses.size()}"
		
		houses.each {
			houseService.asyncCalculConsoAnnuelle(it.id, jobContext.getScheduledFireTime()[Calendar.YEAR])
		}
	}
	
	
	void setHouseService(HouseService houseService) {
		this.houseService = houseService;
	}
}
