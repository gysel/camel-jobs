package org.apache.camel.component;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.nomin.core.Nomin;

/**
 * The Nomin producer.
 */
public class NominProducer extends DefaultProducer {
	private Nomin nomin;
	private final Class<?> destinationClass;

    public NominProducer(NominEndpoint endpoint) throws ClassNotFoundException {
        super(endpoint);
        destinationClass = Class.forName(endpoint.getDestination());
        nomin = new Nomin(endpoint.getMapping());
    }

    public void process(Exchange exchange) throws Exception {
    	Message message = exchange.getIn();
    	message.setBody(nomin.map(message.getBody(), destinationClass));
    }

}
