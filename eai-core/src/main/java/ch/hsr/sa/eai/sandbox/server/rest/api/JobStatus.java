package ch.hsr.sa.eai.sandbox.server.rest.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "jobStatus")
public class JobStatus {

	private String jobName;
	private Date lastSuccessfulRun;
	private Long successfulRecords = 0L;
	private Long failedRecords = 0L;
	private Long ignoredRecords = 0L;

	public JobStatus() {
	}
	
	public JobStatus(String jobName, Date lastSuccessfulRun) {
		this.jobName = jobName;
		this.setLastSuccessfulRun(lastSuccessfulRun);
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Long getSuccessfulRecords() {
		return successfulRecords;
	}

	public void setSuccessfulRecords(Long successfulRecords) {
		this.successfulRecords = successfulRecords;
	}

	public Date getLastSuccessfulRun() {
		return lastSuccessfulRun;
	}

	public void setLastSuccessfulRun(Date lastSuccessfulRun) {
		this.lastSuccessfulRun = lastSuccessfulRun;
	}

	public Long getFailedRecords() {
		return failedRecords;
	}

	public void setFailedRecords(Long failedRecords) {
		this.failedRecords = failedRecords;
	}

	public Long getIgnoredRecords() {
		return ignoredRecords;
	}

	public void setIgnoredRecords(Long ignoredRecords) {
		this.ignoredRecords = ignoredRecords;
	}

}
