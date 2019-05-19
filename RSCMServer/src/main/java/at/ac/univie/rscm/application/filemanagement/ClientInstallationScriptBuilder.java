package at.ac.univie.rscm.application.filemanagement;

import java.io.File;

import at.ac.univie.rscm.model.Applicant;
import at.ac.univie.rscm.model.RSCMClient;

public interface ClientInstallationScriptBuilder {
	public static ClientInstallationScriptManager getInstance() {
		return null;
	}
	
	public File getClientInstallProgram(boolean isExtern, Applicant loggedInApplicant);

	public void confirmAppKey(RSCMClient rsaClientKey);
	
}
