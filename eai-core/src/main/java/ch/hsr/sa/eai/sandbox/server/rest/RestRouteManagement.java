package ch.hsr.sa.eai.sandbox.server.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.springframework.stereotype.Component;

import ch.hsr.sa.eai.sandbox.server.rest.api.Job;
import ch.hsr.sa.eai.sandbox.server.rest.api.Jobs;

@Component(value = "restRouteManagement")
public class RestRouteManagement {

	private static final String ROUTE_PREFIX = "vm://trigger-";

	public Jobs process(Exchange exchange) {
		List<Route> routes = exchange.getContext().getRoutes();
		List<Job> result = new ArrayList<>();
		for (Route route : routes) {
			Endpoint endpoint = route.getEndpoint();
			if (endpoint.getEndpointUri().startsWith(ROUTE_PREFIX)) {
				String jobName = endpoint.getEndpointUri().replace(
						ROUTE_PREFIX, "");
				String uri = (String) exchange.getIn()
						.getHeader("CamelHttpUrl") + "/" + jobName;
				result.add(new Job(jobName, uri));
			}
		}
		return new Jobs(result);
	}

}
