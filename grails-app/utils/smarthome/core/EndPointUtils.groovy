package smarthome.core

import javax.servlet.ServletContext;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

class EndPointUtils {
	/**
	 * Enregistrement manuellement d'un endpoint
	 * Ne sert uniquement qu'en DEV car sinon le serveur détecté auto les @ServerEndPoint 
	 * 
	 * @param servletContext
	 * @param endPointClass
	 * @param endPointUrl
	 * @return
	 */
	static void register(ServletContext servletContext, Class endPointClass) {
		ServerContainer serverContainer = servletContext.getAttribute("javax.websocket.server.ServerContainer")
		serverContainer.addEndpoint(endPointClass)
	}
	
	
	/**
	 * Transforme une requete HTTP en requete Websocket
	 *  
	 * @param url
	 * @return
	 */
	static String httpToWs(String url) {
		if (url.startsWith('https')) {
			return url.replace('https', 'wss')
		} else {
			return url.replace('http', 'ws')
		}
	}
	
	
	/**
	 * Construction d'une instance ServerEndPoint
	 * L'injection de dépendances est déclenchée sur le nouvel objet
	 *
	 * @param endPointClass
	 * @return
	 */
	static <T> T newEndPoint(Class<T> endPointClass) {
		T instance = endPointClass.newInstance()
		return initEndPoint(instance)
	}
	
	
	/**
	 * Init d'une instance EndPoint
	 *
	 * @param instance
	 * @return
	 */
	static <T> T initEndPoint(T instance) {
		ApplicationUtils.autowireBean(instance)
		return instance
	}
}
