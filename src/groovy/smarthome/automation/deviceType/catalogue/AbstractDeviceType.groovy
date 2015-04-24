package smarthome.automation.deviceType.catalogue

import org.apache.commons.lang.StringUtils;
import java.util.Map;
import smarthome.automation.ChartTypeEnum;
import smarthome.automation.Device;

abstract class AbstractDeviceType {
	Device device
	String folder =  StringUtils.uncapitalize(this.getClass().simpleName)
	
	
	/**
	 * Le nom de la vue pour affichage en grille
	 * 
	 * @return
	 */
	def viewGrid() {
		"/deviceType/$folder/$folder"
	}
	
	
	
	/**
	 * Nom de la vue pour saisie des options
	 * 
	 * @return
	 */
	def viewForm() {
		"/deviceType/${folder}/${folder}Form"
	}
	
	
	
	/**
	 * Nom de l'image à associer au device
	 * 
	 * @return
	 */
	def icon() {
		"/deviceType/catalogue/${folder}.png"
	}
	
	
	/**
	 * Retourne le type de graphique par défaut
	 * 
	 * @return
	 */
	ChartTypeEnum defaultChartType() {
		device.deviceType.capteur ? ChartTypeEnum.Line : ChartTypeEnum.Scatter
	}
	
	
	/**
	 * La liste des metavalues du device
	 * 
	 * @return Map. key = metavalue name, value = metavalue label
	 */
	Map metaValuesName() {
		[:]
	}
	
	
	/**
	 * Le template par défaut pour préparer les données du chart
	 * 
	 * @return
	 */
	def chartDataTemplate() {
		
	}
}
