package smarthome.core

import org.springframework.transaction.annotation.Transactional


/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class ImportService extends AbstractService {
	
	
	/**
	 * Les implémentations import sous forme de Map
	 * key = classe impl
	 * value = Libellé de l'import
	 * 
	 * @return Map
	 */
	Map importImpls() {
		[
			(smarthome.core.importImpl.UserIndexExcelDeviceValueImport.name): 'Import profils et index'
		]
	}
	

	/**
	 * Import de données
	 * 
	 * @param command
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void importData(ImportCommand command) throws SmartHomeException {
		if (!command.data) {
			throw new SmartHomeException("Aucune donnée !", command)
		}
		
		if (!command.importImpl) {
			throw new SmartHomeException("Veuillez sélectionner le modèle d'import !", command)
		}


		// création impl pour l'import
		DeviceValueImport deviceValueImportImpl = (DeviceValueImport) ClassUtils.forNameInstance(command.importImpl)
		ApplicationUtils.autowireBean(deviceValueImportImpl)
		
		log.info("Start import ${deviceValueImportImpl.class}...")
		
		try {
			deviceValueImportImpl.importData(command)
		} catch (Exception ex) {
			log.error("Import ${deviceValueImportImpl.class}", ex)
			throw new SmartHomeException(ex)
		}
	}
}
