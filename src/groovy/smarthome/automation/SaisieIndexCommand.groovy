package smarthome.automation

import grails.validation.Validateable

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Validateable
class SaisieIndexCommand {
	Device device
	Date dateValue
	Double value
	Map metavalues = [:]
	Map metadatas = [:]
}
