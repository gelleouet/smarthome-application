package smarthome.automation.export

import javax.servlet.ServletResponse;
import smarthome.automation.ExportCommand;
import smarthome.core.SmartHomeException;


interface DeviceValueExport {
	
	
	/**
	 * Un libellé descriptif
	 * 
	 * @return
	 */
	String libelle()
	
	
	/**
	 * Init avant ouverture du flux
	 * Préparation de la ServletResponse
	 * 
	 * @param command
	 * @param response
	 * @throws Exception
	 */
	void init(ExportCommand command, ServletResponse response) throws Exception
	
	
	/**
	 * Export des données au format déterminé par l'impl et vers un flux quelconque
	 * 
	 * @param command
	 * @param outStream
	 * @throws SmartHomeException
	 */
	void export(ExportCommand command, OutputStream outStream) throws Exception
	
}
