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
 * Les participants sont d'abord classés en fonction de leur profil (commerce,
 * particulier, batiment public).
 * Ensuite dans chaque catégorie, il y a un sous classement par énergie de chauffage :
 * elec (y compris pac), gaz naturel, et le reste.
 * 
 * Le calcul est différent en fonction des sous catégories.
 * 
 * Usage Spécifique Elec (US) = hors elec et gaz naturel
 * Gaz naturel (GN)
 * ELEC
 * 
 * @see /resources/grand-defi/2019.09.26 Calcul des économies GDE 2019.xlsx
 * fichier de simulation du calcul sur 12 équipes
 * 
 * @author gregory
 *
 */
class DefiCalculRule implements Rule<Defi, Defi> {

	static final Long PROFIL_PARTICULIER_ID = 1L
	static final Long CHAUFFAGE_ELEC_ID = 1L
	static final Long CHAUFFAGE_GN_ID = 6L
	static final String SUFFIX_USAGE_SPECIFIQUE = "US"
	static final String SUFFIX_ELECTRICITE = "ELEC"
	static final String SUFFIX_GAZ_NATUREL = "GN"

	Map parameters
	Map moyenneGeneraleGroup = [:]


	@Override
	public Defi execute(Defi defi) throws SmartHomeException {
		// récupère la liste des participants "à plat" depuis les paramètres
		// de la règle
		List<DefiEquipeParticipant> participants = parameters.participants

		// calcul des notes simples au niveau participant
		participants.each {
			it.economie_electricite = noteEconomie(it.evolution_electricite())
			it.economie_gaz = noteEconomie(it.evolution_gaz())
			it.economie_global = noteEconomie(it.evolution_global())
		}

		// regroupement des participants par catégorie (profil du user)
		// et sous-catégorie (mode de chauffage en 3 groupes : ELEC, GN, US)
		// on doit au maximum avoir 9 groupes : 3 profils x 3 catégories
		Map participantsGroup = groupParticipant(participants)

		// calcul des moyennes d'évolution pour chacun de ces groupes
		// on ne calcule pas les moyennes globales car une formule s'applique à ce niveau
		// EXCEL = colonne O
		moyenneGeneraleGroup = participantsGroup.collectEntries { key, values ->
			// ATTENTION : pas de calcul elec pour les GN car déjà pris en compte dans US
			// et pas de calcul gaz pour les autres
			def result = [electricite: null, gaz: null]

			if (key.endsWith(SUFFIX_GAZ_NATUREL)) {
				result.gaz = moyenneGeneraleEvolution(values, DefiCompteurEnum.gaz)
			} else {
				result.electricite = moyenneGeneraleEvolution(values, DefiCompteurEnum.electricite)
			}

			[(key): result]
		}

		// calcul des notes par équipe / profil dans un 1er temps puis note finale
		// sur l'équipe en faisant la moyenne des 3
		// le cacul par profil est lui même la moyenne des notes par catégorie.
		// il faut donc d'abord calculer ces sous-notes
		// EXCEL = ligne 1
		defi.equipes.each { DefiEquipe equipe ->
			// regroupe d'abord par profil et pour chaque profil, calcul les notes
			// par catégorie
			// EXCEL = ligne 7, 13 et 19
			equipe.profils.each { DefiEquipeProfil defiEquipeProfil ->
				// filtre les participants par profil
				def participantsProfil = equipe.participants.findAll {
					it.user.profil == defiEquipeProfil.profil
				}
				// puis les regroupe par catégorie
				// EXCEL = ligne 2, 4, 6, 8, 10, 12, 14, 16, 18
				// calcul les notes par groupe
				def noteGroup = groupParticipant(participantsProfil).collectEntries { key, values ->
					// ATTENTION : pas de calcul elec pour les GN car déjà pris en compte dans US
					// et pas de calcul gaz pour les autres
					def result = [electricite: null, gaz: null]

					if (key.endsWith(SUFFIX_GAZ_NATUREL)) {
						result.gaz = moyenneEvolutionGroupeEquipe(key, values, DefiCompteurEnum.gaz)
					} else {
						result.electricite = moyenneEvolutionGroupeEquipe(key, values, DefiCompteurEnum.electricite)
					}

					[(key): result]
				}

				// calcul de la note générale du profil / catégorie
				// c'est la moyenne des sous-groupes
				defiEquipeProfil.economie_electricite = noteProfil(defiEquipeProfil.profil,
						moyenneMap(noteGroup, DefiCompteurEnum.electricite))
				defiEquipeProfil.economie_gaz = noteProfil(defiEquipeProfil.profil,
						moyenneMap(noteGroup, DefiCompteurEnum.gaz))
				defiEquipeProfil.economie_global =  moyenne(defiEquipeProfil.economie_electricite, defiEquipeProfil.economie_gaz)
			}

			// les données par profil sont calculées, il suffit de faire la moyenne
			// sur celles-ci pour calculer les notes au niveau équipe
			// il faut bien faire la moyenne des économies calculées et non pas
			// des évolutions
			equipe.economie_electricite = moyenneGeneraleEconomie(equipe.profils, DefiCompteurEnum.electricite)
			equipe.economie_gaz = moyenneGeneraleEconomie(equipe.profils, DefiCompteurEnum.gaz)
			equipe.economie_global =  moyenneGeneraleEconomie(equipe.profils, DefiCompteurEnum.global)
		}

		// calcul note simple au niveau profil defi
		defi.profils.each { DefiProfil defiProfil ->
			defiProfil.economie_electricite = noteProfil(defiProfil.profil, noteEconomie(defiProfil.evolution_electricite()))
			defiProfil.economie_gaz = noteProfil(defiProfil.profil, noteEconomie(defiProfil.evolution_gaz()))
			defiProfil.economie_global = noteProfil(defiProfil.profil, noteEconomie(defiProfil.evolution_global()))
		}

		// calcul note simple au niveau défi
		defi.economie_electricite = noteEconomie(defi.evolution_electricite())
		defi.economie_gaz = noteEconomie(defi.evolution_gaz())
		defi.economie_global = noteEconomie(defi.evolution_global())

		// classement des équipes au niveau global, elec e gaz
		defi.equipes.sort { (it.economie_global() ?: -Integer.MAX_VALUE) * -1 }.eachWithIndex { equipe, index ->
			equipe.classement_global = (index + 1)
		}
		defi.equipes.sort { (it.economie_electricite() ?: -Integer.MAX_VALUE) * -1 }.eachWithIndex { equipe, index ->
			equipe.classement_electricite = (index + 1)
		}
		defi.equipes.sort { (it.economie_gaz() ?: -Integer.MAX_VALUE) * -1 }.eachWithIndex { equipe, index ->
			equipe.classement_gaz = (index + 1)
		}

		return defi
	}


	/**
	 * Calcul le groupe (profil + chauffage) d'un participant
	 * 
	 * @param participant
	 * @return
	 */
	private String groupKeyParticipant(DefiEquipeParticipant participant) {
		if (participant.house?.chauffage?.id == CHAUFFAGE_ELEC_ID) {
			return "${participant.user.profil.id}${SUFFIX_ELECTRICITE}"
		} else if (participant.house?.chauffage?.id == CHAUFFAGE_GN_ID) {
			return "${participant.user.profil.id}${SUFFIX_GAZ_NATUREL}"
		} else {
			return "${participant.user.profil.id}${SUFFIX_USAGE_SPECIFIQUE}"
		}
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

			if (result[(groupKey)] == null) {
				result[(groupKey)] = []
			}

			// ajout par défaut dans son groupe
			result[(groupKey)] << participant

			// cas spécial du GN qui doit apparaitre dans US
			if (groupKey.endsWith(SUFFIX_GAZ_NATUREL)) {
				String groupUS = groupKey.replace(SUFFIX_GAZ_NATUREL, SUFFIX_USAGE_SPECIFIQUE)

				if (result[(groupUS)] == null) {
					result[(groupUS)] = []
				}

				result[(groupUS)] << participant
			}
		}

		return result
	}


	/**
	 * Moyenne sur une Map avec les données aggrégées par compteur (elec, gaz, global)
	 * Les éléments sans valeur ne sont pas pris en compte dans le calcul
	 *
	 * @param map
	 * @param compteurType
	 * @return
	 */
	private Double moyenneMap(def map, DefiCompteurEnum compteurType) {
		def filterList = map.values().findAll { it[(compteurType.toString())] != null }
		return filterList ? NumberUtils.round(filterList.sum { it[(compteurType.toString())] } / filterList.size()) : null
	}


	/**
	 * Calcul de la moyenne générale des évolutions d'un groupe pour un type de compteur
	 * Seul les éléments dont la valeur n'est pas nulle sont comptabilisés
	 * 
	 * @param resultatList
	 * @param compteurType
	 * @return
	 */
	private Double moyenneGeneraleEvolution(def resultatList, DefiCompteurEnum compteurType) {
		def filterList = resultatList.findAll { it."evolution_${compteurType}"() != null }
		return filterList ? NumberUtils.round(filterList.sum { it."evolution_${compteurType}"() } / filterList.size()) : null
	}


	/**
	 * Calcul de la moyenne générale des économies d'un groupe pour un type de compteur
	 * Seul les éléments dont la valeur n'est pas nulle sont comptabilisés
	 *
	 * @param resultatList
	 * @param compteurType
	 * @return
	 */
	private Double moyenneGeneraleEconomie(def resultatList, DefiCompteurEnum compteurType) {
		def filterList = resultatList.findAll { it."economie_${compteurType}"() != null }
		return filterList ? NumberUtils.round(filterList.sum { it."economie_${compteurType}"() } / filterList.size()) : null
	}


	/**
	 * Calcul des notes d'un groupe d'une équipe
	 * Le calcul dépend du type du groupe et ne prend pas en compte les participants
	 * qui n'ont pas de note
	 * ATTENTION !! la note du groupe dépend de la catégorie.
	 * pour US, c'est juste la moyenne.
	 * pour les autres (ELEC, GN), c'est la différence de la moyenne générale
	 * avec la moyenne du groupe
	 * 
	 * Comme il y a des catégories en fonction des compteurs, certains calculs en doublon
	 * ne doivent pas lancés. Par exemple, la catégorie GN ne doit pas rajouter les consos
	 * ELEC dans les moyennes, car déjà inclus dans le calcul US
	 * 
	 * @param groupKey
	 * @param resultatList
	 * @param compteurType
	 * @return
	 * @throws SmartHomeException
	 */
	private Double moyenneEvolutionGroupeEquipe(String groupKey, def resultatList, DefiCompteurEnum compteurType) throws SmartHomeException {
		def filterList = resultatList.findAll { it."evolution_${compteurType}"() != null }
		Double note

		if (filterList) {
			note = filterList.sum { it."evolution_${compteurType}"() } / filterList.size()

			// cas des profils ELEC et GN
			if (! groupKey.endsWith(SUFFIX_USAGE_SPECIFIQUE)) {
				// si il y a ce profil sur cette équipe, c'est que forcément
				// il était présent dans la liste des participants et doit
				// forcément apparaître dans les groupes de moyenne. S'il n'y
				// est pas, c'est que problème quelque part
				if (moyenneGeneraleGroup[(groupKey)] == null || moyenneGeneraleGroup[(groupKey)][(compteurType.toString())] == null) {
					throw new SmartHomeException("La moyenne ${compteurType} du groupe ${groupKey} n'existe pas !")
				}

				note = note - moyenneGeneraleGroup[(groupKey)][(compteurType.toString())]
			}
		}

		return NumberUtils.round(note)
	}


	/**
	 * Calcul l'économie par rapport à l'évolution de la conso
	 * C'est l'inverse (si 3% augmentation, alors c'est -3% d'économie)
	 * 
	 * @param evolution
	 * @return
	 */
	private Double noteEconomie(Double evolution) {
		if (evolution == null) {
			return null
		} else {
			return evolution * -1.0
		}
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
	 * Fonction simple de moyenne sur plusieurs valeurs
	 * Il peut y avoir des valeurs nulles à ne pas prendre en compte
	 * 
	 * @param valeurs
	 * @return
	 */
	private Double moyenne(Double... valeurs) {
		Double result

		if (valeurs) {
			List<Double> filterValeurs = valeurs.findAll { it != null }

			if (filterValeurs) {
				result = NumberUtils.round(filterValeurs.sum { it } / filterValeurs.size())
			}
		}

		return result
	}
}
