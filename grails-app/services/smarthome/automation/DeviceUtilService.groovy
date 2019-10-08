package smarthome.automation

import groovyx.gpars.GParsPool

import java.util.Date

import org.springframework.transaction.annotation.Transactional

import smarthome.core.AbstractService
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException


class DeviceUtilService extends AbstractService {
	DeviceValueService deviceValueService
	DeviceService deviceService


	/**
	 * Aggrège les données d'un jour et du mois
	 *
	 * @param deviceId
	 * @param dateReference
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void aggregateSingleDay(Serializable deviceId, Date dateReference) throws SmartHomeException {
		// prepare le device
		Device device = deviceService.findById(deviceId as Long)
		device.newDeviceImpl()

		log.info "Aggregation partielle jour device ${device.id}:${device.label}"

		deviceValueService.aggregateValueDay(device, dateReference)
		deviceValueService.aggregateValueMonth(device, dateReference)
	}


	/**
	 * Aggrège les données de tous les jours d'un mois et du mois
	 *
	 * @param deviceId
	 * @param dateReference
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void aggregateWholeMonth(long deviceId, Date dateReference) throws SmartHomeException {
		// prepare le device
		Device device = deviceService.findById(deviceId)
		device.newDeviceImpl()

		log.info "Aggregation partielle mois device ${device.id}:${device.label}"

		Date dateDebut = DateUtils.firstDayInMonth(dateReference)
		List dates = []

		dateDebut.upto(DateUtils.lastDayInMonth(dateReference)) {
			dates << it
		}

		GParsPool.withPool(2) {
			dates.eachParallel {
				deviceValueService.aggregateValueDay(device, it)
			}
		}

		deviceValueService.aggregateValueMonth(device, dateReference)
	}


	/**
	 * Aggrège les données depuis le début
	 *
	 * @param deviceId
	 * 
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void aggregateWholeDevice(long deviceId) throws SmartHomeException {
		// prepare le device
		Device device = deviceService.findById(deviceId)
		device.newDeviceImpl()

		log.info "Aggregation totale device ${device.id}:${device.label}"

		Date dateDebut = DeviceValue.createCriteria().get {
			eq 'device', device
			projections {
				min 'dateValue'
			}
		}

		dateDebut.clearTime()

		// calcul des jours et des mois
		List days = []
		List months = []
		int lastMonth = -1

		dateDebut.upto(new Date().clearTime()) {
			days << it
			if (it[Calendar.MONTH] != lastMonth) {
				lastMonth = it[Calendar.MONTH]
				months << it
			}
		}

		// aggrégation par jour
		GParsPool.withPool(3) {
			days.eachParallel {
				deviceValueService.aggregateValueDay(device, it)
			}
		}

		// aggrégation par mois
		GParsPool.withPool(2) {
			days.eachParallel {
				deviceValueService.aggregateValueMonth(device, it)
			}
		}
	}
}
