<%=packageName ? "package ${packageName}\n\n" : ''%>

import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.QueryUtils;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;

import org.springframework.security.access.annotation.Secured;

@Secured("hasRole('ROLE_ADMIN')")
class ${className}Controller extends AbstractController {

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
		def ${propertyName}s
		int recordsTotal
		def pagination = this.getPagination([:])

		if (${propertyName}Search) {
			def search = QueryUtils.decorateMatchAll(${propertyName}Search)
			
			def query = ${className}.where {
				// TODO : property1 =~ search || property2 =~ search
			}
			${propertyName}s = query.list(pagination)
			recordsTotal = query.count()
			
			/**
			 * Query by criteria
			 * 
			${propertyName}s = ${className}.createCriteria().list(pagination) {
				or {
					ilike('property1', search)
					ilike('property2', search)
				}
			}			
			recordsTotal = ${propertyName}s.totalCount
			*/
		} else {
			recordsTotal = ${className}.count()
			${propertyName}s = ${className}.list(pagination)
		}

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
		
		// Compléter le model
		// TODO
		
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
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "create", modelName = ${className}Controller.COMMAND_NAME)
	def saveCreate(${className} ${propertyName}) {
		checkErrors(this, ${propertyName})
		${propertyName}Service.save(${propertyName})
		redirect(action: COMMAND_NAME + 's')
	}
}
