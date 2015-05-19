package ch.hsr.camel.jobs.trigger;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Route;
import org.apache.camel.component.seda.SedaEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.util.StopWatch;

import ch.hsr.camel.jobs.trigger.rest.MetricHelper;
import ch.hsr.camel.jobs.trigger.rest.api.JobResult;
import ch.hsr.camel.jobs.trigger.rest.api.JobResult.Status;

@Component("jobManager")
public class JobManager {

	@Autowired
	private ProducerTemplate template;

	@Autowired
	MetricHelper metricHelper;

	private Logger logger = LoggerFactory.getLogger(JobManager.class);

	public JobManager() {
	}

	public JobResult startJob(String jobName) {
		return startJob(jobName, (Object) null);
	}

	/**
	 * start a job
	 * 
	 * @param jobName
	 * @throws IllegalArgumentException
	 *             when the job does not exist.
	 * @throws IllegalArgumentException
	 *             when a job is started that cannot be started by the JobManager.
	 * @throws IllegalStateException
	 *             when there is a problem with job naming.
	 */
	public JobResult startJob(@Header("jobName") String jobName, @Body final Object data) {
		String executionId;
		Status status = JobResult.Status.SUCCESSFUL;
		long countSuccessfulBefore = metricHelper.getSuccessfulRecords(jobName);
		long countFailedBefore = metricHelper.getFailedRecords(jobName);
		long countIgnoredBefore = metricHelper.getIgnoredRecords(jobName);
		long countRejectedBefore = metricHelper.getRejectedRecords(jobName);

		StopWatch sw = new StopWatch();
		String endpointUri = "vm:trigger-" + jobName;
		SedaEndpoint endpoint = (SedaEndpoint) template.getCamelContext().hasEndpoint(endpointUri);
		// TODO instanceof check!

		Route route = template.getCamelContext().getRoute(jobName);

		if (route == null) {
			throw new IllegalArgumentException("Job " + jobName + " not found.");
		} else if (route.getEndpoint().getEndpointUri().startsWith("file://")) {
			throw new IllegalArgumentException("Job " + jobName
					+ " is based on a file poller and cannot be started manually.");
		} else if (endpoint != null) {
			// cannot set timeout in endpointUri as it won't be found by hasEndpoint
			endpoint.setTimeout(0);
			sw.start();
			logger.info("Starting job {}.", jobName);
			Exchange result = template.request(endpoint, new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					exchange.getIn().setBody(data);
				}
			});
			if (result.getException() != null) {
				logger.error("Job {} failed.", jobName, result.getException());
				if (result.getException() instanceof UnexpectedRollbackException) {
					status = JobResult.Status.ROLLED_BACK;
				} else {
					status = JobResult.Status.FAILED;
				}
			}
			executionId = getExecutionId(result);
			sw.stop();
		} else {
			throw new IllegalStateException("Jobs are not named according to the convention. Missing endpoint: "
					+ endpointUri);
		}

		JobResult jobResult = new JobResult(jobName, status, executionId);
		long successfulRecords = metricHelper.getSuccessfulRecords(jobName) - countSuccessfulBefore;
		long ignoredRecords = metricHelper.getIgnoredRecords(jobName) - countIgnoredBefore;
		long failedRecords = metricHelper.getFailedRecords(jobName) - countFailedBefore;
		long rejectedRecords = metricHelper.getRejectedRecords(jobName) - countRejectedBefore;
		jobResult.setSuccessfulRecords(successfulRecords);
		jobResult.setFailedRecords(failedRecords);
		jobResult.setIgnoredRecords(ignoredRecords);
		jobResult.setRejectedRecords(rejectedRecords);
		jobResult.setJobDuration(sw.getLastTaskTimeMillis());

		logger.info("Job {} finished in {}ms. {}", jobName, sw.getLastTaskTimeMillis(), jobResult.getDetails());

		return jobResult;
	}

	private String getExecutionId(Exchange result) {
		if (result.getOut() != null) {
			Object executionIdHeader = result.getOut().getHeader("ExecutionId");
			if (executionIdHeader != null) {
				return executionIdHeader.toString();
			}
		}
		return "";
	}
}
