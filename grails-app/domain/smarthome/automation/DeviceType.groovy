package smarthome.automation

import java.io.Serializable;

import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Un type device
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceType implements Serializable {
	String libelle
	String implClass
	
	
    static constraints = {
		libelle unique: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		sort 'libelle'
	}
	
	
	/**
	 * Instance l'implémentation du device
	 * 
	 * @return
	 */
	def newDeviceType() {
		Class.forName(implClass).newInstance()
	}
}
