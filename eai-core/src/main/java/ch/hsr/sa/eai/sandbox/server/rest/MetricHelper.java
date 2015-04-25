package ch.hsr.sa.eai.sandbox.server.rest;

import java.util.SortedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

@Component
public class MetricHelper {

	public static final String COUNTER_NAME_SUCCESSFUL = ".successful";
	public static final String COUNTER_NAME_REJECTED = ".rejected";

	@Autowired
	MetricRegistry metricRegistry;

	public Long getCounterValue(String jobName, String counterName) {
		SortedMap<String, Counter> counters = metricRegistry.getCounters();
		Counter counter = counters.get(jobName + counterName);
		Long result = 0L;
		if (counter != null) {
			result = counter.getCount();
		}
		return result;
	}
}
