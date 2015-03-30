package smarthome.automation.deviceType.catalogue

import org.apache.commons.lang.StringUtils;

import smarthome.automation.Device;

abstract class AbstractDeviceType {
	Device device
	
	
	/**
	 * Le nom de la vue pour affichage en grille
	 * 
	 * @return
	 */
	def viewGrid() {
		'/deviceType/catalogue/' + StringUtils.uncapitalize(this.getClass().simpleName)
	}
	
	
	
	/**
	 * Nom de la vue pour saisie des options
	 * 
	 * @return
	 */
	def viewForm() {
		'/deviceType/catalogue/' + StringUtils.uncapitalize(this.getClass().simpleName) + 'Form'
	}
	
	
	
	/**
	 * Nom de l'image à associer au device
	 * 
	 * @return
	 */
	def icon() {
		'/deviceType/catalogue/' + StringUtils.uncapitalize(this.getClass().simpleName) + '.png'
	}
	
	
	/**
	 * Retourne le type de graphique par défaut
	 * 
	 * @return
	 */
	def defaultChartType() {
		device.deviceType.capteur ? 'line' : 'scatter'
	}
}
