package smarthome.automation

import java.util.Date;

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

import org.springframework.transaction.annotation.Transactional;

import smarthome.automation.deviceType.AbstractDeviceType;
import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.Chronometre;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class DeviceValueService extends AbstractService {

	public static final int MAX_DAY_WITHOUT_PROJECTION = 1
	public static final int MAX_DAY_PROJECTION_DAY = 31
	
	
	/**
	 * Les dernières valeurs des devices
	 * 
	 * @param devices
	 * @param pagination
	 * @return
	 */
	List<DeviceValue> lastValuesByDevices(List<Device> devices, Map pagination) {
		def values
		Chronometre chrono = new Chronometre()
		
		if (devices) {
			def deviceIds = devices.collect { it.id }
			
			values = DeviceValue.createCriteria().list(pagination) {
				device {
					'in' 'id', deviceIds
				}
				join 'device'
				join 'device.deviceType'
				join 'device.user'
				order 'dateValue', 'desc'	
			}
		}
		
		log.info "List ${devices?.size()} devices values : ${chrono.stop()}ms"
		
		return values
	}	
	
	
	/**
	 * Charge les valeurs du device sur une période
	 *
	 * @param command
	 * @return
	 * @throws SmartHomeException
	 */
	List values(DeviceChartCommand command) throws SmartHomeException {
		def values
		
		// projections automatiques pour les longues périodes (> 1 jour)
		// pour éviter trop de volumes de données
		if (command.viewMode != ChartViewEnum.day) {
			values = projectionValues(command)
		} else {
			values = DeviceValue.values(command.device, command.dateDebut(), command.dateFin(),
				command.metaName)
		}
		
		log.info "Load values ${command.device.mac} at ${command.dateChart} (${command.viewMode}) : ${values?.size()} values"
		
		return values
	}
	
	
	/**
	 * Chargement des valeurs avec projections automatiques (sur une longue période) ou imposées
	 *
	 * @param command
	 * @return
	 */
	List projectionValues(DeviceChartCommand command) {
		return DeviceValue.createCriteria().list({
			eq 'device', command.device
			between 'dateValue', command.dateDebut(), command.dateFin()
			
			if (command.metaName) {
				if (command.metaName == '') {
					isNull 'name'
				} else {
					eq 'name', command.metaName
				}
			}
			
			projections {
				// ne pas mélanger les différents types de valeurs
				groupProperty("name")
				
				// pour les devices qualitatifs, on veut une représentation de la valeur (min, max, avg)
				if (command.deviceImpl.isQualitatif()) {
					max("value")
					min("value")
					avg("value")
					sum("value")
					
					if (command.viewMode == ChartViewEnum.year) {
						groupProperty("year")
						groupProperty("monthOfYear")
						order "year"
						order "monthOfYear"
					} else {
						groupProperty("day")
						order "day"
					}
				}
				// pour les devices quantitatifs, la valeur n'est pas importante mais c'est le nombre
				// qui importe et quand dans la journée
				else {
					count("value")
					
					if (command.viewMode == ChartViewEnum.year) {
						groupProperty("year")
						groupProperty("monthOfYear")
						groupProperty("hourOfDay")
						order "year"
						order "monthOfYear"
						order "hourOfDay"
					} else {
						groupProperty("day")
						groupProperty("hourOfDay")
						order "day"
						order "hourOfDay"
					}
				}
				
				
			}
		})?.collect {
			// suivre l'ordre des projections
			def map = ['name': it[0]]
			
			if (command.deviceImpl.isQualitatif()) {
				map.max = it[1]
				map.min = it[2]
				map.avg = it[3]
				map.sum = it[4]
				
				if (command.viewMode == ChartViewEnum.year) {
					map.year = it[5]
					map.month = it[6]
				} else {
					map.day = it[5]
				}
			} else {
				map.count = it[1]
				
				if (command.viewMode == ChartViewEnum.year) {
					map.year = it[2]
					map.month = it[3]
					map.hour = it[4]
				} else {
					map.day = it[2]
					map.hour = it[3]
				}
			}
			
			return map
		}
	}
}
