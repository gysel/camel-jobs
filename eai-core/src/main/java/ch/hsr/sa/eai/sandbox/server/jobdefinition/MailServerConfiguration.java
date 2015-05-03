package ch.hsr.sa.eai.sandbox.server.jobdefinition;

public class MailServerConfiguration {
	private String user;
	private String server;
	private String port;
	private String password;

	public MailServerConfiguration(String user, String server, String port, String password) {
		super();
		this.user = user;
		this.server = server;
		this.port = port;
		this.password = password;
	}

	public String getConnectionString() {
		StringBuffer result = new StringBuffer();
		result.append(user);
		result.append('@');
		result.append(server);
		result.append(':');
		result.append(port);
		result.append("?password=");
		result.append(password);
		return result.toString();
	}

}
