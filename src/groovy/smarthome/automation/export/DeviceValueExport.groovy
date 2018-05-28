package smarthome.automation.export

import smarthome.automation.ExportCommand;
import smarthome.core.SmartHomeException;

interface DeviceValueExport {
	/**
	 * Export des données au format déterminé par l'impl et vers un flux quelconque
	 * pour un profil admin (export des devices de plusieurs users en même temps)
	 * 
	 * @param command
	 * @param outStream
	 * @throws SmartHomeException
	 */
	void exportAdmin(ExportCommand command, OutputStream outStream) throws SmartHomeException
	
	
	/**
	 * Export des données au format déterminé par l'impl et vers un flux quelconque
	 * pour l'utilisateur lui-même
	 * 
	 * @param command
	 * @param outStream
	 * @throws SmartHomeException
	 */
	void exportUser(ExportCommand command, OutputStream outStream) throws SmartHomeException
}
