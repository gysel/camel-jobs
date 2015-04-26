package ch.hsr.sa.eai.sandbox.server;

import java.util.concurrent.Future;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Route;
import org.apache.camel.component.seda.SedaEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.util.StopWatch;

import ch.hsr.sa.eai.sandbox.server.rest.MetricHelper;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult.Status;

@Component
public class JobManager {

	@Autowired
	private ProducerTemplate template;

	@Autowired
	MetricHelper metricHelper;

	private Logger logger = LoggerFactory.getLogger(JobManager.class);
	private StopWatch sw;

	public JobManager() {
		sw = new StopWatch();
	}

	/**
	 * start a job
	 * 
	 * @param jobName
	 * @throws IllegalArgumentException
	 *             when the job does not exist.
	 * @throws IllegalArgumentException
	 *             when a job is started that cannot be started by the
	 *             JobManager.
	 * @throws IllegalStateException
	 *             when there is a problem with job naming.
	 */
	public JobResult startJob(String jobName) {
		Status status = JobResult.Status.SUCCESSFUL;
		long countBeforeSuccessful = metricHelper.getCounterValue(jobName, MetricHelper.COUNTER_NAME_SUCCESSFUL);
		long countBeforeRejcted = metricHelper.getCounterValue(jobName, MetricHelper.COUNTER_NAME_REJECTED);

		String endpointUri = "vm:trigger-" + jobName;
		SedaEndpoint endpoint = (SedaEndpoint) template.getCamelContext().hasEndpoint(endpointUri);

		Route route = template.getCamelContext().getRoute(jobName);

		if (route == null) {
			throw new IllegalArgumentException("Job " + jobName + " not found.");
		} else if (route.getEndpoint().getEndpointUri().startsWith("file://")) {
			throw new IllegalArgumentException("Job " + jobName
					+ " is based on a file poller and cannot be started manually.");
		} else if (endpoint != null) {
			// cannot set timeout in endpointUri as it won't be found by
			// hasEndpoint
			endpoint.setTimeout(0);
			sw.start();
			logger.info("Starting job {}.", jobName);
			try {

				Future<Object> future = template.asyncRequestBody(endpoint, (Object) null);
				Object result = template.extractFutureBody(future, Object.class);
			} catch (CamelExecutionException e) {
				logger.error("Job {} failed.", jobName, e);
				if (e.getCause() instanceof UnexpectedRollbackException) {
					status = JobResult.Status.ROLLED_BACK;
				} else {
					status = JobResult.Status.FAILED;
				}
			} finally {
				sw.stop();
			}
		} else {
			throw new IllegalStateException("Jobs are not named according to the convention. Endpointuri: "
					+ endpointUri);
		}

		JobResult jobResult = new JobResult(jobName, status);
		long successfulRecords = metricHelper.getCounterValue(jobName, MetricHelper.COUNTER_NAME_SUCCESSFUL)
				- countBeforeSuccessful;
		long rejectedRecords = metricHelper.getCounterValue(jobName, MetricHelper.COUNTER_NAME_REJECTED)
				- countBeforeRejcted;
		jobResult.setSuccessfulRecords(successfulRecords);
		jobResult.setRejectedRecords(rejectedRecords);

		logger.info("Job {} finished in {}ms. {}", jobName, sw.getLastTaskTimeMillis(), jobResult.getDetails());

		return jobResult;
	}
}
