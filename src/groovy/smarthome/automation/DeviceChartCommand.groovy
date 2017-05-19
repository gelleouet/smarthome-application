package smarthome.automation

import smarthome.automation.deviceType.AbstractDeviceType;
import smarthome.core.DateUtils;
import grails.validation.Validateable;
import groovy.time.TimeCategory;


@Validateable
class DeviceChartCommand {
	Device device
	Date dateChart
	String viewMode
	AbstractDeviceType deviceImpl
	
	
	static constraints = {
		deviceImpl nullable: true
	}
	
	
	/**
	 * Defaut constructor
	 * 
	 */
	DeviceChartCommand() {
		dateChart = new Date().clearTime()
		viewMode = "day"
	}
	
	
	/**
	 * Date début en fonction view mode
	 * 
	 * @return
	 */
	Date dateDebut() {
		Date date = dateChart.clearTime()
		
		switch (viewMode) {
			case "year": return DateUtils.firstDayInYear(date) 
			case "month": return DateUtils.firstDayInMonth(date) 
			case "week": return DateUtils.firstDayInWeek(date) 
			default: return date
		}
	}
	
	
	/**
	 * Date fin en fonction view mode
	 * 
	 * @return
	 */
	Date dateFin() {
		Date date = dateChart.clearTime()
		
		switch (viewMode) {
			case "year": 
				date = DateUtils.firstDayInYear(date)
				break
			case "month":
				date = DateUtils.firstDayInMonth(date)
				break
			case "week":
				date = DateUtils.firstDayInWeek(date)
				break
		}
		
		use(TimeCategory) {
			date = date + 23.hours + 59.minutes + 59.seconds
		}
		
		return date
	}
}
