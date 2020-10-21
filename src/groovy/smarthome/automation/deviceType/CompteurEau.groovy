package smarthome.automation.deviceType

import smarthome.automation.ChartTypeEnum
import smarthome.automation.ChartViewEnum
import smarthome.automation.CompteurIndex
import smarthome.automation.DeviceChartCommand
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueDay
import smarthome.automation.DeviceValueMonth
import smarthome.automation.SaisieIndexCommand
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
	* Validation d'un nouvel index
	* Faire uniquement les controles liés au compteur car le command
	* fait déjà des controles de base (index nouveau > ancien, valeur négative)
	*
	* @param command
	*/
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
	 * Parsing des index
	 * L'index est passé en 2 valeurs (idem zone compteur) avec la part en m3 (index1) 
	 * et la part en litre (index2)
	 *
	 * @see smarthome.automation.deviceType.Compteur#parseIndex(smarthome.automation.CompteurIndex)
	 */
	@Override
	void parseIndex(CompteurIndex index) throws SmartHomeException {
		// met à jour la valeur principale
		device.value = (index.index1 as Long).toString()

		// essaie de calculer une conso sur la période si un ancien index est trouvé
		DeviceValue lastIndex = lastIndex()
		addDefaultMetas()

		if (lastIndex) {
			def conso = (index.index1 - lastIndex.value)
			device.addMetavalue(META_METRIC_NAME, [value: (conso as Long).toString()])
		}
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
	 * (non-Javadoc)
	 *
	 * @see smarthome.automation.deviceType.Compteur#addDefaultMetas()
	 */
	@Override
	protected void addDefaultMetas() {
		device.addMetavalue(META_METRIC_NAME, [value: "0", label: "Période consommation", trace: true, unite: 'L'])
	}
}
