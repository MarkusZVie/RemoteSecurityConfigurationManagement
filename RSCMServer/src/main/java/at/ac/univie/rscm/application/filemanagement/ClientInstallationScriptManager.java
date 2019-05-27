package at.ac.univie.rscm.application.filemanagement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;
import at.ac.univie.rscm.application.global.GlobalSettingsAndVariablesInterface;
import at.ac.univie.rscm.model.User;
import at.ac.univie.rscm.model.RSCMClient;
import at.ac.univie.rscm.spring.api.repository.UserRepository;
import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;

public class ClientInstallationScriptManager implements ClientInstallationScriptBuilder{
	
	private int availabilityTime;
	private static ClientInstallationScriptManager cism;
	private ArrayList<ClientInstallationScriptHelper> activeClientInstallations;
	
	private RSCMClientRepository rcrClientRepository;
	private GlobalSettingsAndVariablesInterface gsav;
	private UserRepository userRepository;
	
	private ClientInstallationScriptManager() {
		activeClientInstallations = new ArrayList<ClientInstallationScriptHelper>();
		availabilityTime = 5*60*1000;
		gsav = GlobalSettingsAndVariables.getInstance();
		
	}
	
	public static ClientInstallationScriptManager getInstance() {
		if(cism == null) {
			cism = new ClientInstallationScriptManager();
		}
		return cism;
	}
		
	@Override
	public File getClientInstallProgram(boolean isExtern, int loggedInUserId) {
		//create a new Thread with availabilityTime for the API Key
		ClientInstallationScriptHelper cish = new ClientInstallationScriptHelper(availabilityTime,loggedInUserId);
		//add Thread to list
		activeClientInstallations.add(cish);
		//let the Tread create the exe file
		File file = cish.getFile(isExtern);
		//Start the API-Key Availability timer
		cish.start();
		//return exe file
		return file;
	}
	
	public void removeActiveInstallation(ClientInstallationScriptHelper cish) {
		activeClientInstallations.remove(cish);
	}
	
	public synchronized boolean checkIfApiKeyIsInUse(String apiKey) {
		for(ClientInstallationScriptHelper cish: activeClientInstallations) {
			if(cish.getClient_appkey_value().equals(apiKey)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String confirmAppKey(String applikationKey, String clientRSAPublicKey) {

		userRepository = gsav.getUserRepository();
		rcrClientRepository = gsav.getRSCMClientRepository();
		ClientInstallationScriptHelper cish = getHelperByAppKey(applikationKey);
		
		if(cish!=null) {
			RSCMClient rscmClient = new RSCMClient();
			rscmClient.setApplikationKey(applikationKey);
			rscmClient.setClientRSAPublicKey(clientRSAPublicKey);
			rscmClient.setClientKeypass(cish.getClient_keypass_value());
			rscmClient.setClientPort(cish.getClient_specificport_value());
			rscmClient.setCreatedOn(new Date());
			rscmClient.setKeyCreationDate(cish.getCreatenDate());
			rscmClient.setRscmKeypass(cish.getRscm_keypass_value());
			rscmClient.setRscmPassword(cish.getRscm_password_value());
			System.out.println(cish.getLoggedInUserId());
			User user = userRepository.findById(cish.getLoggedInUserId()).get();
			rscmClient.addUser(user);
			rcrClientRepository.save(rscmClient);
			
			user.addRSCMClient(rscmClient);
			userRepository.save(user);
			
			registerRSAKey(rscmClient.getClientRSAPublicKey());
			gsav.updatePortScannerPortEnd();
			cish.clearUp();
			cish.stop();
			return "client added";
		}else {
			System.out.println("Installation out of time, Client not added");
			return "Installation out of time, Client not added";
		}
		
	}
	
	private void registerRSAKey(String rsaKey) {
		try {
			String path = gsav.getPathToAuthorized_keys();
			File file = new File(path);
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter writer = new BufferedWriter(fw);
			writer.newLine();
			writer.append(rsaKey);
			writer.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private ClientInstallationScriptHelper getHelperByAppKey(String appKey) {
		for(ClientInstallationScriptHelper cish : activeClientInstallations) {
			if(cish.getClient_appkey_value().equals(appKey)) {
				return cish;
			}
		}
		return null;
	}


	public int getPortNumber() {

		rcrClientRepository = gsav.getRSCMClientRepository();
		Collection<Integer> port = rcrClientRepository.getHighestPort();
		if(port.size()==0) {
			gsav.setPortEndRange(22000);
			return 22000;
		}else {
			String portString = port.iterator().next()+"";
			if(!portString.equals("null")) {
				int p = Integer.parseInt(portString);
				gsav.setPortEndRange(++p);
				return p;
			}else {
				gsav.setPortEndRange(22000);
				return 22000;
			}
			
			
		}
		
	}
	
	
	
}
