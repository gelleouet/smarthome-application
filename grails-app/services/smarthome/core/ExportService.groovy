package smarthome.core

import javax.servlet.ServletResponse

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

import smarthome.automation.ExportCommand
import smarthome.automation.export.DeviceValueExport
import smarthome.core.AbstractService;
import smarthome.core.SmartHomeException;


class ExportService extends AbstractService {
	
	
	/**
	 * Export des données
	 *
	 * @param command
	 * @param response
	 *
	 * @throws SmartHomeException
	 */
	void export(ExportCommand command, ServletResponse response) throws SmartHomeException {
		// Vérifs communes avant de lancer une impl
		if (!command.dateDebut || !command.dateFin) {
			throw new SmartHomeException("Veuillez renseigner les dates d'export !", command)
		}

		if (command.dateFin < command.dateDebut) {
			throw new SmartHomeException("Date fin incorrecte !", command)
		}

		// pour des raison de perf, on n'autorise pas d'export > 1 mois
		if (command.dateFin - command.dateDebut > 32) {
			throw new SmartHomeException("Export limité à maximum 1 mois !", command)
		}
		
		if (!command.exportImpl) {
			throw new SmartHomeException("Veuillez renseigner le modèle d'export !", command)
		}


		DeviceValueExport deviceValueExport = (DeviceValueExport) ClassUtils.forNameInstance(command.exportImpl)
		ApplicationUtils.autowireBean(deviceValueExport)
		
		log.info("Start export ${deviceValueExport.class}...")

		deviceValueExport.init(command, response)
		
		// on s'assure que le stream est bien fermé à la fin de l'export et en cas d'erreur
		response.outputStream.withStream {
			try {
				deviceValueExport.export(command, it)
			} catch (Exception ex) {
				log.error("Export ${deviceValueExport.class} : ${ex.message}")
				throw new SmartHomeException(ex.message, command)
			}
		}
	}
}
