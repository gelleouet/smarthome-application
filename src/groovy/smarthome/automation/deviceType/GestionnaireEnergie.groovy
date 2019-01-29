package smarthome.automation.deviceType

import java.util.List;
import java.util.Map;

import smarthome.automation.ChartTypeEnum;

/**
 * Gestionnaire Energie
 * 
 * Chauffage / ECS
 * 
 * @author gregory
 *
 */
class GestionnaireEnergie extends AbstractDeviceType {

	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#planningKeys()
	 */
	@Override
	List<String> planningKeys() {
		return ['ecs', 'zone1', 'zone2', 'zone3'] 
	}

	
	/** 
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#planningValues()
	 */
	@Override
	Map planningValues() {
		return [
			"on": [label: "ECS", color: "#95b3d7"],
			"confort": [label: "Confort", color: "#fac090"],
			"eco": [label: "Eco", color: "#c2d69a"],
			"hg": [label: "Hors-gel", color: "#bfbfbf"],
			"off": [label: "OFF", color: "#ffffff"]
		]
	}
}
