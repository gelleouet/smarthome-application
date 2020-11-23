/**
 * 
 */
package smarthome.application.granddefi.model

import smarthome.core.ClassUtils

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
}
