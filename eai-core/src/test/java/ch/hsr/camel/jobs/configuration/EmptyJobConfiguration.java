package ch.hsr.camel.jobs.configuration;

public class EmptyJobConfiguration implements JobConfiguration {

	@Override
	public String getMailserverConnection() {
		return "";
	}

	@Override
	public boolean shouldSendEmails() {
		return false;
	}

}
