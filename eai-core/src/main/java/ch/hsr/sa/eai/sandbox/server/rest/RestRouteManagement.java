package ch.hsr.sa.eai.sandbox.server.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "restRouteManagement")
public class RestRouteManagement {

	private ProducerTemplate template;

	@Autowired
	public void setTemplate(ProducerTemplate template) {
		this.template = template;
	}
	
	public String process(Exchange exchange) {
		HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);
		if("GET".equals(request.getMethod())) {
			return "job list: fx-import, foo, bar";
		} else if ("POST".equals(request.getMethod())) {
			String jobName = request.getParameter("jobName");
			template.sendBodyAndHeader("jms:triggerQueue", "", "routeName", jobName);
			return "start: " + jobName;
		}
		return "unsupported operation!";
	}

}
