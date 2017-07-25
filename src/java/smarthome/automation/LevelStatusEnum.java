package smarthome.automation;

/**
 * Statut d'une alerte (en cours, annulée, terminée, etc...)
 *  
 * @author Gregory
 *
 */
public enum LevelStatusEnum {
	/**
	 * 1er statut de l'alerte
	 */
	open,
	/**
	 * Alerte terminée normalement
	 */
	closed,
	/**
	 * Alerte prise en compte par utilisateur
	 * Désactive l'envoi des notifications
	 */
	viewed;
	
}
