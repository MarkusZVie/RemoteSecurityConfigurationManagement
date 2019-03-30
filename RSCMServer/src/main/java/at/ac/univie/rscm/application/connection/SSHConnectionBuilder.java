package at.ac.univie.rscm.application.connection;

import java.io.IOException;
import java.io.InputStream;
import com.jcraft.jsch.*;

public class SSHConnectionBuilder {

	
	private Session session;
	
	public SSHConnectionBuilder(String user, String host, int port) throws JSchException, IOException {

		JSch jsch = new JSch();
		jsch.addIdentity("C:\\Users\\rscmserver\\.ssh\\id_rsa", "uxHqtAmt!eZ8Zn!e");
		session = jsch.getSession(user, host, port);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();

	}

	public SSHConnectionBuilder(String user, String host, int port, String password) throws JSchException, IOException {
		JSch jsch = new JSch();
		session = jsch.getSession(user, host, port);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.setPassword(password);
		session.connect();
		
	}

	public String sendComand(String command) throws JSchException, IOException {
		// https://stackoverflow.com/questions/4194439/sending-commands-to-server-via-jsch-shell-channel
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(command);
		channel.setInputStream(null);
		((ChannelExec) channel).setErrStream(System.err);
		InputStream in = channel.getInputStream();
		channel.connect();
		
		StringBuilder sb = new StringBuilder();
		byte[] tmp = new byte[1024];
		while (!channel.isClosed()) {
			
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				sb.append(new String(tmp, 0, i));
			}
		}
		sb.append("<exitCode>" + channel.getExitStatus() + "<\\exitCode>");
		channel.disconnect();
		return sb.toString();
	}

	

	public void closeSession() {
		session.disconnect();
		
	}

}

