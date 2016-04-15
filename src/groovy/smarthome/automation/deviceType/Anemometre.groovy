package smarthome.automation.deviceType

/**
 * Périphérique Anémomètre
 * Aucune action particulière car capteur
 * 
 * @author gregory
 *
 */
class Anemometre extends AbstractDeviceType {

	private static final double NOEUD = 0.53995680346039
	
	private static final Map BEAUFORT = [
		1: [force: 0, desc: 'calme'],
		5: [force: 1, desc: 'Très légère brise'],
		12: [force: 2, desc: 'légère brise'],
		19: [force: 3, desc: 'Petite Brise'],
		28: [force: 4, desc: 'Jolie brise'],
		38: [force: 5, desc: 'Bonne brise'],
		50: [force: 6, desc: 'Vent frais'],
		61: [force: 7, desc: 'Grand frais'],
		74: [force: 8, desc: 'Coup de vent'],
		88: [force: 9, desc: 'Fort coup de vent'],
		102: [force: 10, desc: 'Tempête'],
		117: [force: 11, desc: 'Violente tempête'],
		15000: [force: 12, desc: 'Ouragan']
	]
	
	
	/**
	 * Convertit la valeur initiale en kmm/h en noeud
	 * 
	 * @return
	 */
	def convertToNoeud() {
		return this.convertToNoeud(device.value?.toDouble())
	}
	
	
	/**
	 * Convertit la valeur initiale en kmm/h en noeud
	 * 
	 * @return
	 */
	def convertToNoeud(Double value) {
		if (value != null) {
			return (value * NOEUD).toLong()
		} else {
			return null
		}
	}
	
	
	/**
	 * Convertit la valeur initiale en km/h en échelle de beaufort
	 * 
	 * @return
	 */
	def convertToBeaufort() {
		return this.convertToBeaufort(device.value?.toDouble())
	}
	
	
	/**
	 * Convertit la valeur initiale en km/h en échelle de beaufort
	 * 
	 * @return
	 */
	def convertToBeaufort(Double value) {
		if (value != null) {
			for (def beaufort : BEAUFORT) {
				if (value <= beaufort.key) {
					return beaufort.value
				}
			}
		}
		
		return null
	}
}
