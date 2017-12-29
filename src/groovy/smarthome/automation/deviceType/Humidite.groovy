package smarthome.automation.deviceType

import smarthome.automation.DeviceValue;

/**
 * Périphérique Humidité
 * Aucune action particulière car capteur
 * 
 * @author gregory
 *
 */
class Humidite extends AbstractDeviceType {
	
	@Override
	void implPrepareForView() {
		viewParams << DeviceValue.doubleValueAggregategByDay(device)
	}
}
