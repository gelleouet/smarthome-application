package smarthome.automation

import java.io.Serializable;

import smarthome.automation.deviceType.AbstractDeviceType
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
	Set config = []
	boolean qualitatif
	boolean planning
	
	
	static hasMany = [config: DeviceTypeConfig]
	
	
    static constraints = {
		libelle unique: true
    }
	
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		sort 'libelle'
		config cascade: 'all-delete-orphan'
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(DeviceType) {
			[id: it.id, implClass: it.implClass, libelle: it.libelle, qualitatif: it.qualitatif,
				planning: it.planning]
		}
	}
	
	
	/**
	 * Instance l'implémentation du device
	 * 
	 * @return
	 */
	AbstractDeviceType newDeviceType() {
		Class.forName(implClass).newInstance()
	}
	
	
	/**
	 * La config
	 * 
	 * @return
	 */
	DeviceTypeConfig config() {
		if (config?.size()) {
			return config[0]
		} else {
			return null
		}
	}
}
