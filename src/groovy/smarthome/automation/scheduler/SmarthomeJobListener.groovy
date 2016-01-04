package smarthome.automation.scheduler

import org.quartz.JobExecutionContext;
import org.quartz.listeners.JobListenerSupport
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

class SmarthomeJobListener extends JobListenerSupport {

	AutowireCapableBeanFactory beanFactory
	
	
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		super.jobToBeExecuted(context);
		beanFactory.autowireBean(context.getJobInstance())
	}


	public SmarthomeJobListener(AutowireCapableBeanFactory beanFactory) {
		super();
		this.beanFactory = beanFactory;
	}
	
	
	@Override
	public String getName() {
		return this.getClass().getName()
	}

}
