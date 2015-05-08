package ch.hsr.sa.eai.sandbox.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

	public static <T> List<List<T>> partition(List<T> list, int size) {
		List<List<T>> result = new ArrayList<>();
		double parts = (double) list.size() / size;
		int partBegin = 0;
		for (int i = 1; i <= Math.ceil(parts); i++) {
			List<T> subList = new ArrayList<>();
			int toIndex = Math.min(list.size(), i * size);
			subList.addAll(list.subList(partBegin * size, toIndex));
			result.add(subList);
			partBegin = i;
		}
		return result;
	}

}
