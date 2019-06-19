package smarthome.automation

import org.springframework.transaction.annotation.Transactional
import smarthome.core.AbstractService
import smarthome.core.AsynchronousMessage
import smarthome.core.QueryUtils
import smarthome.core.SmartHomeException
import smarthome.security.User


class ProducteurEnergieService extends AbstractService {

	/**
	 * Recherche multi-critère et paginée
	 * 
	 * @param command
	 * @param pagination
	 * @return
	 * @throws SmartHomeException
	 */
	List<ProducteurEnergie> search(ProducteurEnergieCommand command, Map pagination) throws SmartHomeException {
		return ProducteurEnergie.createCriteria().list(pagination) {
			if (command?.libelle) {
				ilike 'libelle', QueryUtils.decorateMatchAll(command.libelle)
			}
			order 'libelle'
		}
	}
}
