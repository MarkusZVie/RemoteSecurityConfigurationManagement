package at.ac.univie.rscm.application.global;

import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;

public interface GlobalSettingsAndVariablesInterface {
	
	
	
	public static GlobalSettingsAndVariables getInstance() {
		return null;
	}
	
	public int getPortNumber();

	public CharSequence getServer_ip_value();

	public void setRSCMClientRepository(RSCMClientRepository rcrClientRepository);

	public RSCMClientRepository getRSCMClientRepository();
	
	public int getPortBeginRange();
	public void setPortBeginRange(int port);
	public int getPortEndRange();
	public void setPortEndRange(int port);
	
}
