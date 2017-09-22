package smarthome.automation.scheduler

import org.apache.commons.logging.LogFactory;
import org.hibernate.service.spi.InjectService;
import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.apache.commons.lang.StringUtils;
import smarthome.automation.DeviceService;
import smarthome.automation.WorkflowContext;


/**
 * Exécution différée d'un device impl
 * 
 * @author Gregory
 *
 */
class WorkflowContextJob implements Job {

	private static final log = LogFactory.getLog(this)
	
	@Autowired
	DeviceService deviceService
	
	
	/**
	 * Convertit les params du context vers le job
	 * 
	 * @param workflowContext
	 * @return
	 */
	public static Map convertJobParams(WorkflowContext context) {
		Map params = [:]
		params.deviceId = context.device.id
		params.actionName = context.actionName
		params.parameterNames = StringUtils.join(context.parameters.collect { it.key }, ",")
		params.putAll(context.parameters)
		return params
	}
	
	
	@Override
	void execute(JobExecutionContext jobContext) throws JobExecutionException {
		def deviceId = jobContext.getJobDetail().getJobDataMap().getLong("deviceId")
		String actionName = jobContext.getJobDetail().getJobDataMap().getString("actionName")
		String parameterNames = jobContext.getJobDetail().getJobDataMap().getString("parameterNames")
		
		def device = deviceService.findById(deviceId)
		
		Map parameters = [:]
		
		if (parameterNames) {
			for (String name : parameterNames.split(",")) {
				parameters.put(name, jobContext.getJobDetail().getJobDataMap().getString(name))
			}
		}
		
		deviceService.execute(device, actionName, parameters)
	}
}
