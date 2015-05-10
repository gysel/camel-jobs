package ch.hsr.camel.jobs.trigger.rest;

import java.util.Date;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.camel.CamelContext;
import org.apache.camel.Header;
import org.apache.camel.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codahale.metrics.MetricRegistry;

import ch.hsr.camel.jobs.trigger.rest.api.JobStatus;

@Component("restRouteStatus")
public class RestRouteStatus {

	@Autowired
	CamelContext context;

	@Autowired
	MetricRegistry metricRegistry;

	@Autowired
	MetricHelper metricHelper;

	public JobStatus process(@Header("jobName") String jobName) throws MalformedObjectNameException, InstanceNotFoundException, ReflectionException, MBeanException {
		List<Route> routes = context.getRoutes();
		Date lastSuccessfulRun = null;
		
		for (Route route : routes) {
			if (route.getId().equals(jobName)) {
				ObjectName objectName = context.getManagementStrategy().getManagementNamingStrategy().getObjectNameForRoute(route);
				lastSuccessfulRun = (Date) context.getManagementStrategy().getManagementAgent().getMBeanServer().invoke(objectName, "getLastExchangeCompletedTimestamp", null, null);
			}
		}
		
		JobStatus jobStatus = new JobStatus(jobName, lastSuccessfulRun);
		metricHelper.update(jobStatus);
		return jobStatus;
	}

}
