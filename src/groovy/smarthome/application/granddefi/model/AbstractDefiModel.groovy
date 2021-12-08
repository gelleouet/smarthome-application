/**
 * 
 */
package smarthome.application.granddefi.model

import javax.servlet.ServletResponse

import smarthome.application.DefiCommand
import smarthome.core.ClassUtils
import smarthome.core.SmartHomeException

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
abstract class AbstractDefiModel implements DefiModel {
	
	@Override
	final String viewPath() {
		ClassUtils.beanName(this)
	}
	
	
	@Override
	final String ruleName() {
		String ruleName = "smarthome.rule." + this.class.simpleName.replace("Model", "CalculRule")
		return ruleName
	}
	
	
	@Override
	void export(DefiCommand command, Map modeles, ServletResponse response) throws SmartHomeException {
		throw new SmartHomeException("Fonction export non disponible !")
	}
}
