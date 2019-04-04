package at.ac.univie.rscm.application.filemanagement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
import at.ac.univie.rscm.model.RSCMClient;
import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;

public class ClientInstallationScriptManager implements ClientInstallationScriptBuilder{
	
	private int availabilityTime;
	private static ClientInstallationScriptManager cism;
	private ArrayList<ClientInstallationScriptHelper> activeClientInstallations;
	
	private RSCMClientRepository rcrClientRepository;
	
	private ClientInstallationScriptManager() {
		activeClientInstallations = new ArrayList<ClientInstallationScriptHelper>();
		availabilityTime = 5*60*1000;
		GlobalSettingsAndVariablesInterface gsav = GlobalSettingsAndVariables.getInstance();
		rcrClientRepository = gsav.getRSCMClientRepository();
	}
	
	public static ClientInstallationScriptManager getInstance() {
		if(cism == null) {
			cism = new ClientInstallationScriptManager();
		}
		return cism;
	}
		
	@Override
	public File getClientInstallProgram() {
		//create a new Thread with availabilityTime for the API Key
		ClientInstallationScriptHelper cish = new ClientInstallationScriptHelper(availabilityTime);
		//add Thread to list
		activeClientInstallations.add(cish);
		//let the Tread create the exe file
		File file = cish.getFile();
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
	public void confirmAppKey(RSCMClient rsaClientKey) {
		ClientInstallationScriptHelper cish = getHelperByAppKey(rsaClientKey.getApplikationKey());
		System.out.println(rsaClientKey.toString() + "test");
		if(cish!=null) {
			rsaClientKey.setClientKeypass(cish.getClient_keypass_value());
			rsaClientKey.setClientPort(cish.getClient_specificport_value());
			rsaClientKey.setCreatedOn(new Date());
			rsaClientKey.setKeyCreationDate(cish.getCreatenDate());
			rsaClientKey.setRscmKeypass(cish.getRscm_keypass_value());
			rsaClientKey.setRscmPassword(cish.getRscm_password_value());
			rcrClientRepository.save(rsaClientKey);
			cish.clearUp();
			cish.stop();
			System.out.println(rsaClientKey.toString());
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
		System.out.println();
		Collection<Integer> port = rcrClientRepository.getHighestPort();
		GlobalSettingsAndVariablesInterface gsav = GlobalSettingsAndVariables.getInstance();
		if(port.size()==0) {
			gsav.setPortEndRange(22000);
			return 22000;
		}else {
			int p = Integer.parseInt(port.iterator().next()+"");
			gsav.setPortEndRange(++p);
			return p;
		}
		
	}
	
	
	
}
