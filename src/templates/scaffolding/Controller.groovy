<%=packageName ? "package ${packageName}\n\n" : ''%>

import lims.core.AbstractLimsController;
import lims.core.ExceptionNavigationHandler;
import lims.core.QueryUtils;
import lims.plugin.NavigableAction;
import lims.plugin.NavigationEnum;

import org.springframework.security.access.annotation.Secured;

@Secured("hasRole('ROLE_ADMIN')")
class ${className}Controller extends AbstractLimsController {

    private static final String COMMAND_NAME = '${propertyName}'
	
	${className}Service ${propertyName}Service
	
	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "${className}s", navigation = NavigationEnum.configuration, header = "${className}s", breadcrumb = [
		NavigableAction.CONFIG_APPLICATION,
		"${className}s"
	])
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
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): edit${className}]))
	}


	/**
	 * Création
	 *
	 * @return
	 */
	def create() {
		def edit${className} = parseFlashCommand(COMMAND_NAME, new ${className}())
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
	def saveEdit(${className} ${propertyName}) {
		checkErrors(this, ${propertyName})
		${propertyName}Service.save(${propertyName})
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Enregistrement d'un nouveau
	 *
	 * @param ${propertyName}
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "create", modelName = ${className}Controller.COMMAND_NAME)
	def saveCreate(${className} ${propertyName}) {
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
		${propertyName}Service.save(${propertyName})
		redirect(action: COMMAND_NAME + 's')
	}
}
