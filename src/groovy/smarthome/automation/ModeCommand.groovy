/**
 * 
 */
package smarthome.automation

import smarthome.automation.House;
import smarthome.automation.Mode;
import smarthome.security.SmartHomeSecurityUtils;
import grails.validation.Validateable


/**
 * Enregistement profil
 * 
 * @author gregory
 *
 */
@Validateable
class ModeCommand {
	List<Mode> modes = []
}
