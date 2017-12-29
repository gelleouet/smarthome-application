package smarthome.automation.deviceType

import smarthome.automation.DeviceValue;

/**
 * Périphérique Température
 * Aucune action particulière car capteur
 * 
 * @author gregory
 *
 */
class Temperature extends AbstractDeviceType {

	@Override
	void implPrepareForView() {
		viewParams << DeviceValue.doubleValueAggregategByDay(device)
	}

}
