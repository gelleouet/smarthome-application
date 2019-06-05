/**
 * 
 */
package smarthome.api

import grails.validation.Validateable

/**
 * Résultat d'un appel fetch
 * 
 * @author gregory.elleouet@gmail.com <Grégory Elléoouet>
 *
 */
class FetchResult {
	long count
	long offset
	long size
	List<Map> datas = []
}
