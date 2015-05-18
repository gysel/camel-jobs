package ch.hsr.camel.jobs.trigger.jms;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

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

import ch.hsr.camel.jobs.trigger.rest.api.JobStatus;
import ch.hsr.camel.jobs.trigger.rest.api.Jobs;

import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration("/test-context.xml")
public class RestRoutesTest extends AbstractJUnit4SpringContextTests {

	private static final String JOB_NAME = "job-testjob";

	@Autowired
	protected CamelContext camelContext;

	@Produce(uri = "direct:load-job-list")
	private ProducerTemplate loadJobList;

	@Produce(uri = "direct:load-job-details")
	private ProducerTemplate loadJobDetails;

	@EndpointInject(uri = "mock:jobList")
	protected MockEndpoint jobList;
	
	@EndpointInject(uri = "mock:jobStatus")
	protected MockEndpoint jobStatus;

	@Test
	@DirtiesContext
	public void testGetJobList() throws Exception {
		jobList.expectedMessageCount(1);
		// use the test route to download the json file
		loadJobList.sendBody(null);
		// verify result
		jobList.assertIsSatisfied();
		String jsonFile = jobList.getReceivedExchanges().get(0).getIn().getBody(String.class);
		assertThat(jsonFile, containsString(JOB_NAME));
		// parse the json file
		ObjectMapper mapper = new ObjectMapper();
		Jobs actualJobs = mapper.readValue(jsonFile, Jobs.class);
		assertThat(actualJobs.getJobs().size(), is(1));
		assertThat(actualJobs.getJobs().get(0).getName(), is(JOB_NAME));
	}

	@Test
	@DirtiesContext
	public void testGetJobDetail() throws Exception {
		jobStatus.expectedMessageCount(1);
		// use the test route to download the json file
		loadJobDetails.sendBody(null);
		// verify result
		jobStatus.assertIsSatisfied();
		String jsonFile = jobStatus.getReceivedExchanges().get(0).getIn().getBody(String.class);
		assertThat(jsonFile, containsString(JOB_NAME));
		// parse the json file
		ObjectMapper mapper = new ObjectMapper();
		JobStatus jobStatus = mapper.readValue(jsonFile, JobStatus.class);
		assertThat(jobStatus.getJobName(), is(JOB_NAME));
		assertThat(jobStatus.getSuccessfulRecords(), is(0L));
	}

	@Before
	public void setUp() throws Exception {
		camelContext.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("vm:trigger-" + JOB_NAME).id(JOB_NAME).log("job running").end();
				from("direct:load-job-list").to("ahc:http://localhost:12345/jobs/").convertBodyTo(String.class).to("mock:jobList");
				from("direct:load-job-details").to("ahc:http://localhost:12345/jobs/" + JOB_NAME).convertBodyTo(String.class).to("mock:jobStatus");
			}

		});
	}
}