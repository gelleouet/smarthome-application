package smarthome.security

import smarthome.security.RoleGroupService;
import grails.plugin.springsecurity.annotation.Secured;
import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.QueryUtils;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;
import smarthome.security.Role;
import smarthome.security.RoleGroup;

@Secured("hasRole('ROLE_ADMIN')")
class RoleGroupController extends AbstractController {

    RoleGroupService roleGroupService
	
	/**
	 * Affichage des groupes
	 * 
	 * @return
	 */
	@NavigableAction(label = "Groupes", navigation = NavigationEnum.configuration, header = SmartHomeSecurityConstantes.UTILISATEURS, breadcrumb = [
		NavigableAction.CONFIG_APPLICATION,
		SmartHomeSecurityConstantes.UTILISATEUR_SECURITE
	])
	def groupes(String groupeSearch) {
		def groupes
		int recordsTotal
		def pagination = this.getPagination([:])

		if (groupeSearch) {
			def search = QueryUtils.decorateMatchAll(groupeSearch)
			
			def query = RoleGroup.where {
				name =~ search
			}
			groupes = query.list(pagination)
			recordsTotal = query.count()
		} else {
			recordsTotal = RoleGroup.count()
			groupes = RoleGroup.list(pagination)
		}

		// groupes est accessible depuis le model avec la variable groupe[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond groupes, model: [recordsTotal: recordsTotal, groupeSearch: groupeSearch]
	}
	
	
	/**
	 * Edition d'un role
	 *
	 * @param role
	 * @return
	 */
	def edit(RoleGroup command) {
		def editCommand = parseFlashCommand("groupe", command)
		def groupRole = editCommand.getAuthorities()
		render(view: 'groupe', model: [groupe: editCommand, allRole: Role.list(), groupRole: groupRole])
	}


	/**
	 * Création d'un utilisateur
	 *
	 * @param user
	 * @return
	 */
	def create() {
		def editCommand = parseFlashCommand("groupe", new RoleGroup())
		render(view: 'groupe', model: [groupe: editCommand, allRole: Role.list(), groupRole: []])
	}
	
	
	/**
	 * Enregistrement d'un utilisateur existant avec toutes ses associations
	 *
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = "groupe")
	def saveEdit(RoleGroup command) {
		checkErrors(this, command)
		roleGroupService.save(command)
		redirect(action: 'groupes')
	}


	/**
	 * Enregistrement d'un nouvel utilisateur avec toutes ses associations
	 *
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "create", modelName = "groupe")
	def saveCreate(RoleGroup command) {
		checkErrors(this, command)
		roleGroupService.save(command)
		redirect(action: 'groupes')
	}
}
