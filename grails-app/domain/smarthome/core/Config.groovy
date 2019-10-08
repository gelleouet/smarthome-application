package smarthome.core

import grails.validation.Validateable

/**
 * Enregistrement état d'un composant vue pour un utilisateur donné
 *  
 * @author gregory
 *
 */
@Validateable
class Config {
	String name
	String value


	static constraints = {
		name unique: true
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		name index: "Config_Idx"
	}
}
