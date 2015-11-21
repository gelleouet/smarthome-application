package smarthome.automation.scheduler

import java.util.Properties;

import groovy.util.ConfigObject;

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Le gestionnaire de cron
 * 
 * @author Gregory
 *
 */
class SmarthomeScheduler implements InitializingBean {

	Scheduler scheduler
	
	@Autowired
	GrailsApplication grailsApplication
	
	def jobs = []

	@Override
	public void afterPropertiesSet() throws Exception {
		Properties quartzProperties = new Properties()
		
		// chargement des configs
		if (grailsApplication.config.containsKey('quartz')) {
			ConfigObject configObject = new ConfigObject()
			configObject.putAll(grailsApplication.config.quartz)
			quartzProperties << configObject.toProperties('org.quartz')
		}
		
		SchedulerFactory factory = new StdSchedulerFactory()
		factory.initialize(quartzProperties)
		
		scheduler = factory.getScheduler()
		
		// ajout des jobs injectés
		jobs?.each {
			scheduler.scheduleJob(it.getJobDetail(), it.getTrigger())
		}
	}
	
	
	/**
	 * Démarre le gestionnaire
	 * 
	 * @return
	 */
	def start() {
		scheduler.start()
	}
}
