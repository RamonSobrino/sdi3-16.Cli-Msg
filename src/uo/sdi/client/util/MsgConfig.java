package uo.sdi.client.util;

public class MsgConfig {

	private static MsgConfig config;
	private final String JMS_CONNECTION_FACTORY =
			"jms/RemoteConnectionFactory";
	private final String NOTANEITOR_QUEUE = "jms/queue/TaskManager";
	
	private String user;
	private String password;
	
	private MsgConfig(){}
	
	public static MsgConfig getInstance(){
		if(config==null)
		{
			config = new MsgConfig();
		}
			return config;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getNOTANEITOR_QUEUE() {
		return NOTANEITOR_QUEUE;
	}

	public String getJMS_CONNECTION_FACTORY() {
		return JMS_CONNECTION_FACTORY;
	}

}
