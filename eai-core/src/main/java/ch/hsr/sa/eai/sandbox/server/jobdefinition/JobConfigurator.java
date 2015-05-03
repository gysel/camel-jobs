package ch.hsr.sa.eai.sandbox.server.jobdefinition;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.SimpleBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.OnExceptionDefinition;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import ch.hsr.sa.eai.sandbox.server.errorhandling.ExceptionAggregationStrategy;

public class JobConfigurator implements ApplicationListener<ContextRefreshedEvent>, CamelContextAware {

	// use ModelCamelContext, CamelContext.getRouteDefinitions() is deprecated
	private ModelCamelContext camelContext;
	private boolean initiated = false;

	@Autowired
	private JobConfiguration config;

	private Logger logger = LoggerFactory.getLogger(JobConfigurator.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// ContextStartedEvent is not always fired, so ContextRefreshedEvent.
		// Initiated makes sure the routes are
		// adviced only once
		if (initiated) {
			return;
		}
		logger.info("ContextStartedEvent received, advice routes with configuration");
		initiated = true;
		adviceRoutes();

	}

	private void adviceRoutes() {
		for (RouteDefinition route : getListOfJobs()) {
			// note: adviceWith should only be called once per route!
			// http://camel.apache.org/advicewith.html
			adviceRoute(route);
		}
		try {
			logger.info("start all routes");
			camelContext.startAllRoutes();
		} catch (Exception e) {
			logger.error("could not start camel routes", e);
		}
	}

	private void adviceRoute(RouteDefinition route) {
		try {
			route.adviceWith(camelContext, new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					// intercept From
					interceptFrom().setHeader("ExecutionId", simple("${routeId}-${date:now:yyyyMMdd-HHmmss}")).log("${headers.ExecutionId} started");

					// exception onComplete
					// TODO: is not invoked anymore?
					if(config.shouldSendEmails()) {
						SimpleBuilder emailBody = simple("<simple>An exception has occured during process of ${header.ExecutionId} \n ${exception.message} ${exception.stacktrace}</simple>");
						String emailEndpoint = "smtp://" + config.getMailserverConnection() + "&subject=Exception in integration job";
						onCompletion().onFailureOnly().wireTap(emailEndpoint).newExchangeBody(emailBody).end();
					}

					// exception
					OnExceptionDefinition definition = onException(RuntimeException.class)
					// onException configuration
							.maximumRedeliveries(1).handled(true)
							// prepare for output
							.convertBodyTo(String.class).transform(simple("${in.body}\n"))
							// update metrics
							.setHeader("CamelMetricsName", simple("${routeId}.failed")).to("metrics:counter:nameNotUsed")
							// write failed record to file
							.to("file://failedRecords/?fileName=${headers.ExecutionId}-failed&fileExist=Append");
					if (config.shouldSendEmails()) {
						// aggregate all messages
						definition.aggregate(simple("header.ExecutionId"), new ExceptionAggregationStrategy())
						// wait 30 sec for further failed records so that
						// not multiple mails are sent per job
						.completionTimeout(30000)
						.to("smtp:" + config.getMailserverConnection() + "&subject=New error file in integration job");
					}
				}
			});
		} catch (Exception e) {
			logger.error("could not enhance route", e);
		}
	}

	private List<RouteDefinition> getListOfJobs() {
		List<RouteDefinition> result = new ArrayList<>();
		for (RouteDefinition route : this.camelContext.getRouteDefinitions()) {
			if (route.getId().startsWith("job")) {
				result.add(route);
			}
		}
		return result;
	}

	@Override
	public void setCamelContext(CamelContext camelContext) {
		if (camelContext instanceof ModelCamelContext) {
			this.camelContext = (ModelCamelContext) camelContext;
		} else {
			throw new InvalidParameterException("ModelCamelContext expected!");
		}
	}

	@Override
	public CamelContext getCamelContext() {
		return this.camelContext;
	}

}
