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
	Double moyenne_electricite
	Integer classement_electricite
	Integer total_electricite

	Double reference_gaz
	Double action_gaz
	Double economie_gaz
	Double moyenne_gaz
	Integer classement_gaz
	Integer total_gaz
	
	Double reference_eau
	Double action_eau
	Double economie_eau
	Double moyenne_eau
	Integer classement_eau
	Integer total_eau

	Double economie_global
	Double moyenne_global
	Integer classement_global
	Integer total_global // le nombre particpants dans la catégorie


	static constraints = {
		reference_electricite nullable: true
		action_electricite nullable: true
		economie_electricite nullable: true
		moyenne_electricite nullable: true
		classement_electricite nullable: true
		total_electricite nullable: true
		reference_gaz nullable: true
		action_gaz nullable: true
		economie_gaz nullable: true
		moyenne_gaz nullable: true
		classement_gaz nullable: true
		total_gaz nullable: true
		reference_eau nullable: true
		action_eau nullable: true
		economie_eau nullable: true
		moyenne_eau nullable: true
		classement_eau nullable: true
		total_eau nullable: true
		economie_global nullable: true
		moyenne_global nullable: true
		classement_global nullable: true
		total_global nullable: true
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
	Double moyenne_global() {
		return moyenne_global
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
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Integer total_global() {
		return total_global
	}


	/**
	 * Résultat global des consos sur période de réference
	 *
	 * @return
	 */
	Double reference_global() {
		if (reference_electricite != null || reference_gaz != null || reference_eau != null) {
			return NumberUtils.round((reference_electricite ?: 0) + (reference_gaz ?: 0) + (reference_eau ?: 0))
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
		if (action_electricite != null || action_gaz != null || action_eau != null) {
			return NumberUtils.round((action_electricite ?: 0) + (action_gaz ?: 0) + (action_eau ?: 0))
		} else {
			return null
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
	 * Résultat global des consos sur période de réference
	 *
	 * @return
	 */
	Double reference_energie() {
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
	Double action_energie() {
		if (action_electricite != null || action_gaz != null) {
			return NumberUtils.round((action_electricite ?: 0) + (action_gaz ?: 0))
		} else {
			return null
		}
	}


	/**
	 * Différence sur résultat global
	 *
	 * @return
	 */
	Double difference_energie() {
		def action = action_energie()
		def reference = reference_energie()

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
	Double evolution_energie() {
		def difference = difference_energie()
		def reference = reference_energie()

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
	Double economie_energie() {
		NumberUtils.moyenne(economie_electricite(), economie_gaz())
	}
	

	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Double moyenne_energie() {
		null
	}
	
	
	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Integer classement_energie() {
		null
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Integer total_energie() {
		null
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
	Double moyenne_electricite() {
		return moyenne_electricite
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
	Integer total_electricite() {
		return total_electricite
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
	Double moyenne_gaz() {
		return moyenne_gaz
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
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Integer total_gaz() {
		return total_gaz
	}
	
	
	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Double reference_eau() {
		return reference_eau
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Double action_eau() {
		return action_eau
	}


	/**
	 * Différence sur résultat gaz
	 * Le nom avec _ n'est pas anodin. le suffixe doit correspondre à @see DefiCompteurEnum
	 * Les propriétés peuvent être accédées dynamiquement en fonction type compteur
	 *
	 * @return
	 */
	Double difference_eau() {
		if (reference_eau != null && action_eau != null) {
			return NumberUtils.round(action_eau - reference_eau)
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
	Double evolution_eau() {
		def difference = difference_eau()

		// check difference et reference pas 0 pour la division
		if (difference != null && reference_eau) {
			return NumberUtils.round(difference * 100 / reference_eau, 1)
		} else {
			return null
		}
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Double economie_eau() {
		return economie_eau
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Double moyenne_eau() {
		return moyenne_eau
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Integer classement_eau() {
		return classement_eau
	}


	/**
	 * Coompatibilité avec l'accès dynamique aux properties par méthode
	 *
	 * @return
	 */
	Integer total_eau() {
		return total_eau
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
		result.type = defiCompteur
		result.resultat = this

		if (canDisplay()) {
			result.reference = this."reference_${ defiCompteur.toString() }"()
			result.action = this."action_${ defiCompteur.toString() }"()
			result.difference = this."difference_${ defiCompteur.toString() }"()
			result.evolution = this."evolution_${ defiCompteur.toString() }"()
			result.economie = this."economie_${ defiCompteur.toString() }"()
			result.moyenne = this."moyenne_${ defiCompteur.toString() }"()
			result.classement = this."classement_${ defiCompteur.toString() }"()
			result.total = this."total_${ defiCompteur.toString() }"()
		}

		return this
	}


	/**
	 * Efface les résultats
	 * 
	 * @return
	 */
	AbstractDefiResultat cleanResultat() {
		reference_electricite = null
		action_electricite = null
		economie_electricite = null
		moyenne_electricite = null
		classement_electricite = null
		total_electricite = null

		reference_gaz = null
		action_gaz = null
		economie_gaz = null
		moyenne_gaz = null
		classement_gaz = null
		total_gaz = null
		
		reference_eau = null
		action_eau = null
		economie_eau = null
		moyenne_eau = null
		classement_eau = null
		total_eau = null

		economie_global = null
		moyenne_global = null
		classement_global = null
		total_global = null

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
