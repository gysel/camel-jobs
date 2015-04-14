package ch.hsr.sa.eai.sandbox.server.rest.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "jobResult")
public class JobResult {
	
	public enum Status {
		SCHEDULED, RUNNING, SUCCESSFUL, FAILED
	}
	
	private String jobName;
	private Status status;
	
	public JobResult() {
	}
	
	public JobResult(String jobName, Status status) {
		this.jobName = jobName;
		this.status = status;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
