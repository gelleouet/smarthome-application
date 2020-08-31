package smarthome.automation

import org.springframework.transaction.annotation.Transactional

import smarthome.automation.deviceType.Compteur
import smarthome.automation.deviceType.CompteurEau
import smarthome.automation.deviceType.CompteurGaz
import smarthome.automation.deviceType.TeleInformation
import smarthome.core.AbstractService
import smarthome.core.AsynchronousMessage
import smarthome.core.SmartHomeException


class DeviceTypeService extends AbstractService {

	/**
	 * Enregistrement d'un domain
	 *
	 * @param domain
	 *
	 * @return domain
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DeviceType save(DeviceType deviceType, String configuration) throws SmartHomeException {
		if (configuration) {
			if (!deviceType.config.size()) {
				deviceType.addToConfig(new DeviceTypeConfig())
			}
			deviceType.config[0].data = configuration
		} else {
			deviceType.config.clear()
		}

		if (!deviceType.save()) {
			throw new SmartHomeException("Erreur enregistrement device type", deviceType)
		}

		return deviceType
	}


	/**
	 * Liste les impl Compteur
	 * 
	 * @return
	 */
	List<DeviceType> listCompteur() {
		def impls = [TeleInformation.name, Compteur.name, CompteurGaz.name, CompteurEau.name]
		return DeviceType.findAllByImplClassInList(impls, [sort: 'libelle'])
	}
}
