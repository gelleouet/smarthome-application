package smarthome.automation.workflow

import org.apache.commons.logging.Log;

import smarthome.automation.Device;
import smarthome.automation.DeviceEvent;
import smarthome.automation.DeviceService;
import smarthome.automation.Scenario;

class FermetureVoletEnsoleillement {
	Device device
	DeviceEvent deviceEvent
	Map<String, Device> devices = [:]
	DeviceService deviceService
	Scenario workflow
	Log log
	
	boolean execute() {
		Date dateJour = new Date().clearTime()
		def workflowParams = [lux: 900, nbLux: 5, labelTemperature: "T° séjour (FGMS)",
			maxTemperature: 23.5]
		
		// device = luminosité
		
		// moyenne d'ensoleillement sur les dernières valeurs 
		def lastValues = device.lastValues(null, workflowParams.nbLux).collect{it.value}
		
		if (!lastValues) {
			log.warn("FermetureVoletEnsoleillement : no values")
			return false
		}
		
		def avg = (int) lastValues.sum() / lastValues.size()
		
		if (avg < workflowParams.lux) {
			log.warn("FermetureVoletEnsoleillement : luminosité not matching ${workflowParams.lux}lum ($avg)")
			return false
		}
		
		log.info("FermetureVoletEnsoleillement : luminosité matching ${workflowParams.lux}lum ($avg)")
		
		def temperatureDevice = devices.find({ key, device -> device.label == workflowParams.labelTemperature })?.value
		
		// valeur température du jour
		if (dateJour != temperatureDevice?.dateValue?.clearTime()) {
			log.warn("FermetureVoletEnsoleillement : T° too old (${temperatureDevice?.dateValue})")
			return false
		}
		
		// limite température
		if (temperatureDevice?.value?.toDouble() <= workflowParams.maxTemperature) {
			log.warn("FermetureVoletEnsoleillement : T° not matching ${workflowParams.maxTemperature}°C (${temperatureDevice?.value?.toDouble()})")
			return false
		}
		
		log.info("FermetureVoletEnsoleillement : T° matching ${workflowParams.maxTemperature}°C (${temperatureDevice?.value?.toDouble()})")
		
		// conditions sont réunies pour fermer à moitié les volets
		// seuls les volets ouverts à plus de 50% sont fermés
		devices.each { key, device ->
			if (device.label in ["Panoramique", "Baie salle", "Baie salon", "Baie salon [Gilles]"]) {
				if (device.value?.toDouble() > 95) {
					device.value = "25"
					deviceService.invokeAction(device, "level")
				}
			}
		}
		
		return true
	}
}
