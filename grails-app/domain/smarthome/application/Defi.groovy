package smarthome.application

import smarthome.core.SmartHomeCoreConstantes
import smarthome.security.User
import grails.validation.Validateable

@Validateable
class Defi extends AbstractDefiResultat {

	User user
	String libelle
	String organisation
	Date referenceDebut
	Date referenceFin
	Date actionDebut
	Date actionFin
	boolean actif
	boolean publique
	Set equipes = []
	Set profils = []


	static hasMany = [equipes: DefiEquipe, profils: DefiProfil]

	static belongsTo = [user: User]

	static constraints = {
		// ne pas mapper la contrainte car hibernate produit des requetes de test à chaque modif/load
		// laisser faire la base
		//libelle unique: true
		organisation nullable: true
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.APPLICATION_SCHEMA
		user index: 'Defi_Idx'
		publique defaultValue: true
		//equipes cascade: 'all-delete-orphan'
		//profils cascade: 'all-delete-orphan'
	}
}
