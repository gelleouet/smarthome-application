package smarthome.automation.deviceType


/**
 * Une prise avec fonctionnement d'un bouton on/off
 * Le bouton conserve son etat
 * 
 * Actions:
 * - on : envoit la valeur 1 à l'agent.
 * - off : envoit la valeur 0 à l'agent
 * - onOff : en fonction de son etat, envoit la valeur inverse
 * 
 * @author gregory
 *
 */
class Prise extends BoutonOnOff {
	
}
