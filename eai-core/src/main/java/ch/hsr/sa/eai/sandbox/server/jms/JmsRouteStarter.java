package ch.hsr.sa.eai.sandbox.server.jms;

import javax.jms.JMSException;

import org.apache.camel.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.hsr.sa.eai.sandbox.server.JobManager;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult;

@Component(value = "jmsRouteStarter")
public class JmsRouteStarter {

	@Autowired
	JobManager jobManager;

	public String process(@Header("JobName") String jobName) throws JMSException {
		JobResult jobResult = jobManager.startJob(jobName);
		return jobName + jobResult.getDetails();
	}

}
