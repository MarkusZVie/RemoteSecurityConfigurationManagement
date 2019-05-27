package at.ac.univie.rscm.application.filemanagement;

import java.io.File;

import at.ac.univie.rscm.model.User;
import at.ac.univie.rscm.model.RSCMClient;
import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;

public interface ClientInstallationScriptBuilder {
	public static ClientInstallationScriptManager getInstance() {
		return null;
	}
	
	public File getClientInstallProgram(boolean isExtern, int loggedInUserId);

	public String confirmAppKey(String applikationKey, String clientRSAPublicKey);
	
}
