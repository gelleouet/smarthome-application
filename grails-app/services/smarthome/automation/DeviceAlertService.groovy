package smarthome.automation



import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.ExchangeType;
import smarthome.core.SmartHomeException;


class DeviceAlertService extends AbstractService {

	
	/**
	 * Suppression d'une alerte. L'opération n'est pas persitée
	 *
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	Device deleteLevelAlert(Device device, int status) throws SmartHomeException {
		device.clearNotBindingLevelAlert()
		device.levelAlerts?.removeAll {
			it.status == status
		}
		return device
	}
	
	
	/**
	 * Ajout d'une alerte. L'opération n'est pas persitée
	 * 
	 * @param Device
	 * @return
	 * @throws SmartHomeException
	 */
	Device addLevelAlert(Device device) throws SmartHomeException {
		device.clearNotBindingLevelAlert()
		device.addToLevelAlerts(new DeviceLevelAlert(tempoAlerte: 1))
		return device
	}
	
	
	/**
	 * Compte le nombre de devices configurés avec une alerte monitoring
	 * 
	 * @return
	 */
	long countDeviceLevelAlertMonitoring() {
		return DeviceLevelAlert.createCriteria().get {
			eq 'level', LevelAlertEnum.monitoring
			projections {
				count("id")
			}
		}	
	}
	
	
	/**
	 * Liste toutes les configs d'alerte monitoring
	 * 
	 * @param pagination
	 * @return
	 */
	List<DeviceLevelAlert> listDeviceLevelAlertMonitoring(Map pagination) {
		return DeviceLevelAlert.createCriteria().list(pagination) {
			eq 'level', LevelAlertEnum.monitoring
			join 'device'
			order 'id'
		}
	}
	
	
	/**
	 * Monitoring des devices et déclenchement d'une alerte si pas de valeur récente
	 * Renvoit les devices nouvellement en alerte et ceux qui ne le sont plus
	 *
	 * @param dateMonitoring
	 * @param pagination
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousMessage(exchange = "smarthome.automation.deviceAlertService.processMonitoring", exchangeType =  ExchangeType.FANOUT)
	List<DeviceAlert> processMonitoring(Date dateMonitoring, Map pagination) throws SmartHomeException {
		List<DeviceAlert> deviceAlerts = []
		
		// recherche des sondes actives
		List<DeviceLevelAlert> levelAlerts = listDeviceLevelAlertMonitoring(pagination)
		
		for (DeviceLevelAlert levelAlert : levelAlerts) {
			// récupère la dernière alerte du device
			DeviceAlert alert = levelAlert.device.lastDeviceAlert()
			
			// Vérifie si alerte monitoring sur device
			if (levelAlert.isAlerteMonitoring(dateMonitoring)) {
				// ouvre une nouvelle alerte si la dernière est fermée ou inexistante
				if (!alert?.isOpen()) {
					alert = new DeviceAlert(device: levelAlert.device, level: LevelAlertEnum.monitoring)
					deviceAlerts << alert
				} else {
					// alerte déjà ouverte mais peut-être sur un autre type d'alerte
					// il faut la réintialiser
					if (alert.level != LevelAlertEnum.monitoring) {
						alert.reset(LevelAlertEnum.monitoring) 
						deviceAlerts << alert
					} 
					// l'alerte n'a pas été checkée par l'utilisateur.
					// il faut gérer les relances pour ne pas envoyer un mail à appel de la méthode
					// mais seulement tous les N x tempo 
					else if (!alert.isViewed()) {
						int oldRelance = alert.relance
						this.calculRelance(alert, levelAlert.tempo)
						
						if (oldRelance != alert.relance) {
							deviceAlerts << alert
						}
					}
				}
			} else {
				// aucune alerte détectée :
				// si la dernière alerte est toujours en cours, on la cloture
				// seulement si c'était une alerte monitoring
				if (alert?.isOpen() && alert?.level == LevelAlertEnum.monitoring) {
					alert.close()
				}
			}
			
			// enregistre les éventuels changements 
			if (alert) {
				this.save(alert)
			}
		}
		
		return deviceAlerts
	}
	
	
	/**
	 * Process des alertes
	 * 
	 * @param alert
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousMessage(exchange = "smarthome.automation.deviceAlertService.processDeviceAlert", exchangeType =  ExchangeType.FANOUT)
	DeviceAlert processDeviceAlert(DeviceAlert alert) throws SmartHomeException {
		// on ne fait rien. si workflow branché, c'est lui qui va faire le traitement
		return alert
	}
	
	
	/**
	 * Inverse la marque viewed sur une alarte.
	 * Cela ne marche que pour les alertes ouvertes
	 * 
	 * @param alert
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DeviceAlert markViewed(DeviceAlert alert) throws SmartHomeException {
		if (!alert.isOpen()) {
			throw new SmartHomeException("Alert is not open !")
		}
		
		if (alert.status == LevelStatusEnum.open) {
			alert.status = LevelStatusEnum.viewed
		} else {
			alert.status = LevelStatusEnum.open
		}
		
		return this.save(alert)
	}
	
	
	/**
	 * Calcul nombre de relance sur l'alerte depuis le début de sa création
	 *
	 * @param alert
	 * @return
	 * @throws SmartHomeException
	 */
	DeviceAlert calculRelance(DeviceAlert alert, int tempo) throws SmartHomeException {
		// calcul du temps écoulé depuis le début de l'alerte
		Date now = new Date()
		long minuteEllape = (now.getTime() - alert.dateDebut.getTime()) / 1000 / 60
		
		// les relances ne commencent qu'à la 3e période car la 1ère est autorisée par la tempo,
		// la 2e est le 1er envoi de l'alerte, la 3e est donc la 1ere relance
		int nbPeriode = minuteEllape / tempo
		
		if (nbPeriode >= 3) {
			alert.relance = nbPeriode - 2
		} else {
			alert.relance = 0
		}
		
		return alert
	}
	
	
	/**
	 * Compte le nombre d'alertes en cours
	 *
	 * @param userId
	 * @param maxEvent
	 * @param maxDay
	 * @return
	 * @throws SmartHomeException
	 */
	long countOpenAlert(Long userId) throws SmartHomeException {
		if (!userId) {
			throw new SmartHomeException("userId required !")
		}
		
		return DeviceAlert.createCriteria().count() {
			device {
				user {
					idEq(userId)
				}
			}
			ne 'status', LevelStatusEnum.closed
		}
	}
	
	
	/**
	 * Recherche multi-critère
	 * 
	 * @param command
	 * @param pagination
	 * @return
	 * @throws SmartHomeException
	 */
	List<DeviceAlert> search(DeviceAlertCommand command, Map pagination) throws SmartHomeException {
		if (!command.userId) {
			throw new SmartHomeException("userId required !")
		}
		
		return DeviceAlert.createCriteria().list(pagination) {
			device {
				user {
					idEq(command.userId)
				}
			}
			
			if (command.open != null) {
				if (command.open) {
					ne 'status', LevelStatusEnum.closed
				} else {
					eq 'status', LevelStatusEnum.closed
				}
			}
			
			if (command.id) {
				idEq command.id
			}
			
			order "dateDebut", "desc"
		}
	} 
}
