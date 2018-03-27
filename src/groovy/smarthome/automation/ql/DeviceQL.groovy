package smarthome.automation.ql

import smarthome.automation.Device;
import smarthome.automation.DeviceAlert;
import smarthome.automation.DeviceLevelAlert;
import smarthome.automation.DeviceValue;
import smarthome.automation.Event;
import smarthome.security.User;

/**
 * Classe utilisée dans les scripts de condition ou scénarios
 * pour requêter sur les objets smarthome. Permet de faciliter la construction
 * de conditions sur le système smarthome sans connaître toute l'API smarthome
 * 
 * Dédié à un device
 * 
 * @author Gregory
 *
 */
class DeviceQL {
	Device device
	User user
	
	
	/**
	 * La valeur au format double du device
	 * 
	 * @param deviceMac
	 * @return
	 */
	Double deviceValue() {
		DeviceValue.parseDoubleValue(device?.value)
	}
	
	
	/**
	 * Valeur device > value
	 * 
	 * @param value
	 * @return
	 */
	boolean deviceValueGt(double value) {
		deviceValue() > value
	}
	
	
	/**
	 * Valeur device >= value
	 * 
	 * @param value
	 * @return
	 */
	boolean deviceValueGe(double value) {
		deviceValue() >= value
	}
	
	/**
	 * Valeur device < value
	 *
	 * @param value
	 * @return
	 */
	boolean deviceValueLt(double value) {
		deviceValue() < value
	}
	
	
	/**
	 * Valeur device <= value
	 *
	 * @param value
	 * @return
	 */
	boolean deviceValueLe(double value) {
		deviceValue() <= value
	}
	
	
	/**
	 * Valeur device = value
	 *
	 * @param value
	 * @return
	 */
	boolean deviceValueEq(double value) {
		deviceValue() == value
	}
	
	/**
	 * Valeur device != value
	 *
	 * @param value
	 * @return
	 */
	boolean deviceValueNe(double value) {
		deviceValue() != value
	}
	
	
	/**
	 * Valeur device pas nulle
	 *
	 * @return
	 */
	boolean deviceValueNull() {
		deviceValue() == null
	}
	
	
	/**
	 * Valeur device on (> 0)
	 *
	 * @param value
	 * @return
	 */
	boolean deviceValueOn() {
		deviceValue() > 0
	}
}
