package ch.hsr.sa.eai.sandbox;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;
import org.apache.camel.impl.DefaultExchange;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;

public class ArrayListAggregationStrategyTest {

	private ArrayListAggregationStrategy strategy;
	private CamelContext context;
	private TypeConverter converter;

	@Test
	public void createList() {
		Integer payload = 42;
		//
		Exchange oldExchange = null;
		Exchange newExchange = new DefaultExchange(context);
		newExchange.getIn().setBody(payload);
		//
		Exchange aggregate = strategy.aggregate(oldExchange, newExchange);
		//
		Object actualPayload = aggregate.getIn().getBody();
		assertThat(actualPayload, IsInstanceOf.instanceOf(ArrayList.class));
		@SuppressWarnings("unchecked")
		List<Integer> actualPayloadList = (List<Integer>) actualPayload;
		assertEquals(1, actualPayloadList.size());
		assertEquals(payload, actualPayloadList.get(0));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void updateList() {
		Integer existingListEntry = 42;
		Integer payload = 43;
		ArrayList<Integer> existingList = new ArrayList<>();
		existingList.add(existingListEntry);
		//
		Exchange oldExchange = new DefaultExchange(context);
		oldExchange.getIn().setBody(existingList);
		Exchange newExchange = new DefaultExchange(context);
		newExchange.getIn().setBody(payload);
		when(context.getTypeConverter()).thenReturn(converter);
		when(converter.convertTo((Class<ArrayList<Integer>>) any(), (Exchange)anyObject(), anyObject())).thenReturn(existingList);
		//
		Exchange aggregate = strategy.aggregate(oldExchange, newExchange);
		//
		Object actualPayload = aggregate.getIn().getBody();
		assertThat(actualPayload, IsInstanceOf.instanceOf(ArrayList.class));
		List<Integer> actualPayloadList = (List<Integer>) actualPayload;
		assertEquals(2, actualPayloadList.size());
		assertEquals(existingListEntry, actualPayloadList.get(0));
		assertEquals(payload, actualPayloadList.get(1));
	}

	@Before
	public void setUp() {
		strategy = new ArrayListAggregationStrategy();
		context = mock(CamelContext.class);
		converter = mock(TypeConverter.class);
	}

}
