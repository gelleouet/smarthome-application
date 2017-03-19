package smarthome.automation.deviceType

import smarthome.automation.ChartTypeEnum;
import smarthome.automation.DeviceValue;
import smarthome.automation.WorkflowContext;
import smarthome.automation.WorkflowEvent;

/**
 * Un bouton normal on/off
 * Le bouton conserve son etat
 * 
 * Actions:
 * - on : envoit la valeur 1 à l'agent.
 * - off : envoit la valeur 0 à l'agent
 * - onOff : en fonction de son etat, envoit la valeur inverse
 * 
 * @author gregory
 *
 */
class BoutonOnOff extends AbstractDeviceType {
	
	/**
	 * Envoit la valeur 1
	 * 
	 * @return
	 */
	@WorkflowEvent
	def on(WorkflowContext context) {
		device.command = "on"
		device.value = "1"
	}
	
	
	/**
	 * Envoit la valeur 0
	 *
	 * @return
	 */
	@WorkflowEvent
	def off(WorkflowContext context) {
		device.command = "off"
		device.value = "0"
	}
	
	
	/**
	 * Envoit la valeur 1
	 *
	 * @return
	 */
	@WorkflowEvent
	def onOff(WorkflowContext context) {
		device.value = DeviceValue.parseDoubleValue(device.value) == 1 ? "0" : "1"
		device.command = device.value == "1" ? "on" : "off"
	}
	
	
	def isQualitatif() {
		return false
	} 
}
