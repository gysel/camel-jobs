package ch.hsr.sa.eai.sandbox.server;

import java.util.Properties;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobStatus;
import ch.hsr.sa.eai.sandbox.server.rest.api.Jobs;

@Component("apiRouteBuilder")
public class APIRouteBuilder extends RouteBuilder {
	
	@Autowired
	private Properties properties;

	@Override
	public void configure() throws Exception {
		if("true".equals(properties.get("rest.api.enabled"))) {
			rest("/jobs").id("restRouteOverview").get().outType(Jobs.class).to("restRouteManagement");
			rest("/jobs/{jobName}").id("restRouteJobInfo").get().outType(JobStatus.class).to("restRouteStatus");
			rest("/jobs/{name}").id("restRouteStartJob").post().outType(JobResult.class).to("restRouteStarter");
			// the parameter name is different intentionally. camel does not allow
			// two rest routes with the exact same uri.
			from("jms:JobTrigger?exchangePattern=InOut").id("jmsRouteStarter").beanRef("jmsRouteStarter");
			from("quartz2://resetMetricsTimer?cron={{metrics.reset.cron}}").id("resetMetrics").to(
					"bean:metricHelper?method=resetMetrics");
		}

	}

}
