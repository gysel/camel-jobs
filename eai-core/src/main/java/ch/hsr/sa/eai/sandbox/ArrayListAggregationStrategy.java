package ch.hsr.sa.eai.sandbox;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

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