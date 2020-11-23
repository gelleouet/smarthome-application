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
class GrandDefi2019CalculRule implements Rule<Defi, Defi> {

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
		List equipeProfils = []

		// regroupement des participants par catégorie (profil du user)
		// et sous-catégorie (mode de chauffage en 3 groupes : ELEC, GN, US)
		// on doit au maximum avoir 9 groupes : 3 profils x 3 catégories
		Map participantsGroup = groupParticipant(participants)

		// calcul des moyennes d'évolution pour les catégories GAZ et ELEC
		// pour chaque groupe
		// ces moyennes servent à calculer "l'économie" par rapport à l'évolution
		// du participant
		moyenneGeneraleGroup = participantsGroup.collectEntries { key, values ->
			// ATTENTION : pas de calcul elec pour les GN car déjà pris en compte dans US
			// et pas de calcul gaz pour les autres
			def result = [electricite: null, gaz: null]

			if (key.endsWith(SUFFIX_GAZ_NATUREL)) {
				result.gaz = moyenneEvolution(values, DefiCompteurEnum.gaz)
			} else {
				result.electricite = moyenneEvolution(values, DefiCompteurEnum.electricite)
			}

			[(key): result]
		}

		// calcul des notes au niveau participant. Les notes sont calculées par catégorie
		// et le calcul dépend de la catégorie. Si ELEC ou GN, les notes dépendent
		// de la moyenne générale (par catégorie et profil). Sinon si US, la note
		// c'est simplement l'évolution
		// la note globale, c'est la moyenne des notes elec et gaz
		participantsGroup.each { key, value ->
			value.each { participant ->
				// ATTENTION : pas de calcul elec pour les GN car déjà pris en compte dans US
				// et pas de calcul gaz pour les autres
				if (key.endsWith(SUFFIX_GAZ_NATUREL)) {
					participant.economie_gaz = noteParticipant(key, participant, DefiCompteurEnum.gaz)
				} else {
					participant.economie_electricite = noteParticipant(key, participant, DefiCompteurEnum.electricite)
				}
				// on ne peut pas faire le calcul du global car le participant GN
				// appartient aussi au groupe US, donc dans cette boucle il peut
				// encore manquer l'une ou l'autre des valeurs pour faire la moyenne
			}
		}
		participants.each { participant ->
			participant.economie_global = NumberUtils.moyenne(participant.economie_electricite,
					participant.economie_gaz)
		}

		// calcul des notes par équipe / profil
		// il s'agit simplement de faire les moyennes des économies des participants
		// à chaque niveau : catégorie puis équipe
		defi.equipes.each { DefiEquipe equipe ->
			if (!equipe.libelle.contains("ALEC")) {
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
			// pas de classement elec pour le profil gaz
			// et inversement
			// on effectue le classement global que les catégories US et ELEC
			// car la catégorie GAZ est incluse dans US
			if (key.endsWith(SUFFIX_GAZ_NATUREL)) {
				classementList(value, DefiCompteurEnum.gaz)
			} else {
				classementList(value, DefiCompteurEnum.electricite)
				classementList(value, DefiCompteurEnum.global)
			}
		}


		return defi
	}


	/**
	 * Calcul le groupe (profil + chauffage) d'un participant
	 * 
	 * @param participant
	 * @return
	 */
	String groupKeyParticipant(DefiEquipeParticipant participant) {
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
			participant.groupKey = groupKey

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
	 * Calcul de la moyenne générale des évolutions d'un groupe pour un type de compteur
	 * Seul les éléments dont la valeur n'est pas nulle sont comptabilisés
	 * 
	 * @param resultatList
	 * @param compteurType
	 * @return
	 */
	private Double moyenneEvolution(def resultatList, DefiCompteurEnum compteurType) {
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
	private Double moyenneEconomie(def resultatList, DefiCompteurEnum compteurType) {
		def filterList = resultatList.findAll { it."economie_${compteurType}"() != null }
		return filterList ? NumberUtils.round(filterList.sum { it."economie_${compteurType}"() } / filterList.size()) : null
	}


	/**
	 * Calcule la note d'un participant en fonction de son évolution et de sa
	 * catégorie
	 * 
	 * @param groupKey
	 * @param participant
	 * @param compteurType
	 * @return
	 */
	private Double noteParticipant(String groupKey, def participant, DefiCompteurEnum compteurType) {
		Double note

		if (participant."evolution_${compteurType}"() != null) {
			note = participant."evolution_${compteurType}"()

			if (! groupKey.endsWith(SUFFIX_USAGE_SPECIFIQUE)) {
				// si il y a ce profil sur cette équipe, c'est que forcément
				// il était présent dans la liste des participants et doit
				// forcément apparaître dans les groupes de moyenne. S'il n'y
				// est pas, c'est que problème quelque part
				if (moyenneGeneraleGroup[(groupKey)] == null || moyenneGeneraleGroup[(groupKey)][(compteurType.toString())] == null) {
					throw new SmartHomeException("La moyenne ${compteurType} du groupe ${groupKey} n'existe pas !")
				}

				note = note - moyenneGeneraleGroup[(groupKey)][(compteurType.toString())]

				// on conserve la valeur
				participant."moyenne_${compteurType}" = moyenneGeneraleGroup[(groupKey)][(compteurType.toString())]
			}
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