package smarthome.core

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class NumberUtils {
	/**
	 * Arrondi d'une valeur
	 * 
	 * @param value
	 * @param precision
	 * @return
	 */
	static Double round(Number value, int precision = 1) {
		if (value == null) {
			return null
		}

		return (value as Double).round(precision)
	}


	/**
	 * Fonction simple de moyenne sur plusieurs valeurs
	 * Il peut y avoir des valeurs nulles à ne pas prendre en compte
	 *
	 * @param valeurs
	 * @return
	 */
	static Double moyenne(Double... valeurs) {
		Double result

		if (valeurs) {
			List<Double> filterValeurs = valeurs.findAll { it != null }

			if (filterValeurs) {
				result = NumberUtils.round(filterValeurs.sum { it } / filterValeurs.size())
			}
		}

		return result
	}
}
