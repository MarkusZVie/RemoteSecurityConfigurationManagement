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

public class SSHManager {
	
	private PortScanner ps;
	
	
	
	public SSHManager() {
		PortScanner ps = PortScanner.getInstance();
		ps.setScanPortBegin(22220);
		ps.setScanPortEnd(22500);
		ps.start();
	}



	public static void main(String[] arg) {
		try {
		
			
			
			SSHConnectionBuilder cb = new SSHConnectionBuilder("rscm", "localhost", 22222,"test1234");
			System.out.println(cb.sendComand("dir"));
			cb.closeSession();
			SSHManager sm = new SSHManager();

		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
