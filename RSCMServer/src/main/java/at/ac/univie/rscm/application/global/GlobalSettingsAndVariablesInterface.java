package at.ac.univie.rscm.application.global;

import java.util.ArrayList;
import java.util.Map;

import at.ac.univie.rscm.application.connection.PortScanner;
import at.ac.univie.rscm.application.connection.PortScannerInterface;
import at.ac.univie.rscm.model.RSCMClientConnection;
import at.ac.univie.rscm.spring.api.repository.ApplicantRepository;
import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;
import at.ac.univie.rscm.spring.api.repository.RoleRepository;

public interface GlobalSettingsAndVariablesInterface {
	
	
	
	public static GlobalSettingsAndVariables getInstance() {
		return null;
	}
	
	public int getPortNumber();

	public CharSequence getServer_intern_ip_value();
	public CharSequence getServer_extern_ip_value();
	public String getServer_rsafingerprint_value();
	public String getServer_publickey_value();
	
	public void addDownload(String key, String value);
	public Map<String,String> getProvidedDownloads();
	public String getFileDownloadDirectory();
	public String getPathToAuthorized_keys();
	
	public void setRSCMClientRepository(RSCMClientRepository rcrClientRepository);

	public RSCMClientRepository getRSCMClientRepository();
	
	public int getPortBeginRange();
	public void setPortBeginRange(int port);
	public int getPortEndRange();
	public void setPortEndRange(int port);
	public boolean isInteger(String s);

	public void setRoleRepository(RoleRepository rr);

	public void setApplicantRepository(ApplicantRepository ar);
	public PortScannerInterface getPortScanner();

	public void initPortScanner();

	public void addConnectionLog(RSCMClientConnection rscmClientConnection);
	
}
