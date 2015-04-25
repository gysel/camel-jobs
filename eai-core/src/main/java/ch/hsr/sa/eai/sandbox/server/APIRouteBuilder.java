package ch.hsr.sa.eai.sandbox.server;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobStatus;
import ch.hsr.sa.eai.sandbox.server.rest.api.Jobs;

@Component("apiRouteBuilder")
public class APIRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		rest("/jobs").get().outType(Jobs.class).to("restRouteManagement");
		rest("/jobs/{jobName}").get().outType(JobStatus.class).to("restRouteStatus");
		rest("/jobs/{name}").post().outType(JobResult.class).to("restRouteStarter");
		// the parameter name is different intentionally. camel does not allow two rest routes with the exact same uri.
		from("jms:JobTrigger?exchangePattern=InOut").beanRef("jmsRouteStarter");
		from("cxf:bean:jobsEndpoint").log("test").end();
	}

}
