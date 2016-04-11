package smarthome.rule;

import smarthome.core.SmartHomeException;

/**
 * Règle métier.
 * 
 * La règle prend un objet en entrée (I) et rend un résultat (O) en sortie
 * 
 * @author gregory
 *
 * @param <I>
 * @param <O>
 */
public interface Rule<I, O> {
	
	/**
	 * Exécute de la règle sur l'objet entrant.
	 * 
	 * @param object
	 * @return O
	 */
	O execute(I object) throws SmartHomeException;
}
