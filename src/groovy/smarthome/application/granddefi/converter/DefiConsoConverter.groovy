/**
 * 
 */
package smarthome.application.granddefi.converter

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
interface DefiConsoConverter {
	/**
	 * Conversion d'une conso pour affichage
	 * 
	 * @param conso
	 * @return
	 */
	Double convert(Double conso)
	
	
	/**
	 * L'unité des consos pour les affichages
	 * 
	 * @return
	 */
	String unite()
}
