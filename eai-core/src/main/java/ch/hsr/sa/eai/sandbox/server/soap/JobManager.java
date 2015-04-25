package ch.hsr.sa.eai.sandbox.server.soap;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import ch.hsr.sa.eai.sandbox.server.rest.api.Jobs;

@WebService(serviceName = "jobManagerService", targetNamespace = "http://eai.sa.hsr.ch")
@Component("jobManagerBean")
public class JobManager {

	public void getJobs() {
		System.out.println("foobar");
	}

}
