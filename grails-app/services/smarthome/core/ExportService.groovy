package smarthome.core

import javax.servlet.ServletResponse

import smarthome.automation.SupervisionCommand

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class ExportService extends AbstractService {
	
	
	/**
	 * Les implémentations export sous forme de Map
	 * key = classe impl
	 * value = Libellé de l'export
	 * 
	 * @return Map
	 */
	Map exportImpls() {
		[
			(smarthome.core.exportImpl.UserIndexExcelDeviceValueExport.name): 'Export profils et données',
			(smarthome.core.exportImpl.UserExcelDeviceValueExport.name): 'Export profils'
		]
	}
	

	/**
	 * Export des données
	 *
	 * @param command
	 * @param response
	 *
	 * @throws SmartHomeException
	 */
	void export(SupervisionCommand command, ServletResponse response) throws SmartHomeException {
		// Vérifs communes avant de lancer une impl
		if (!command.dateDebut || !command.dateFin) {
			throw new SmartHomeException("Veuillez renseigner les dates d'export !", command)
		}

		if (!command.adminId) {
			throw new SmartHomeException("L'administrateur doit être renseigné !", command)
		}

		if (command.dateFin < command.dateDebut) {
			throw new SmartHomeException("Date fin incorrecte !", command)
		}
		
		if (!command.exportImpl) {
			throw new SmartHomeException("Veuillez sélectionner le modèle d'export !", command)
		}


		// création impl pour l'export
		DeviceValueExport deviceValueExport = (DeviceValueExport) ClassUtils.forNameInstance(command.exportImpl)
		ApplicationUtils.autowireBean(deviceValueExport)
		
		log.info("Start export ${deviceValueExport.class}...")
		deviceValueExport.init(command, response)

		// on s'assure que le stream est bien fermé à la fin de l'export et en cas d'erreur
		response.outputStream.withStream {
			try {
				deviceValueExport.export(command, it)
			} catch (Exception ex) {
				log.error("Export ${deviceValueExport.class} : ${ex.message}", ex)
				throw new SmartHomeException(ex.message, command)
			}

		}
	}
}
