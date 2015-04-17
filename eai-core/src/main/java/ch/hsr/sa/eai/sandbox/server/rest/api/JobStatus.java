package ch.hsr.sa.eai.sandbox.server.rest.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "jobStatus")
public class JobStatus {

	private String jobName;
	private Long successfulRecords = 0L;
	private Date lastSuccessfulRun;

	public JobStatus() {
	}
	
	public JobStatus(String jobName, Date lastSuccessfulRun, Long successfulRecords) {
		this.jobName = jobName;
		this.setLastSuccessfulRun(lastSuccessfulRun);
		this.successfulRecords = successfulRecords;
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

}
