package smarthome.security

import smarthome.security.RoleService;
import grails.plugin.springsecurity.annotation.Secured;
import grails.transaction.Transactional;
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.QueryUtils;
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum
import smarthome.security.Role;

@Secured("hasRole('ROLE_ADMIN')")
class RoleController extends AbstractController {

	RoleService roleService
	
	/**
	 * Affichage des rôles
	 * 
	 * @return
	 */
	@NavigableAction(label = "Permissions", navigation = NavigationEnum.configuration, header = SmartHomeSecurityConstantes.UTILISATEURS, breadcrumb = [
		NavigableAction.CONFIG_APPLICATION,
		SmartHomeSecurityConstantes.UTILISATEUR_SECURITE
	])
	def roles(String roleSearch) {
		def roles
		int recordsTotal
		def pagination = this.getPagination([:])

		if (roleSearch) {
			def search = QueryUtils.decorateMatchAll(roleSearch)
			
			def query = Role.where {
				authority =~ search
			}
			roles = query.list(pagination)
			recordsTotal = query.count()
		} else {
			recordsTotal = Role.count()
			roles = Role.list(pagination)
		}

		// roles est accessible depuis le model avec la variable role[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond roles, model: [recordsTotal: recordsTotal, roleSearch: roleSearch]
	}
	
	
	/**
	 * Edition d'un role
	 *
	 * @param role
	 * @return
	 */
	def edit(Role role) {
		def editRole = parseFlashCommand("role", role)
		render(view: 'role', model: [role: editRole])
	}


	/**
	 * Création d'un utilisateur
	 *
	 * @param user
	 * @return
	 */
	def create() {
		def editRole = parseFlashCommand("role", new Role(authority: Role.ROLE_PREFIX))
		render(view: 'role', model: [role: editRole])
	}
	
	
	/**
	 * Enregistrement d'un utilisateur existant avec toutes ses associations
	 *
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = "role")
	def saveEdit(Role role) {
		checkErrors(this, role)
		roleService.save(role)
		redirect(action: 'roles')
	}


	/**
	 * Enregistrement d'un nouvel utilisateur avec toutes ses associations
	 *
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "create", modelName = "role")
	def saveCreate(Role role) {
		checkErrors(this, role)
		roleService.save(role)
		redirect(action: 'roles')
	}
}
