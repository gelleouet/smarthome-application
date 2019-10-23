package smarthome.application



import org.springframework.security.access.annotation.Secured

import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.SmartHomeException
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum


@Secured("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMIN_GRAND_DEFI')")
class DefiController extends AbstractController {

	private static final String COMMAND_NAME = 'defi'

	DefiService defiService

	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Défis", navigation = NavigationEnum.configuration, header = "Compte")
	def defis(DefiCommand command) {
		command.user = authenticatedUser // spring security plugin
		def defis = defiService.listByAdmin(command, this.getPagination([:]))
		def recordsTotal = defis.totalCount

		// defis est accessible depuis le model avec la variable defi[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond defis, model: [recordsTotal: recordsTotal, command: command]
	}


	/**
	 * Edition
	 *
	 * @param defi
	 * @return
	 */
	def edit(Defi defi) {
		def editDefi = parseFlashCommand(COMMAND_NAME, defi)

		if (defi?.id) {
			defiService.edit(defi)
		}

		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editDefi]))
	}


	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]

		// on remplit avec les infos du user
		model << userModel

		if (model.defi?.id) {
			model.participants = defiService.listParticipantResultat(new DefiCommand(defi: model.defi), [:])
			model.equipeProfils = defiService.listEquipeProfilResultat(model.defi)
		}

		return model
	}


	/**
	 * Enregistrement modification
	 *
	 * @param defi
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = DefiController.COMMAND_NAME)
	def save(Defi defi) {
		defi.user = authenticatedUser // spring security plugin
		defi.validate()

		checkErrors(this, defi)
		defiService.save(defi)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Suppression
	 *
	 * @param defi
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "defis")
	def delete(Defi defi) {
		defiService.delete(defi)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Efface les résultats
	 * 
	 * @param command
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = DefiController.COMMAND_NAME)
	def effacerResultat(DefiCommand command) {
		Defi defi

		if (command.defiEquipeParticipant) {
			defi = command.defiEquipeParticipant.defiEquipe.defi
			defiService.effacerParticipant(command.defiEquipeParticipant)
		} else if (command.defiEquipe) {
			defi = command.defiEquipe.defi
			defiService.effacerEquipe(command.defiEquipe)
		} else if (command.defi) {
			defi = command.defi
		}

		renderTemplateParticipants(defi)
	}


	/**
	 * Efface les résultats
	 *
	 * @param command
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = DefiController.COMMAND_NAME)
	def calculerResultat(DefiCommand command) {
		defiService.calculerConsommations(command.defi)
		defiService.calculerResultats(command.defi)
		renderTemplateParticipants(command.defi)
	}


	/**
	 * Dialogue défi equipe : ajout/modif
	 * 
	 * @param defiEquipe
	 * @return
	 */
	def dialogDefiEquipe(DefiEquipe defiEquipe) {
		render(template: 'dialogDefiEquipe', model: [defiEquipe: defiEquipe])
	}


	/**
	 * Dialogue défi participant
	 * 
	 * @param command
	 * @return
	 */
	def dialogDefiParticipant(DefiParticipantCommand command) {
		render(template: 'dialogDefiParticipant', model: [command: command,
			participants: defiService.listAvailableParticipants(command.defi),
			equipes: command.defi.equipes])
	}


	/**
	 * Enregistrement modif défi équipe
	 * 
	 * @param defiEquipe
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = DefiController.COMMAND_NAME)
	def saveDefiEquipe(DefiEquipe defiEquipe) {
		checkErrors(this, defiEquipe)
		defiService.saveDefiEquipe(defiEquipe)
		renderTemplateParticipants(defiEquipe.defi)
	}


	/**
	 * Enregistrement modif défi participant
	 *
	 * @param defiEquipe
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = DefiController.COMMAND_NAME)
	def saveDefiParticipant(DefiParticipantCommand command) {
		checkErrors(this, command)
		defiService.saveDefiParticipant(command)
		renderTemplateParticipants(command.defi)
	}


	/**
	 * Template participants
	 * 
	 * @param defi
	 * @return
	 */
	def renderTemplateParticipants(Defi defi) {
		render(template: 'participants', model: fetchModelEdit([defi: defi]))
	}


	/**
	 * Suppression d'un participant
	 *  
	 * @param defiEquipeParticipant
	 * @return
	 */
	def deleteParticipant(DefiEquipeParticipant defiEquipeParticipant) {
		Defi defi = defiEquipeParticipant.defiEquipe.defi
		defiService.delete(defiEquipeParticipant)
		renderTemplateParticipants(defi)
	}
}
