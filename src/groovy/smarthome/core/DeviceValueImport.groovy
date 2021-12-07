package smarthome.core

import javax.servlet.ServletResponse;
import smarthome.automation.SupervisionCommand
import smarthome.core.SmartHomeException;


interface DeviceValueImport {
	
	/**
	 * Import des données au format déterminé par l'impl
	 * 
	 * @param command
	 * @throws SmartHomeException
	 */
	void importData(ImportCommand command) throws Exception
}
