package ch.hsr.sa.eai.sandbox.server.rest;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import ch.hsr.sa.eai.sandbox.server.rest.api.Job;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult;
import ch.hsr.sa.eai.sandbox.server.rest.api.Jobs;

@Component("restRouteBuilder")
public class RestRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		rest("/jobs").get().outType(Jobs.class).to("restRouteManagement");
		rest("/jobs/{jobName}").get().outType(JobResult.class).to("restRouteStatus");
		// TODO make it work without the /start
		rest("/jobs/{jobName}/start").post().outType(JobResult.class).to("restRouteStarter");
//		rest("/jobs").post("/{jobName}}").outType(JobResult.class).to("restRouteStarter");
	}

}
