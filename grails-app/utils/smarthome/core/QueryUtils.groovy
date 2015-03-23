package smarthome.core

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
	 * PArse une date au format SQL
	 * 
	 * @param date
	 * @return
	 */
	static Date parseDate(String date) {
		Date.parse(FORMAT_DATE, date)
	}
	
	
	/**
	 * D�core la valeur avec le pattern � gauche
	 * @param value
	 * @param pattern
	 * @return
	 */
	static String decoratePatternLeft(String value, String pattern) {
		pattern + value
	}
	
	/**
	 * D�core la valeur avec le pattern � droite
	 * @param value
	 * @param pattern
	 * @return
	 */
	static String decoratePatternRight(String value, String pattern) {
		value + pattern
	}
	
	/**
	 * D�core la valeur avec le pattern � gauche et � droite
	 * @param value
	 * @param pattern
	 * @return
	 */
	static String decoratePattern(String value, String pattern) {
		decoratePatternRight(decoratePatternLeft(value, pattern), pattern)
	}
	
	
	/**
	 * D�core la valeur avec le pattern � gauche et � droite
	 * @param value
	 * @param pattern
	 * @return
	 */
	static String decorateMatchAll(String value) {
		decoratePatternRight(decoratePatternLeft(value, MATH_ALL_PATTERN), MATH_ALL_PATTERN)
	}
}
