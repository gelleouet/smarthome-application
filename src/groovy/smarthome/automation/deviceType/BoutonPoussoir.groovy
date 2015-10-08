package smarthome.automation.deviceType

import smarthome.automation.WorkflowContext;
import smarthome.automation.WorkflowEvent;

/**
 * Un bouton poussoir avec impulsion
 * Le bouton n'a pas d'état on/off, il permet juste d'envoyer un etat on
 * juste le temps d'une impulsion (1 seconde)
 * 
 * Actions:
 * - push : envoit la valeur 1 à l'agent. L'agent est en charge de gérer l'impulsion
 * 
 * @author gregory
 *
 */
class BoutonPoussoir extends AbstractDeviceType {
	
	/**
	 * Envoit une valeur ON juste pendant 1 seconde avant de renvoyer une valeur OFF
	 * 
	 * @return
	 */
	@WorkflowEvent
	def push(WorkflowContext context) {
		device.value = "1"
	}
}
