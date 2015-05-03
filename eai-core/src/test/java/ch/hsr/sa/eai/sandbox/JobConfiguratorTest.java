package ch.hsr.sa.eai.sandbox;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class JobConfiguratorTest implements CamelContextAware {

	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

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

	@Override
	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;
	}

	@Override
	public CamelContext getCamelContext() {
		return this.camelContext;
	}
}
