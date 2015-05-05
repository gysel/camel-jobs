package ch.hsr.sa.eai.sandbox.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ListUtilTest {

	@Test
	public void testPartition() {
		List<Integer> given = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
		//
		List<List<Integer>> partitions = ListUtil.partition(given, 2);
		//
		assertEquals(5, partitions.size());
		assertEquals((Integer) 1, partitions.get(0).get(0));
		assertEquals((Integer) 2, partitions.get(0).get(1));
		assertEquals((Integer) 3, partitions.get(1).get(0));
	}

}
