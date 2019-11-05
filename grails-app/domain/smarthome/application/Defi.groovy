package smarthome.application

import smarthome.core.SmartHomeCoreConstantes
import smarthome.security.User
import grails.validation.Validateable

@Validateable
class Defi extends AbstractDefiResultat {

	User user
	String libelle
	Date referenceDebut
	Date referenceFin
	Date actionDebut
	Date actionFin
	boolean actif
	Set equipes = []
	Set profils = []


	static hasMany = [equipes: DefiEquipe, profils: DefiProfil]

	static belongsTo = [user: User]

	static constraints = {
		libelle unique: true
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.APPLICATION_SCHEMA
		user index: 'Defi_Idx'
		//equipes cascade: 'all-delete-orphan'
		//profils cascade: 'all-delete-orphan'
	}
}