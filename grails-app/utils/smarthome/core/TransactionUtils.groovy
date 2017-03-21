package smarthome.core

import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Méthode utilitaire sur transactions
 * 
 * @author Gregory
 *
 */
class TransactionUtils {

	/**
	 * Exécution d'un code après commit de la transaction si elle existe
	 * Sinon exécuté immédiatement
	 * 
	 * @param closure
	 */
	static void executeAfterCommit(Closure closure) {
		if (!TransactionSynchronizationManager.isSynchronizationActive()) {
			closure()
		} else {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					closure()
				}
			})
		}
	}

}
