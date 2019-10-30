package smarthome.core

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class CompteurUtils {
	/**
	 * Arrondi d'une valeur
	 * 
	 * @param value
	 * @param precision
	 * @return
	 */
	static Double convertWhTokWh(Number value, int precision = 1) {
		if (value == null) {
			return 0.0
		}

		return NumberUtils.round(value / 1000.0, precision)
	}


	/**
	 * Arrondi d'une valeur
	 * 
	 * @param value
	 * @param precision
	 * @return
	 */
	static Double convertkWhToWh(Number value, int precision = 1) {
		if (value == null) {
			return 0.0
		}

		return NumberUtils.round(value * 1000.0, precision)
	}
}
