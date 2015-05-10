package ch.hsr.camel.jobs.trigger.rest.api;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "job")
@XmlType
public class Job {

	private String name;
	private String uri;
	
	public Job() {
	}

	public Job(String name, String uri) {
		this.setUri(uri);
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
