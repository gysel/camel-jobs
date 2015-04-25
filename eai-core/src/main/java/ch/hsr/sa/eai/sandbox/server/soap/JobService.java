package ch.hsr.sa.eai.sandbox.server.soap;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import ch.hsr.sa.eai.sandbox.server.rest.api.Jobs;

@WebService(serviceName = "jobService")
@Component
public class JobService {
	
	public Jobs getJobs() {
		return new Jobs();
	}

}
