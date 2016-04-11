package smarthome.rule

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
	 * @throws LimsException
	 */
    abstract O execute(I object, boolean ruleObligatoire) throws SmartHomeException;
	
	
	/**
	 * Exécution d'une règle avec des paramètres supplémentaires
	 *
	 * @param object
	 * @param ruleObligatoire
	 * @param parameters
	 * @return
	 * @throws LimsException
	 */
	abstract O execute(I object, boolean ruleObligatoire, Map parameters) throws SmartHomeException;
	
	
	/**
	 * Exécution d'une règle fournie via un script
	 * 
	 * @param script
	 * @param Object
	 * @return
	 * @throws LimsException
	 */
	abstract O executeFromScript(String script, I Object) throws SmartHomeException;
	
	
	/**
	 * Exécution d'une règle fournie via un script avec des paramètres supplémentaires
	 * 
	 * @param script
	 * @param Object
	 * @param parameters
	 * @return
	 * @throws LimsException
	 */
	abstract O executeFromScript(String script, I Object, Map parameters) throws SmartHomeException;
}
