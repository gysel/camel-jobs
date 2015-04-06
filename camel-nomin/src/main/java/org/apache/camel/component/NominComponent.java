package org.apache.camel.component;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.UriEndpointComponent;

/**
 * Represents the component that manages {@link camel-nomin}.
 */
public class NominComponent extends UriEndpointComponent {
    
    public NominComponent() {
        super(NominEndpoint.class);
    }

    public NominComponent(CamelContext context) {
        super(context, NominEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new NominEndpoint(uri, this, remaining);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
