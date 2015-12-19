package smarthome.automation.deviceType

import smarthome.automation.ChartTypeEnum;

/**
 * Un contact sec de type on / off
 * 0 = off, 1 = on
 * Aucune action particulière car capteur
 * 
 * @author gregory
 *
 */
class ContactSec extends AbstractDeviceType {
	
	def isQualitatif() {
		return false
	} 
}
