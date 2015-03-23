package smarthome.core

import smarthome.core.SmartHomeException;
import grails.transaction.Transactional


/**
 * Classe de base pour les services de règles
 * 
 * @author gregory
 *
 */
abstract class AbstractRuleService<I, O> {

	/**
	 * Exécution d'une règle
	 * 
	 * @param object
	 * @param ruleObligatoire
	 * @return
	 * @throws SmartHomeException
	 */
    abstract O execute(I object, boolean ruleObligatoire) throws SmartHomeException;
	
	
	/**
	 * Exécution d'une règle fournie via un script
	 * 
	 * @param script
	 * @param Object
	 * @return
	 * @throws SmartHomeException
	 */
	abstract O executeFromScript(String script, I Object) throws SmartHomeException;
}
