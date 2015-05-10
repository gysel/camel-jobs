package ch.hsr.camel.jobs.trigger.rest;

import org.apache.camel.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.hsr.camel.jobs.trigger.JobManager;
import ch.hsr.camel.jobs.trigger.rest.api.JobResult;

@Component(value = "restRouteStarter")
public class RestRouteStarter {

	@Autowired
	JobManager jobManager;

	public JobResult process(@Header("name") String jobName) {
		JobResult jobResult = jobManager.startJob(jobName);
		return jobResult;
	}

}
