package at.ac.univie.rscm.application.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import com.jcraft.jsch.*;

import lombok.Getter;
import lombok.Setter;

public class SSHConnectionBuilder {

	
	private Session session;
	@Getter
	private int port;
	@Getter
	@Setter
	private int keyId;
	
	public SSHConnectionBuilder(String user, String host, int port) throws JSchException, IOException {
		//Possibility not tested
		JSch jsch = new JSch();
		jsch.addIdentity("C:\\Users\\rscmserver\\.ssh\\id_rsa", "uxHqtAmt!eZ8Zn!e");
		session = jsch.getSession(user, host, port);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();

	}

	public SSHConnectionBuilder(String user, String host, int port, String password) throws JSchException, IOException {
		this.port = port;
		JSch jsch = new JSch();
		session = jsch.getSession(user, host, port);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.setPassword(password);
		session.connect();
		
	}
	
	public String sendFileToClient(File f) {
		//https://www.programcreek.com/java-api-examples/?class=com.jcraft.jsch.ChannelSftp&method=put
		Channel channel = null;
		try {
			channel = session.openChannel("sftp");
			channel.connect();
	        ChannelSftp channelSftp = (ChannelSftp) channel;
	        //channelSftp.cd("sd");
	        channelSftp.put(new FileInputStream(f), f.getName(),ChannelSftp.OVERWRITE);
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(channel != null) {
				channel.disconnect();
			}
		}
       	return "File successful uploaded";
	}

	public String sendComand(String command) throws JSchException, IOException {
		// https://stackoverflow.com/questions/4194439/sending-commands-to-server-via-jsch-shell-channel
		Channel channel = session.openChannel("exec");
		if(command.endsWith(".ps1")) {
			command = "powershell.exe -NoLogo -NoProfile -file C:\\Users\\rscm\\" + command;
		}
			
		((ChannelExec) channel).setCommand(command);
		channel.setInputStream(null);
		((ChannelExec) channel).setErrStream(System.err);
		InputStream in = channel.getInputStream();
		channel.connect();
		System.out.println("assdasd");
		System.out.println(command);
		StringBuilder sb = new StringBuilder();
		
		/*
		
		
		
		*/
		int counter =0;
		//http://www.jcraft.com/jsch/examples/Exec.java.html
		byte[] tmp=new byte[1024];
		 while(true){
			 if(counter >=7) {
					System.out.println("eeeeee");
					break;
				}
			 counter++;
			 System.out.println("1");
			 System.out.println("ssdsd " + channel.isClosed());
		        while(in.available()>0){
		        	counter=0;
		        	System.out.println("2");
		          int i=in.read(tmp, 0, 1024);
		          System.out.println("3");
		          if(i<0)break;
		          System.out.println("4");
		          sb.append(new String(tmp, 0, i));
		          System.out.println(new String(tmp, 0, i));
		        }
		        if(channel.isClosed()){
		          if(in.available()>0) continue; 
		          sb.append("exit-status: "+channel.getExitStatus());
		          break;
		        }
		        try{Thread.sleep(1000);}catch(Exception ee){}
		}
		
		/*
		int counter =0;
		byte[] tmp = new byte[1024];
		while (!channel.isClosed()) {
			System.out.println("1");
			counter++;
			if(counter >=7) {
				System.out.println("eeeeee");
				break;
			}
			while (in.available() > 0) {
				System.out.println("2");
				counter =0;
				int i = in.read(tmp, 0, 1024);
				System.out.println("3");
				if (i < 0)
					break;
				System.out.println("assdasd" + new String(tmp, 0, i));
				sb.append(new String(tmp, 0, i));
			}
		}
		sb.append("<exitCode>" + channel.getExitStatus() + "<\\exitCode>");
		System.out.println("assdasdsdsd");
		*/
		
		channel.disconnect();
		return sb.toString();
	}

	

	public void closeSession() {
		session.disconnect();
		
	}


}

