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
	DeviceShare addShare(Device device, long userId) throws SmartHomeException {
		// vérifie d'abord si le partage n'existe pas
		def share = DeviceShare.createCriteria().get {
			eq 'device', device
			
			sharedUser {
				idEq userId
			}
		}
		
		if (!share) {
			share = new DeviceShare(device: device, sharedUser: User.read(userId))
			this.save(share)	
		}
		
		return share
	}
	
	
	/**
	 * Suppression d'un partage
	 * 
	 * @param device
	 * @param userId
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void deleteShare(Device device, long userId) throws SmartHomeException {
		DeviceShare.executeUpdate("delete DeviceShare ds where ds.device = :device and ds.sharedUser.id = :sharedUserId",
			[device: device, sharedUserId: userId])	
	}
	
	
	/**
	 * Suppression de tous les partages d'un utilisateur avec un autre
	 *
	 * @param device
	 * @param userId
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void deleteAllShare(User user, User sharedUser) throws SmartHomeException {
		DeviceShare.executeUpdate("""delete DeviceShare ds 
			where ds.device in (select device from Device device where device.user = :user)
			and ds.sharedUser = :sharedUser""",
			[user: user, sharedUser: sharedUser])
	}
}
