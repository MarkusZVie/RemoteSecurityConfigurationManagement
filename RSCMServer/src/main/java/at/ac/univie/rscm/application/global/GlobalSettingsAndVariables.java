package at.ac.univie.rscm.application.global;

import at.ac.univie.rscm.spring.api.repository.RSCMClientRepository;
import lombok.Getter;
import lombok.Setter;

public class GlobalSettingsAndVariables implements GlobalSettingsAndVariablesInterface{
	private static GlobalSettingsAndVariables gsav;	
	@Getter
	@Setter
	private String server_ip_value;
	@Getter
	@Setter
	private String server_rsafingerprint_value;
	@Getter
	@Setter
	private String server_publickey_value;
	@Getter
	@Setter
	private int portBeginRange;
	@Getter
	@Setter
	private int portEndRange;

	private RSCMClientRepository rSCMClientRepository;
	
	private int portNumber;
	
	
	private GlobalSettingsAndVariables() {
		portBeginRange = 22000;
		portEndRange = 22000;
		server_ip_value = "192.168.178.16";
		server_rsafingerprint_value = "ecdsa-sha2-nistp256 AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAyNTYAAABBBAYu3YwUiKt2/pnh8wiXHHAHLhGB8xhrsKSE1vwpoTO89LYFh9Pf1MGGdoDhLzKLTlJRKVmu6bq5ZNCzQmKfHdM=";
		server_publickey_value = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC+Wz5RK3pjdP6nuu3W80YfvAAhUBKxOfOj3GWjd4gN21t98A8c548HYhlnStLE5GZxN1uqsLSg5bPkDSqwLBYZ1e/DZxCWTtaK+Ef9hzxhRV4EPX82k4QHI3HHccqpUA3f5/gpkmcrrLxeDohpUPwWBLwQroyD43ZpCfRzU2C5F2FzA1KX4DLRJAv5GwiCNIuGWv8rBH7o7BBp5Mj+/8OtfUDPshaA2Bcte45FwI05ak8sTJfoJq80QWgoAJPQjgRMB0U3OG1KJX5pkpBYPjUfcDholBDroohqdmvMjqXt15FocIEp3EnYsBCOSKcRGTMmXErwGvMvk/inXabI4rsL rscmserver@DESKTOP-QH02IEJ";
		portNumber = 22000;
	}
	
	public static GlobalSettingsAndVariables getInstance() {
		if(gsav == null) {
			gsav = new GlobalSettingsAndVariables();
		}
		return gsav;
	}
	
	public int getPortNumber() {
		return portNumber++;
	}

	@Override
	public void setRSCMClientRepository(RSCMClientRepository rcrClientRepository) {
		this.rSCMClientRepository = rcrClientRepository;
		
	}

	@Override
	public RSCMClientRepository getRSCMClientRepository() {
		return rSCMClientRepository;
	}

	
	

}
