package at.ac.univie.rscm.application.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import at.ac.univie.rscm.application.connection.PortScanner;
import at.ac.univie.rscm.application.connection.PortScannerInterface;
import at.ac.univie.rscm.model.RSCMClient;
import at.ac.univie.rscm.model.RSCMClientConnection;
import at.ac.univie.rscm.spring.api.repository.ApplicantRepository;
import at.ac.univie.rscm.spring.api.repository.EnvironmentRepository;
import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;
import at.ac.univie.rscm.spring.api.repository.RoleRepository;
import at.ac.univie.rscm.spring.api.repository.ScriptexecutionRepository;
import lombok.Getter;
import lombok.Setter;

public class GlobalSettingsAndVariables implements GlobalSettingsAndVariablesInterface{
	private static GlobalSettingsAndVariables gsav;	
	@Getter
	@Setter
	private String server_intern_ip_value;
	@Getter
	@Setter
	private String server_extern_ip_value;
	@Getter
	@Setter
	private String server_rsafingerprint_value;
	@Getter
	@Setter
	private String server_publickey_value;
	@Getter
	@Setter
	private int portBeginRange;
	@Getter
	@Setter
	private int portEndRange;
	@Getter
	@Setter
	private RSCMClientRepository rSCMClientRepository;
	@Getter
	@Setter
	private ApplicantRepository applicantRepository;
	@Getter
	@Setter
	private EnvironmentRepository environmentRepository;
	@Getter
	@Setter
	private RoleRepository roleRepository;
	@Setter
	@Getter
	private Map<String, String> providedDownloads;
	
	@Getter
	private PortScannerInterface portScanner;
	
	@Getter
	private String pathToAuthorized_keys;
	
	private int portNumber;
	@Getter
	@Setter
	private String fileDownloadDirectory;
	@Getter
	@Setter
	private SimpleDateFormat dateTime;
	@Getter
	@Setter
	private SimpleDateFormat date;
	
	@Getter
	@Setter
	private ScriptexecutionRepository scriptexecutionRepository;
	
	private GlobalSettingsAndVariables() {
		
		dateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		date = new SimpleDateFormat("dd-MM-yyyy");
		
		try {
			fileDownloadDirectory = new File(".").getCanonicalPath() + "\\src\\main\\webapp\\WEB-INF\\managementFiles";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		providedDownloads = new HashMap<String, String>();
		server_intern_ip_value = "192.168.178.10";
		server_extern_ip_value = "77.119.228.8";
		server_rsafingerprint_value = "ecdsa-sha2-nistp256 AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAyNTYAAABBBAYu3YwUiKt2/pnh8wiXHHAHLhGB8xhrsKSE1vwpoTO89LYFh9Pf1MGGdoDhLzKLTlJRKVmu6bq5ZNCzQmKfHdM=";
		pathToAuthorized_keys = "C:\\Users\\rscmserver\\.ssh\\authorized_keys";
		server_publickey_value = getHostPublicKey();
		portNumber = 22000;
	}
	
	public void addDownload(String key, String value) {
		providedDownloads.put(key, value);
	}
	
	
	
	
	@Override
	public void initPortScanner() {
		Iterator<Integer> dbPortResult = rSCMClientRepository.getHighestPort().iterator();
		int highestUsedPortnumber = 0;
		
		if(dbPortResult.hasNext()) {
			String portString = dbPortResult.next()+"";
			if(!portString.equals("null")) {
				highestUsedPortnumber = Integer.parseInt(portString);
			}else {
				highestUsedPortnumber = 22000;
			}
			
		}else {
			highestUsedPortnumber = 22000;
		}
		portNumber = highestUsedPortnumber;
		
		portScanner = new PortScanner();
		portScanner.setScanPortBegin(22000);
		portScanner.setScanPortEnd(highestUsedPortnumber);
		portScanner.start();
		
	}

	public static GlobalSettingsAndVariables getInstance() {
		if(gsav == null) {
			gsav = new GlobalSettingsAndVariables();
		}
		return gsav;
	}
	
	public int getPortNumber() {
		return portNumber++;
	}


	
	private String getHostPublicKey() {
		String fileName = "C:\\Users\\rscmserver\\.ssh\\id_rsa.pub";
		String line = null;
		StringBuilder sb = new StringBuilder();
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
		return sb.toString();
	}
	
	public boolean isInteger(String s) {
		//https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}


	@Override
	public synchronized void addConnectionLog(RSCMClientConnection rscmClientConnection) {
		String exitCode = rscmClientConnection.getConnectionExitcode();
		int exitCodeIndexOf = exitCode.indexOf(';')+1;
		String portSubString = exitCode.substring(exitCodeIndexOf);
		int exitCodeExtention = Integer.parseInt(portSubString);
		
		Optional<RSCMClient> dbResult = rSCMClientRepository.findOptionalByClientPort(exitCodeExtention);
		rscmClientConnection.setConnectionExitcode(exitCode.substring(0, exitCode.indexOf(';')));
		if(dbResult.isPresent()) {
			RSCMClient rClient = dbResult.get();
			rscmClientConnection.setRscmClient(rClient);
			rClient.addRSCMClientConnection(rscmClientConnection);
			rSCMClientRepository.save(rClient);

		}
		
	}

	@Override
	public void updatePortScannerPortEnd() {
		Iterator<Integer> dbPortResult = rSCMClientRepository.getHighestPort().iterator();
		int highestUsedPortnumber = 0;
		
		if(dbPortResult.hasNext()) {
			String portString = dbPortResult.next()+"";
			if(!portString.equals("null")) {
				highestUsedPortnumber = Integer.parseInt(portString);
			}else {
				highestUsedPortnumber = 22000;
			}
			
		}else {
			highestUsedPortnumber = 22000;
		}
		portNumber = highestUsedPortnumber;
		getPortScanner().setScanPortEnd(portNumber);
		
	}

	
	



}
