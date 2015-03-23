package smarthome.core

import grails.validation.Validateable;

/**
 * Enregistrement état d'un composant vue pour un utilisateur donné
 *  
 * @author gregory
 *
 */
@Validateable
class ComposantVue {
	String name
	String page
	long userId
	// data au format JSON
	String data
	
    static constraints = {
		name unique: ['page','userId']
		data nullable: true
    }
	
	static mapping = {
		name index: "ComposantVue_NamePageUser_Idx"
		page index: "ComposantVue_NamePageUser_Idx"
		userId index: "ComposantVue_NamePageUser_Idx"
		cache true
	}
}
