package ch.hsr.sa.eai.sandbox.server.rest;

import org.apache.camel.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.hsr.sa.eai.sandbox.server.JobFacade;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult;

@Component(value = "restRouteStarter")
public class RestRouteStarter {

	@Autowired
	JobFacade jobManager;

	public JobResult process(@Header("name") String jobName) {
		JobResult jobResult = jobManager.startJob(jobName);
		return jobResult;
	}

}
