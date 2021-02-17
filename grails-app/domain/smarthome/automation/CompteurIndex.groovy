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
	String commentaire

	// propriétés utilisateur
	Double lastIndex1
	Double lastIndex2
	Double highindex1
	Double lowindex1
	Double highindex2
	Double lowindex2

	
	static transients = ['lastIndex1', 'lastIndex2', 'highindex1', 'lowindex1', 'highindex2', 'lowindex2']
	
	static belongsTo = [device: Device]

	static constraints = {
		index2 nullable: true
		param1 nullable: true
		photo nullable: true
		commentaire nullable: true
		lastIndex1 nullable: true, bindable: true
		lastIndex2 nullable: true, bindable: true
		highindex1 nullable: true, bindable: true
		lowindex1 nullable: true, bindable: true
		highindex2 nullable: true, bindable: true
		lowindex2 nullable: true, bindable: true
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
	
	
	/**
	 * Contrôles sur les index
	 * Ne pas faire les contrôles sur validité des valeurs car ils sont fait avec
	 * l'annotation @Validateable
	 *
	 */
	void asserts() throws SmartHomeException {
		if (index1 == null) {
			throw new SmartHomeException("Au moins un index doit être renseigné !", this)
		}
		
		if (lastIndex1 && lastIndex1 > index1) {
			throw new SmartHomeException("Le nouvel index doit être supérieur à l'ancien !", this)
		}

		if (lastIndex2 && index2 && lastIndex2 > index2) {
			throw new SmartHomeException("Le nouvel index doit être supérieur à l'ancien !", this)
		}
		
		if (index1 < 0 || (index2 != null && index2 < 0)) {
			throw new SmartHomeException("Les index ne peuvent pas être négatifs !", this)
		}
	}
}
