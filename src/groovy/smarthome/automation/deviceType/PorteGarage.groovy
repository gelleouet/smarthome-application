package smarthome.automation.deviceType

import smarthome.automation.ChartTypeEnum;
import smarthome.automation.DeviceValue;
import smarthome.automation.WorkflowContext;
import smarthome.automation.WorkflowEvent;
import smarthome.automation.WorkflowEventParameter;
import smarthome.automation.WorkflowEventParameters;

/**
 * Lumière. Composant de haut niveau avec abstraction d'un bouton poussoir, ou d'un interrupteur
 * 
 * @author gregory
 *
 */
class PorteGarage extends AbstractDeviceType {
	
	/**
	 * Envoit la valeur 1
	 * Gère les minuteries
	 * 
	 * @return
	 */
	@WorkflowEvent
	@WorkflowEventParameters([
		@WorkflowEventParameter(name=WorkflowContext.DELAY_PARAM, label="Delay (min)", type="number", minValue="0", required=false),
		@WorkflowEventParameter(name=WorkflowContext.TIMER_PARAM, label="Timer (min)", type="number", minValue="0", required=false)
	])
	def open(WorkflowContext context) {
		if (device.metadata("timeout")?.value) {
			device.command = "on"
			device.value = "1"
		} else {
			device.command = "on"
			device.value = "1"
		}
		
		return context.withTimer("close", [:])
	}
	
	
	/**
	 * Envoit la valeur 0
	 *
	 * @return
	 */
	@WorkflowEvent
	@WorkflowEventParameters([
		@WorkflowEventParameter(name=WorkflowContext.DELAY_PARAM, label="Delay (min)", type="number", minValue="0", required=false),
		@WorkflowEventParameter(name=WorkflowContext.TIMER_PARAM, label="Timer (min)", type="number", minValue="0", required=false)
	])
	def close(WorkflowContext context) {
		if (device.metadata("timeout")?.value) {
			device.command = "on"
			device.value = "1"
		} else {
			device.command = "off"
			device.value = "0"
		}
		
		return context.withTimer("open", [:])
	}
	
	
	/**
	 * Envoit la valeur 1
	 *
	 * @return
	 */
	@WorkflowEvent
	@WorkflowEventParameters([
		@WorkflowEventParameter(name=WorkflowContext.DELAY_PARAM, label="Delay (min)", type="number", minValue="0", required=false),
		@WorkflowEventParameter(name=WorkflowContext.TIMER_PARAM, label="Timer (min)", type="number", minValue="0", required=false)
	])
	def openOrClose(WorkflowContext context) {
		if (device.metadata("timeout")?.value) {
			device.command = "on"
			device.value = "1"
		} else {
			device.value = DeviceValue.parseDoubleValue(device.value) == 1 ? "0" : "1"
			device.command = (device.value == "1" ? "on" : "off")
		}
		
		return context.withTimer("openOrClose", [:])
	}
	
	
	def isQualitatif() {
		return false
	} 
}
