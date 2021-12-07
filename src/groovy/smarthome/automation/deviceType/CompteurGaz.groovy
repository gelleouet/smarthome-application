package smarthome.automation.deviceType

import java.text.DecimalFormat

import org.codehaus.groovy.grails.commons.GrailsApplication

import smarthome.automation.ChartTypeEnum
import smarthome.automation.ChartViewEnum
import smarthome.automation.CompteurIndex
import smarthome.automation.DeviceChartCommand
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueDay
import smarthome.automation.DeviceValueMonth
import smarthome.core.CompteurUtils
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.core.chart.GoogleDataTableCol

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class CompteurGaz extends Compteur {

	static final String CONFIG_DEFAULT_COEF_CONVERSION = "DEFAULT_COEF_CONVERSION_GAZ"
	static final String DEFAULT_MODELE = "Gaz"
	protected static final String META_COEF_CONVERSION = "coefConversion"


	@Override
	void prepareForEdition(CompteurIndex command) {
		command.highindex1 = indexHigh(command.index1)
		command.lowindex1 = indexLow(command.index1)
	}
	
	
	/**
	 * Renvoit la part en m3 d'un index
	 *
	 * @param index
	 * @return
	 */
	Double indexHigh(Double index) {
		// 
		if (index) {
			(index / 1000.0).trunc()
		} else {
			null
		}
	}
	
	
	/**
	 * Renvoit la part en dm3 d'un index
	 *
	 * @param index
	 * @return
	 */
	Double indexLow(Double index) {
		if (index) {
			index - (indexHigh(index) * 1000)
		} else {
			null
		}
	}
	
	
	/**
	 * Coef de conversion pour le calcul des Wh
	 * 
	 * @return
	 */
	Double coefficientConversion() {
		device.metadata(META_COEF_CONVERSION)?.value?.toDouble()
	}
	
	
	@Override
   void bindCompteurIndex(CompteurIndex command) throws SmartHomeException {
	   if (command.lowindex1 >= 1000) {
		   throw new SmartHomeException("La part en dm3 ne peut pas être supérieure à 1000 !", command)
	   }
	   
	   // obligé de faire le test de index négatif ici car on va ajouter les 2 parties
	   // et peut-être perdre le négatif après addition mais ca sera faux
	   if (command.lowindex1 < 0 || command.highindex1 <= 0) {
		   throw new SmartHomeException("Les index ne peuvent pas être négatifs ou nuls !", command)
	   }
	   
	   command.index1 = command.highindex1 * 1000 + command.lowindex1
   }
	
	
	@Override
	void parseIndex(CompteurIndex compteurIndex) throws SmartHomeException {
		if (!compteurIndex.param1) {
			throw new SmartHomeException("Coefficient de conversion obligatoire pour le calcul des consommations !")
		}
		
		super.parseIndex(compteurIndex)
		
		device.addMetadata(META_COEF_CONVERSION, [value: compteurIndex.param1])
	}
	
	
	@Override
	protected Long calculConsoBetweenIndex(double newIndex, double lastIndex, String param1) throws SmartHomeException {
		Double coefConversion = param1 ? param1.toDouble() : coefficientConversion()
		
		if (coefConversion == null) {
			throw new SmartHomeException("Coefficient de conversion non renseigné !")
		}
		
		if (newIndex > lastIndex) {
			((newIndex - lastIndex) * coefConversion) as Long
		} else {
			0
		}
	}


	@Override
	protected void addDefaultMetas() {
		super.addDefaultMetas()
		device.addMetadata(META_COEF_CONVERSION, [label: 'Coefficient conversion'])
	}


	@Override
	String defaultUnite() {
		"kWh"
	}
	
	
	@Override
	String defaultMetaConsoUnite() {
		"Wh"
	}
	
	
	@Override
	public String uniteByView(ChartViewEnum view) {
		if (view == ChartViewEnum.day) {
			defaultMetaConsoUnite()
		} else {
			defaultUnite()
		}
	}


	@Override
	public Number valueByView(Number value, ChartViewEnum view) {
		if (view == ChartViewEnum.day) {
			value
		} else {
			CompteurUtils.convertWhTokWh(value)
		}
	}


	@Override
	String formatHtmlIndex(Double index) {
		// Les index sont enregistrés en dm3.
		// On formatte l'index avec part m3 en noir, et part dm3 en rouge
		DecimalFormat formatHigh = new DecimalFormat("00000")
		DecimalFormat formatLow = new DecimalFormat("000")
		"""<span class="index-high-part-text">${ formatHigh.format(indexHigh(index)) }</span><span class="index-low-part-text"> ${ formatLow.format(indexLow(index)) }</span>"""
	}
	
	
	@Override
	Double convertValueForCalculPrix(Double value) {
		CompteurUtils.convertWhTokWh(value)
	}
	
	
	@Override
	boolean isConnected(GrailsApplication grailsApplication) {
		device.label == grailsApplication.config.grdf.compteurLabel
	}
	
}
