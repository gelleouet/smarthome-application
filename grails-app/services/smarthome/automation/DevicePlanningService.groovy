package smarthome.automation

import org.springframework.transaction.annotation.Transactional;
import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.SmartHomeException;


class DevicePlanningService extends AbstractService {

	PlanningService planningService
	
	
	/**
	 * Retrouve tous les plannings d'un device
	 * 
	 * @param device
	 * @return
	 */
	List<DevicePlanning> listByDevice(Device device) {
		return DevicePlanning.createCriteria().list {
			eq 'device', device
			join 'planning'
		}
	}
	
	
	/**
	 * Ajout d'un planning sur un device
	 * Le planning n'est pas persisté
	 * 
	 * @param device
	 * @return
	 */
	Planning addPlanning(Device device) {
		device.clearNotBindingPlanning()
		Planning planning = new Planning(label: "Nouveau planning")
		device.addToDevicePlannings(new DevicePlanning(planning: planning))
		return planning
	}
	
	
	/**
	 * Duplique un planning pour en créer un nouveau
	 * 
	 * @param device
	 * @param status
	 * @return
	 */
	Planning copyPlanning(Device device, int status) {
		device.clearNotBindingPlanning()
		
		def devicePlanning = device.devicePlannings.find { it.status == status }
		
		Planning planning = new Planning(label: devicePlanning.planning.label + " (copie)",
			data: devicePlanning.planning.data, rule: devicePlanning.planning.rule)
		device.addToDevicePlannings(new DevicePlanning(planning: planning))
		return planning
	}
	
	
	/**
	 * Supprime un planning en fonction de sa position
	 * Le planning n'est pas persisté
	 * 
	 * @param device
	 * @param status
	 */
	void deletePlanning(Device device, int status) {
		device.clearNotBindingPlanning()
		device.devicePlannings.removeAll {
			it.status == status
		}
	}
}
