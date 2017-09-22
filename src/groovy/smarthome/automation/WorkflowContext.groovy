package smarthome.automation

import groovy.time.TimeCategory;
import groovy.time.TimeDuration;
import smarthome.automation.deviceType.AbstractDeviceType;
import smarthome.core.SmartHomeException;


/**
 * Contexte d'exécution des implémentations devices
 * 
 * @author Gregory
 *
 */
class WorkflowContext {
	static final String DELAY_PARAM = "delay"
	static final String TIMER_PARAM = "timer"
	
	
	Map parameters = [:]
	String actionName
	Device device
	Date dateAction
	
	
	
	/**
	 * Déclenche un nouveau context si option delay
	 * 
	 * @param newActionName
	 * @param newParameters
	 * @return
	 */
	WorkflowContext withDelay() {
		return withParameter(DELAY_PARAM, actionName, parameters)
	}
	
	
	/**
	 * Déclenche un nouveau context si option timer
	 * 
	 * @param newActionName
	 * @param newParameters
	 * @return
	 */
	WorkflowContext withTimer(String newActionName, Map newParameters) {
		return withParameter(TIMER_PARAM, newActionName, newParameters)
	}
	
	/**
	 * Déclenche un nouveau context différé (soit delay, soit timer)
	 * 
	 * @param newAction
	 */
	private WorkflowContext withParameter(String parameter, String newActionName, Map newParameters) {
		Integer parameterValue = this.getParameter(parameter)
		
		if (parameterValue) {
			WorkflowContext context = new WorkflowContext(device: device,
				actionName: newActionName, parameters: newParameters)
			
			use (TimeCategory) {
				context.dateAction = dateAction + new TimeDuration(0, parameterValue, 0, 0)
			}
			
			// ne pas oublier d'enlever le paramètre en question sinon effet sans fin !
			context.clearParameter(parameter)
			
			return context
		}
		
		return null
	}
	
	/**
	 * Retrouve un paramètre Integer
	 * 
	 * @return
	 */
	Integer getParameter(String name) {
		if (parameters[name]?.isInteger()) {
			return parameters[name].toInteger()
		} else {
			return null
		}
	}
	
	
	/**
	 * Efface un paramètre
	 * 
	 * @param name
	 */
	void clearParameter(String name) {
		parameters.remove(name)	
	}
}
