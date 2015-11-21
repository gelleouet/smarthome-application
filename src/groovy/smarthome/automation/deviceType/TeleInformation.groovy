package smarthome.automation.deviceType

import java.util.Map;

import smarthome.automation.DeviceValue;

/**
 * Périphérique Télé-info EDF
 * 
 * @author gregory
 *
 */
class TeleInformation extends AbstractDeviceType {
	/**
	 * @see smarthome.automation.deviceType.AbstractDeviceType.metaValuesName()
	 */
	@Override
	Map metaValuesInfo() {
		[
			'opttarif': [label: 'Option tarifaire', trace: false],
			'ptec': [label: 'Période tarifaire', trace: false],
			'isousc': [label: 'Instansité souscrite (A)', trace: false],
			'imax': [label: 'Instansité maximale (A)', trace: false],
			'hchp': [label: 'Total heures pleines (Wh)', trace: true],
			'hchc': [label: 'Total heures creuses (Wh)', trace: true],
			'papp': [label: 'Puissance apparente (VA)', trace: false],
			'hcinst': [label: 'Période heures creuses (Wh)', trace: true],
			'hpinst': [label: 'Période heures pleines (Wh)', trace: true],
			'adps' : [label: 'Avertissement Dépassement Puissance Souscrite (A)', trace: false]
		]
	}

	
	/**
	 * @see smarthome.automation.deviceType.AbstractDeviceType.chartDataTemplate()
	 */
	@Override
	def chartDataTemplate() {
		'/deviceType/teleInformation/teleInformationChartDatas'
	}
	
	
	
	/**
	 * @see smarthome.automation.deviceType.AbstractDeviceType.prepateMetaValuesForSave()
	 */
	@Override
	def prepareMetaValuesForSave() {
		// calcul conso heure creuse sur la période
		def hc = device.metavalue("hchc")
		device.addMetavalue("hcinst", [value: "0"])
		
		if (hc) {
			// récupère la dernière valeur hchc
			def lastHC = DeviceValue.findAllByDeviceAndName(device, "hchc", [sort: "dateValue", order: "desc", max: 1])
			
			if (lastHC) {
				def conso = hc.value.toLong() - lastHC[0].value.toLong()
				device.addMetavalue("hcinst", [value: conso.toString()])
			}
		}

		// calcul conso heure pleine sur la période
		def hp = device.metavalue("hchp")
		device.addMetavalue("hpinst", [value: "0"])
		
		if (hp) {
			// récupère la dernièer valeur hchc
			def lastHP = DeviceValue.findAllByDeviceAndName(device, "hchp", [sort: "dateValue", order: "desc", max: 1])
					
			if (lastHP) {
				def conso = hp.value.toLong() - lastHP[0].value.toLong()
				device.addMetavalue("hpinst", [value: conso.toString()])
			}
		}
	}
}
