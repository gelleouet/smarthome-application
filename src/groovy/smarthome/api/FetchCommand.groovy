/**
 * 
 */
package smarthome.api

import grails.validation.Validateable

/**
 * Réception d'un message fetch
 * 
 * @see https://github.com/gelleouet/smarthome-application/wiki/API
 * @author gregory.elleouet@gmail.com <Grégory Elléoouet>
 *
 */
@Validateable
class FetchCommand {
	String application
	String name
	String metaname
	Date start
	Date end
	long offset = 0
	Long limit
	String order = "asc"


	static constraints = {
		start nullable: true
		end nullable: true
		limit nullable: true
	}
}
