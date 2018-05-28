package smarthome.security

import java.io.Serializable;
import java.util.List;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;

class SmartHomePermissionEvaluator implements PermissionEvaluator {

	PermissionFactory permissionFactory
	
	@Override
	boolean hasPermission(Authentication authentication, Object domainObject, Object permission) {
		Permission perm = resolvePermission(permission)
		
		if (perm.mask == SmartHomePermission.OWNER.mask) {
			return hasOwnerPermission(authentication, domainObject)
		} else {
			return false
		}
	}

	@Override
	boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return false
	}

	
	
	protected boolean hasOwnerPermission(Authentication authentication, Object domainObject) {
		// pas d'évaluation sur les nouveaux objets
		if (domainObject && domainObject.properties['id'] == null) {
			return true
		} else if (domainObject && domainObject['user']?.id && authentication && authentication['principal']?.id) {
			// propriétaire direct de l'objet
			if (domainObject['user'].id == authentication.principal.id) {
				return true
			} else {
				// le suer connecté est peut être un admin d'un groupe d'utilisateur
				// dans ce cas, il a accès à tous les objets des utilisateurs du groupe
				return UserAdmin.createCriteria().list {
					eq 'admin.id', authentication.principal.id 
					eq 'user.id', domainObject['user'].id
				}
			}
		}
		
		return false
	}
	
	
	protected Permission resolvePermission(Object permission) {
		if (permission instanceof Permission) {
			return permission
		} else if (permission instanceof String) {
			return permissionFactory.buildFromName(((String)permission).toUpperCase())
		}
		throw new IllegalArgumentException("Unsupported permission: " + permission)
	}
}
