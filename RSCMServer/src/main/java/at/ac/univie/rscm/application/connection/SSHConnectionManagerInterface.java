package at.ac.univie.rscm.application.connection;

import at.ac.univie.rscm.application.global.GlobalSettingsAndVariables;

public interface SSHConnectionManagerInterface {
	
	public static SSHConnectionManager getInstance() {
		return null;
	}
	
	public void removeActiveClient(int port);
	public SSHConnectionBuilder getConnection(int keyId);
}
