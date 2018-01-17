package smarthome.automation.scheduler

import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import smarthome.automation.House;
import smarthome.automation.HouseService;
import smarthome.automation.HouseWeatherService;


/**
 * Sous job du job principal HouseWeatherCronMainJob pour calculer la météo sur une liste paginée
 * Les infos de pagination sont récupérées dans les params du job (offset, max)
 * 
 * @author Gregory
 *
 */
class HouseWeatherCronPaginateSubJob implements Job {

	private static final log = LogFactory.getLog(this)
	
	@Autowired
	HouseWeatherService houseWeatherService
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		int offset = jobContext.getJobDetail().getJobDataMap().getInt("offset")
		int max = jobContext.getJobDetail().getJobDataMap().getInt("max")
		String providerImpl = jobContext.getJobDetail().getJobDataMap().getString("providerImpl")
		def providerParams = jobContext.getJobDetail().getJobDataMap().get("providerParams")
		
		List<Map> houses = houseWeatherService.listHouseIdsForWeather([offset: offset, max: max])
		
		log.info "Scheedule house weather from ${offset} to ${offset+max} : ${houses.size()}"
		
		houses.each {
			houseWeatherService.calculWeather(House.read(it.id), providerImpl, providerParams)
		}
	}
	
	
	public void setHouseWeatherService(HouseWeatherService houseWeatherService) {
		this.houseWeatherService = houseWeatherService;
	}
}
