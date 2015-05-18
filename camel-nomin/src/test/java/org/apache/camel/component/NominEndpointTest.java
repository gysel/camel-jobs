package org.apache.camel.component;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class NominEndpointTest extends CamelTestSupport {

	@Test
	public void testCreateConsumer() throws Exception {
		int before = context.getRoutes().size();
		try {
			context.addRoutes(new RouteBuilder() {
				public void configure() {
						from("nomin:java.util.HashMap").to("direct:foobar");
				}
			});
			fail("a route with a nomin consumer (=from) is not supported!");
		} catch (UnsupportedOperationException e) {
			// exception expected!
		}
		assertEquals(before, context.getRoutes().size());
	}

}
