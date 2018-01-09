package smarthome.core

import grails.converters.JSON;
import org.apache.commons.lang.StringUtils;

/**
 * Binding d'une propriété texte persistée avec une propriété json transiente
 * 
 * Gère les appels de 2 méthodes :
 *  - loadJson[text property] : charge les données json à partir de la propriété texte
 *  - saveJson[text property] : sauvegarde les données json dans la propriété texte
 *  
 * Les propriétés associés doivent suivre la convention de nommage :
 *  - [name] pour la propriété texte
 *  - json[Name] pour la propriété json
 *  
 * @author Gregory
 * 
 */
abstract class JsonDataDomain {
	Object methodMissing(String name, def args) {
		if (name.startsWith("loadJson") || name.startsWith("saveJson")) {
			// le reste du nom de la méthode contient la propriété texte
			String dataName = StringUtils.uncapitalise(name.substring(8))
			String jsonDataName = StringUtils.uncapitalise(name.substring(4))
			
			if (metaClass.hasProperty(this, dataName) && metaClass.hasProperty(this, jsonDataName)) {
				if (name.startsWith("loadJson")) {
					if (this."$dataName") {
						this."$jsonDataName" = JSON.parse(this."$dataName")
					} else {
						this."$jsonDataName" = [:]
					}
				} else {
					this."$dataName" = this."$jsonDataName" as JSON
				}
				
				return this		
			}
		} else if (name.startsWith("cleanJson")) {
			String jsonDataName = StringUtils.uncapitalise(name.substring(5))
			
			if (metaClass.hasProperty(this, jsonDataName)) {
				this."$jsonDataName" = this."$jsonDataName".findAll { key, value ->
					!key.contains(".")
				}
				return this
			}
		}
		
		throw new MissingMethodException(name, getClass(), args)
   }
}
