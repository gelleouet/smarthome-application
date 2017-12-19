package smarthome.automation

import java.util.Date;

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

import org.springframework.security.access.prepost.PreAuthorize;
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
	 * Ajoute une nouvelle valeur. Si c'est la valeur la plus récente, met à jour le device
	 * 
	 * @param deviceValue
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DeviceValue addValue(DeviceValue deviceValue) throws SmartHomeException {
		this.save(deviceValue)
		
		if (deviceValue.dateValue > deviceValue.device.dateValue) {
			deviceValue.device.dateValue = deviceValue.dateValue
			deviceValue.device.value = deviceValue.value.toString()
			super.save(deviceValue.device)
		}
		
		return deviceValue	
	}
	
	
	/**
	 * Suppression d'une valeur. Si dernière valeur, il faut ajuster la date du device 
	 * avec l'avant dernière valeur
	 * 
	 * @param deviceValue
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DeviceValue delete(DeviceValue deviceValue) throws SmartHomeException {
		// recherche d'une valeur plus récente
		DeviceValue findValue = DeviceValue.createCriteria().get {
			eq 'device', deviceValue.device
			gt 'dateValue', deviceValue.dateValue
			isNull 'name'
			maxResults 1
		}
		
		// c'était la valeur la plus récente, donc on doit rafraichir les infos générales de la sonde
		// avec la valeur précédente
		if (!findValue) {
			findValue = DeviceValue.createCriteria().get {
				eq 'device', deviceValue.device
				lt 'dateValue', deviceValue.dateValue
				isNull 'name'
				order 'dateValue', 'desc'
				maxResults 1
			}
			
			if (findValue) {
				deviceValue.device.dateValue = findValue.dateValue
				deviceValue.device.value = findValue.value.toString()
				super.save(deviceValue.device)
			}
		}
		
		deviceValue.delete()
		
		return deviceValue
	}


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
				or {
					for (String token : command.metaName.split(",")) {
						if (token == "null" || !token) {
							isNull "name"
						} else {
							eq "name", token
						}
					}
				}
			} else {
				isNull 'name'
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
	
	
	/**
	 * Trace le changement de valeur pour garder un historique
	 *
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DeviceValue traceValue(Device device) throws SmartHomeException {
		DeviceValue value, defaultValue
		Double doubleValue
		
		if (!device.attached) {
			device.attach()
		}
		
		def deviceType = device.newDeviceImpl()
		doubleValue = DeviceValue.parseDoubleValue(device.value)
		
		// trace la valeur principale du device
		if (doubleValue != null) {
			defaultValue = new DeviceValue(device: device, value: doubleValue, dateValue: device.dateValue)
			
			if (!defaultValue.save()) {
				throw new SmartHomeException("Erreur trace valeur !", defaultValue)
			}
		}
		
		// trace les metavalues
		device.metavalues?.each {
			if (it.value) {
				// si la meta est principale, pas besoin de tracer car déjà fait au niveau device
				// si meta virtuelle, pas besoin non car ca sera fait au niveau du device virtuel
				// sinon on regarde si activée au niveau meta
				if (it.trace && !it.main && !it.virtualDevice) {
					doubleValue = DeviceValue.parseDoubleValue(it.value)
					
					if (doubleValue != null) {
						value = new DeviceValue(device: device, name: it.name, value: doubleValue,
							dateValue: device.dateValue)
						
						if (!value.save()) {
							throw new SmartHomeException("Erreur trace meta valeur !", value)
						}
					}
				}
			}
		}
		
		return defaultValue
	}
}
