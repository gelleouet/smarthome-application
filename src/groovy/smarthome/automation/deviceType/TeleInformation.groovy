package smarthome.automation.deviceType

import java.util.Map;

import smarthome.automation.DeviceValue;

/**
 * Périphérique Télé-info EDF
 * 
 * <g:set var="opttarif" value="${  device?.metavalue('opttarif') }"/>
<g:set var="ptec" value="${  device?.metavalue('ptec') }"/>
<g:set var="isousc" value="${  device?.metavalue('isousc') }"/>
<g:set var="imax" value="${  device?.metavalue('imax') }"/>
<g:set var="hchp" value="${  device?.metavalue('hchp') }"/>
<g:set var="hchc" value="${  device?.metavalue('hchc') }"/>
<g:set var="papp" value="${  device?.metavalue('papp') }"/>
<g:set var="hcinst" value="${  device?.metavalue('hcinst') }"/>
<g:set var="hpinst" value="${  device?.metavalue('hpinst') }"/>
 * 
 * @author gregory
 *
 */
class TeleInformation extends AbstractDeviceType {
	/**
	 * @see smarthome.automation.deviceType.AbstractDeviceType.metaValuesName()
	 */
	@Override
	Map metaValuesName() {
		[
			'opttarif': 'Option tarifaire',
			'ptec': 'Période tarifaire',
			'isousc': 'Instansité souscrite (A)',
			'imax': 'Instansité maximale (A)',
			'hchp': 'Total heures pleines (Wh)',
			'hchc': 'Total heures creuses (Wh)',
			'papp': 'Puissance apparente (VA)',
			'hcinst': 'Période heures creuses (Wh)',
			'hpinst': 'Période heures pleines (Wh)',
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
		device.addMetavalue("hcinst", "0")
		
		if (hc) {
			// récupère la dernière valeur hchc
			def lastHC = DeviceValue.findAllByDeviceAndName(device, "hchc", [sort: "dateValue", order: "desc", max: 1])
			
			if (lastHC) {
				def conso = hc.value.toLong() - lastHC[0].value.toLong()
				device.addMetavalue("hcinst", conso.toString())
			}
		}

		// calcul conso heure pleine sur la période
		def hp = device.metavalue("hchp")
		device.addMetavalue("hpinst", "0")
		
		if (hp) {
			// récupère la dernièer valeur hchc
			def lastHP = DeviceValue.findAllByDeviceAndName(device, "hchp", [sort: "dateValue", order: "desc", max: 1])
					
			if (lastHP) {
				def conso = hp.value.toLong() - lastHP[0].value.toLong()
				device.addMetavalue("hpinst", conso.toString())
			}
		}
	}
}
