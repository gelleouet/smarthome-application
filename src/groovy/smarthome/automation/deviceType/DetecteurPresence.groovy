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
	/**
	 * Le détecteur a t'il détecté un mouvement
	 * 
	 * @return
	 */
	boolean isMovement() {
		(device.value?.isDouble() && device.value?.toDouble() > 0) || "true".equals(device.value)
	}
}
