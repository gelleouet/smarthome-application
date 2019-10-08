package smarthome.application

import grails.validation.Validateable
import smarthome.security.User

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Validateable
class DefiCommand {
	String search
	User user
	Defi defi


	static constraints = {
		search nullable: true
		defi nullable: true
	}
}
