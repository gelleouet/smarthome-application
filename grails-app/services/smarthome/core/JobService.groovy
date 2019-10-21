package smarthome.core

import org.quartz.Job
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.transaction.annotation.Transactional

import smarthome.core.AsynchronousMessage
import smarthome.core.SmartHomeException


class JobService extends AbstractService {

	/*@Autowired
	 Map smarthomeJobMap
	 AutowireCapableBeanFactory beanFactory*/


	/**
	 * Liste des jobs sous forme Map key(className) value(cron)
	 * 
	 * @return
	 */
	Map list() {
		//return smarthomeJobMap
	}


	/**
	 * Exécution d'un job
	 * 
	 * @param jobInstance
	 * @throws SmartHomeException
	 */
	void execute(String jobInstance) throws SmartHomeException {
		/*Job job = ClassUtils.forNameInstance(jobInstance)
		 beanFactory.autowireBean(job)
		 job.execute(null)*/
	}
}
