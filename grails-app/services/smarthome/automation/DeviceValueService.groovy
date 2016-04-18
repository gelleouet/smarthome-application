package smarthome.automation

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.Chronometre;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class DeviceValueService extends AbstractService {

	/**
	 * Les dernières valeurs des devices
	 * 
	 * @param devices
	 * @param pagination
	 * @return
	 */
	List<DeviceValue> lastValuesByDevices(List<Device> devices, Map pagination) {
		def values
		Chronometre chrono = new Chronometre()
		
		if (devices) {
			def deviceIds = devices.collect { it.id }
			
			values = DeviceValue.createCriteria().list(pagination) {
				device {
					'in' 'id', deviceIds
				}
				join 'device'
				join 'device.user'
				order 'dateValue', 'desc'	
			}
		}
		
		log.info "List ${devices?.size()} devices values : ${chrono.stop()}ms"
		
		return values
	}	
}
