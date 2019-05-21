package at.ac.univie.rscm.application.connection;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jcraft.jsch.JSchException;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;
import at.ac.univie.rscm.model.Environment;
import at.ac.univie.rscm.model.Environmentthreat;
import at.ac.univie.rscm.model.RSCMClient;
import at.ac.univie.rscm.model.RSCMClientConnection;
import at.ac.univie.rscm.model.Scriptexecution;
import at.ac.univie.rscm.spring.api.repository.EnvironmentRepository;
import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;
import at.ac.univie.rscm.spring.api.repository.ScriptexecutionRepository;

public class SSHConnectionScriptExecution extends Thread{
	//this class is instanciate by the Portscannerthread class
	//Every new Client connection create one this object. 
	
	private RSCMClient rscmClient;

	private GlobalSettingsAndVariablesInterface gsav;
	
	private RSCMClientRepository rscmClientRepository;
	
	private ScriptexecutionRepository scriptexecutionRepository;
	
	private SSHConnectionManagerInterface sshConnectionManager;
	
	public SSHConnectionScriptExecution(int portNumber) {
		sshConnectionManager =SSHConnectionManager.getInstance();
		gsav =  GlobalSettingsAndVariables.getInstance();
		rscmClientRepository = gsav.getRSCMClientRepository();
		rscmClient = rscmClientRepository.findOptionalByClientPort(portNumber).get();
		scriptexecutionRepository = gsav.getScriptexecutionRepository();
	}



	@Override
	public void run() {
		System.out.println("run");
		if(rscmClient==null) {
			return;
		}
		System.out.println("clientFound");
		SSHConnectionBuilder sshConnectionBuilder = sshConnectionManager.getConnection(rscmClient.getRscmclientId());
		if(sshConnectionBuilder == null) {
			return;
		}
		System.out.println("connection here");
		//check Public Ip Adress
		String publicIP = "";
		try {
			String resultPart1 = sshConnectionBuilder.sendComand("echo irm http://ifconfig.me/ip ^> C:\\Users\\rscm\\ip.txt > checkIp.ps1");
			System.out.println(resultPart1);
			String resultPart2 = sshConnectionBuilder.sendComand("powershell.exe -NoLogo -NoProfile -file C:\\Users\\rscm\\checkIp.ps1");
			System.out.println(resultPart2);
			String resultPart3 = sshConnectionBuilder.sendComand("type C:\\USers\\rscm\\ip.txt");
			System.out.println(resultPart3);
			publicIP = resultPart3.substring(0, resultPart3.indexOf('\n')).trim();
			System.out.println(">>"+publicIP + "<<");
			if(validateIPAddress(publicIP)) {
				System.out.println("valid");
				EnvironmentRepository environmentRepository = gsav.getEnvironmentRepository();
				List<Environment> environmentList =  environmentRepository.findAllByIpRangeBeginAndEnd(publicIP, publicIP);
				Environment e = null;
				if(environmentList.size()!=0) {
					for(Environment indexE : environmentList) {
						if(indexE.getEnvironmentDescription().equals("Self-Sign-In")) {
							e = indexE;
						}
					}
				}
				if(e == null) {
					e = new Environment();
					e.setIpRangeBegin(publicIP);
					e.setIpRangeEnd(publicIP);
					e.setEnvironmentDescription("Self-Sign-In");
				}
				
				environmentRepository.save(e);

				System.out.println("sdsdsdsd");
				List<Environment> environmentListThatShouldBeOne = environmentRepository.findByIpAndDescriptin(publicIP, publicIP, "Self-Sign-In");
				if(environmentListThatShouldBeOne.size()== 0) {
					System.out.println(this.getClass().getName() + " Problem Environment Not found");
				}
				Environment uplodedEnvironment = environmentListThatShouldBeOne.get(0);

				System.out.println("sdsdsdsd");
				gsav.getPortScanner().addEnvironment(uplodedEnvironment.getEnvironmentId(), rscmClient.getClientPort());
			}
			
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//execution Based on Environment
		List<Scriptexecution> seNotAssignedList = scriptexecutionRepository.findAllToExecuteScriptEnvironment();
		for(Scriptexecution se: seNotAssignedList) {
			Scriptexecution newScriptexecution = null;
			if(se.getEnvironment_fs()!=null) {//when it depends environments
				if(se.getEnvironment().getIpRangeEnd()==null) {
					if(checkIfIpInRange(se.getEnvironment().getIpRangeBegin(), se.getEnvironment().getIpRangeBegin(), publicIP)) {
						newScriptexecution = new Scriptexecution();
						newScriptexecution.setEnvironment(se.getEnvironment());
					}
				}else{
					if(checkIfIpInRange(se.getEnvironment().getIpRangeBegin(), se.getEnvironment().getIpRangeEnd(), publicIP)) {
						newScriptexecution = new Scriptexecution();
						newScriptexecution.setEnvironment(se.getEnvironment());
					}
				}
				
			}else if (se.getEnvironmentthreat_fs()!=null) {//when depends threats
				Set<Environment> listEnvironmentsByThreat = se.getEnvironmentthreat().getEnvironment();
				for(Environment environmentToCheck : listEnvironmentsByThreat) {
					if(environmentToCheck.getIpRangeEnd()==null) {
						if(checkIfIpInRange(environmentToCheck.getIpRangeBegin(), environmentToCheck.getIpRangeBegin(), publicIP)) {
							newScriptexecution = new Scriptexecution();
							newScriptexecution.setEnvironmentthreat(se.getEnvironmentthreat());
						}
					}else{
						if(checkIfIpInRange(environmentToCheck.getIpRangeBegin(), environmentToCheck.getIpRangeEnd(), publicIP)) {
							newScriptexecution = new Scriptexecution();
							newScriptexecution.setEnvironmentthreat(se.getEnvironmentthreat());
						}
					}
				}
			}
			if(newScriptexecution != null) {
				//when its required to execute the script
				newScriptexecution.setRSCMClient(rscmClient);
				newScriptexecution.setScriptexecutionAssigneddate(se.getScriptexecutionAssigneddate());
				newScriptexecution.setScriptName(se.getScriptName());
				scriptexecutionRepository.save(newScriptexecution);
				System.out.println(se.getScriptName() + " added to db");
			}
		}
		
		//execution of all assigned scripts
		List<Scriptexecution> seList = scriptexecutionRepository.findAllToExecuteScriptAssigns(rscmClient.getRscmclientId());
		Set<String> executeFileList = new HashSet<String>();
		System.out.println("listHere");
		System.out.println(Arrays.toString(seList.toArray()));
		
		for(Scriptexecution se: seList) {
			if(executeFileList.contains(se.getScriptName())) {
				System.out.println("newDate");
				se.setScriptexecutionExecutiondate(new Date());
				scriptexecutionRepository.save(se);
				System.out.println("newDate");
			}else {
				//do script
				System.out.println("sendFile " + se.getScriptName());
				String answere = sshConnectionBuilder.sendFileToClient(new File(gsav.getFileDownloadDirectory()+"/"+se.getScriptName()));
				System.out.println(answere);
				System.out.println("newDate");
				se.setScriptexecutionExecutiondate(new Date());
				scriptexecutionRepository.save(se);
				executeFileList.add(se.getScriptName());
				System.out.println("newDate");
				try {
					String s = sshConnectionBuilder.sendComand(se.getScriptName());
					System.out.println(s);
				} catch (JSchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
		
		
	}
	
	private boolean validateIPAddress( String ipAddress ){ 
		
		try {
			Inet4Address.getByName(ipAddress);
			return true;
		} catch (UnknownHostException e) {
			return false;
		}
		
	}
	
	private long ipToLong(InetAddress ip) {
		//https://stackoverflow.com/questions/4256438/calculate-whether-an-ip-address-is-in-a-specified-range-in-java
        byte[] octets = ip.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xff;
        }
        return result;
    }
	
	private boolean checkIfIpInRange(String lowerBound, String higherBound, String targetIp) {
		try {
			long lLowerBound = ipToLong(InetAddress.getByName(lowerBound));
			long lHigherBound = ipToLong(InetAddress.getByName(higherBound));
	        long lTargetIp = ipToLong(InetAddress.getByName(targetIp));
	        if(lLowerBound > lHigherBound) {
	        	long swabVar = lHigherBound;
	        	lHigherBound = lLowerBound;
	        	lLowerBound = swabVar;
	        }
	        if(lTargetIp >= lLowerBound && lTargetIp <= lHigherBound) {
	        	return true;
	        }else {
	        	return false;
	        }
	        
		} catch (UnknownHostException e) {
			return false;
		}
        
	}
	
	
	

}
