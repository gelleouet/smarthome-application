package smarthome.application

import smarthome.application.granddefi.model.DefiModel
import smarthome.core.ApplicationUtils
import smarthome.core.ClassUtils
import smarthome.core.SmartHomeCoreConstantes
import smarthome.core.SmartHomeException
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
	boolean publique = true
	boolean valider = false
	Set equipes = []
	Set profils = []
	String modele // impl DefiModel pour l'affichage


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
		valider defaultValue: false
		//equipes cascade: 'all-delete-orphan'
		//profils cascade: 'all-delete-orphan'
	}
	
	
	/**
	 * Création instance du modèle
	 * 
	 * @return
	 */
	DefiModel newModeleImpl() throws SmartHomeException {
		DefiModel defiModel = ClassUtils.forNameInstance(modele)
		ApplicationUtils.autowireBean(defiModel)
		return defiModel
	}
	
	
	@Override
	boolean canDisplay() {
		valider
	}
}
