package smarthome.security

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService
import smarthome.core.AsynchronousWorkflow;
import smarthome.core.QueryUtils;
import smarthome.core.SmartHomeException


/**
 * 
 * @author gregory
 *
 */
class UserApplicationService extends AbstractService {

	/**
	 * Suppression d'un device
	 *
	 * @param device
	 * @return
	 */
	@PreAuthorize("hasPermission(#userApplication, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void delete(UserApplication userApplication) {
		super.delete(userApplication)
	}
	
	
	/**
	 * Recherche multi-critère
	 * 
	 * @param command
	 * @return
	 * @throws SmartHomeException
	 */
	List<UserApplication> search(UserApplicationCommand command, Map pagination) throws SmartHomeException {
		if (!command.user) {
			throw new SmartHomeException("user is required !", command)
		}
		
		return UserApplication.createCriteria().list(pagination) {
			eq 'user', command.user		
			
			if (command.search) {
				ilike 'name', QueryUtils.decorateMatchAll(command.search)
			}
			
			order 'name'
		}
	}
	
	
	/**
	 * Enregistrement d'une nouvelle (ou mise à jour) application pour un user
	 * 
	 * @param user
	 * @param applicationId
	 * @param applicationName
	 * @return
	 * @throws SmartHomeException
	 */
	@AsynchronousWorkflow("userApplicationService.registerApplication")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	UserApplication registerApplication(User user, String applicationId, String applicationName) throws SmartHomeException {
		UserApplication userApp = findByUserAndApplicationId(user, applicationId)
		
		if (!userApp) {
			userApp = new UserApplication(user: user, applicationId: applicationId)
			userApp.name = applicationName
			userApp.dateAuth = new Date()
			userApp.token = UUID.randomUUID().toString().bytes.encodeBase64().toString()
		}
		
		return this.save(userApp)
	}
	
	
	/**
	 * Recherche une application d'un user
	 * 
	 * @param user
	 * @param applicationId
	 * @return
	 */
	UserApplication findByUserAndApplicationId(User user, String applicationId) {
		return UserApplication.findByUserAndApplicationId(user, applicationId)	
	}
}
