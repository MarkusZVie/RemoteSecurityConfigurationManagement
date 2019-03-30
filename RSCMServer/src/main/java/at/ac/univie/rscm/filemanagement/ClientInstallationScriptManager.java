package at.ac.univie.rscm.filemanagement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;

public class ClientInstallationScriptManager implements ClientInstallationScriptBuilder{
	
	private int availabilityTime;
	private static ClientInstallationScriptManager cism;
	private ArrayList<ClientInstallationScriptHelper> activeClientInstallations;
	
	private ClientInstallationScriptManager() {
		activeClientInstallations = new ArrayList<ClientInstallationScriptHelper>();
		availabilityTime = 5*60*1000;
	}
	
	public static ClientInstallationScriptManager getInstance() {
		if(cism == null) {
			cism = new ClientInstallationScriptManager();
		}
		return cism;
	}
	
	
	//for later remove
	public static void main(String[] args) {
		cism = new ClientInstallationScriptManager();
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
			if(cish.getApiKey().equals(apiKey)) {
				return true;
			}
		}
		return false;
	}
	
	
	
}
