package smarthome.automation.deviceType

import org.apache.commons.lang.StringUtils;

import java.util.Map;

import smarthome.automation.ChartTypeEnum;
import smarthome.automation.DataModifierEnum;
import smarthome.automation.Device;
import smarthome.automation.DeviceValue;
import smarthome.automation.WorkflowEvent;

abstract class AbstractDeviceType {
	Device device
	String name = simpleName()
	
	/**
	 * Son nom simple pour le système de convention
	 * (recherche des vues, des ressources icones, etc...)
	 * 
	 * @return
	 */
	final def simpleName() {
		StringUtils.uncapitalize(this.getClass().simpleName)
	}
	
	/**
	 * Le nom de la vue pour affichage en grille
	 * 
	 * @return
	 */
	final def viewGrid() {
		"/deviceType/${name}/${name}"
	}
	
	
	
	/**
	 * Nom de la vue pour saisie des options
	 * 
	 * @return
	 */
	final def viewForm() {
		"/deviceType/${name}/${name}Form"
	}

	
	/**
	 * Nom de l'image à associer au device
	 * 
	 * @return
	 */
	def icon() {
		"/deviceType/${name}.png"
	}
	
	
	/**
	 * Le template par défaut pour préparer les données du chart
	 * 
	 * @return
	 */
	def chartDataTemplate() {
		if (isQualitatif()) {
			'/chart/datas/chartQualitatifDatas'
		} else {
			'/chart/datas/chartQuantitatifDatas'
		}
	}
	
	
	def isQualitatif() {
		return true
	} 
	
	
	/**
	 * Retourne le type de graphique par défaut
	 *
	 * @return
	 */
	ChartTypeEnum defaultChartType() {
		if (isQualitatif()) {
			ChartTypeEnum.Line
		} else {
			ChartTypeEnum.Bubble
		}
	}
	
	
	/**
	 * La liste des actions disponibles pour ce device
	 * toutes les méthodes annotées WorkflowEvent
	 * 
	 * @return
	 */
	final def events() {
		def events = []
		
		getClass().getMethods()?.each { method ->
			if (method.getAnnotation(WorkflowEvent)) {
				events << method.name
			}
		}
		
		return events.sort()
	}
	
	
	/**
	 * Prépare les méta données avant enregistrement de celles-ci
	 * Permet à un type device de transformer ou d'ajouter de nouvelles 
	 * meta données calculées par exemple. Les nouvelles metadonnées doivent quand
	 * même être définies dans "metaValuesName" pour être utilisées 
	 * 
	 * @return
	 */
	def prepareMetaValuesForSave() {
		
	}
}
