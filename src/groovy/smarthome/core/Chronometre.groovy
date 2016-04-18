package smarthome.core

/**
 * Permet de calculer un temps entre 2 appels
 * 
 * @author Gregory
 *
 */
class Chronometre {
	Date dateStart = new Date()
	long ellapse
	
	/**
	 * Arrête le compteur et renvoit le nombre de millisecondes écoulées
	 * Reset la date début pour pouvoir être appelé une seconde fois
	 * 
	 * @return
	 */
	long restart() {
		stop()
		dateStart = new Date()
		return ellapse
	}
	
	/**
	 * Arrête le compteur et renvoit le nombre de millisecondes écoulées
	 * 
	 * @return
	 */
	long stop() {
		Date dateStop = new Date()
		ellapse = dateStop.getTime() - dateStart.getTime()
		return ellapse
	}
}
