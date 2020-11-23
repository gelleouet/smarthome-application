package smarthome.application;

import smarthome.application.granddefi.converter.DefiConsoConverter;
import smarthome.application.granddefi.converter.EauDefiConsoConverter;
import smarthome.application.granddefi.converter.ElecDefiConsoConverter;
import smarthome.application.granddefi.converter.GazDefiConsoConverter;
import smarthome.application.granddefi.converter.NoneDefiConsoConverter;

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
public enum DefiCompteurEnum {
	electricite("compteur", new ElecDefiConsoConverter()),
	gaz("compteurGaz", new GazDefiConsoConverter()),
	eau("compteurEau", new EauDefiConsoConverter()),
	/**
	 * virtuel non associé à un compteur Utilisé pour l'accès dynamique aux
	 * propriétés de la classe @see AbstractDefiResultat
	 */
	energie("", new ElecDefiConsoConverter()),
	global("", new NoneDefiConsoConverter());
	
	
	private String property;
	private DefiConsoConverter converter;
	
	
	private DefiCompteurEnum(String property, DefiConsoConverter converter) {
		this.property = property;
		this.converter = converter;
	}
	
	
	/**
	 * @return the converter
	 */
	public DefiConsoConverter getConverter() {
		return converter;
	}


	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}
	
	
}
