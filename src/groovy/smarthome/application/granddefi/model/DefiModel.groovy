package smarthome.application.granddefi.model


import smarthome.application.Defi
import smarthome.application.DefiCommand
import smarthome.core.SmartHomeException


/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
interface DefiModel {
	
	/**
	 * Complète le modèle pour la vue participants
	 * 
	 * @param command
	 * @param model
	 * @return
	 * @throws SmartHomeException
	 */
	Map modelParticipants(DefiCommand command, Map model) throws SmartHomeException
	
	
	/**
	 * Modèle vue résultats défi
	 * 
	 * @param command
	 * @param model
	 * @return
	 * @throws SmartHomeException
	 */
	Map modelResultatsDefi(DefiCommand command, Map model) throws SmartHomeException
	
	
	/**
	 * Modèle vue résultats équipe
	 * 
	 * @param command
	 * @param model
	 * @return
	 * @throws SmartHomeException
	 */
	Map modelResultatsEquipe(DefiCommand command, Map model) throws SmartHomeException
	
	
	/**
	 * Modèle vue mes résultats
	 * 
	 * @param command
	 * @param model
	 * @return
	 * @throws SmartHomeException
	 */
	Map modelMesResultats(DefiCommand command, Map model) throws SmartHomeException
	
	
	/**
	 * Le chemin vers les vues du model
	 * 
	 * @return
	 */
	String viewPath()
	
	
	/**
	 * Le nom de la règle métier à utiliser
	 *
	 * @return
	 */
	String ruleName()
	
	
	/**
	 * Calcul des consos des participants d'un défi
	 * 
	 * @param defi
	 * @return
	 * @throws SmartHomeException
	 */
	Defi calculerConsommations(Defi defi) throws SmartHomeException
}
