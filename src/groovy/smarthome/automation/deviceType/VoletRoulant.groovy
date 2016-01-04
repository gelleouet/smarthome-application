package smarthome.automation.deviceType

import smarthome.automation.ChartTypeEnum;
import smarthome.automation.WorkflowContext;
import smarthome.automation.WorkflowEvent;

/**
 * Périphérique Volet roulant
 * 
 * Actions
 * - open
 * - close
 * - level
 * 
 * @author gregory
 *
 */
class VoletRoulant extends AbstractDeviceType {

	/**
	 * Ouvre entièrement le volet roulant
	 * 
	 * @param context
	 * @return
	 */
	@WorkflowEvent
	def open(WorkflowContext context) {
		this.device.command = "on"
		this.device.value = 99
	}
	
	
	/**
	 * Ferme entièrement le volet roulant
	 *
	 * @param context
	 * @return
	 */
	@WorkflowEvent
	def close(WorkflowContext context) {
		this.device.command = "off"
		this.device.value = 0
	}
	
	
	/**
	 * Arrête la commande en cours
	 * Il suffit de renvoyer la même command avec la même valeur
	 *
	 * @param context
	 * @return
	 */
	@WorkflowEvent
	def stop(WorkflowContext context) {
		
	}

	
	/**
	 * Ouvre (ou ferme) à une position intermédiaire définie par value
	 *
	 * @param context
	 * @return
	 */
	@WorkflowEvent
	def level(WorkflowContext context) {
		this.device.command = "level"
		// la valeur doit déjà être injectée sur le device
		
		// vérifie la valeur max
		if (this.device.value?.toDouble() >= 100) {
			this.device.value = 99
		}
	}
	
	
	def isQualitatif() {
		return false
	} 
}
