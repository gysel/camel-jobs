package ch.hsr.sa.eai.sandbox.server.rest.api;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;

@XmlRootElement(name = "jobResult")
public class JobResult {

	public enum Status {
		SCHEDULED, RUNNING, SUCCESSFUL, FAILED
	}

	private String jobName;
	private Status status;
	private Long successfulRecords = 0L;
	private Long rejectedRecords = 0L;

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

	public Long getSuccessfulRecords() {
		return successfulRecords;
	}

	public void setSuccessfulRecords(Long successfulRecords) {
		this.successfulRecords = successfulRecords;
	}

	public Long getRejectedRecords() {
		return rejectedRecords;
	}

	public void setRejectedRecords(Long rejectedRecords) {
		this.rejectedRecords = rejectedRecords;
	}

	public String getDetails() {
		return "[status=" + status + ", successfulRecords=" + successfulRecords + ", rejectedRecords="
				+ rejectedRecords + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JobResult) {
			final JobResult other = (JobResult) obj;
			return new EqualsBuilder().append(jobName, other.jobName).append(status, other.status)
					.append(successfulRecords, other.successfulRecords).append(rejectedRecords, other.rejectedRecords)
					.isEquals();
		} else {
			return false;
		}
	}

}
