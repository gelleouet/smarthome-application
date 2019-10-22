package smarthome.application

import grails.validation.Validateable

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Validateable
class DefiParticipantCommand {
	Defi defi
	List<Long> participants = []
	String defiEquipeName
	DefiEquipeParticipant defiParticipant


	static constraints = {
		defiParticipant nullable: true
	}
}
