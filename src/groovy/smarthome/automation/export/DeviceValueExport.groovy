package smarthome.automation.export

import javax.servlet.ServletResponse;

import smarthome.automation.ExportCommand;
import smarthome.core.SmartHomeException;

interface DeviceValueExport {
	void initExportAdmin(ExportCommand command, ServletResponse response)
	void initExportUser(ExportCommand command, ServletResponse response)
	
	/**
	 * Export des données au format déterminé par l'impl et vers un flux quelconque
	 * pour un profil admin (export des devices de plusieurs users en même temps)
	 * 
	 * Sur cet export multiple, la liste des IDs utilisateurs est déjà calculée dans l'objet command 
	 * 
	 * @param command
	 * @param outStream
	 * @throws SmartHomeException
	 */
	void exportAdmin(ExportCommand command, OutputStream outStream) throws Exception
	
	
	/**
	 * Export des données au format déterminé par l'impl et vers un flux quelconque
	 * pour l'utilisateur lui-même
	 * 
	 * @param command
	 * @param outStream
	 * @throws SmartHomeException
	 */
	void exportUser(ExportCommand command, OutputStream outStream) throws Exception
}
