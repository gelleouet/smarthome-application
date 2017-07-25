package smarthome.core

import grails.validation.Validateable;

/**
 * Gestion des workflows métier
 * 
 *  
 * @author gregory
 *
 */
@Validateable
class Workflow {
	String libelle
	String description
	byte[] data
	
	
    static constraints = {
		
    }
	
	static mapping = {
		sort 'libelle'
	}
	
	
	/**
	 * Retourne la clé BPMN
	 * 
	 * @return
	 */
	String bpmnKey() {
		return "${libelle}.bpmn20.xml"	
	}
}
