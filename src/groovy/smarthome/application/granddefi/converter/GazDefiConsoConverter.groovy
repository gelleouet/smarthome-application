/**
 * 
 */
package smarthome.application.granddefi.converter

import smarthome.core.CompteurUtils

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class GazDefiConsoConverter implements DefiConsoConverter {

	@Override
	public Double convert(Double conso) {
		CompteurUtils.convertWhTokWh(conso)
	}

	@Override
	public String unite() {
		"kWh"
	}
}
