package at.ac.univie.rscm.application.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class SSHConnectionManager {
	
	private PortScanner ps;
	
	public SSHConnectionManager() {
	}

	public static void main(String[] arg) {
		try {
		
			SSHConnectionBuilder cb = new SSHConnectionBuilder("rscm", "localhost", 22222,"test1234");
			System.out.println(cb.sendComand("dir"));
			cb.closeSession();
			SSHConnectionManager sm = new SSHConnectionManager();

		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
