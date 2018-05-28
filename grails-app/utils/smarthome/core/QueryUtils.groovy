package smarthome.core

import java.util.Map;

import org.hibernate.Query;

class QueryUtils {
	
	public static final String MATH_ALL_PATTERN = "%";
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	
	
	/**
	 * Format une date au format SQL
	 * 
	 * @param date
	 * @return
	 */
	static String formatDate(Date date) {
		date.format(FORMAT_DATE)
	}
	
	
	/**
	 * Parse une date au format SQL
	 * 
	 * @param date
	 * @return
	 */
	static Date parseDate(String date) {
		Date.parse(FORMAT_DATE, date)
	}
	
	
	/**
	 * Décore la valeur avec le pattern à gauche
	 * 
	 * @param value
	 * @param pattern
	 * @return
	 */
	static String decoratePatternLeft(String value, String pattern) {
		pattern + value
	}
	
	/**
	 * Décore la valeur avec le pattern à droite
	 * 
	 * @param value
	 * @param pattern
	 * @return
	 */
	static String decoratePatternRight(String value, String pattern) {
		value + pattern
	}
	
	/**
	 * Décore la valeur avec le pattern à gauche et à droite
	 * 
	 * @param value
	 * @param pattern
	 * @return
	 */
	static String decoratePattern(String value, String pattern) {
		decoratePatternRight(decoratePatternLeft(value, pattern), pattern)
	}
	
	
	/**
	 * Décore la valeur avec le pattern à gauche et à droite
	 * @param value
	 * @param pattern
	 * @return
	 */
	static String decorateMatchAll(String value) {
		decoratePatternRight(decoratePatternLeft(value, MATH_ALL_PATTERN), MATH_ALL_PATTERN)
	}
	
	
	/**
	 * Binding des paramètres de la query
	 *
	 * @param query
	 * @param parameters
	 * @return
	 */
	static Query bindParameters(Query query, Map parameters) {
		if (parameters) {
			parameters.each { key, value ->
				if (value instanceof Collection) {
					query.setParameterList(key, (Collection) value)
				} else {
					query.setParameter(key, value)
				}
			}
		}
		
		return query
	}
}
