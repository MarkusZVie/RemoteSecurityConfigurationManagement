package at.ac.univie.rscm.application.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

public class PortScanner extends Thread implements PortScannerInterface{

	private static PortScanner portscanner;
	@Getter
	@Setter
	private int scanPortBegin;
	@Getter
	@Setter
	private int scanPortEnd;
	@Getter
	private Set<Integer> openPorts;
	
	@Setter
	@Getter
	private int timeSpaceScanningMS;
	
	private PortScanner() {
		scanPortBegin = 22000;
		scanPortEnd = 22001;
		openPorts = new HashSet<Integer>();
		double time = (double)120000/(double)(scanPortEnd-scanPortBegin);
		if(time > 1000) {
			time = 1000;
		}
		timeSpaceScanningMS = (int)time;
	}
	
	public static PortScanner getInstance() {
		if(portscanner == null) {
			portscanner = new PortScanner();
		}
		return portscanner;
	}
	
	@Override
	public void run() {
		for (int port = 1; port <= 65535; port++) {
	         try {
	        	 
	            Socket socket = new Socket();
	            socket.connect(new InetSocketAddress("localhost", port),1);
	            socket.close();
	            openPorts.add(port);
	        } catch (IOException ex) {
	        	if(openPorts.contains(port)) {
	        		openPorts.remove(port);
	        	}
	        }
	      }
	}
	
	
	
}
