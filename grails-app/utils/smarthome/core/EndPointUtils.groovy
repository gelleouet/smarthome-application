package smarthome.core

import javax.servlet.ServletContext;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

class EndPointUtils {
	/**
	 * Création et enregistrement d'un endpoint (websocket)
	 * 
	 * @param servletContext
	 * @param endPointClass
	 * @param endPointUrl
	 * @return
	 */
	static ServerEndpointConfig register(ServletContext servletContext, Class endPointClass, String endPointUrl) {
		ServerContainer serverContainer = servletContext.getAttribute("javax.websocket.server.ServerContainer")
		ServerEndpointConfig configEndPoint = ServerEndpointConfig.Builder.create(endPointClass, endPointUrl).build();
		serverContainer.addEndpoint(configEndPoint)
		return configEndPoint
	}
}
