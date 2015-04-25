package ch.hsr.sa.eai.sandbox.server;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import ch.hsr.sa.eai.sandbox.server.rest.MetricHelper;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult.Status;

@Component
public class JobFacade {

	@Autowired
	private ProducerTemplate template;

	@Autowired
	MetricHelper metricHelper;
	
	private Logger logger = LoggerFactory.getLogger(JobFacade.class);
	private StopWatch sw;

	public JobFacade() {
		sw = new StopWatch();
	}
	/**
	 * start a job
	 * @param jobName
	 * @throws IllegalArgumentException when the job does not exist.
	 * @throws IllegalArgumentException when a job is started that cannot be started by the JobManager.
	 * @throws IllegalStateException when there is a problem with job naming.
	 */
	public JobResult startJob(String jobName) {
		Status status = JobResult.Status.SUCCESSFUL;
		long countBefore = metricHelper.getCounterValue(jobName, ".successful");

		String endpointUri = "vm:trigger-" + jobName;
		Endpoint endpoint = template.getCamelContext().hasEndpoint(endpointUri);

		Route route = template.getCamelContext().getRoute(jobName);

		if (route == null) {
			throw new IllegalArgumentException("Job " + jobName + " not found.");
		} else if (route.getEndpoint().getEndpointUri().startsWith("file://")) {
			throw new IllegalArgumentException("Job " + jobName + " is based on a file poller and cannot be started manually.");
		} else if (endpoint != null) {
			sw.start();
			logger.info("Starting job {}.", jobName);
			try {
				template.requestBody(endpoint, (Object) null);
			} catch (CamelExecutionException e) {
				logger.error("Job {} failed.", jobName, e);
				status = JobResult.Status.FAILED;
			} finally {
				sw.stop();
			}
		} else {
			throw new IllegalStateException("Jobs are not named according to the convention.");
		}

		JobResult jobResult = new JobResult(jobName, status);
		long successfulRecords = metricHelper.getCounterValue(jobName, ".successful") - countBefore;
		jobResult.setSuccessfulRecords(successfulRecords);

		logger.info("Job {} finished in {}ms. {}", jobName, sw.getLastTaskTimeMillis(), jobResult.getDetails());

		return jobResult;
	}

}
