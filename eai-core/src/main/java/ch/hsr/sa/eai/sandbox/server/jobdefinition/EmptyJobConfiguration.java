package ch.hsr.sa.eai.sandbox.server.jobdefinition;

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
