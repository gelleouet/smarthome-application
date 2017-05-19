package smarthome.automation.deviceType

import org.apache.commons.lang.StringUtils;

import java.util.Map;

import smarthome.automation.ChartTypeEnum;
import smarthome.automation.DataModifierEnum;
import smarthome.automation.Device;
import smarthome.automation.DeviceValue;
import smarthome.automation.WorkflowEvent;

abstract class AbstractDeviceType {
	public static final int MAX_DAY_WITHOUT_PROJECTION = 1
	public static final int MAX_DAY_PROJECTION_DAY = 31
	
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
			'/chart/chartQualitatifDatas'
		} else {
			'/chart/chartQuantitatifDatas'
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
	
	
	/**
	 * Chargement des valeurs sans projections (sur une courte période)
	 * 
	 * @param dateDebut
	 * @param dateFin
	 * @param name
	 * @return
	 */
	def values(Date dateDebut, Date dateFin, String name, int days) {
		return DeviceValue.createCriteria().list {
			eq 'device', device
			between 'dateValue', dateDebut, dateFin
			
			if (name) {
				if (name == '') {
					isNull 'name'
				} else {
					eq 'name', name
				}
			}
			
			order 'dateValue', 'name'
		}
	}
	
	
	/**
	 * Chargement des valeurs avec projections automatiques (sur une longue période) ou imposées
	 *
	 * @param dateDebut
	 * @param dateFin
	 * @param name
	 * @return
	 */
	def projectionValues(Date dateDebut, Date dateFin, String name, int days, DataModifierEnum projection = null) {
		return DeviceValue.createCriteria().list({
			eq 'device', device
			between 'dateValue', dateDebut, dateFin
			
			if (name) {
				if (name == '') {
					isNull 'name'
				} else {
					eq 'name', name
				}
			}
			
			projections {
				// ne pas mélanger les différents types de valeurs
				groupProperty("name")
				
				// pour les devices qualitatifs, on veut une représentation de la valeur (min, max, avg)
				if (this.isQualitatif()) {
					max("value")
					min("value")
					avg("value")
					sum("value")
					
					if (days > MAX_DAY_PROJECTION_DAY) {
						groupProperty("year")
						groupProperty("monthOfYear")
						order "year"
						order "monthOfYear"
					} else {
						groupProperty("day")
						order "day"
					}
				} 
				// pour les devices quantitatifs, la valeur n'est pas importante mais c'est le nombre
				// qui importe et quand dans la journée
				else {
					count("value")
					
					if (days > MAX_DAY_PROJECTION_DAY) {
						groupProperty("year")
						groupProperty("monthOfYear")
						groupProperty("hourOfDay")
						order "year"
						order "monthOfYear"
						order "hourOfDay"
					} else {
						groupProperty("day")
						groupProperty("hourOfDay")
						order "day"
						order "hourOfDay"
					}
				}
				
				
			}
		})?.collect {
			// suivre l'ordre des projections
			def map = ['name': it[0]]
			
			if (this.isQualitatif()) {
				map.max = it[1]
				map.min = it[2]
				map.avg = it[3]
				map.sum = it[4]
				
				if (days > MAX_DAY_PROJECTION_DAY) {
					map.year = it[5]
					map.month = it[6]
				} else {
					map.day = it[5]
				}
			} else {
				map.count = it[1]
				
				if (days > MAX_DAY_PROJECTION_DAY) {
					map.year = it[2]
					map.month = it[3]
					map.hour = it[4]
				} else {
					map.day = it[2]
					map.hour = it[3]
				}
			}
			
			return map
		}
	}
}
