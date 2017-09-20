<%=packageName ? "package ${packageName}\n\n" : ''%>

import org.springframework.security.access.annotation.Secured;
import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.SmartHomeException;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;
import smarthome.core.QueryUtils;


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
	def ${propertyName}s(String ${propertyName}Search) {
		def search = QueryUtils.decorateMatchAll(${propertyName}Search)
		
		def ${propertyName}s = ${className}.createCriteria().list(this.getPagination([:])) {
			if (${propertyName}Search) {
				or {
					ilike('property1', search)
					ilike('property2', search)
				}
			}
		}			
		def recordsTotal = ${propertyName}s.totalCount

		// ${propertyName}s est accessible depuis le model avec la variable ${propertyName}[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond ${propertyName}s, model: [recordsTotal: recordsTotal, ${propertyName}Search: ${propertyName}Search]
	}
	
	
	/**
	 * Edition
	 *
	 * @param ${propertyName}
	 * @return
	 */
	def edit(${className} ${propertyName}) {
		def edit${className} = parseFlashCommand(COMMAND_NAME, ${propertyName})
		${propertyName}Service.edit(${propertyName})
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
		redirect(action: 'edit', id: ${propertyName}.id)
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
