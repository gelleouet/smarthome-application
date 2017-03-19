package smarthome.automation.deviceType

import java.util.Date;
import java.util.Map;

import smarthome.automation.DataModifierEnum;
import smarthome.automation.DeviceValue;

/**
 * Périphérique Télé-info EDF
 * 
 * @author gregory
 *
 */
class TeleInformation extends AbstractDeviceType {
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
		device.addMetavalue("hcinst", [value: "0", label: "Période heures creuses (Wh)", trace: true])
		
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
		device.addMetavalue("hpinst", [value: "0", label: "Période heures pleines (Wh)", trace: true])
		
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
