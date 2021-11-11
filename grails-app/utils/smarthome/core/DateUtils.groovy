package smarthome.core

import java.text.SimpleDateFormat

import groovy.time.TimeCategory
import groovy.time.TimeDuration

class DateUtils {

	static final String FORMAT_DATETIME_ISO = "yyyy-MM-dd'T'HH:mm:ss'Z'"
	static final String FORMAT_DATE_ISO = "yyyy-MM-dd"
	static final String FORMAT_DATE = "yyyyMMdd"
	static final String JSON_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
	static final String FORMAT_DATETIME_USER = "dd/MM/yyyy HH:mm"
	static final String FORMAT_DATE_USER = "dd/MM/yyyy"


	/**
	 * Format d'une date user
	 *
	 * @param date
	 * @return
	 */
	static String formatDateUser(Date date) {
		return date?.format(FORMAT_DATE_USER)
	}
	
	
	/**
	 * Format d'une date
	 *
	 * @param date
	 * @return
	 */
	static String formatDate(Date date) {
		return date?.format(FORMAT_DATE)
	}


	/**
	 * Format d'une date ISO8601
	 *
	 * @param date
	 * @return
	 */
	static String formatDateIso(Date date) {
		return date?.format(FORMAT_DATE_ISO)
	}


	/**
	 * Format d'une date/time ISO8601
	 *
	 * @param date
	 * @return
	 */
	static String formatDateTimeIso(Date date) {
		return date?.format(FORMAT_DATETIME_ISO)
	}


	/**
	 * Parse date ISO
	 *
	 * @param date
	 * @return
	 */
	static Date parseDateIso(String iso) {
		return new SimpleDateFormat(FORMAT_DATE_ISO).parse(iso)
	}


	/**
	 * Parse une date au format JSON
	 * 
	 * @param date
	 * @param offset en minute
	 * @return
	 */
	static Date parseJson(def json, def timezoneOffset) {
		def date = json ? Date.parse(JSON_FORMAT, json) : new Date()

		if (timezoneOffset) {
			def offset = new TimeDuration(0, timezoneOffset.toInteger(), 0, 0)
			use(TimeCategory) {
				date = date - offset
			}
		}

		return date
	}


	/**
	 * Formatte une date avec le format "Il y a ...."
	 * Ex: "il y a 1 minute, il y a 1 jour, etc.
	 * 
	 * @param date
	 * @return
	 */
	static String formatTimeAgo(Date date) {
		Date now = new Date()

		if (date) {
			use(groovy.time.TimeCategory) {
				def duration = now - date

				if (duration.years > 0) {
					return "${duration.years} an${duration.years > 1 ? 's' : ''}"
				} else if (duration.months > 0) {
					return "${duration.months} mois"
				} else if (duration.days > 0) {
					// cas spécial pour les jours car les mois en année ne sont pas pris en charge
					if (duration.days > 365) {
						def annee = (int) duration.days / 365
						return "${annee} an${annee > 1 ? 's' : ''}"
					} else if (duration.days > 31) {
						// pour les mois, on arrondit. on ne tient pas compte du nombre de jours par mois
						def mois = (int) duration.days / 30
						return "${mois} mois"
					} else {
						return "${duration.days} jour${duration.days > 1 ? 's' : ''}"
					}
				} else if (duration.hours > 0) {
					return "${duration.hours} heure${duration.hours > 1 ? 's' : ''}"
				} else if (duration.minutes > 0) {
					return "${duration.minutes} minute${duration.minutes > 1 ? 's' : ''}"
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
		return isBlindTime(date, dateField, 1)
	}


	/**
	 * Vrai si la date courante est en "temps mort" par rapport à la date indiquée et le référentiel (heure, minute, etc.)
	 * "Temps mort" = si la date courante est comprise dans l'intervalle [date, date + (dateField + delta)]
	 *
	 * @param date
	 * @param dateField
	 * @return
	 */
	static boolean isBlindTime(Date date, int dateField, int delta) {
		if (date == null) {
			return false
		}

		Date currentDate = new Date()
		Date endDate = date.copyWith([:])
		endDate[dateField] = endDate[dateField] + delta

		return currentDate >= date && currentDate <= endDate
	}


	/**
	 * Dernier jour du mois
	 * 
	 * @param date
	 * @return
	 */
	static Date lastDayInMonth(Date date) {
		Calendar calendar = Calendar.getInstance()
		calendar.setTime(date)
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
		return calendar.getTime().clearTime()
	}


	/**
	 * Dernier jour de la semaine
	 * 
	 * @param date
	 * @return
	 */
	static Date lastDayInWeek(Date date) {
		Calendar calendar = Calendar.getInstance()
		calendar.setTime(date)

		if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			calendar.add(Calendar.DAY_OF_YEAR, 7 - (calendar.get(Calendar.DAY_OF_WEEK) - 1))
		}

		return calendar.getTime()
	}


	/**
	 * 1er jour de la semaine
	 * 
	 * @param date
	 * @return
	 */
	static Date firstDayInWeek(Date date) {
		Calendar calendar = Calendar.getInstance()
		calendar.setTime(date)

		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			calendar.add(Calendar.DAY_OF_YEAR, -6)
		} else {
			calendar.add(Calendar.DAY_OF_YEAR, -(calendar.get(Calendar.DAY_OF_WEEK) - 2))
		}

		return calendar.getTime()
	}


	/**
	 * 1er jour du mois
	 * 
	 * @param date
	 * @return
	 */
	static Date firstDayInMonth(Date date) {
		Calendar calendar = Calendar.getInstance()
		calendar.setTime(date)
		calendar.set(Calendar.DAY_OF_MONTH, 1)
		return calendar.getTime().clearTime()
	}


	/**
	 * 1er jour de l'année
	 * 
	 * @param date
	 * @return
	 */
	static Date firstDayInYear(int year) {
		Calendar calendar = Calendar.getInstance()
		calendar.set(Calendar.DAY_OF_YEAR, 1)
		calendar.set(Calendar.YEAR, year)
		return calendar.getTime().clearTime()
	}


	/**
	 * 1er jour de l'année
	 * 
	 * @param date
	 * @return
	 */
	static Date firstDayInYear(Date date) {
		Calendar calendar = Calendar.getInstance()
		calendar.setTime(date)
		calendar.set(Calendar.DAY_OF_YEAR, 1)
		return calendar.getTime().clearTime()
	}


	/**
	 * Dernier jour de l'année
	 * 
	 * @param date
	 * @return
	 */
	static Date lastDayInYear(Date date) {
		Calendar calendar = Calendar.getInstance()
		calendar.setTime(date)
		calendar.set(Calendar.DAY_OF_MONTH, 31)
		calendar.set(Calendar.MONTH, Calendar.DECEMBER)
		return calendar.getTime().clearTime()
	}


	/**
	 * Tronque une date à l'heure
	 *  
	 * @param date
	 * @return
	 */
	static Date truncHour(Date date) {
		int hour = date[Calendar.HOUR_OF_DAY]
		date.clearTime()
		date[Calendar.HOUR_OF_DAY] = hour
		return date
	}


	/**
	 * Tronque une date à la 10e minute (Ex : 10h53 => 10h50)
	 *
	 * @param date
	 * @return
	 */
	static Date truncMinute10(Date date) {
		int minute = Math.floor(date[Calendar.MINUTE] / 10.0) * 10
		date[Calendar.SECOND] = 0
		date[Calendar.MILLISECOND] = 0
		date[Calendar.MINUTE] = minute
		return date
	}


	/**
	 * Tronque une date à la 5e minute
	 * Ex : 10h56 => 10h55, 10h53 => 10h50
	 *
	 * @param date
	 * @return
	 */
	static Date truncMinute5(Date date) {
		int minute = Math.floor(date[Calendar.MINUTE] / 5.0) * 5
		date[Calendar.SECOND] = 0
		date[Calendar.MILLISECOND] = 0
		date[Calendar.MINUTE] = minute
		return date
	}


	/**
	 * Tronque une date à l'heure
	 *  
	 * @param date
	 * @return
	 */
	static Date copyTruncHour(Date date) {
		int hour = date[Calendar.HOUR_OF_DAY]
		Date newDate = date.clone().clearTime()
		newDate[Calendar.HOUR_OF_DAY] = hour
		return newDate
	}


	/**
	 * Tronque une date au jour
	 *  
	 * @param date
	 * @return
	 */
	static Date copyTruncDay(Date date) {
		return date.clone().clearTime()
	}


	/**
	 * Début du jour
	 * 
	 * @param date
	 * @return
	 */
	static Date firstTimeInDay(Date date) {
		return date.clone().clearTime()
	}


	/**
	 * Fin du jour
	 * 
	 * @param date
	 * @return
	 */
	static Date lastTimeInDay(Date date) {
		Date endDate

		use(TimeCategory) {
			endDate = firstTimeInDay(date) + 23.hours + 59.minutes + 59.seconds
		}

		return endDate
	}


	/**
	 * Décale l'année sur la date
	 * 
	 * @param date
	 * @param inc
	 * @return
	 */
	static Date incYear(Date date, int inc) {
		Date result

		if (date) {
			use(TimeCategory) {
				result = date + inc.year
			}
		}

		return result
	}
	
	
	/**
	 * Décale le mois sur la date
	 * 
	 * @param date
	 * @param inc
	 * @return
	 */
	static Date incMonth(Date date, int inc) {
		Date result
		
		if (date) {
			use(TimeCategory) {
				result = date + inc.month
			}
		}
		
		return result
	}

}
