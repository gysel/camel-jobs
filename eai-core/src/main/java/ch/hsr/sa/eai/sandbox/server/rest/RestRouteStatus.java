package ch.hsr.sa.eai.sandbox.server.rest;

import org.apache.camel.Header;
import org.springframework.stereotype.Component;

import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult.Status;

@Component("restRouteStatus")
public class RestRouteStatus {

	public JobResult process(@Header("jobName") String jobName) {
		return new JobResult(jobName, Status.SUCCESSFUL);
	}
	
}
