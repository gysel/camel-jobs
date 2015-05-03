package ch.hsr.sa.eai.sandbox.server.jobdefinition;

public interface JobConfiguration {
	
	boolean shouldSendEmails();

	String getMailserverConnection();
}
