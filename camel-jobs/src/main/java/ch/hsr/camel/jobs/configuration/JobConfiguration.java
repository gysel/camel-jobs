package ch.hsr.camel.jobs.configuration;

public interface JobConfiguration {
	
	boolean shouldSendEmails();

	String getMailserverConnection();
}
