package smarthome.application

import grails.validation.Validateable
import smarthome.security.Profil
import smarthome.security.User

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Validateable
class DefiCommand {
	String search
	User user
	Defi defi
	DefiEquipe defiEquipe
	DefiEquipeParticipant defiEquipeParticipant
	boolean profilPublic


	static constraints = {
		search nullable: true
		defi nullable: true
		defiEquipe nullable: true
		defiEquipeParticipant nullable: true
		profilPublic nullable: true
	}
}
