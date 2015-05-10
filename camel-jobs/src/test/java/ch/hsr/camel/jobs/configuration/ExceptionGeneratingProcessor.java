package ch.hsr.camel.jobs.configuration;

import java.text.ParseException;

import org.springframework.stereotype.Component;

@Component("exceptionGeneratingProcesser")
public class ExceptionGeneratingProcessor {

	public Object process(Object object) throws ParseException {
		if (object != null) {
			throw new RuntimeException("exception generated for test cases");
		}
		return object;
	}

}
