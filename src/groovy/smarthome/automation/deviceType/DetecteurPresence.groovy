package smarthome.automation.deviceType

import smarthome.automation.ChartTypeEnum;

/**
 * Périphérique Détecteur présence
 * Aucune action particulière car capteur
 * 
 * @author gregory
 *
 */
class DetecteurPresence extends AbstractDeviceType {
	def isQualitatif() {
		return false
	} 
}
