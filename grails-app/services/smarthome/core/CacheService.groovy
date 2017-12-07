package smarthome.core

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.SmartHomeException;


class CacheService extends AbstractService {
	CacheManager grailsCacheManager
	
	
	/**
	 * Vide un cache en asynchrone avec gestion cluster cache
	 *
	 * @param cacheName
	 * @return
	 */
	@AsynchronousMessage(exchange = SmartHomeCoreConstantes.CACHE_QUEUE, exchangeType =  ExchangeType.FANOUT)
	void clearCluster(String cacheName) {
		
	}
	
	
	/**
	 * Vide un cache sur l'application actuelle
	 *
	 * @param cacheName
	 * @return
	 */
	void clearApp(String cacheName) {
		log.info "Clearing cache ${cacheName}..."
		Cache cache =  grailsCacheManager.getCache(cacheName)
		cache.clear()
	}
	
	
	/**
	 * Renvoit un objet dans un cache
	 *
	 * @param cacheName
	 * @param key
	 * @return
	 */
	def get(String cacheName, Object key) {
		Cache cache = grailsCacheManager.getCache(cacheName)
		return cache.get(key)?.get()
	}
	
	
	/**
	 * Mise en cache d'un objet
	 *
	 * @param cacheName
	 * @param key
	 * @param value
	 * @return
	 */
	def put(String cacheName, Object key, Object value) {
		Cache cache = grailsCacheManager.getCache(cacheName)
		cache.put(key, value)
	}
	
	
	/**
	 * Supprime un objet du cache
	 *
	 * @param cacheName
	 * @param key
	 * @return
	 */
	def evict(String cacheName, Object key) {
		Cache cache = grailsCacheManager.getCache(cacheName)
		cache.evict(key)
	}
}
