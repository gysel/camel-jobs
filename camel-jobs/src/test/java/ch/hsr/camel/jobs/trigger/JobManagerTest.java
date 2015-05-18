package ch.hsr.camel.jobs.trigger;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.hsr.camel.jobs.trigger.rest.api.JobResult;

@ContextConfiguration("/JobManagerTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JobManagerTest implements CamelContextAware {

	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

	private CamelContext camelContext;

	@Autowired
	private JobManager jobManager;

	@Test
	public void testRoutesCanRunInParallel() throws Exception {
		resultEndpoint.expectedMessageCount(2);
		resultEndpoint.setResultWaitTime(6000);
		// start two routes in parallel
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.initialize();
		executor.execute(new JobRunner());
		executor.execute(new JobRunner());

		resultEndpoint.assertIsSatisfied();
	}

	@Test
	public void testMetricsSuccessfullRecords() {
		int[] intArray = new int[] { 1, 2, 3, 4, 5 };
		JobResult result = jobManager.startJob("job-testroute-countRecords", intArray);
		Assert.assertTrue(intArray.length == result.getSuccessfulRecords());
	}

	@Test
	public void testPerformanceInJobResult() {
		JobResult result = jobManager.startJob("job-testroute-delayed", null);
		Assert.assertTrue(result.getJobDurationInMilis() > 2000);
	}

	public class JobRunner implements Runnable {
		@Override
		public void run() {
			jobManager.startJob("job-testroute-delayed");
		}
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
		MockEndpoint.resetMocks(camelContext);
	}

}
