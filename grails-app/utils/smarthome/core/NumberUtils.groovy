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
}
