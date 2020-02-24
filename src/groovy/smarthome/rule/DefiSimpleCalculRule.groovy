package smarthome.rule


import smarthome.application.Defi
import smarthome.application.DefiCompteurEnum
import smarthome.application.DefiEquipe
import smarthome.application.DefiEquipeParticipant
import smarthome.application.DefiEquipeProfil
import smarthome.application.DefiProfil
import smarthome.core.NumberUtils
import smarthome.core.SmartHomeException
import smarthome.security.Profil



/**
 * Calcul d'un défi consommation
 * 
 * Version simplifiée par calcul des évolutions
 * 
 * @see /resources/grand-defi/2019.09.26 Calcul des économies GDE 2019.xlsx
 * fichier de simulation du calcul sur 12 équipes
 * 
 * @author gregory
 *
 */
class DefiSimpleCalculRule implements Rule<Defi, Defi> {

	static final Long PROFIL_PARTICULIER_ID = 1L
	static final String SUFFIX_ALL = "ALL"

	Map parameters


	@Override
	public Defi execute(Defi defi) throws SmartHomeException {
		// récupère la liste des participants "à plat" depuis les paramètres
		// de la règle
		List<DefiEquipeParticipant> participants = parameters.participants
		List equipeProfils = []

		// regroupement des participants par catégorie (profil du user)
		Map participantsGroup = groupParticipant(participants)


		// calcul des notes au niveau participant
		// la note globale, c'est la moyenne des notes elec et gaz
		participants.each { participant ->
			participant.economie_gaz = noteParticipant(participant, DefiCompteurEnum.gaz)
			participant.economie_electricite = noteParticipant(participant, DefiCompteurEnum.electricite)
			participant.economie_global = NumberUtils.moyenne(participant.economie_electricite,
					participant.economie_gaz)
		}

		// calcul des notes par équipe / profil
		// il s'agit simplement de faire les moyennes des économies des participants
		// à chaque niveau : catégorie puis équipe
		defi.equipes.each { DefiEquipe equipe ->
			// d'abord calcul des notes par profil
			equipe.profils.each { DefiEquipeProfil defiEquipeProfil ->
				// mise à plat des profils des équipes pour faire le calcul par
				// profil au niveau racine défi
				equipeProfils << defiEquipeProfil

				// filtre les participants par profil
				def participantsProfil = equipe.participants.findAll {
					it.user.profil == defiEquipeProfil.profil
				}

				// note profil = moyenne des participants
				// on paramètre la note du profil avec noteProfil
				defiEquipeProfil.economie_electricite = noteProfil(defiEquipeProfil.profil,
						moyenneEconomie(participantsProfil, DefiCompteurEnum.electricite))
				defiEquipeProfil.economie_gaz = noteProfil(defiEquipeProfil.profil,
						moyenneEconomie(participantsProfil, DefiCompteurEnum.gaz))
				defiEquipeProfil.economie_global = NumberUtils.moyenne(defiEquipeProfil.economie_electricite,
						defiEquipeProfil.economie_gaz)
			}

			// note équipe = moyenne des profils
			equipe.economie_electricite = moyenneEconomie(equipe.profils, DefiCompteurEnum.electricite)
			equipe.economie_gaz = moyenneEconomie(equipe.profils, DefiCompteurEnum.gaz)
			equipe.economie_global =  NumberUtils.moyenne(equipe.economie_electricite, equipe.economie_gaz)
		}

		// calcul note simple au niveau profil defi
		// note = moyenne des profils de chaque équipe
		defi.profils.each { DefiProfil defiProfil ->
			def profilList = equipeProfils.findAll {
				it.profil == defiProfil.profil
			}
			defiProfil.economie_electricite = moyenneEconomie(profilList, DefiCompteurEnum.electricite)
			defiProfil.economie_gaz = moyenneEconomie(profilList, DefiCompteurEnum.gaz)
			defiProfil.economie_global = NumberUtils.moyenne(defiProfil.economie_electricite, defiProfil.economie_gaz)
		}

		// calcul au niveau défi
		// on refait les moyennes des profils
		defi.economie_electricite = moyenneEconomie(defi.profils, DefiCompteurEnum.electricite)
		defi.economie_gaz = moyenneEconomie(defi.profils, DefiCompteurEnum.gaz)
		defi.economie_global = NumberUtils.moyenne(defi.economie_electricite, defi.economie_gaz)

		// ---------------------------------------------------------------------
		// CLASSEMENTS
		// ---------------------------------------------------------------------

		// classement des équipes selon les types de consommation
		classementList(defi.equipes, DefiCompteurEnum.electricite)
		classementList(defi.equipes, DefiCompteurEnum.gaz)
		classementList(defi.equipes, DefiCompteurEnum.global)

		// classement des équipes profil selon les types de consommation
		def equipeProfilGroup = equipeProfils.groupBy { it.profil }.each { key, value ->
			classementList(value, DefiCompteurEnum.electricite)
			classementList(value, DefiCompteurEnum.gaz)
			classementList(value, DefiCompteurEnum.global)
		}

		// classement des participants en fonction de leurs profils
		participantsGroup.each { key, value ->
			classementList(value, DefiCompteurEnum.gaz)
			classementList(value, DefiCompteurEnum.electricite)
			classementList(value, DefiCompteurEnum.global)
		}


		return defi
	}


	/**
	 * Calcul le groupe (profil) d'un participant
	 * 
	 * @param participant
	 * @return
	 */
	String groupKeyParticipant(DefiEquipeParticipant participant) {
		"${participant.user.profil.id}${SUFFIX_ALL}"
	}


	/**
	 * Classe les participants dans leurs groupes respectifs
	 * Un participant peut appartenir à plusieurs groupes (Ex: GN + US)
	 * 
	 * @param participants
	 * @return Map avec les clés des sous-catégories
	 */
	private Map groupParticipant(Collection<DefiEquipeParticipant> participants) {
		Map result = [:]

		participants.each { participant ->
			String groupKey = groupKeyParticipant(participant)
			participant.groupKey = groupKey

			if (result[(groupKey)] == null) {
				result[(groupKey)] = []
			}

			// ajout par défaut dans son groupe
			result[(groupKey)] << participant
		}

		return result
	}


	/**
	 * Calcul de la moyenne générale des économies d'un groupe pour un type de compteur
	 * Seul les éléments dont la valeur n'est pas nulle sont comptabilisés
	 *
	 * @param resultatList
	 * @param compteurType
	 * @return
	 */
	private Double moyenneEconomie(def resultatList, DefiCompteurEnum compteurType) {
		def filterList = resultatList.findAll { it."economie_${compteurType}"() != null }
		return filterList ? NumberUtils.round(filterList.sum { it."economie_${compteurType}"() } / filterList.size()) : null
	}


	/**
	 * Calcule la note d'un participant
	 * 
	 * @param participant
	 * @param compteurType
	 * @return
	 */
	private Double noteParticipant(def participant, DefiCompteurEnum compteurType) {
		Double note

		if (participant."evolution_${compteurType}"() != null) {
			note = participant."evolution_${compteurType}"()
		}

		return note
	}


	/**
	 * Particularité des notes au niveau profil
	 * Les particuliers sont x2
	 * 
	 * @param profil
	 * @param note
	 * @return
	 */
	private Double noteProfil(Profil profil, Double note) {
		if (note && profil.id == PROFIL_PARTICULIER_ID) {
			return note * 2
		} else {
			return note
		}
	}


	/**
	 * Calcul des classements sur des résultats (équipes, participants)
	 * Les listes sont filtrées et les résultats sans valeur sont écartés
	 * 
	 * @param values
	 * @param compteurType
	 */
	private void classementList(Collection values, DefiCompteurEnum compteurType) {
		def filterList = values.findAll { it."economie_${compteurType}"() != null }

		filterList.sort { it."economie_${compteurType}"() }.eachWithIndex {  resultat, index ->
			resultat."classement_${compteurType}" = index + 1
			resultat."total_${compteurType}" = filterList.size()
		}
	}
}
