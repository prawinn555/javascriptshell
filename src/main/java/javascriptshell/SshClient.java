package javascriptshell;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SshClient {

	
	// session ssh timeout valeur par défaut 5s
	private static final int SESSION_TIMEOUT = 5;



	private Session session;
	private ChannelShell channel;
    public ChannelShell getChannel() {
		return channel;
	}


	private OutputStream out;
	private InputStream in;
    
	public OutputStream getOut() {
		return out;
	}

	public InputStream getIn() {
		return in;
	}

	/**
	 * 
	 * @param env ex. "e8a10@achille".
	 * @throws Exception 
	 */
	public SshClient(final String user, String password, String host, int port) throws Exception {
		super();


		final JSch jsch = new JSch();

		    session = jsch.getSession(user, host, port);
	        // username and password will be given via UserInfo interface.
	        session.setPassword(password);
		    
			final Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setTimeout(SESSION_TIMEOUT * 1000* 60);
			session.setServerAliveInterval(1000*1);
	}
	
	public void connect() throws Exception {
		session.connect();
		channel = (ChannelShell) session.openChannel("shell");
		channel.setPtyType("xterm");
		
		channel.connect();
		out = channel.getOutputStream();
		in = channel.getInputStream();

	}
	
	
	public void disconnect() {
		try {
			if(channel!=null) {
				channel.disconnect();
			}
		} finally {
			session.disconnect();
		}
	}




}

