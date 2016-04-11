package smarthome.rule


import static java.util.Calendar.DAY_OF_MONTH
import static java.util.Calendar.MONTH
import static java.util.Calendar.YEAR
import static java.util.Calendar.MINUTE
import static java.util.Calendar.HOUR

import org.quartz.CronExpression;

import groovy.time.BaseDuration;
import groovy.time.TimeCategory;
import groovy.time.TimeDuration;
import smarthome.automation.DeviceEvent;
import smarthome.core.SmartHomeException;



/**
 * Calcule le décalage pour les événements relatifs aux solstices
 * 
 * @author gregory
 *
 */
class DeviceEventDecalageRule implements Rule<DeviceEvent, Date> {

	private static final String HEURE_ETE_CRON = "0 0 0 ? 3 1L *" // Dernier dimanche mars
	private static final String HEURE_HIVER_CRON = "0 0 0 ? 10 1L *" // Dernier dimanche octobre
	
	
	Map parameters
	
	
	@Override
	public Date execute(DeviceEvent deviceEvent) throws SmartHomeException {
		Date scheduledDate = parameters.scheduledDate
		Date dateDecalage = null
		
		// calcul des dates avec changement d'heure
		Date jour1 = scheduledDate.copyWith([date: 1, month: 0]).clearTime()
		Date changementEte = new CronExpression(HEURE_ETE_CRON).getNextValidTimeAfter(jour1)
		Date changementHiver = new CronExpression(HEURE_HIVER_CRON).getNextValidTimeAfter(jour1)
		
		if (deviceEvent.synchroSoleil && deviceEvent.heureDecalage) {
			// calcul des solstices
			Date solsticeEte = scheduledDate.copyWith([date: 21, month: 5])
			Date solsticeEteSuiv = scheduledDate.copyWith([date: 21, month: 5, year: scheduledDate[YEAR]+1])
			Date solsticeHiver = scheduledDate.copyWith([date: 21, month: 11])
			Date solsticeHiverPrec = scheduledDate.copyWith([date: 21, month: 11, year: scheduledDate[YEAR]-1])
			
			BaseDuration ellapseDays = null
			BaseDuration totalDays = null
			BaseDuration duree = this.dureeDecalage(deviceEvent, scheduledDate)
			
			// calcul de la durée entre le jour J et le solstice de référence
			use(TimeCategory) {
				// l'heure de planif est en hiver et le décalage max en été
				// l'heure augmente jusqu'en été puis diminue jusqu'en hiver
				if (deviceEvent.solstice == "été") {
					if (scheduledDate <= solsticeEte) {
						ellapseDays = scheduledDate - solsticeHiverPrec
						totalDays = solsticeEte - solsticeHiverPrec
					} else if (scheduledDate > solsticeEte && scheduledDate <= solsticeHiver) {
						ellapseDays = solsticeHiver - scheduledDate
						totalDays = solsticeHiver - solsticeEte
					} else {
						ellapseDays = scheduledDate - solsticeHiver
						totalDays = solsticeEteSuiv - solsticeHiver
					}
				}
				// l'heure de planif est en été et le décalage max en hiver
				// l'heure diminue jusqu'en été puis augmente jusqu'en hiver
				else if (deviceEvent.solstice == "hiver") {
					if (scheduledDate <= solsticeEte) {
						ellapseDays = solsticeEte - scheduledDate
						totalDays = solsticeEte - solsticeHiverPrec
					} else if (scheduledDate > solsticeEte && scheduledDate <= solsticeHiver) {
						ellapseDays = scheduledDate - solsticeEte
						totalDays = solsticeHiver - solsticeEte
					} else {
						ellapseDays = solsticeEteSuiv - scheduledDate
						totalDays = solsticeEteSuiv - solsticeHiver
					}
				}
			}
			
			// produit en X pour calculer le décalage journalier en fonction du total
			double coef = (double)ellapseDays.days / (double)totalDays.days
			int minutes = duree.hours * 60 + duree.minutes
			int minuteEllapse = (minutes * coef) as Integer
			
			if (minuteEllapse) {
				use(TimeCategory) {
					dateDecalage = scheduledDate + minuteEllapse.minutes
				}
			}
		}
		
		// gestion passage heure été
		if (deviceEvent.heureEte && scheduledDate >= changementEte && scheduledDate < changementHiver) {
			if (!dateDecalage) {
				dateDecalage = scheduledDate
			}
			
			use(TimeCategory) {
				dateDecalage = dateDecalage + 1.hour
			}
 		}
		
		return dateDecalage
	}
	
	
	/**
	 * Calcule la durée du décalage
	 * 
	 * @param deviceEvent
	 * @param date
	 * @return
	 */
	BaseDuration dureeDecalage(DeviceEvent deviceEvent, Date date) {
		int hour = deviceEvent.heureDecalage.substring(0, 2) as Integer
		int minute = deviceEvent.heureDecalage.substring(3, 5) as Integer
		
		Date dateFin = date.copyWith([hourOfDay: hour, minute: minute])
		BaseDuration duree
		
		use(TimeCategory) {
			duree = dateFin - date
		}
		
		return duree
	}
}
