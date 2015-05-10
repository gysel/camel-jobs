package ch.hsr.camel.jobs.errorhandling;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

@Component("exceptionAggregatorStrategy")
public class ExceptionAggregationStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Object executionId = newExchange.getIn().getHeader("ExecutionId");
		newExchange.getIn().setBody(
				"Processing failure in integration job " + executionId
						+ ", please check the failure file failedRecords/" + executionId);
		return newExchange; // no real aggregation needed
	}

}
