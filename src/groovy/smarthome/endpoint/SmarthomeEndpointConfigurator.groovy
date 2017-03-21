package smarthome.endpoint

import javax.websocket.server.ServerEndpointConfig;

import smarthome.core.ApplicationUtils;

/**
 * Configurator pour les endpoints
 * Permet d'injecter les beans Spring sur endpoint Java
 * 
 * @author Gregory
 *
 */
class SmarthomeEndpointConfigurator extends ServerEndpointConfig.Configurator {

	@Override
	<T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
		T instance = super.getEndpointInstance(endpointClass)
		ApplicationUtils.autowireBean(instance)
		return instance
	}
}
