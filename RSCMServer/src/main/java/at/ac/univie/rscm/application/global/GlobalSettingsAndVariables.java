package at.ac.univie.rscm.application.global;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

import at.ac.univie.rscm.application.connection.PortScanner;
import at.ac.univie.rscm.application.connection.PortScannerInterface;
import at.ac.univie.rscm.model.RSCMClient;
import at.ac.univie.rscm.model.RSCMClientConnection;
import at.ac.univie.rscm.spring.api.repository.ApplicantRepository;
import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;
import at.ac.univie.rscm.spring.api.repository.RoleRepository;
import lombok.Getter;
import lombok.Setter;

public class GlobalSettingsAndVariables implements GlobalSettingsAndVariablesInterface{
	private static GlobalSettingsAndVariables gsav;	
	@Getter
	@Setter
	private String server_ip_value;
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
	private RoleRepository roleRepository;
	
	@Getter
	private PortScannerInterface portScanner;
	
	private int portNumber;
	
	
	private GlobalSettingsAndVariables() {
		server_ip_value = "192.168.178.16";
		server_rsafingerprint_value = "ecdsa-sha2-nistp256 AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAyNTYAAABBBAYu3YwUiKt2/pnh8wiXHHAHLhGB8xhrsKSE1vwpoTO89LYFh9Pf1MGGdoDhLzKLTlJRKVmu6bq5ZNCzQmKfHdM=";
		server_publickey_value = getHostPublicKey();
		portNumber = 22000;
	}
	
	
	@Override
	public void initPortScanner() {
		Iterator<Integer> dbPortResult = rSCMClientRepository.getHighestPort().iterator();
		int highestUsedPortnumber = 0;
		if(dbPortResult.hasNext()) {
			highestUsedPortnumber = Integer.parseInt(dbPortResult.next()+"");
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
		String exitCode = rscmClientConnection.getConnectionExitCode();
		int exitCodeIndexOf = exitCode.indexOf(';')+1;
		String portSubString = exitCode.substring(exitCodeIndexOf);
		int exitCodeExtention = Integer.parseInt(portSubString);
		
		Optional<RSCMClient> dbResult = rSCMClientRepository.findOptionalByClientPort(exitCodeExtention);
		rscmClientConnection.setConnectionExitCode(exitCode.substring(0, exitCode.indexOf(';')));
		if(dbResult.isPresent()) {
			RSCMClient rClient = dbResult.get();
			rscmClientConnection.setRscmClient(rClient);
			rClient.addConnectionLog(rscmClientConnection);
			rSCMClientRepository.save(rClient);

		}
		
	}

	



}
