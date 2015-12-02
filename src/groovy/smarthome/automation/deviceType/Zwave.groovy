package smarthome.automation.deviceType

import smarthome.automation.WorkflowContext;
import smarthome.automation.WorkflowEvent;

/**
 * Composant ZWave générique
 * Permet d'envoyer des commandes directement au device
 * 
 * @author gregory
 *
 */
class Zwave extends AbstractDeviceType {
	
	/**
	 * Envoit une commande
	 * 
	 * @return
	 */
	@WorkflowEvent
	def send(WorkflowContext context) {
		
	}

	@Override
	public Object icon() {
		"/deviceType/signal.png"
	}
}
