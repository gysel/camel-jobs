package ch.hsr.camel.jobs.trigger.rest.api;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlRootElement(name = "jobResult")
public class JobResult {

	public enum Status {
		SCHEDULED, RUNNING, SUCCESSFUL, FAILED, ROLLED_BACK
	}

	private String jobName;
	private Status status;
	/**
	 * Successfully integrated records
	 */
	private Long successfulRecords = 0L;
	/**
	 * Failed records, an exception or other problem has occurred
	 */
	private Long failedRecords = 0L;
	private Long ignoredRecords = 0L;
	/**
	 * Rejected as the record contained unwanted content or attributes
	 */
	private Long rejectedRecords = 0L;
	private String executionId;
	private long jobDurationInMilis;

	public JobResult(String jobName, Status status, String executionId) {
		this.jobName = jobName;
		this.status = status;
		this.executionId = executionId;
	}

	public JobResult() {
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

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public Long getSuccessfulRecords() {
		return successfulRecords;
	}

	public void setSuccessfulRecords(Long successfulRecords) {
		this.successfulRecords = successfulRecords;
	}

	public String getDetails() {
		return "[status=" + status + ", successful=" + successfulRecords + ", ignored=" + ignoredRecords + ", failed="
				+ failedRecords + ", rejected=" + rejectedRecords + ", executionId=" + executionId + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JobResult) {
			final JobResult other = (JobResult) obj;
			return new EqualsBuilder().append(jobName, other.jobName).append(status, other.status)
					.append(successfulRecords, other.successfulRecords).append(rejectedRecords, other.rejectedRecords)
					.append(ignoredRecords, other.ignoredRecords).append(failedRecords, other.failedRecords)
					.append(executionId, other.executionId).isEquals();
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(jobName).append(status)
				.append(successfulRecords).append(rejectedRecords)
				.append(ignoredRecords).append(failedRecords)
				.append(executionId).hashCode();
	}

	public Long getRejectedRecords() {
		return rejectedRecords;
	}

	public void setRejectedRecords(Long rejectedRecords) {
		this.rejectedRecords = rejectedRecords;
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

	public void setJobDuration(long jobDurationInMilis) {
		this.jobDurationInMilis = jobDurationInMilis;
	}

	public long getJobDurationInMilis() {
		return jobDurationInMilis;
	}

}
