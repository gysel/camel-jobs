package ch.hsr.sa.eai.sandbox;

import ch.hsr.sa.eai.sandbox.server.jobdefinition.JobConfiguration;

public class TestJobConfiguration implements JobConfiguration {

	@Override
	public String getMailserverConnection() {
		return "user@server:25?password=password";
	}

	@Override
	public boolean shouldSendEmails() {
		return true;
	}

}
