package smarthome.core

import groovy.time.TimeCategory;
import groovy.time.TimeDuration;

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
	
	
	/**
	 * Renvoit dans la map la date debut (start) et la date fin (end)
	 * 
	 * @param sinceHour
	 * @param offsetHour
	 * @return
	 */
	static Map durationToDates(Long sinceHour, Long offsetHour) {
		Date end = new Date()
		Date start
		Map map = [:]
		
		TimeDuration endDuration = new TimeDuration(sinceHour.toInteger() * offsetHour.toInteger(), 0, 0, 0)
		TimeDuration duration = new TimeDuration(sinceHour.toInteger(), 0, 0, 0)
		
		use(TimeCategory) {
			end = end - endDuration
			start = end - duration
		}
		
		map.end = end
		map.start = start
		
		return map
	}
	
	
	/**
	 * Vrai si la date courante est en "temps mort" par rapport à la date indiquée et le référentiel (heure, minute, etc.)
	 * "Temps mort" = si la date courante est comprise dans l'intervalle [date, date + (dateField + 1)]
	 * 
	 * @param date
	 * @param dateField
	 * @return
	 */
	static boolean isBlindTime(Date date, int dateField) {
		if (date == null) {
			return false
		}
		
		Date currentDate = new Date()
		Date endDate = date.copyWith([:])
		endDate[dateField] = endDate[dateField] + 1
		
		return currentDate >= date && currentDate <= endDate
	} 
}
