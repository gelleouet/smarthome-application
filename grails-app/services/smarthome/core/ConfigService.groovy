package smarthome.core

import org.springframework.transaction.annotation.Transactional

import smarthome.core.AsynchronousMessage
import smarthome.core.SmartHomeException


class ConfigService extends AbstractService {


	/**
	 * Recherche multi-critères paginée
	 * 
	 * @param command
	 * @param pagination
	 * @return
	 */
	List<Config> list(ConfigCommand command, Map pagination) {
		return Config.createCriteria().list(pagination) {
			if (command.search) {
				ilike 'name', QueryUtils.decorateMatchAll(command.search)
			}

			order 'name'
		}
	}


	/**
	 * La valeur d'une Config par son nom
	 * 
	 * @param name
	 * @return
	 */
	String value(String name) {
		return Config.findByName(name)?.value
	}
}
