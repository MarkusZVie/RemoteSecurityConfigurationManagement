package at.ac.univie.rscm.application.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;
import at.ac.univie.rscm.model.Environment;
import at.ac.univie.rscm.model.RSCMClientConnection;
import at.ac.univie.rscm.spring.api.repository.EnvironmentRepository;
import lombok.Getter;
import lombok.Setter;

public class PortScanner extends Thread implements PortScannerInterface {

	@Getter
	@Setter
	private int scanPortBegin;
	@Getter
	private int scanPortEnd;
	private Map<Integer, Date> openPorts;
	private Map<Integer, Integer> openEnvironments;
	@Setter
	@Getter
	private int timeSpaceScanningMS;
	private GlobalSettingsAndVariablesInterface gsav;
	private int scriptsHasChanged;
	
	public PortScanner() {
		scriptsHasChanged = 0;
		gsav = GlobalSettingsAndVariables.getInstance();
		scanPortBegin = 22000;
		scanPortEnd = 22001;
		openPorts = new HashMap<Integer, Date>();
		openEnvironments = new HashMap<Integer, Integer>();
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
					boolean shouldExecuteScripts = false;
					if(!openPorts.containsKey(port)) {
						openPorts.put(port, new Date());
						shouldExecuteScripts = true;
					}
					if(scriptsHasChanged==port) {
						scriptsHasChanged = 0;
					}
					
					if(scriptsHasChanged>0) {
						scriptsHasChanged = port;
						shouldExecuteScripts = true;
					}
					
					
					
					if(shouldExecuteScripts) {
						SSHConnectionScriptExecution sshConnectionScriptExecution = new SSHConnectionScriptExecution(port);
						sshConnectionScriptExecution.start();
					}
					
				} catch (IOException ex) {
					if (openPorts.containsKey(port)) {
						RSCMClientConnection rscmClientConnection = new RSCMClientConnection();
						rscmClientConnection.setConnectionStart(openPorts.get(port));
						rscmClientConnection.setConnectionEnd(new Date());
						rscmClientConnection.setConnectionExitcode("0;"+port);
						
						EnvironmentRepository er = gsav.getEnvironmentRepository();
						
						Optional<Environment> oe = er.findById(openEnvironments.get(port));
						if(oe.isPresent()) {
							rscmClientConnection.setEnvironment(oe.get());
						}else {
							System.err.println("Saved environment is not present (portscanner)");
						}
						
						
						
						
						
						//rscmClientConnection.setConnectionDescription("fine");
						gsav.addConnectionLog(rscmClientConnection);
						openPorts.remove(port);
						openEnvironments.remove(port);
						SSHConnectionManagerInterface cm= SSHConnectionManager.getInstance();
						cm.removeActiveClient(port);
						
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//that the for loop is at least 1 time visited
			if(scriptsHasChanged >=2) {
				scriptsHasChanged=0;
			}
			if(scriptsHasChanged==1) {
				scriptsHasChanged++;
			}
		
		}
	}

	@Override
	public synchronized void addEnvironment(int environmentId, int portNumber) {
		openEnvironments.put(portNumber, environmentId);
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

	@Override
	public synchronized void scriptsAssignmentHasChaneged() {
		scriptsHasChanged = 1;
		
	}

}
