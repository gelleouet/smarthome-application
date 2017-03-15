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
 * Les événements ne sont pas excutés dans ce service mais en mode asynchrone
 * dans la queue AMQP pour être répartis entre les différents serveurs
 * 
 * @author Gregory
 *
 */
class HouseConsoCronJob implements Job {

	private static final log = LogFactory.getLog(this)
	
	@Autowired
	HouseService houseService
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		log.info "Calcul consommation maison..."
		
		
		log.info "Fin calcul maison."
	}
	
	
	void setHouseService(HouseService houseService) {
		this.houseService = houseService;
	}
}
