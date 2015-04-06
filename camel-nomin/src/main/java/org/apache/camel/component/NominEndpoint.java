package org.apache.camel.component;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a Nomin endpoint.
 */
@UriEndpoint(scheme = "nomin", title = "Nomin", syntax = "nomin:destination", producerOnly = true, label = "transformation")
public class NominEndpoint extends DefaultEndpoint {
	@UriPath(name = "destination", defaultValue = "java.util.Map")
	@Metadata(required = "true")
	private String destination;
	@UriParam(defaultValue = "mapping.groovy")
	private String mapping;

	public NominEndpoint() {
	}

	public NominEndpoint(String uri, NominComponent component, String remaining) {
		super(uri, component);
		destination = remaining;
	}

	public Producer createProducer() throws Exception {
		return new NominProducer(this);
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		throw new UnsupportedOperationException(
				"Consumer not supported for Nomin endpoints");
	}

	public boolean isSingleton() {
		return true;
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

}
