package org.apache.camel.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.Car.EngineType;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class NominComponentTest extends CamelTestSupport {

	@Test
	public void testMapToCar() throws Exception {
		Map<String, Object> in = new HashMap<>();
		in.put("MARKE", "Tesla");
		in.put("MODELL", "Model S");
		in.put("MOTORENTYP", "Elektrisch");
		//
		MockEndpoint mock = getMockEndpoint("mock:carResult");
		template().sendBody("vm://map2Car", in);
		//
		mock.expectedMinimumMessageCount(1);
		assertMockEndpointsSatisfied();
		//
		List<Exchange> receivedExchanges = mock.getReceivedExchanges();
		Exchange exchange = receivedExchanges.get(0);
		assertEquals(Car.class, exchange.getIn().getBody().getClass());
		//
		Car actual = (Car) exchange.getIn().getBody();
		assertEquals("Tesla", actual.getBrand());
		assertEquals("Model S", actual.getModel());
		assertEquals(new Integer(4), actual.getNrOfWheels());
		assertEquals(EngineType.ELECTRIC, actual.getEngineType());
	}

	@Test
	public void testCarToMap() throws Exception {
		Car in = new Car();
		in.setBrand("Paramount Group");
		in.setModel("Marauder");
		in.setEngineType(EngineType.DIESEL);
		//
		MockEndpoint mock = getMockEndpoint("mock:mapResult");
		template().sendBody("vm://car2map", in);
		//
		mock.expectedMinimumMessageCount(1);
		assertMockEndpointsSatisfied();
		//
		List<Exchange> receivedExchanges = mock.getReceivedExchanges();
		Exchange exchange = receivedExchanges.get(0);
		assertEquals(HashMap.class, exchange.getIn().getBody().getClass());
		//
		@SuppressWarnings("unchecked")
		Map<String, Object> actual = (Map<String, Object>) exchange.getIn()
				.getBody();
		assertEquals("Paramount Group", actual.get("MARKE"));
		assertEquals("Marauder", actual.get("MODELL"));
		assertEquals(EngineType.DIESEL, actual.get("MOTORENTYP"));
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			public void configure() {
				from("vm://map2Car")
						.to("nomin:org.apache.camel.component.Car?mapping=map2car.groovy")
						.to("mock:carResult");
				//
				from("vm://car2map").to(
						"nomin:java.util.HashMap?mapping=map2car.groovy").to(
						"mock:mapResult");
			}
		};
	}

}
