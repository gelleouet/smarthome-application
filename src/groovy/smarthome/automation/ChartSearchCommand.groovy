package smarthome.automation

import java.util.Map;

import grails.validation.Validateable;

@Validateable
class ChartSearchCommand {
	String search
	String groupe
	Date dateChart
	def timeAgo
	
	static constraints = {
		search nullable: true
		groupe nullable: true
	}
	
	/**
	 * Defaut constructor
	 * 
	 */
	ChartSearchCommand() {
		dateChart = new Date().clearTime()
		timeAgo = 24
	}
}
