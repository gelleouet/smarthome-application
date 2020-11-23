/**
 * 
 */
package smarthome.application.granddefi.converter

import smarthome.core.CompteurUtils

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class EauDefiConsoConverter implements DefiConsoConverter {

	@Override
	public Double convert(Double conso) {
		conso ?: 0
	}

	@Override
	public String unite() {
		"L"
	}
}
