package smarthome.core

import org.codehaus.groovy.grails.commons.GrailsApplication

import smarthome.core.query.HQL

/**
 * A hériter par les command qui gèrent eux-meme la pagination
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
abstract class PaginableCommand {
	
	private static final String PROPERTIES_SEPARATOR = ","
	
	// Command objects can participate in dependency injection.
	GrailsApplication grailsApplication
	
	
	long offset
	int max
	/**
	 * Liste de propriétés à trier
	 */
	List<SortCommand> sortProperties = []
	
	
	Map pagination() {
		[offset: offset, max: max ?: ApplicationUtils.configDefaultMax(grailsApplication)]
	}
	
	
	/**
	 * Applique le tri par défaut si la propriété est vide
	 * 
	 * @return
	 */
	Object bindDefaultSort() {
		
	}
	
	
	/**
	 * Applique les propriétés de tri sur une query
	 * 
	 * @param query
	 * @return query
	 */
	HQL addOrder(HQL query) {
		sortProperties?.each { sort ->
			query.addOrder(sort.property, sort.order)
		}
		return query
	}
	
	
	/**
	 * Retrouve property est activé dans la liste des properties
	 * 
	 * @param propertyName peut être 1 ou plusieurs propriétés séparées par ,
	 * @param properties
	 * @return
	 */
	static SortCommand findSortProperty(String propertyName, List<SortCommand> properties) {
		SortCommand search
		
		if (propertyName && properties) {
			String[] tokens = propertyName.split(PROPERTIES_SEPARATOR)
			
			for (String token : tokens) {
				search = properties.find { it.property == token }
				
				if (search) {
					break
				}
			}
		}
		
		return search
		
	}
}
