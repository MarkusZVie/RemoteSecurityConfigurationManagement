package at.ac.univie.rscm.application.filemanagement;

import java.io.File;

import at.ac.univie.rscm.model.Applicant;
import at.ac.univie.rscm.model.RSCMClient;
import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;

public interface ClientInstallationScriptBuilder {
	public static ClientInstallationScriptManager getInstance() {
		return null;
	}
	
	public File getClientInstallProgram(boolean isExtern, int loggedInApplicantId);

	public String confirmAppKey(String applikationKey, String clientRSAPublicKey);
	
}
