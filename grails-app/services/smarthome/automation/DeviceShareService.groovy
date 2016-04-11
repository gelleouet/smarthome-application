package smarthome.automation

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class DeviceShareService extends AbstractService {

	/**
	 * Les partages associés à un device avec les bonnes jointures fetchées
	 * 
	 * @param device
	 * @return
	 */
	List<DeviceShare> listByDevice(Device device) {
		return DeviceShare.createCriteria().list {
			eq 'device', device
			
			sharedUser {
				order 'nom'
				order 'prenom'
			}
		}
	}
	
}
