package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Un type device
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceType {
	String libelle
	boolean capteur
	String implClass
	
	
    static constraints = {
		libelle unique: true
    }
	
	static mapping = {
		capteur defaultValue: false
		sort 'libelle'
	}
	
	
	String viewGrid(Device device) {
		def deviceType = Class.forName(implClass).newInstance()
		deviceType.device = device
		deviceType.viewGrid()
	}
	
	
	String viewForm(Device device) {
		def deviceType = Class.forName(implClass).newInstance()
		deviceType.device = device
		deviceType.viewForm()
	}
	
	String icon(Device device) {
		def deviceType = Class.forName(implClass).newInstance()
		deviceType.device = device
		deviceType.icon()
	}
	
	
	String defaultChartType(Device device) {
		def deviceType = Class.forName(implClass).newInstance()
		deviceType.device = device
		deviceType.defaultChartType()
	}
	
	
	String chartDataTemplate(Device device) {
		def deviceType = Class.forName(implClass).newInstance()
		deviceType.device = device
		deviceType.chartDataTemplate()
	}
}
