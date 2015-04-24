package smarthome.automation;

public enum CapteurTypeEnum {
	/**
	 * Compteur sur le nombre de données remontées. 
	 * La valeur de la donnée n'a pas d'importance puisque toujours la même (o ou 1, etc..)
	 * Opérations possibles : count
	 */
	event,
	/**
	 * Mesure qualitative qui ne peut pas cumulée (Ex : valeur instantanée, température)
	 * Opérations possibles : max, min, avg
	 */
	qualitative,
	/**
	 * Mesure quantitative qui peut être cumulée (Ex : consommation sur une période) avec d'autres mesures pour avoir la valeur
	 * totale sur une plus grande période
	 * Opérations possible : sum, max, min, avg
	 */
	quantitative;
}
