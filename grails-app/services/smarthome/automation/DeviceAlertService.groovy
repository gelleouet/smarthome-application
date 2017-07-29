package smarthome.automation


import org.springframework.transaction.annotation.Transactional;
import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.ExchangeType;
import smarthome.core.SmartHomeException;


class DeviceAlertService extends AbstractService {

	private static final MAX_VALUES_ALERT_SEARCH = 50
	
	
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
	 * Les configs alertes au niveau valeur d'un device
	 * 
	 * @param device
	 * @return
	 */
	List<DeviceLevelAlert> listDeviceLevelAlertValue(Device device) {
		List<DeviceLevelAlert> levelAlerts = DeviceLevelAlert.createCriteria().list() {
			eq 'device', device
			join 'device'
		}
		
		// Tri des alertes par criticité décroissante
		return levelAlerts.sort {
			-it.level.criticite
		}
	}
	
	
	/**
	 * Monitoring d'un device et déclenchement d'une alerte si pas de valeur récente
	 *
	 * @param dateMonitoring
	 * @param pagination
	 * 
	 * @return non null si nouvelle alerte à avertir
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousMessage(exchange = "smarthome.automation.deviceAlertService.processMonitoring", exchangeType =  ExchangeType.FANOUT)
	DeviceAlert processMonitoring(DeviceLevelAlert levelAlert, Date dateMonitoring) throws SmartHomeException {
		// récupère la dernière alerte du device
		DeviceAlert alert = levelAlert.device.lastDeviceAlert()
		
		// Vérifie si nouvelle alerte monitoring sur device
		if (levelAlert.isAlerteMonitoring(dateMonitoring)) {
			return doUpdateNewDeviceAlert(alert, levelAlert, dateMonitoring)
		} else {
			// aucune alerte détectée :
			// si la dernière alerte est toujours en cours, on la cloture
			// seulement si c'était une alerte monitoring
			doUpdateCloseDeviceAlert(alert, LevelAlertEnum.monitoring)
		}
		
		return null
	}
	
	
	/**
	 * Vérifie si une alerte doit être déclenchée sur la valeur d'un objet
	 *
	 * @param deviceValue
	 *
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousMessage(exchange = "smarthome.automation.deviceAlertService.processDeviceValue", exchangeType =  ExchangeType.FANOUT)
	DeviceAlert processDeviceValue(DeviceValue deviceValue) throws SmartHomeException {
		List<DeviceLevelAlert> levelAlerts = listDeviceLevelAlertValue(deviceValue.device)
		DeviceAlert deviceAlert = deviceValue.device.lastDeviceAlert()
		
		for (DeviceLevelAlert levelAlert : levelAlerts) {
			// si la valeur est en alerte, il faut vérifier depuis quand par rapport à la config
			// on en profite du coup pour calculer le nombre de relance
			// on charge toutes les dernières valeurs du device jusqu'à trouver une valeur normale
			if (levelAlert.isAlerteValue(deviceValue.value)) {
				boolean searchValue = true
				DeviceValue lastAlertValue = deviceValue
				int offsetValues = 0
				
				while (searchValue) {
					List<DeviceValue> values = DeviceValue.createCriteria().list([offset: offsetValues,
						max: MAX_VALUES_ALERT_SEARCH]) {
						eq "device", deviceValue.device
						lt "dateValue", deviceValue.dateValue
						order "dateValue", "desc"
					}
					
					// IMPORTANT : casser la boucle si pas de valeur sinon boucle while infinie
					if (!values) {
						break // casse la boucle WHILE
					} 
					
					// recherche des value avec la même alerte
					// dès qu'une autre alerte est détectée ou pas d'alerte, on arrête tout
					for (DeviceValue value : values) {
						if (levelAlert.isAlerteValue(value.value)) {
							lastAlertValue = value
						} else {
							searchValue = false	// casse la boucle WHILE
							break	// casse la boucle FOR
						}
					}	
					
					offsetValues += MAX_VALUES_ALERT_SEARCH
				} // while
				
				// une alerte n'est déclenchée que si au moins 2 valeurs consécutives en alerte
				// et que la durée est supérieure à la tempo
				if (lastAlertValue.dateValue != deviceValue.dateValue) {
					long alertDuree = (deviceValue.dateValue.time - lastAlertValue.dateValue.time) / 1000 / 60
					
					// a ce stade, on est bien en alerte sur la value
					// on lance le process de verif si nouvelle alerte à avertir
					// on n'oublie surtout pas de flagger la value en erreur
					if (alertDuree > levelAlert.tempo) {
						deviceValue.alertLevel = levelAlert.level
						deviceValue.save()
						return doUpdateNewDeviceAlert(deviceAlert, levelAlert, deviceValue.dateValue)
					}
				}
			} // if isAlertValue
			
			// si pas d'alerte sur la config testée, on peut fermer une éventuelle alerte sur ce type
			doUpdateCloseDeviceAlert(deviceAlert, levelAlert)
		}
		
		return null
	}
	
	
	/**
	 * Met à jour l'alerte en cours en fonction de la nouvelle alerte.
	 * En fonction retour fonction, permet de savoir si le status a changé et qu'il faut avertir 
	 * 
	 * @param deviceAlert
	 * @param level
	 * @return deviceAlert si nouvelle alerte à avertir
	 */
	private DeviceAlert doUpdateNewDeviceAlert(DeviceAlert deviceAlert, DeviceLevelAlert levelAlert,
		Date dateReference) {
		DeviceAlert returnAlert = null
		
		// ouvre une nouvelle alerte si la dernière est fermée ou inexistante
		if (!deviceAlert?.isOpen()) {
			deviceAlert = new DeviceAlert(device: levelAlert.device, level: levelAlert.level,
				dateDebut: dateReference)
			returnAlert = deviceAlert
		} else {
			// alerte déjà ouverte mais peut-être sur un autre type d'alerte
			// il faut la réintialiser
			if (deviceAlert.level != levelAlert.level) {
				deviceAlert.reset(levelAlert.level)
				returnAlert = deviceAlert
			}
			// l'alerte n'a pas été checkée par l'utilisateur.
			// il faut gérer les relances pour ne pas envoyer un mail à appel de la méthode
			// mais seulement tous les N x tempo
			else if (!deviceAlert.isViewed()) {
				int oldRelance = deviceAlert.relance
				this.calculRelance(deviceAlert, levelAlert.tempo, dateReference)
				
				if (oldRelance != deviceAlert.relance) {
					returnAlert = deviceAlert
				}
			}
		}
		
		if (returnAlert) {
			return this.save(returnAlert)
		}
		
		return null
	}
	
	
	/**
	 * Ferme une alerte en cours
	 * 
	 * @param deviceAlert
	 * @param level
	 * @return
	 */
	private DeviceAlert doUpdateCloseDeviceAlert(DeviceAlert deviceAlert, DeviceLevelAlert levelAlert) {
		if (deviceAlert?.isOpen() && deviceAlert?.level == levelAlert.level) {
			deviceAlert.close()
			return this.save(deviceAlert)
		}
		
		return null
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
	DeviceAlert calculRelance(DeviceAlert alert, int tempo, Date dateReference) throws SmartHomeException {
		// calcul du temps écoulé depuis le début de l'alerte
		long minuteEllape = (dateReference.time - alert.dateDebut.time) / 1000 / 60
		
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
