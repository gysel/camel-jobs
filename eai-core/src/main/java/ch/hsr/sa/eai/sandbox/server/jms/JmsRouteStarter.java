package ch.hsr.sa.eai.sandbox.server.jms;

import javax.jms.JMSException;

import org.apache.camel.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.hsr.sa.eai.sandbox.server.JobFacade;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult;

@Component("jmsRouteStarter")
public class JmsRouteStarter {

	@Autowired
	JobFacade jobFacade;

	public String process(@Header("JobName") String jobName) throws JMSException {
		JobResult jobResult = jobFacade.startJob(jobName);
		return jobName + jobResult.getDetails();
	}

}
