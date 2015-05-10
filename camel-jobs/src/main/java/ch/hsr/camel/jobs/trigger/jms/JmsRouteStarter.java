package ch.hsr.camel.jobs.trigger.jms;

import javax.jms.JMSException;

import org.apache.camel.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.hsr.camel.jobs.trigger.JobManager;
import ch.hsr.camel.jobs.trigger.rest.api.JobResult;

@Component(value = "jmsRouteStarter")
public class JmsRouteStarter {

	@Autowired
	JobManager jobManager;

	public String process(@Header("JobName") String jobName) throws JMSException {
		JobResult jobResult = jobManager.startJob(jobName);
		return jobName + jobResult.getDetails();
	}

}
