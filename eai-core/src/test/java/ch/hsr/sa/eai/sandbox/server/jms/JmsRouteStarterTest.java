package ch.hsr.sa.eai.sandbox.server.jms;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult;
import ch.hsr.sa.eai.sandbox.server.rest.api.JobResult.Status;

@ContextConfiguration("/test-context.xml")
@DirtiesContext
public class JmsRouteStarterTest extends AbstractJUnit4SpringContextTests {

	private static final String JOB_NAME = "somejob";

	@Autowired
	protected CamelContext camelContext;

	@EndpointInject(uri = "mock:jmsreply")
	protected MockEndpoint resultEndpoint;

	@Produce(uri = "direct:send-jms")
	protected ProducerTemplate template;

	@Test
	public void testSendMessageAndExpectResult() throws Exception {
		JobResult expectedResult = new JobResult();
		expectedResult.setJobName(JOB_NAME);
		expectedResult.setStatus(Status.SUCCESSFUL);

		resultEndpoint.expectedMessageCount(1);
		resultEndpoint.expectedBodiesReceived(JOB_NAME + expectedResult.getDetails());

		template.sendBodyAndHeader(null, "JobName", JOB_NAME);

		resultEndpoint.assertIsSatisfied();
	}

	@Before
	public void setUp() throws Exception {
		camelContext.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from("vm:trigger-somejob").id(JOB_NAME).to("log:ch.hsr.sa.eai.sandbox.server.jms").end(); // dummy
																											// job
																											// to
																											// be
																											// started
																											// from
																											// JMS
				from("direct:send-jms").inOut("jms:JobTrigger?exchangePattern=InOut").to("mock:jmsreply");
			}

		});
	}

}