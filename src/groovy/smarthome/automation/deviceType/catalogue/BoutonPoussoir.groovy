package smarthome.automation.deviceType.catalogue

/**
 * Un bouton poussoir avec impulsion
 * Le bouton n'a pas d'état on/off, il permet juste d'envoyer un etat on
 * juste le temps d'une impulsion (1 seconde)
 * 
 * Actions:
 * - push : envoit la valeur 1 à l'agent. L'agent est en charge de gérer l'impulsion
 * 
 * @author gregory
 *
 */
class BoutonPoussoir extends AbstractDeviceType {
	
	/**
	 * Nom de l'image à associer au device
	 *
	 * @return
	 */
	def icon() {
		'/deviceType/catalogue/boutonOnOff.png'
	}
	
	
	/**
	 * Envoit une valeur ON juste pendant 1 seconde avant de renvoyer une valeur OFF
	 * 
	 * @return
	 */
	def push() {
		device.value = "1"
	}
}
