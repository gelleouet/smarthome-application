package smarthome.automation.deviceType.catalogue

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
	def on() {
		device.value = "1"
	}
	
	
	/**
	 * Envoit la valeur 0
	 *
	 * @return
	 */
	def off() {
		device.value = "0"
	}
	
	
	/**
	 * Envoit la valeur 1
	 *
	 * @return
	 */
	def onOff() {
		device.value = device.value as Double == 1 ? "0" : "1"
	}
}
