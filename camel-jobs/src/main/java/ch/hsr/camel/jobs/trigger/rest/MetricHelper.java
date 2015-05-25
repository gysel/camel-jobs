package ch.hsr.camel.jobs.trigger.rest;

import java.util.Map;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.hsr.camel.jobs.trigger.rest.api.JobStatus;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

@Component("metricHelper")
public class MetricHelper {

	public static final String IGNORED = ".ignored";
	public static final String FAILED = ".failed";
	public static final String SUCCESSFUL = ".successful";
	public static final String REJECTED = ".rejected";

	private Logger logger = LoggerFactory.getLogger(MetricHelper.class);


	@Autowired
	private MetricRegistry metricRegistry;

	public Long getCounterValue(String jobName, String counterName) {
		SortedMap<String, Counter> counters = metricRegistry.getCounters();
		Counter counter = counters.get(jobName + counterName);
		Long result = 0L;
		if (counter != null) {
			result = counter.getCount();
		}
		return result;
	}
	
	public void resetMetrics() {
		SortedMap<String, Counter> counters = metricRegistry.getCounters();
		for (Map.Entry<String, Counter> e : counters.entrySet()) {
			logger.info("Resetting metric '{}'.", e.getKey());
			Counter counter = e.getValue();
			counter.dec(counter.getCount());
		}
	}

	public Long getSuccessfulRecords(String jobName) {
		return getCounterValue(jobName, SUCCESSFUL);
	}

	public long getFailedRecords(String jobName) {
		return getCounterValue(jobName, FAILED);
	}

	public long getIgnoredRecords(String jobName) {
		return getCounterValue(jobName, IGNORED);
	}
	
	public long getRejectedRecords(String jobName) {
		return getCounterValue(jobName, REJECTED);
	}

	public void update(JobStatus jobStatus) {
		jobStatus.setSuccessfulRecords(getSuccessfulRecords(jobStatus.getJobName()));
		jobStatus.setFailedRecords(getFailedRecords(jobStatus.getJobName()));
		jobStatus.setIgnoredRecords(getIgnoredRecords(jobStatus.getJobName()));
		jobStatus.setIgnoredRecords(getRejectedRecords(jobStatus.getJobName()));
	}
}
