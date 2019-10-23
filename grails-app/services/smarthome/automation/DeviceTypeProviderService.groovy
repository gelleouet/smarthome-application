package smarthome.automation

import org.springframework.transaction.annotation.Transactional
import smarthome.core.AbstractService
import smarthome.core.SmartHomeException


class DeviceTypeProviderService extends AbstractService {

	/**
	 * Liste les providers pour une implémentation d'objets
	 * 
	 * @param deviceTypeClass
	 * @return
	 */
	List<DeviceTypeProvider> listByDeviceTypeImpl(String deviceTypeImpl) {
		DeviceTypeProvider.createCriteria().list() {
			deviceType {
				eq 'implClass', deviceTypeImpl
			}

			order 'libelle'
		}
	}
}
