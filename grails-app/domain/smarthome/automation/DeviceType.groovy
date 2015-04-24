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
	boolean autoScan
	String implClass
	String typeCapteur
	
	
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
	
	
	ChartTypeEnum defaultChartType(Device device) {
		def deviceType = Class.forName(implClass).newInstance()
		deviceType.device = device
		deviceType.defaultChartType()
	}
	
	
	String chartDataTemplate(Device device) {
		def deviceType = Class.forName(implClass).newInstance()
		deviceType.device = device
		deviceType.chartDataTemplate()
	}
	
	Map metaValuesName(Device device) {
		def deviceType = Class.forName(implClass).newInstance()
		deviceType.device = device
		deviceType.metaValuesName()
	}
}
