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
	String icon() {
		"/deviceType/signal.png"
	}
}
