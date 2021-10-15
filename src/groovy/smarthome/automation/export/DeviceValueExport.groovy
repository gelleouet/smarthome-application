package smarthome.automation.export

import javax.servlet.ServletResponse;
import smarthome.automation.SupervisionCommand
import smarthome.core.SmartHomeException;


interface DeviceValueExport {
	/**
	 * Préparation export depuis un flux HTTP
	 * 
	 * @param command
	 * @param response
	 */
	void init(SupervisionCommand command, ServletResponse response)
	
	
	/**
	 * Export des données au format déterminé par l'impl et vers un flux quelconque
	 * 
	 * @param command
	 * @param outStream
	 * @throws SmartHomeException
	 */
	void export(SupervisionCommand command, OutputStream outStream) throws Exception
}
