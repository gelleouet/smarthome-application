package smarthome.rule

import smarthome.core.SmartHomeException;



/**
 * Détecte l'implémentation de l'objet
 * 
 * @author gregory
 *
 */
class DeviceTypeDetectRule implements Rule<Object, String> {

	/*
	 * Map contenant un mot clé en key et l'implémentation en value
	 */
	private static final Map IMPL = [
		"luminance": "smarthome.automation.deviceType.Luminosite",
		"temperature": "smarthome.automation.deviceType.Temperature",
		"roller shutter": "smarthome.automation.deviceType.VoletRoulant",
		"smoke": "smarthome.automation.deviceType.DetecteurFumee",
		"motion": "smarthome.automation.deviceType.DetecteurPresence",
		"switch": "smarthome.automation.deviceType.BoutonOnOff"
	]
	
	Map parameters
	
	
	@Override
	String execute(Object datas) throws SmartHomeException {
		String implClass = datas?.implClass
		
		if (datas.label) {
			def detectImpl = IMPL.find { key, value ->
				datas.label.toLowerCase().contains(key)
			}
			
			if (detectImpl) {
				return detectImpl.value
			}
		}
		
		return implClass ?: "smarthome.automation.deviceType.Capteur"
	}
}
