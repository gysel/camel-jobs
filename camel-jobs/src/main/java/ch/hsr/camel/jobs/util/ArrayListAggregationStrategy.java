package ch.hsr.camel.jobs.util;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

/**
 * Aggregation strategy used to aggregate all messages processed in a splitter into one message as the resulting message
 * of a split action. See http://camel.apache.org/splitter.html.
 */
@Component("listAggregationStrategy")
public class ArrayListAggregationStrategy implements AggregationStrategy {

	public ArrayListAggregationStrategy() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Message newIn = newExchange.getIn();
		Object newBody = newIn.getBody();
		ArrayList<Object> list = null;
		if (oldExchange == null) {
			list = new ArrayList<Object>();
			list.add(newBody);
			newIn.setBody(list);
			return newExchange;
		} else {
			Message in = oldExchange.getIn();
			list = in.getBody(ArrayList.class);
			list.add(newBody);
			return oldExchange;
		}
	}

}