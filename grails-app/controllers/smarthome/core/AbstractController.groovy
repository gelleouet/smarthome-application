package smarthome.core

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import java.lang.reflect.Method

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
	def getPagination(userPaginate) {
		params.max = params.max ?: grailsApplication.config.smarthome.pagination.defaultMax
		params.offset = params.offset ?: 0

		if (userPaginate) {
			params << userPaginate
		}

		return params
	}


	/**
	 * Vérifie dans le scope flash si un objet du nom "command" existe.
	 * Si oui renvoit cet objet, sinon renvoit l'objet par défaut
	 * 
	 * @param commandName
	 * @param defaultObject
	 * @return
	 */
	def parseFlashCommand(commandName, defaultObject) {
		request[commandName] ?: defaultObject
	}


	/**
	 * Redirection vers la dernière page
	 * 
	 * @return
	 */
	def redirectLastURI() {
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


	void setError(def error) {
		request.setAttribute("error", error)
	}


	void setErrors(def errors) {
		request.setAttribute("errors", errors)
	}


	void setCommand(String name, def command) {
		request.setAttribute(name, command)
	}


	void setInfo(def info) {
		request.setAttribute("info", info)
	}

	void setWarning(def warning) {
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

		if (metaHandler) {
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
		} else if (request.xhr) {
			// rendu erreur uniquement pour les appels Ajax
			render (status: 400, template: '/templates/messageError', model: [title: exception.message])
		}
	}


	/**
	 * Supprime et renvoit un objet de la session
	 * 
	 * @param attribute
	 * @return
	 */
	def removeSession(String attribute) {
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
	def getSessionAttribute(String attribute) {
		return session[(attribute)]
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
	def parsePluginCommand() {
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
	def nop() {
		render(status: 200, text: '')
	}


	/**
	 * Retour simple avec message d'erreur et code http
	 * 
	 * @param error
	 * @param status
	 * @return
	 */
	def nopError(String error, int status) {
		render(status: status, text: error)
	}

	/**
	 * Retour simple avec message d'erreur et code http par défaut 400
	 *
	 * @param error
	 * @param status
	 * @return
	 */
	def nopError400(String error) {
		nopError(error, 400)
	}


	/**
	 * Récupère le fichier si uploadé
	 */
	byte[] parseFile(String name) {
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
	def bindFile(def domain, String domainProperty, String paramFile) {
		def dataFile = this.parseFile(paramFile)

		if (dataFile) {
			domain."$domainProperty" = dataFile
			domain.clearErrors()
			domain.validate()
		}

		return domain
	}
}
