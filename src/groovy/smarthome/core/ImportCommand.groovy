package smarthome.core

import grails.validation.Validateable


/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Validateable
class ImportCommand {
	byte[] data
	String importImpl
}
