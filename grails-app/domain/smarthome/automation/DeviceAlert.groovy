package smarthome.automation

import java.io.Serializable;

import smarthome.core.SmartHomeCoreConstantes;
import grails.validation.Validateable;

/**
 * Alertes au niveau des devices 
 *  
 * @author gregory
 *
 */

@Validateable
class DeviceAlert implements Serializable {
	static belongsTo = [device: Device]
	
	Date dateDebut = new Date()
	Date dateFin
	LevelAlertEnum level
	int relance
	LevelStatusEnum status = LevelStatusEnum.open
	
	
    static constraints = {
		dateFin nullable: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		device index: "DeviceAlert_Idx"
		dateDebut index: "DeviceAlert_Idx"
		level length: 16
		status length: 16
	}
	
	
	/**
	 * Indique si l'alerte est toujours en cours.
	 * Une alerte viewed est toujours considérée comme ouverte car l'erreur est toujours présente
	 * 
	 * @return
	 */
	boolean isOpen() {
		return status == LevelStatusEnum.open || status == LevelStatusEnum.viewed
	}
	
	
	/**
	 * Indique si une alerte est examinée par l'utilisateur
	 * 
	 * @return
	 */
	boolean isViewed() {
		return status == LevelStatusEnum.viewed
	}
	
	
	/**
	 * Cloture une alerte
	 * 
	 * @return
	 */
	DeviceAlert close() {
		dateFin = new Date()	
		status = LevelStatusEnum.closed
		return this
	}
	
	
	/**
	 * Reset une alerte avec un autre type
	 * 
	 * @return
	 */
	DeviceAlert reset(LevelAlertEnum alertLevel) {
		dateDebut = new Date()
		relance = 0	
		status = LevelStatusEnum.open
		level = alertLevel
		return this
	}
}
