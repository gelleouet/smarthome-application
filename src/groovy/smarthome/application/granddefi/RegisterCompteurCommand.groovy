/**
 * 
 */
package smarthome.application.granddefi

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * @author gregory.elleouet@gmail.com<Grégory Elléoouet>
 *
 */
@Validateable
class RegisterCompteurCommand {
	String compteurType
	String compteurModel
	User user
}
