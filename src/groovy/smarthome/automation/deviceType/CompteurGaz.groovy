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
	protected static final String META_COEF_CONVERSION = "coefConversion"


	/**
	 * Prépare l'objet pour édition dans formulaire
	 *
	 * @param command
	 */
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
		if (index) {
			(index / 1000.0).trunc()
		} else {
			null
		}
	}
	
	
	/**
	 * Renvoit la part en litre d'un index
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
	* Validation d'un nouvel index
	* Faire uniquement les controles liés au compteur car le command
	* fait déjà des controles de base (index nouveau > ancien, valeur négative)
	*
	* @param command
	*/
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
	
	
	/**
	 * (non-Javadoc)
	 *
	 * @see smarthome.automation.deviceType.Compteur#parseIndex(smarthome.automation.CompteurIndex)
	 */
	@Override
	void parseIndex(CompteurIndex index) throws SmartHomeException {
		if (!index.param1) {
			throw new SmartHomeException("Coefficient de conversion obligatoire pour le calcul en Wh !")
		}

		// met à jour la valeur principale
		device.value = (index.index1 as Long).toString()

		// essaie de calculer une conso sur la période si un ancien index est trouvé
		DeviceValue lastIndex = lastIndex()
		addDefaultMetas()

		if (lastIndex) {
			// Cas ici si tout l'index est saisi (avec la part en dm3) => on obtient de suite les Wh
			// coef conversion = XX kWh pour 1m3
			def conso = (index.index1 - lastIndex.value) * index.param1.toDouble()
			device.addMetavalue(META_METRIC_NAME, [value: (conso as Long).toString()])
		}

		device.addMetadata(META_COEF_CONVERSION, [value: index.param1])
	}


	/** 
	 * (non-Javadoc)
	 *
	 * @see smarthome.automation.deviceType.Compteur#addDefaultMetas()
	 */
	@Override
	protected void addDefaultMetas() {
		device.addMetavalue(META_METRIC_NAME, [value: "0", label: "Période consommation", trace: true, unite: 'Wh'])
		device.addMetadata(META_COEF_CONVERSION, [label: 'Coefficient conversion'])
	}


	/**
	 * Unité pour les widgets (peut être différent)
	 *
	 * @return
	 */
	@Override
	String defaultUnite() {
		"kWh"
	}
	
	
	/**
	 * Unité sur les graphes en fonction vue
	 */
	@Override
	public String uniteByView(ChartViewEnum view) {
		if (view == ChartViewEnum.day) {
			"Wh"
		} else {
			"KWh"
		}
	}


	/**
	 * 
	 */
	@Override
	public Number valueByView(Number value, ChartViewEnum view) {
		if (view == ChartViewEnum.day) {
			value
		} else {
			CompteurUtils.convertWhTokWh(value)
		}
	}


	/**
	 * Les index sont enregistrés en dm3.
	 * On formatte l'index avec part m3 en noir, et part dm3 en rouge
	 * 
	 */
	@Override
	String formatHtmlIndex(Double index) {
		DecimalFormat formatHigh = new DecimalFormat("00000")
		DecimalFormat formatLow = new DecimalFormat("000")
		"""<span class="index-high-part-text">${ formatHigh.format(indexHigh(index)) }</span><span class="index-low-part-text"> ${ formatLow.format(indexLow(index)) }</span>"""
	}
	
	
	/**
	 * Conversion des valeurs enregistrés pour le calcul des prix
	 *
	 * @param value
	 * @return
	 */
	@Override
	Double convertValueForCalculPrix(Double value) {
		CompteurUtils.convertWhTokWh(value)
	}
	
	
	/**
	 * Vrai si compteur connecté sur DataConnect
	 *
	 * @return
	 */
	boolean isConnected(GrailsApplication grailsApplication) {
		device.label == grailsApplication.config.grdf.compteurLabel
	}
	
}
