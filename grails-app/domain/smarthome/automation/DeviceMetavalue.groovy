package smarthome.automation

import java.io.Serializable;

import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Les valeurs spécifiques à un type device
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceMetavalue implements Serializable {
	static belongsTo = [device: Device]
	
	String name
	String label
	String value
	String type
	String unite
	
	/*
	 * sa valeur sera la valeur principale du device
	 */
	boolean main = false
	/*
	 *  la valeur doit être historisée à chaque changement 
	 */
	boolean trace = false
	/*
	 * la valeur devient un device virtuel (enregistrée dans un autre device sous son nom)
	 */
	boolean virtualDevice = false
		
	
	
    static constraints = {
		value nullable: true;
		label nullable: true;
		type nullable: true;
		unite nullable: true;
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		device index: "DeviceMetavalue_Device_Idx"
		unite length: 32
		version false
	}
	
	
	/**
	 * Conversion en double avec possibilité d'arrondi
	 * 
	 * @param precision
	 * @return
	 */
	Double convertValueToDouble(Integer precision = null) {
		if (value) {
			if (value.isDouble()) {
				Double doubleValue = value.toDouble()
				
				if (precision) {
					doubleValue = doubleValue.round(precision)
				}
				
				return doubleValue
			}
		}
		
		return null
	}
}
