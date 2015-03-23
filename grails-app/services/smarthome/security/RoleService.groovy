package smarthome.security

import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService
import smarthome.core.SmartHomeException
import smarthome.security.Role;


/**
 * 
 * @author gregory
 *
 */
class RoleService extends AbstractService {

	// injecté via spring
	def aclPermissionFactory
	
	/**
	 * Enregistrement d'un rôle
	 * 
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def save(Role role) throws SmartHomeException {
		if (!role.save()) {
			throw new SmartHomeException("Erreur enregistrement rôle", role)
		}
		
		// on met à jour la liste des permissions
		if (role.acl) {
			aclPermissionFactory.addPermission(role)
		} else {
			aclPermissionFactory.removePermission(role)
		}
		
		return role
	}
}
