package smarthome.core

import javax.servlet.ServletContext;

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import smarthome.automation.export.DeviceValueExport

/**
 * Méthodes utilitaires sur application
 *
 * @author gregory
 *
 */
class ApplicationUtils implements ApplicationContextAware {

	private static ApplicationContext springContext
	
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		springContext = context
	}
	
	
	/**
	 * Contexte Spring depuis contexte servlet
	 * 
	 * @param servletContext
	 * @return
	 */
	static ApplicationContext getApplicationContextFromServletContext(ServletContext servletContext) {
		return servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
	}
	
	
	/**
	 * Application Grails depuis contexte Spring
	 *
	 * @param applicationContext
	 * @return
	 */
	static GrailsApplication getGrailsApplicationFromApplicationContext(ApplicationContext applicationContext) {
		return applicationContext.getBean("grailsApplication")
	}
	
	
	/**
	 * Injecte les dépendances manuellement sur un bean
	 *
	 * @param bean
	 * @return
	 */
	static Object autowireBean(Object bean) {
		springContext.getAutowireCapableBeanFactory().autowireBean(bean)
		return bean
	}
	
	
	/**
	 * La liste des impls DeviceValueExport disponibles dans l'application
	 * 
	 * @return
	 */
	static List<DeviceValueExport> exportImpls() {
		ServiceLoader.load(DeviceValueExport).sort {
			it.libelle
		}
	}
	
	
	/**
	 * Pagination max pour les traitements par lot
	 * 
	 * @param grailsApplication
	 * @return
	 */
	static int configMaxBackend(GrailsApplication grailsApplication) {
		grailsApplication.config.smarthome.pagination.maxBackend
	}

}
