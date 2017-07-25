package smarthome.automation

import java.io.Serializable;
import java.util.Date;

import grails.validation.Validateable;
import groovy.time.TimeCategory;
import groovy.time.TimeDuration;

/**
 * Configuration des niveaux d'alerte sur un device
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceLevelAlert implements Serializable {
	static belongsTo = [device: Device]
	
	LevelAlertEnum level
	ModeAlertEnum mode
	Double value
	int tempo
	boolean notifMail
	boolean notifSms
	
	// propriétés utilisateur
	Integer status
	
	
	static transients = ['status']
	
	
    static constraints = {
		tempo min:1
		status bindable: true
    }
	
	static mapping = {
		device index: "DeviceLevelAlert_Idx"
	}
	
	
	/**
	 * Vérifie si une valeur est en alerte
	 *
	 * @param deviceValue
	 * @return
	 */
	boolean isAlerteValue(Double deviceValue) {
		if (this.value != null) {
			if (mode == ModeAlertEnum.min) {
				return deviceValue < this.value
			} else if (mode == ModeAlertEnum.max) {
				return deviceValue > this.value
			}
		}
		
		return false
	}
	
	
	/**
	 * Alerte monitoring
	 *
	 * Sa valeur aurait du être rafraichie par rapport à sa fréquence d'enregistrement
	 * Prise en compte de la tempo
	 *
	 * @return
	 */
	boolean isAlerteMonitoring(Date date) {
		if (!this.tempo) {
			return false
		}
		
		Date dateNewValue
		
		use (TimeCategory) {
			// +5 : on ajoute une marge d'erreur car les boitiers ne sont pas synchronisés avec l'heure des
			// serveurs et surtout ils ne sont pas planifiés à la minute ET 0sec alors que le job lui l'est
			dateNewValue = thisObject.device.dateValue + new TimeDuration(0, this.tempo +5, 0, 0)
		}
		
		return dateNewValue < date
	}
}
