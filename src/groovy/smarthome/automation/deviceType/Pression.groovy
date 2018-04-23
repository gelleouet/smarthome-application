package smarthome.automation.deviceType

import smarthome.automation.DeviceValue;

/**
 * Périphérique Pression atmosphérique
 * 
 * Aucune action particulière car capteur
 * 
 * @author gregory
 *
 */
class Pression extends AbstractDeviceType {
	
	@Override
	void implPrepareForView() {
		viewParams << DeviceValue.doubleValueAggregategByDay(device)
	}
}
