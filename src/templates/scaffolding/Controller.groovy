<%=packageName ? "package ${packageName}\n\n" : ''%>

import org.springframework.security.access.annotation.Secured;
import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.SmartHomeException;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;


@Secured("hasRole('ROLE_ADMIN')")
class ${className}Controller extends AbstractController {

    private static final String COMMAND_NAME = '${propertyName}'
	
	${className}Service ${propertyName}Service
	
	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "${className}s", navigation = NavigationEnum.configuration, header = "")
	def ${propertyName}s(${className}Command command) {
		def ${propertyName}s = ${propertyName}Service.list(command, this.getPagination([:]))		
		def recordsTotal = ${propertyName}s.totalCount

		// ${propertyName}s est accessible depuis le model avec la variable ${propertyName}[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond ${propertyName}s, model: [recordsTotal: recordsTotal, command: command]
	}
	
	
	/**
	 * Edition
	 *
	 * @param ${propertyName}
	 * @return
	 */
	def edit(${className} ${propertyName}) {
		def edit${className} = parseFlashCommand(COMMAND_NAME, ${propertyName})
		if (${propertyName}.id) {
			${propertyName}Service.edit(${propertyName})
		}
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): edit${className}]))
	}


	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]
		
		// TODO Compléter le model
		// model.toto = 'toto'
		
		// on remplit avec les infos du user
		model << userModel
		
		return model
	}
	
	
	/**
	 * Enregistrement modification
	 *
	 * @param ${propertyName}
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = ${className}Controller.COMMAND_NAME)
	def save(${className} ${propertyName}) {
		checkErrors(this, ${propertyName})
		${propertyName}Service.save(${propertyName})
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Suppression
	 *
	 * @param ${propertyName}
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "${propertyName}s")
	def delete(${className} ${propertyName}) {
		${propertyName}Service.delete(${propertyName})
		redirect(action: COMMAND_NAME + 's')
	}
}
