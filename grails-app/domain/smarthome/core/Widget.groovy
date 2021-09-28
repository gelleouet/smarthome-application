package smarthome.core

import grails.validation.Validateable

/**
 * Gestion des widgets sur tableau de bord
 * 
 *  
 * @author gregory
 *
 */
@Validateable
class Widget {

	String libelle
	String description
	Integer refreshPeriod // nombre de minutes pour rafraichissement auto
	String controllerName
	String actionName
	String configName
	String contentView
	String style


	static constraints = {
		refreshPeriod nullable: true
		configName nullable: true
		contentView nullable: true
		style nullable: true
	}


	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
	}
	
	
	/**
	 * Parse le style et le transforme en Map
	 * 
	 * @return
	 */
	Map parseStyle() {
		Map styles = [:]
		
		if (style) {
			style.split(";").each { token ->
				String[] subtokens = token.split(":")
				
				if (subtokens && subtokens.length == 2) {
					styles.put(subtokens[0].trim(), subtokens[1].trim())
				}
			}
		}
		
		return styles
	}
	
	
	/**
	 * Raccourci vers le style height
	 * 
	 * @return
	 */
	String height() {
		Map styles = parseStyle()
		return styles.height
	}
	
	
	/**
	 * Raccourci vers le style height converti en nombre (sans les unités)
	 * 
	 * @return
	 */
	Integer intHeight() {
		String height = height()
		
		if (height) {
			return height.replace("px", "").toInteger()
		} else {
			return null
		}
	}
	
	
	/**
	 * Raccourci vers le style contentHeight
	 *
	 * @return
	 */
	String contentHeight() {
		Map styles = parseStyle()
		return styles.contentHeight
	}
	
	
	/**
	 * Raccourci vers le style contentHeight converti en nombre (sans les unités)
	 *
	 * @return
	 */
	Integer intContentHeight() {
		String height = contentHeight()
		
		if (height) {
			return height.replace("px", "").toInteger()
		} else {
			return null
		}
	}
}
