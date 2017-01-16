package smarthome.automation.workflow

import org.apache.commons.logging.Log;

import smarthome.automation.Device;
import smarthome.automation.DeviceEvent;
import smarthome.automation.DeviceService;
import smarthome.automation.Workflow;

class FermetureVoletEnsoleillement {
	Device device
	DeviceEvent deviceEvent
	Map<String, Device> devices = [:]
	DeviceService deviceService
	Workflow workflow
	Log log
	
	boolean execute() {
		Date dateJour = new Date().clearTime()
		
		// device = luminosité
		
		// moyenne d'ensoleillement > 1000lux sur les 3 dernières valeurs 
		def lastValues = device.lastValues(null, 3).collect{it.value}
		
		if (!lastValues) {
			log.warn("FermetureVoletEnsoleillement : no values")
			return false
		}
		
		def avg = lastValues.sum() / lastValues.size()
		
		if (avg < 1000) {
			log.warn("FermetureVoletEnsoleillement : luminosité not matching 1000lum ($avg)")
			return false
		}
		
		log.info("FermetureVoletEnsoleillement : luminosité matching 950lum ($avg)")
		
		def temperatureDevice = devices.find({ key, device -> device.label == "T° séjour (FGMS)" })?.value
		
		// valeur température du jour
		if (dateJour != temperatureDevice?.dateValue?.clearTime()) {
			log.warn("FermetureVoletEnsoleillement : T° too old (${temperatureDevice?.dateValue})")
			return false
		}
		
		// limite température (23°C)
		if (temperatureDevice?.value?.toDouble() <= 23) {
			log.warn("FermetureVoletEnsoleillement : T° not matching 23°C (${temperatureDevice?.value?.toDouble()})")
			return false
		}
		
		log.info("FermetureVoletEnsoleillement : T° matching 23°C (${temperatureDevice?.value?.toDouble()})")
		
		// conditions sont réunies pour fermer à moitié les volets
		// seuls les volets ouverts à plus de 50% sont fermés
		devices.each { key, device ->
			if (device.label in ["Volet panoramique", "Volet salle", "Volet salon", "Volet salon [Gilles]"]) {
				if (device.value?.toDouble() > 50) {
					device.value = "50"
					deviceService.invokeAction(device, "level")
				}
			}
		}
		
		return true
	}
}
