package smarthome.automation

import java.io.Serializable
import smarthome.core.SmartHomeCoreConstantes
import grails.validation.Validateable

/**
 * Producteurs d'énergie
 *  
 * @author gregory
 *
 */
@Validateable
class ProducteurEnergie implements Serializable {
	String libelle
	Double surface
	Double investissement
	Integer nbaction


	static constraints = {
	}


	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
	}
}
