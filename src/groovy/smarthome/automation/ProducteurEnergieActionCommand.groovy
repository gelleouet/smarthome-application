/**
 * 
 */
package smarthome.automation

import grails.validation.Validateable


/**
 * 
 * 
 * @author gregory
 *
 */
@Validateable
class ProducteurEnergieActionCommand {
	String producteur
	String sort
	String order
}
