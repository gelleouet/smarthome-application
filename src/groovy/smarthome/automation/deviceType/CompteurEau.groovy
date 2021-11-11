package smarthome.automation.deviceType

import java.text.DecimalFormat

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
 * Enregistrement des consos eau en litre
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class CompteurEau extends Compteur {

	
	@Override
	String defaultMetaConsoUnite() {
		"L"
	}
	
	
	@Override
	void prepareForEdition(CompteurIndex command) {
		command.highindex1 = indexHigh(command.index1)
		command.lowindex1 = indexLow(command.index1)
	}
	
	
	@Override
   void bindCompteurIndex(CompteurIndex command) throws SmartHomeException {
	   if (command.lowindex1 >= 1000) {
		   throw new SmartHomeException("La part en litre ne peut pas être supérieure à 1000 !", command)
	   }
	   
	   // obligé de faire le test de index négatif ici car on va ajouter les 2 parties
	   // et peut-être perdre le négatif après addition mais ca sera faux
	   if (command.lowindex1 < 0 || command.highindex1 < 0) {
		   throw new SmartHomeException("Les index ne peuvent pas être négatifs ou nuls !", command)
	   }
	   
	   command.index1 = command.highindex1 * 1000 + command.lowindex1
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
	
	
	@Override
	String formatHtmlIndex(Double index) {
		// Les index sont enregistrés en litre.
		// On formatte l'index avec part m3 en noir, et part litre en rouge
		DecimalFormat formatHigh = new DecimalFormat("00000")
		DecimalFormat formatLow = new DecimalFormat("000")
		"""<span class="index-high-part-text">${ formatHigh.format(indexHigh(index)) }</span><span class="index-low-part-text"> ${ formatLow.format(indexLow(index)) }</span>"""
	}
	
}
