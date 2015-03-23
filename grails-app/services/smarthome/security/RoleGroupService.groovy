package smarthome.security

import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService
import smarthome.core.SmartHomeException
import smarthome.security.Role;
import smarthome.security.RoleGroup;
import smarthome.security.RoleGroupRole;


/**
 * 
 * @author gregory
 *
 */
class RoleGroupService extends AbstractService {


	/**
	 * Enregistrement d'un rôle
	 * 
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def save(RoleGroup group) throws SmartHomeException {
		if (!group.save()) {
			throw new SmartHomeException("Erreur enregistrement groupe", group)
		}
		
		// mise à jour des roles
		RoleGroupRole.removeAll(group)
		
		if (group.roles) {
			group.roles.each {
				def role = new Role()
				role.id = it.toInteger()
				RoleGroupRole.create(group, role)
			}
		}
		
		return group
	}
}
