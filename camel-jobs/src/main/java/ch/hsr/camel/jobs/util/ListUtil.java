package ch.hsr.camel.jobs.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("listUtil")
public class ListUtil {
	
	private Integer recordsPerTransaction = 1;
	
	@Autowired
	public void configure(@Value("${batch.recordsPerTransaction}") String records) {
		recordsPerTransaction = new Integer(records);
	}

	public <T> List<List<T>> partition(List<T> list) {
		List<List<T>> result = new ArrayList<>();
		double parts = (double) list.size() / recordsPerTransaction;
		int partBegin = 0;
		for (int i = 1; i <= Math.ceil(parts); i++) {
			List<T> subList = new ArrayList<>();
			int toIndex = Math.min(list.size(), i * recordsPerTransaction);
			subList.addAll(list.subList(partBegin * recordsPerTransaction, toIndex));
			result.add(subList);
			partBegin = i;
		}
		return result;
	}

}
