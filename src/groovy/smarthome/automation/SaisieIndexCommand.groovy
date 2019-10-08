package smarthome.automation

import grails.validation.Validateable
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException


@Validateable
class SaisieIndexCommand {
	long deviceId
	Double index1
	Double index2
	String param1
	Date dateIndex = new Date().clearTime()
	byte[] photo
	Double lastIndex1
	Double lastIndex2


	static constraints = {
		index2 nullable: true
		param1 nullable: true
		photo nullable: true
		lastIndex1 nullable: true
		lastIndex2 nullable: true
	}


	static {
		// surcharge du serialiser pour ne pas passer le buffer pour la photo
		grails.converters.JSON.registerObjectMarshaller(SaisieIndexCommand) {
			[deviceId: it.deviceId, index1: it.index1, index2: it.index2,
				param1: it.param1, dateIndex: it.dateIndex?.format(DateUtils.JSON_FORMAT),
				lastIndex1: it.lastIndex1, lastIndex2: it.lastIndex2]
		}
	}


	/**
	 * Contrôles sur les index
	 * Ne pas faire les contrôles sur validité des valeurs car ils sont fait avec 
	 * l'annotation @Validateable
	 * 
	 */
	void asserts() throws SmartHomeException {
		if (lastIndex1 && lastIndex1 > index1) {
			throw new SmartHomeException("Le nouvel index doit être supérieur à l'ancien !", this)
		}

		if (lastIndex2 && index2 && lastIndex2 > index2) {
			throw new SmartHomeException("Le nouvel index doit être supérieur à l'ancien !", this)
		}
	}
}
