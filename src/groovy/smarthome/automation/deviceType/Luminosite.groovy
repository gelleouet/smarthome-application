package smarthome.automation.deviceType

import smarthome.automation.DeviceValue;

/**
 * Périphérique Luminosité
 * Aucune action particulière car capteur
 * 
 * @author gregory
 *
 */
class Luminosite extends AbstractDeviceType {

	@Override
	void implPrepareForView() {
		viewParams << DeviceValue.doubleValueAggregategByDay(device)
	}
}
