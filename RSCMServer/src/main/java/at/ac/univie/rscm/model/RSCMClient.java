package at.ac.univie.rscm.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "clientkeys")
public class RSCMClient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int keyID;
	private String applikationKey;
	private String clientRSAPublicKey;
	private Date createdOn;
	private Date keyCreationDate;
	private String rscmPassword;
	private String rscmKeypass;
	private String clientKeypass;
	private int clientPort;
	@OneToMany(fetch = FetchType.EAGER, mappedBy="rscmClient",cascade=CascadeType.ALL, orphanRemoval = true)
	private List<RSCMClientConnection> connectionLog;
	private boolean isActive;
	
	public boolean getIsActive(){
		return isActive;
	}
	
	public void setIsActive(boolean isActive){
		this.isActive = isActive;
	}
	
	
	public void addConnectionLog(RSCMClientConnection rscmClientConnection) {
		connectionLog.add(rscmClientConnection);
	}
	
	@Override
	public String toString() {
		String s = "";
		s =s+ "KeyID: " + keyID + "<BR>";
		s =s+ "APKey: " + applikationKey + "<BR>";
		s =s+ "RSKey: " + clientRSAPublicKey + "<BR>";
		s =s+ "Date : " + createdOn + "<BR>";
		s =s+ "KDate : " + keyCreationDate + "<BR>";
		s =s+ "rPassw : " + rscmPassword + "<BR>";
		s =s+ "rkeypa" + rscmKeypass + "<BR>";
		s =s+ "ckpass : " + clientKeypass + "<BR>";
		s =s+ "cPort : " + clientPort + "<BR>";
		s =s+ "---------------------<BR>";
		return s;
	}
	
}


