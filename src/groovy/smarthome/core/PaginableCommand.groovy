package smarthome.core

import org.codehaus.groovy.grails.commons.GrailsApplication

/**
 * A hériter par les command qui gèrent eux-meme la pagination
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
abstract class PaginableCommand {
	// Command objects can participate in dependency injection.
	GrailsApplication grailsApplication
	
	
	long offset
	int max
	
	
	Map pagination() {
		[offset: offset, max: max ?: ApplicationUtils.configDefaultMax(grailsApplication)]
	}
}
