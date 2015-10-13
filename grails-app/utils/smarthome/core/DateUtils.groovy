package smarthome.core

class DateUtils {
	/**
	 * Formatte une date avec le format "Il y a ...."
	 * Ex: "il y a 1 minute, il y a 1 jour, etc.
	 * 
	 * @param date
	 * @return
	 */
	static String formatTimeAgo(Date date) {
		Date now = new Date()
		String ilya = "Il y a"
		
		if (date) {
			use(groovy.time.TimeCategory) {
				def duration = now - date
				
				if (duration.years > 0) {
					return "${ilya} ${duration.years} an${duration.years > 1 ? 's' : ''}" 
				} else if (duration.months > 0) {
					return "${ilya} ${duration.months} mois" 
				} else if (duration.days > 0) {
					// cas spécial pour les jours car les mois en année ne sont pas pris en charge
					if (duration.days > 365) {
						def annee = (int) duration.days / 365
						return "${ilya} ${annee} an${annee > 1 ? 's' : ''}"
					} else if (duration.days > 31) {
						// pour les mois, on arrondit. on ne tient pas compte du nombre de jours par mois
						def mois = (int) duration.days / 30 
						return "${ilya} ${mois} mois"
					} else {
						return "${ilya} ${duration.days} jour${duration.days > 1 ? 's' : ''}"
					}
				} else if (duration.hours > 0) {
					return "${ilya} ${duration.hours} heure${duration.hours > 1 ? 's' : ''}" 
				} else if (duration.minutes > 0) {
					return "${ilya} ${duration.minutes} minute${duration.minutes > 1 ? 's' : ''}" 
				} else {
					return "A l'instant"
				}
			}
		} else {
			return null
		}
	}
}
