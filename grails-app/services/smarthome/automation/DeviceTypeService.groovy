package smarthome.automation

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class DeviceTypeService extends AbstractService {

	
	/**
	 * Enregistrement d"un deviceType
	 * 
	 * @param deviceType
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def save(DeviceType deviceType) throws SmartHomeException {
		if (!deviceType.save()) {
			throw new SmartHomeException("Erreur enregistrement deviceType !", deviceType)
		}
	}
	
}
