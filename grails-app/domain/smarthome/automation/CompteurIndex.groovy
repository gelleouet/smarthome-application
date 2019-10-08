package smarthome.automation

import java.io.Serializable

import smarthome.core.DateUtils
import smarthome.core.SmartHomeCoreConstantes
import smarthome.core.SmartHomeException
import grails.validation.Validateable

/**
 * Saisie manuelle d'un index de compteur avec photo
 * 
 * @author gregory
 *
 */
@Validateable
class CompteurIndex implements Serializable {
	Device device
	Double index1
	Double index2
	String param1
	Date dateIndex
	byte[] photo


	static belongsTo = [device: Device]

	static constraints = {
		index2 nullable: true
		param1 nullable: true
		photo nullable: true
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
	}


	static {
		// surcharge du serialiser pour ne pas passer le buffer pour la photo
		grails.converters.JSON.registerObjectMarshaller(CompteurIndex) {
			[id: it.id, device: [id: it.device.id], index1: it.index1, index2: it.index2,
				param1: it.param1, dateIndex: it.dateIndex?.format(DateUtils.JSON_FORMAT)]
		}
	}
}
