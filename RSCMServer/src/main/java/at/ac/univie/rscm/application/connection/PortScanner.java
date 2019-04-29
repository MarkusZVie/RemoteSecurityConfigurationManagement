package at.ac.univie.rscm.application.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;
import at.ac.univie.rscm.model.RSCMClientConnection;
import lombok.Getter;
import lombok.Setter;

public class PortScanner extends Thread implements PortScannerInterface {

	@Getter
	@Setter
	private int scanPortBegin;
	@Getter
	private int scanPortEnd;
	private Map<Integer, Date> openPorts;
	@Setter
	@Getter
	private int timeSpaceScanningMS;
	private GlobalSettingsAndVariablesInterface gsav;

	public PortScanner() {
		gsav = GlobalSettingsAndVariables.getInstance();
		scanPortBegin = 22000;
		scanPortEnd = 22001;
		openPorts = new HashMap<Integer, Date>();
		calcTimePeriod();
	}

	@Override
	public void run() {
		while (true) {
			for (int port = scanPortBegin; port <= scanPortEnd; port++) {
				try {
					Thread.sleep(timeSpaceScanningMS);
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress("localhost", port), 1);
					socket.close();
					if(!openPorts.containsKey(port)) {
						openPorts.put(port, new Date());
					}
					
				} catch (IOException ex) {
					if (openPorts.containsKey(port)) {
						RSCMClientConnection rscmClientConnection = new RSCMClientConnection();
						rscmClientConnection.setConnectionStart(openPorts.get(port));
						rscmClientConnection.setConnectionEnd(new Date());
						rscmClientConnection.setConnectionExitCode("0;"+port);
						//rscmClientConnection.setConnectionDescription("fine");
						gsav.addConnectionLog(rscmClientConnection);
						openPorts.remove(port);
						SSHConnectionManagerInterface cm= SSHConnectionManager.getInstance();
						cm.removeActiveClient(port);
						
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public synchronized void setScanPortEnd(int port) {
		scanPortEnd = port;
		calcTimePeriod();

	}

	private void calcTimePeriod() {
		double time = (double) 120000 / (double) (scanPortEnd - scanPortBegin);
		if (time > 1000) {
			time = 1000;
		}
		timeSpaceScanningMS = (int) time;

	}

	@Override
	public synchronized Map<Integer, Date> getOpenPorts() {
		return openPorts;
	}

}
