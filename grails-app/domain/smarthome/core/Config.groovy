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
	static final String GRAND_DEFI_ID = "GRAND_DEFI_ID"
	static final String GRAND_DEFI_ADMIN_IDS = "GRAND_DEFI_ADMIN_IDS"
	static final String GRAND_DEFI_MAX_CLASSEMENT = "GRAND_DEFI_MAX_CLASSEMENT"


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
