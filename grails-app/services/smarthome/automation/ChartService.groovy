package smarthome.automation

import java.util.Date;
import java.util.List;

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

import org.codehaus.groovy.grails.web.mapping.LinkGenerator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.ClassUtils;
import smarthome.core.QueryUtils;
import smarthome.automation.Chart;
import smarthome.core.ExchangeType;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class ChartService extends AbstractService {

	DeviceService deviceService
	
	
	/**
	 * Charts user
	 * 
	 * @param command
	 * @param userId
	 * @param pagination
	 * @return
	 */
	List<Chart> listByUser(ChartSearchCommand command, Long userId, Map pagination) {
		Chart.createCriteria().list(pagination) {
			user {
				idEq(userId)
			}
			
			if (command.search) {
				ilike 'label', QueryUtils.decorateMatchAll(command.search)
			}
			if (command.groupe) {
				eq 'groupe', command.groupe
			}
			order "label"
		}
	}
	
	
	/**
	 * Edition ACL
	 * 
	 * @param chart
	 * @return
	 */
	@PreAuthorize("hasPermission(#chart, 'OWNER')")
	Chart edit(Chart chart) {
		return chart	
	}
	
	
	/**
	 * Enregistrement d'un Chart
	 *
	 * @param chart
	 *
	 * @return Chart
	 */
	@PreAuthorize("hasPermission(#chart, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Chart save(Chart chart) throws SmartHomeException {
		// suppression des résultats non bindés
		chart.devices?.removeAll { 
			! it.persist
		}
		
		if (!chart.save()) {
			throw new SmartHomeException("Erreur enregistrement chart", chart);
		}
		
		return chart
	}
	
	
	/**
	 * Suppression d'un chart
	 *
	 * @param chart
	 *
	 * @return Chart
	 */
	@PreAuthorize("hasPermission(#chart, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void delete(Chart chart) throws SmartHomeException {
		try {
			// flush direct pour catcher une erreur SQL (ex : clé étrangère) et la renvoyer en SmartHomeException
			// sinon l'erreur est déclenchée hors méthode
			chart.delete(flush: true);
		} catch (Exception e) {
			throw new SmartHomeException(e, chart)
		}
	}
	
	
	/**
	 * Charge les valeurs du device depuis sinceHour
	 *
	 * @param device
	 * @param sinceHour
	 * @param name
	 * @return
	 * @throws SmartHomeException
	 */
	Map values(Chart chart, Date start, Date end) throws SmartHomeException {
		log.info "Load trace values for chart ${chart.label} from ${start} to ${end}"
		def map = [:]
		
		chart.devices?.each {
			// attention au 3e parametre, il faut lui passer '' pour récupérer les valeurs par défaut dont le name est null
			// car si on passe null, on récupère toutes les valeurs sans distinction du name
			map.put(it, deviceService.values(it.device, start, end, it.metavalue ?: ''))
		}
		
		return map
	}
	
	
	/**
	 * Renvoit les périodes disponibles pour la construction des graphiques
	 * 
	 * @return
	 */
	def timesAgo() {
		[24: 'jour', 168: 'semaine', 744: 'mois', 8760: 'an']
	}
	
	
	/**
	 * La période par défaut
	 * 
	 * @return
	 */
	def defaultTimeAgo() {
		return 24
	}
	
	
	/**
	 * Calcul des groupes
	 *
	 * @return
	 */
	List<String> listGroupes(long userId) {
		return Chart.createCriteria().list {
			isNotNull 'groupe'
			eq 'user.id', userId
			projections {
				groupProperty 'groupe'
			}
			order 'groupe'
		}
	}
	
}
