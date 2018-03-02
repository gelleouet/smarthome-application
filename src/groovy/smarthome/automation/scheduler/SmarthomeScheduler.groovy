package smarthome.automation.scheduler

import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

import java.util.Properties;

import groovy.util.ConfigObject;

import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Le gestionnaire de cron
 * 
 * @author Gregory
 *
 */
class SmarthomeScheduler implements InitializingBean, ApplicationContextAware {

	private static final log = LogFactory.getLog(this)
	
	AutowireCapableBeanFactory beanFactory;
	
	Scheduler scheduler
	
	@Autowired
	GrailsApplication grailsApplication
	
	Map<String, String> jobs

	public void setJobs(Map<String, String> jobs) {
		this.jobs = jobs;
	}
	
	@Override
	public void setApplicationContext(final ApplicationContext context) {
		beanFactory = context.getAutowireCapableBeanFactory();
	}


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
	}
	
	
	/**
	 * Démarre le gestionnaire
	 * 
	 * @return
	 */
	def start() {
		log.info "Starting jobs scheduling..."
		
		scheduler.getListenerManager().addJobListener(new SmarthomeJobListener(beanFactory))
		scheduler.start()
		
		// ajout des jobs injectés
		jobs?.each { className, cron ->
			Class jobClass = Class.forName(className)
			Set triggers = [getTrigger(jobClass, cron)]
			scheduler.scheduleJob(getJobDetail(jobClass), triggers, true)
		}
	}
	
	
	/**
	 * Arrete le gestionnaire
	 */
	def shutdown() {
		log.info "Stopping jobs scheduling..."
		
		scheduler.shutdown()
	}
	
	
	JobDetail getJobDetail(Class<Job> jobClass) {
		return newJob(jobClass)
			 .withIdentity(jobClass.getSimpleName())
			 .storeDurably()
			 .build();
	}
	
	
	Trigger getTrigger(Class<Job> jobClass, String cron) {
		return newTrigger()
			.withIdentity(jobClass.getSimpleName() + "Trigger")
			.withSchedule(cronSchedule(cron))
			.build()
	}
	
	
	/**
	 * Planifie un job en one-shot
	 */
	void scheduleOneShotJob(Class<Job> jobClass, Date scheduledDate, Map data) {
		def jobName = jobClass.getSimpleName() + UUID.randomUUID().toString()
		
		def jobDetail = newJob(jobClass)
			.withIdentity(jobName)
			.storeDurably(false)
			.usingJobData(new JobDataMap(data))
			.build()
			
		def trigger = newTrigger().forJob(jobName).startAt(scheduledDate).build()
		
		scheduler.scheduleJob(jobDetail, trigger)
	}
}
