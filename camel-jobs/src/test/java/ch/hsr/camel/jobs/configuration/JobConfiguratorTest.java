package ch.hsr.camel.jobs.configuration;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("/JobConfiguratorTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JobConfiguratorTest implements CamelContextAware {

	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

	@Produce(uri = "direct:start-exception")
	protected ProducerTemplate templateWithException;

	private CamelContext camelContext;

	@Test
	public void testExecutionIdIsSet() throws Exception {
		String expectedBody = "Message";

		resultEndpoint.expectedBodiesReceived(expectedBody);
		template.sendBody(expectedBody);
		resultEndpoint.assertIsSatisfied();
		Object id = resultEndpoint.getExchanges().get(0).getIn().getHeader("ExecutionId");
		Assert.assertNotNull(id);
		Assert.assertFalse(id.equals(""));
	}

	@Test
	public void testOnExceptionHandlerExists() throws Exception {
		resultEndpoint.expectedMessageCount(1);
		templateWithException.sendBody(new int[] { 1, 2, 3 });
		resultEndpoint.assertIsSatisfied();
		Object id = resultEndpoint.getExchanges().get(0).getIn().getHeader("ExecutionId");

		String expectedFile = "failedRecords/" + id + "-failed";
		resultEndpoint.expectedFileExists(expectedFile, "123");
		resultEndpoint.assertIsSatisfied();

		// cleanup
		new File(expectedFile).delete();
	}

	@Override
	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;
	}

	@Override
	public CamelContext getCamelContext() {
		return this.camelContext;
	}

	@After
	public void cleanup() {
		resultEndpoint.reset();
	}

}
