package ch.hsr.sa.eai.sandbox.server.rest.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "jobs")
@XmlType
public class Jobs {
	
	private List<Job> jobs = new ArrayList<>();
	
	public Jobs() {
	}

	public Jobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

}
