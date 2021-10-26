package smarthome.core

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import java.lang.reflect.Method

import org.apache.commons.io.IOUtils
import org.springframework.security.access.AccessDeniedException

import smarthome.core.ExceptionNavigationHandler
import smarthome.core.SmartHomeCoreConstantes
import smarthome.core.SmartHomeException


/**
 * Classe de base pour tous les controller
 * <ul>
 * <li>Gestion des erreurs métier (SmartHomeException) et redirection vers la page en cours</li>
 * <li>Protection par défaut des URL avec utilisateur connecté</li>
 * </ul>
 * 
 * !! IMPORTANT : penser à bien passer toutes les méthodes "utilitaires" en protected
 * pour éviter qu'elles soient considérées en action et qu'elles soient parsées par 
 * tous les plugins et proxy liés à l'artefact Controller (sauf les handlers exception
 * qui doivent rester public)
 * 
 * @author gregory
 *
 */
@Secured("isAuthenticated()")
abstract class AbstractController {

	// injecté automatiquement
	def grailsApplication


	/**
	 * Controle des erreurs de validation et de binding.
	 * A appeler en 1er dans une action du controller
	 *
	 * IMPORTANT : la méthode est statique pour éviter d'être traitée avec le système AOP des exceptions handling
	 *
	 * @param controller
	 * @return
	 * @throws SmartHomeException
	 */
	static def checkErrors(controller) {
		if (controller.hasErrors()) {
			throw new SmartHomeException("Erreur validation du formulaire", controller)
		}
	}


	/**
	 * Controle des erreurs de validation et de binding.
	 * A appeler en 1er dans une action du controller
	 * 
	 * IMPORTANT : la méthode est statique pour éviter d'être traitée avec le système AOP des exceptions handling
	 * 
	 * @param controller
	 * @param command
	 * @return
	 * @throws SmartHomeException
	 */
	static def checkErrors(controller, command) throws SmartHomeException {
		checkErrors(controller)

		if (command.hasErrors()) {
			throw new SmartHomeException("Erreur validation du formulaire", command)
		}
	}


	/**
	 * Scanne les infos de la request et construit un objet pagination avec les valeurs par défaut
	 * si tout n'est pas renseigné
	 * 
	 * @return
	 */
	protected def getPagination(userPaginate) {
		params.max = params.max ?: getPaginationDefaultMax()
		params.offset = params.offset ?: 0

		if (userPaginate) {
			params << userPaginate
		}

		return params
	}
	
	
	/**
	 * Injecte les valeurs de pagination dans la request
	 * @param max
	 */
	protected void setPagination(Map pagination) {
		params.max = pagination.max ?: getPaginationDefaultMax()
		params.offset = pagination.offset ?: 0
	}
	
	
	/**
	 * Config : smarthome.pagination.defaultMax
	 * 
	 * @return
	 */
	protected int getPaginationDefaultMax() {
		ApplicationUtils.configDefaultMax(grailsApplication)
	}


	/**
	 * Vérifie dans le scope flash si un objet du nom "command" existe.
	 * Si oui renvoit cet objet, sinon renvoit l'objet par défaut
	 * 
	 * !! IMPORTANT : le scope flash n'étant pas utilisable en cluster et compatible stateless,
	 * les objets sont stockées simplement dans la request. On conserve le nom dans un souci
	 * de compatibilité avec le code existant (à moins de faire un refactoring complet)
	 * 
	 * FIXME : renommer en parseRequestCommand
	 * 
	 * @param commandName
	 * @param defaultObject
	 * @return
	 */
	protected def parseFlashCommand(commandName, defaultObject) {
		request[commandName] ?: defaultObject
	}

	
	/**
	 * Gestion d'un objet command en view session
	 * Sur les appels POST, l'objet est récupéré depuis la request et mis en session
	 * Sur les appels GET, l'objet est récupéré depuis la session s'il existe ou depuis la request
	 *
	 * Dans tous les cas, l'objet est remis en session
	 *
	 * @param defaultObject
	 * @return
	 */
	protected def parseViewCommand(defaultObject) {
		def command

		if (request.post) {
			command = defaultObject
		} else {
			command = getViewAttribute(defaultObject)
		}

		return setSessionAttribute(SmartHomeCoreConstantes.ATTRIBUTE_VIEW_NAME, command)
	}
	

	/**
	 * Redirection vers la dernière page
	 * 
	 * @return
	 */
	protected def redirectLastURI() {
		redirect (uri: session[(SmartHomeCoreConstantes.SESSION_LAST_URI)])
	}


	/**
	 * Gestionnaire d'erreur pour les accès non autorisés
	 *
	 * @param exception
	 * @return
	 */
	def handleAccessDeniedException(AccessDeniedException exception) {
		setError(exception.message)
	}


	protected void setError(def error) {
		request.setAttribute("error", error)
	}


	protected void setErrors(def errors) {
		request.setAttribute("errors", errors)
	}


	protected void setCommand(String name, def command) {
		request.setAttribute(name, command)
	}


	protected void setInfo(def info) {
		request.setAttribute("info", info)
	}

	protected void setWarning(def warning) {
		request.setAttribute("message", warning)
	}


	/**
	 * Gestionnaire d'erreur pour les exceptions métier LIMS.
	 * Redirection vers la page indiquée avec l'objet command en flash paramètre
	 * POST / REDIRECT / GET pattern
	 * Du fait du redirect, les objets flash seront supprimés après le dernier GET
	 * 
	 * @param exception
	 * @return
	 */
	def handleSmartHomeException(SmartHomeException exception) {
		// on insère l'exception dans la request
		request.setAttribute("exception", exception)


		// ajout des erreurs pour la redirection (sauf ajax)
		if (!request.xhr) {
			setError(exception.message)

			if (exception.errors) {
				setErrors(exception.errors)
			} else if (exception.artefactObject && exception.artefactObject.metaClass.respondsTo(exception.artefactObject, 'hasErrors')) {
				if (exception.artefactObject.hasErrors()) {
					setErrors(exception.artefactObject)
				}
			}
		}

		// recherche de l'annotation @ActionExceptionHandler sur l'action en cours
		Method method = this.getClass().getDeclaredMethod(actionName)
		ExceptionNavigationHandler metaHandler = method.getAnnotation(ExceptionNavigationHandler)

		
		// on ne fait plus rien si la reponse est déjà commité sinon nouvelle exception en cascade
		if (! response.committed) {
			if (request.xhr) {
				// rendu erreur uniquement pour les appels Ajax
				render (status: 400, template: '/templates/messageError', model: [title: exception.message])
			} else if (metaHandler) {
				if (metaHandler.json()) {
					render(status: 400, contentType: "application/json") {
						[error: exception.message, details: request.errors]
					}
				} else {
					// ajout de l'objet en erreur dans le modèle
					if (metaHandler.modelName()) {
						setCommand(metaHandler.modelName(), exception.artefactObject)
					}
	
					// sélection du controller
					def controller = metaHandler.controllerName() ?: controllerName
	
					forward (controller: controller, action : metaHandler.actionName())
				}
			}
		} else {
			// la resonse est déjà commitée, on s"assure que le flux est bien fermé
			IOUtils.closeQuietly(response.outputStream)
		}
	}


	/**
	 * Supprime et renvoit un objet de la session
	 * 
	 * @param attribute
	 * @return
	 */
	protected def removeSession(String attribute) {
		def object = session[(attribute)]
		session.removeAttribute(attribute)
		return object
	}


	/**
	 * Renvoit un objet de la session
	 * 
	 * @param attribute
	 * @return
	 */
	protected def getSessionAttribute(String attribute) {
		return session[(attribute)]
	}
	
	
	/**
	 * Renvoit un objet de la session. Si l'objet n'est pas trouvé, l'objet par défaut
	 * est mis en session et renvoyé.
	 *
	 * La méthode géère aussi le cas où l'objet existe en session mais n'est pas du même type
	 * que l'objet par défaut. On utilise pour diminuer le nombre d'objets en session le même nom
	 * d'attribut pour certains objets notamment pour les moteurs de recherche. En changeant de page
	 * l'objet de la page précédente est écrasé. Cela correspond un peu au scope view
	 *
	 * @param defaultValue
	 * @return
	 */
	protected Object getViewAttribute(Object defaultValue) {
		Object value = getSessionAttribute(SmartHomeCoreConstantes.ATTRIBUTE_VIEW_NAME)


		if (!value || value.getClass() != defaultValue?.getClass()) {
			value = defaultValue
			this.setSessionAttribute(SmartHomeCoreConstantes.ATTRIBUTE_VIEW_NAME, defaultValue)
		}

		return value
	}


	/**
	 * Insère un objet dans la session
	 * 
	 * @param attribute
	 * @param value
	 * @return
	 */
	def setSessionAttribute(String attribute, value) {
		session[(attribute)] = value
		return value
	}


	/**
	 * L'objet command d'un plugin peut être soit l'objet lui-même, soit l'objet au format JSON (pour les appels ajax)
	 * Il faut donc le convertir avant de le traiter
	 * 
	 * 
	 * @return
	 */
	protected def parsePluginCommand() {
		if (params.command instanceof String) {
			def json = JSON.parse(params.command)
			def object =  Class.forName(json['class']).newInstance(json)

			// cas spécial pour le id qui n'est pas déclaré explicitement
			if (json.id) {
				object.id = json.id
			}

			return object
		} else {
			return params.command
		}
	}


	/**
	 * Ne fait rien en retour de l'action (affichage chaine vide)
	 * Surtout utilisé pour les appels Ajax
	 * 
	 * @return
	 */
	protected def nop() {
		render(status: 200, text: '')
	}


	/**
	 * Retour simple avec message d'erreur et code http
	 * 
	 * @param error
	 * @param status
	 * @return
	 */
	protected def nopError(String error, int status) {
		render(status: status, text: error)
	}

	/**
	 * Retour simple avec message d'erreur et code http par défaut 400
	 *
	 * @param error
	 * @param status
	 * @return
	 */
	protected def nopError400(String error) {
		nopError(error, 400)
	}


	/**
	 * Récupère le fichier si uploadé
	 */
	protected byte[] parseFile(String name) {
		def file = request.getFile(name)
		if (file && !file.empty) {
			return file.getInputStream().getBytes()
		} else {
			return null
		}
	}


	/**
	 * Bind un fichier sur une entité
	 *
	 * @param domain
	 * @param domainProperty
	 * @param paramFile
	 */
	protected def bindFile(def domain, String domainProperty, String paramFile) {
		def dataFile = this.parseFile(paramFile)

		if (dataFile) {
			domain."$domainProperty" = dataFile
			domain.clearErrors()
			domain.validate()
		}

		return domain
	}
}
