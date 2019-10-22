package smarthome.rule


import smarthome.application.Defi
import smarthome.application.DefiEquipeParticipant
import smarthome.core.SmartHomeException;



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
 * @author gregory
 *
 */
class DefiCalculRule implements Rule<Defi, Defi> {

	static final Long CHAUFFAGE_ELEC_ID = 1L
	static final Long CHAUFFAGE_GN_ID = 2L
	
	Map parameters
	
	
	@Override
	public Defi execute(Defi defi) throws SmartHomeException {
		// récupère la liste des participants "à plat" depuis les paramètres
		// de la règle
		List<DefiEquipeParticipant> participants = parameters.participants
		
		// regroupement des participants par sous-catégorie
		Map participantGroup = participants.groupBy { 
			if (it.house?.chauffage?.id == CHAUFFAGE_ELEC_ID) {
				return "ELEC"
			} else if (it.house?.chauffage?.id == CHAUFFAGE_GN_ID) {
				return "GN"
			}
		}
		
		return defi
	}
	
	
	/**
	 * Calcul des notes seulement pour les catégories US
	 * La liste des participants est déjà filtrée sur cette catégorie
	 * 
	 * @param participants
	 */
	private void calculNoteUS(List<DefiEquipeParticipant> participants) {
		
	}

}
