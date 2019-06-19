package smarthome.automation

import java.io.Serializable
import smarthome.core.SmartHomeCoreConstantes
import smarthome.security.User
import grails.validation.Validateable

/**
 * Actions Producteurs d'énergie
 *  
 * @author gregory
 *
 */
@Validateable
class ProducteurEnergieAction implements Serializable {
	User user
	ProducteurEnergie producteur
	/**
	 * Nombre d'actions sur le total producteur
	 */
	Integer nbaction
	/**
	 * Un objet type "panneau solaire" avec les données du producteur
	 * Permet de visualiser son % de production
	 */
	Device device

	// propriétés utilisateur
	Double productionTotal


	static transients = ['productionTotal']


	static belongsTo = [user: User, producteur: ProducteurEnergie]


	static constraints = {
		device nullable: true
	}


	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		user index: 'ProducteurEnergieAction_Idx'
	}


	Double percentAction() {
		return nbaction / producteur.nbaction
	}

	Double investissement() {
		return percentAction() * producteur.investissement
	}

	Double surface() {
		return percentAction() * producteur.surface
	}

	Double production() {
		return percentAction() * productionTotal()
	}

	Double productionTotal() {
		// met en cache pendant la durée de vie de l'objet l'extraction totale
		if (productionTotal == null) {
			productionTotal = 0.0

			if (device) {
				Date now = new Date()
				productionTotal = device.newDeviceImpl().productionTotalAnnee(now[Calendar.YEAR])
			}
		}
		return productionTotal
	}
}
