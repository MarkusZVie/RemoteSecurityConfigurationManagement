package at.ac.univie.rscm.application.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;
import at.ac.univie.rscm.model.RSCMClient;

public class SSHConnectionManager implements SSHConnectionManagerInterface{
	
	private GlobalSettingsAndVariablesInterface gsav;
	private PortScannerInterface ps;
	private static SSHConnectionManager sshConnectionManager;
	private Map<Integer, SSHConnectionBuilder> activeClientList;
	
	public SSHConnectionManager() {
		activeClientList = new HashMap<Integer, SSHConnectionBuilder>();
		gsav = GlobalSettingsAndVariables.getInstance();
		ps = gsav.getPortScanner();
	}
	
	public static SSHConnectionManager getInstance() {
		if(sshConnectionManager == null) {
			sshConnectionManager = new SSHConnectionManager();
		}
		return sshConnectionManager;
	}
	
	public SSHConnectionBuilder getConnection(int keyId) {
		if(activeClientList.containsKey(keyId)) {
			return activeClientList.get(keyId);
		}else {
			Optional<RSCMClient> orc = gsav.getRSCMClientRepository().findById(keyId);
			if (!orc.isPresent()) {
				//client not found
				return null;
			}
			RSCMClient client = orc.get();
			if(!ps.getOpenPorts().containsKey(client.getClientPort())) {
				//port not open
				return null;
			}
			try {
				SSHConnectionBuilder sb = new SSHConnectionBuilder("rscm", "localhost", client.getClientPort(), client.getRscmPassword());
				sb.setKeyId(keyId);
				activeClientList.put(keyId, sb);
				return sb;
			} catch (JSchException | IOException e) {
				e.printStackTrace();
				//error
				return null;
			}
		}
	}
	
	public synchronized void removeActiveClient(int port) {
		int keyIdFound = -1;
		for (Map.Entry<Integer, SSHConnectionBuilder> entry : activeClientList.entrySet()) {
		    if(entry.getValue().getPort()==port) {
		    	keyIdFound= entry.getKey();
		    	break;
		    }
		}
		if(activeClientList.containsKey(keyIdFound)) {
			activeClientList.get(keyIdFound).closeSession();
			activeClientList.remove(keyIdFound);
		}
	}
	

	}
	
	
	

