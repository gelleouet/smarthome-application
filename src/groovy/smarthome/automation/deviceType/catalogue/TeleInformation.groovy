package smarthome.automation.deviceType.catalogue

import java.util.Map;

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
	Map metaValuesName() {
		[
			'isousc': 'Instansité souscrite (A)',
			'imax': 'Instansité maximale (A)',
			'hchp': 'Total heures pleines (Wh)',
			'hchc': 'Total heures creuses (Wh)',
			'papp': 'Puissance apparente (VA)',
			'hcinst': 'Heures creuses (Wh)',
			'hpinst': 'Heures pleines (Wh)'
		]
	}

	/**
	 * Le template par défaut pour préparer les données du chart
	 *
	 * @return
	 */
	def chartDataTemplate() {
		'/deviceType/teleInformation/teleInformationChartDatas'
	}
}
