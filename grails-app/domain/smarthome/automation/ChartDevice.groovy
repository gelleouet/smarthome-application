package smarthome.automation

import org.apache.commons.lang.StringUtils;

import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Les devices associés à un chart avec sa config (type graphe, etc...)
 *  
 * @author gregory
 *
 */
@Validateable
class ChartDevice {
	static belongsTo = [chart: Chart]
	
	Device device
	String chartType
	String metavalue
	String function
	int position
	
	boolean persist
	
	static transients = ['persist']
	
	
    static constraints = {
		metavalue nullable: true
		persist bindable: true
		function nullable: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		chart index: "ChartDevice_Chart_Idx"
	}
	
}
