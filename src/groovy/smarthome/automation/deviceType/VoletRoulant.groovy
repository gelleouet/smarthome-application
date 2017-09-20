package smarthome.automation.deviceType

import smarthome.automation.ChartTypeEnum;
import smarthome.automation.WorkflowContext;
import smarthome.automation.WorkflowEvent;
import smarthome.automation.WorkflowEventParameter;
import smarthome.automation.WorkflowEventParameters;

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
	@WorkflowEventParameters([
		@WorkflowEventParameter(name="level", label="Level", type="number", minValue="0", maxValue="100")
	])
	def level(WorkflowContext context) {
		this.device.command = "level"
		
		// la valeur est récupérée depuis le param level
		// sinon elle doit être injectée dans "device.value" avant l'appel de la méthode
		if (context.parameters.level != null) {
			this.device.value = context.parameters.level
		}
		
		// vérifie la valeur max
		if (this.device.value?.toDouble() >= 100) {
			this.device.value = 99
		}
	}
	
	
	def isQualitatif() {
		return false
	} 
}
