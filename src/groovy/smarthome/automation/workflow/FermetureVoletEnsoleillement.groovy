package smarthome.automation.workflow

import smarthome.automation.Device;
import smarthome.automation.DeviceEvent;
import smarthome.automation.DeviceService;

class FermetureVoletEnsoleillement {
	Device device
	DeviceEvent deviceEvent
	Map<String, Device> devices = [:]
	DeviceService deviceService
	
	void execute() {
		Date dateJour = new Date().clearTime()
		
		// device = luminosité
		
		// limite d'ensoleillement (900lux) et valeur du jour
		if (device.value?.toDouble() <= 900 || dateJour != device.dateValue?.clearTime()) {
			return	
		}
		
		def temperatureDevice = devices.find { value.label == "T° séjour (FGMS)" }
		
		// limite température (23°C) et valeur du jour
		if (temperatureDevice?.value?.toDouble() <= 23 || dateJour != temperatureDevice?.dateValue?.clearTime()) {
			return
		}
		
		// le seuil de luminosité doit être maintenu pendant au moins 15min
		// cela évite les fermetures sur des pics de valeur brefs
		def duration
		
		use(groovy.time.TimeCategory) {
			duration = new Date() - device.dateValue
		}
		
		if (duration.minutes < 15) {
			return
		}
		
		// conditions sont réunies pour fermer à moitié les volets
		// seuls les volets ouverts à plus de 50% sont fermés
		devices.each {
			if (value.label in ["Volet panoramique", "Volet salle", "Volet salon", "Volet salon [Gilles]"]) {
				if (value.value?.toDouble() > 50) {
					value.value = "50"
					deviceService.invokeAction(value, "level")
				}
			}
		}
	}
}
