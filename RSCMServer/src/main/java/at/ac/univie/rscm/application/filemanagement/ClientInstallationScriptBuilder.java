package at.ac.univie.rscm.application.filemanagement;

import java.io.File;

import at.ac.univie.rscm.model.RSCMClient;

public interface ClientInstallationScriptBuilder {
	public static ClientInstallationScriptManager getInstance() {
		return null;
	}
	
	public File getClientInstallProgram();

	public void confirmAppKey(RSCMClient rsaClientKey);
}
