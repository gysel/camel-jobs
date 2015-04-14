package ch.hsr.sa.eai.sandbox.server.rest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.camel.Header;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult;

@Component(value = "restRouteStarter")
public class RestRouteStarter {

	@Autowired
	private ProducerTemplate template;

	public JobResult process(@Header("jobName") String jobName) throws InterruptedException, ExecutionException {
		String endpointUri = "vm:trigger-" + jobName;
		if (template.getCamelContext().hasEndpoint(endpointUri) != null) {
			Future<Object> future = template.asyncSendBody(endpointUri, null);
			future.get();
		} else {
			throw new IllegalArgumentException("Job " + jobName + " not found.");
		}
		return new JobResult(jobName, JobResult.Status.SUCCESSFUL); // TODO replace with proper job result
	}

}
