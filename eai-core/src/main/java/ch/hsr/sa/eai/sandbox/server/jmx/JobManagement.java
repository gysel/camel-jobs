package ch.hsr.sa.eai.sandbox.server.jmx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import ch.hsr.sa.eai.sandbox.server.JobManager;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult.Status;

@Component(value = "ch.hsr.sa.eai:name=JobManagement")
@ManagedResource(description = "MBean for Route Management")
public class JobManagement {
	
	@Autowired
	JobManager jobManager;

	@ManagedOperation(description = "start a route")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "jobName", description = "The name of the job to start") })
	public int startJob(String jobName) {
		JobResult result = jobManager.startJob(jobName);
		int code = 0;
		if(!Status.SUCCESSFUL.equals(result.getStatus())) {
			code = 1;
		}
		return code;
	}
}
