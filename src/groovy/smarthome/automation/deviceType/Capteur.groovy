package smarthome.automation.deviceType

import smarthome.automation.WorkflowContext;
import smarthome.automation.WorkflowEvent;

/**
 * Capteur générique
 * 
 * @author gregory
 *
 */
class Capteur extends AbstractDeviceType {
	
	@Override
	public Object icon() {
		"/deviceType/signal.png"
	}
	
	
	def isQualitatif() {
		return true
	} 
}
