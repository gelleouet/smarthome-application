/**
 * 
 */
package smarthome.security

import smarthome.automation.House;
import smarthome.security.SmartHomeSecurityUtils;
import grails.validation.Validateable


/**
 * Enregistement profil
 * 
 * @author gregory
 *
 */
@Validateable
class ProfilCommand {
	User user
	House house
}
