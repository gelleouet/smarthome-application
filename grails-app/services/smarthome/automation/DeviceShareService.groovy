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
			join 'sharedUser'
		}
	}
	
	
	/**
	 * Ajout nouveau partage sur un device
	 * 
	 * @param device
	 * @param sharedUser
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DeviceShare addShare(Device device, Long sharedUserId) throws SmartHomeException {
		// vérifie d'abord si le partage n'existe pas
		def share = DeviceShare.createCriteria().get {
			eq 'device', device
			
			if (sharedUserId) {
				sharedUser {
					idEq sharedUserId
				}
			} else {
				isNull 'sharedUser'
			}
		}
		
		if (!share) {
			share = new DeviceShare(device: device, sharedUser: sharedUserId ? User.read(sharedUserId) : null)
			this.save(share)	
		}
		
		return share
	}
	
}
