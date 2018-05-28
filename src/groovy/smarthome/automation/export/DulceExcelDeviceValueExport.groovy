/**
 * 
 */
package smarthome.automation.export

import java.io.OutputStream;

import smarthome.automation.ExportCommand;
import smarthome.core.SmartHomeException;

/**
 * @author Gregory
 *
 */
class DulceExcelDeviceValueExport implements DeviceValueExport {

	/**
	 * Export des données au format Excel selon CCTP DULCE
	 * 
	 * @see smarthome.automation.export.DeviceValueExport#exportAdmin(smarthome.automation.ExportCommand, java.io.OutputStream)
	 */
	@Override
	void exportAdmin(ExportCommand command, OutputStream outStream) throws SmartHomeException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Export des données au format Excel selon CCTP DULCE
	 * 
	 * @see smarthome.automation.export.DeviceValueExport#exportUser(smarthome.automation.ExportCommand, java.io.OutputStream)
	 */
	@Override
	void exportUser(ExportCommand command, OutputStream outStream) throws SmartHomeException {
		throw new SmartHomeException("Fonctionnalité pas implémentée !", command)
		
	}

}
