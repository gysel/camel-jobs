package ch.hsr.sa.eai.sandbox.server.jmx;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component(value="ch.hsr.sa.eai:name=RouteManagement")
@ManagedResource(description="MBean for Route Management")
public class RouteManagement {

	private ProducerTemplate template;

	public RouteManagement() {
	}

	@Autowired
	public void setTemplate(ProducerTemplate template) {
		this.template = template;
	}

	@ManagedOperation(description="start a route")
	@ManagedOperationParameters({@ManagedOperationParameter(name = "routeName", description = "The name of the route to start")})
	public void startRoute(String routeName) {
		// TODO: use a command message?
		template.sendBodyAndHeader("jms:triggerQueue", "", "routeName", routeName);
	}
}
