package smarthome.application

import smarthome.core.NumberUtils


/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
abstract class AbstractDefiResultat implements Serializable {
	// le suffixe doit correspondre à @see DefiCompteurEnum
	Double reference_electricite
	Double action_electricite
	Double economie_electricite
	Integer classement_electricite

	Double reference_gaz
	Double action_gaz
	Double economie_gaz
	Integer classement_gaz

	Double economie_global
	Integer classement_global


	static constraints = {
		reference_electricite nullable: true
		action_electricite nullable: true
		economie_electricite nullable: true
		classement_electricite nullable: true
		reference_gaz nullable: true
		action_gaz nullable: true
		economie_gaz nullable: true
		classement_gaz nullable: true
		economie_global nullable: true
		classement_global nullable: true
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Double economie_global() {
		return economie_global
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Integer classement_global() {
		return classement_global
	}


	/**
	 * Résultat global des consos sur période de réference
	 *
	 * @return
	 */
	Double reference_global() {
		if (reference_electricite != null || reference_gaz != null) {
			return NumberUtils.round((reference_electricite ?: 0) + (reference_gaz ?: 0))
		} else {
			return null
		}
	}


	/**
	 * Résultat global des consos sur période d'action
	 *
	 * @return
	 */
	Double action_global() {
		if (action_electricite != null || action_gaz != null) {
			return NumberUtils.round((action_electricite ?: 0) + (action_gaz ?: 0))
		} else {

		}
	}


	/**
	 * Différence sur résultat global
	 * 
	 * @return
	 */
	Double difference_global() {
		def action = action_global()
		def reference = reference_global()

		if (action != null && reference != null) {
			return NumberUtils.round(action - reference)
		} else {
			return null
		}
	}


	/**
	 * Evolution sur résultat global par rapport à la référence
	 *
	 * @return
	 */
	Double evolution_global() {
		def difference = difference_global()
		def reference = reference_global()

		// check difference et reference pas 0 pour la division
		if (difference != null && reference) {
			return NumberUtils.round(difference * 100 / reference, 1)
		} else {
			return null
		}
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Double economie_electricite() {
		return economie_electricite
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Integer classement_electricite() {
		return classement_electricite
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 * 
	 * @return
	 */
	Double reference_electricite() {
		return reference_electricite
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Double action_electricite() {
		return action_electricite
	}


	/**
	 * Différence sur résultat elec
	 * Le nom avec _ n'est pas anodin. le suffixe doit correspondre à @see DefiCompteurEnum
	 * Les propriétés peuvent être accédées dynamiquement en fonction type compteur
	 *
	 * @return
	 */
	Double difference_electricite() {
		if (reference_electricite != null && action_electricite != null) {
			return NumberUtils.round(action_electricite - reference_electricite)
		} else {
			return null
		}
	}


	/**
	 * Evolution sur résultat elec par rapport à la référence
	 * Le nom avec _ n'est pas anodin. le suffixe doit correspondre à @see DefiCompteurEnum
	 * Les propriétés peuvent être accédées dynamiquement en fonction type compteur
	 *
	 * @return
	 */
	Double evolution_electricite() {
		def difference = difference_electricite()

		// check difference et reference pas 0 pour la division
		if (difference != null && reference_electricite) {
			return NumberUtils.round(difference * 100 / reference_electricite, 1)
		} else {
			return null
		}
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Double reference_gaz() {
		return reference_gaz
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Double action_gaz() {
		return action_gaz
	}


	/**
	 * Différence sur résultat gaz
	 * Le nom avec _ n'est pas anodin. le suffixe doit correspondre à @see DefiCompteurEnum
	 * Les propriétés peuvent être accédées dynamiquement en fonction type compteur
	 *
	 * @return
	 */
	Double difference_gaz() {
		if (reference_gaz != null && action_gaz != null) {
			return NumberUtils.round(action_gaz - reference_gaz)
		} else {
			return null
		}
	}


	/**
	 * Evolution sur résultat gaz par rapport à la référence
	 * Le nom avec _ n'est pas anodin. le suffixe doit correspondre à @see DefiCompteurEnum
	 * Les propriétés peuvent être accédées dynamiquement en fonction type compteur
	 *
	 * @return
	 */
	Double evolution_gaz() {
		def difference = difference_gaz()

		// check difference et reference pas 0 pour la division
		if (difference != null && reference_gaz) {
			return NumberUtils.round(difference * 100 / reference_gaz, 1)
		} else {
			return null
		}
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Double economie_gaz() {
		return economie_gaz
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Integer classement_gaz() {
		return classement_gaz
	}


	/**
	 * Injecte les résultats relatifs au compteur dans la map result sous les fields
	 * sans suffixe (ie reference_electricite => reference)
	 * 
	 * @param result
	 * @param defiCompteur
	 * @return
	 */
	AbstractDefiResultat injectResultat(Map result, DefiCompteurEnum defiCompteur) {
		if (canDisplay()) {
			result.reference = this."reference_${ defiCompteur.toString() }"()
			result.action = this."action_${ defiCompteur.toString() }"()
			result.difference = this."difference_${ defiCompteur.toString() }"()
			result.evolution = this."evolution_${ defiCompteur.toString() }"()
			result.economie = this."economie_${ defiCompteur.toString() }"()
			result.classement = this."classement_${ defiCompteur.toString() }"()
			result.type = defiCompteur
			result.resultat = this
		}
		return this
	}


	/**
	 * Les résultats peuvent-ils être affichés
	 * 
	 * @return
	 */
	boolean canDisplay() {
		economie_global != null
	}
}
