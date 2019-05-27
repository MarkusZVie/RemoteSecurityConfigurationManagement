package at.ac.univie.rscm.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.ManyToMany;
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
@Table(name = "rscmclients")
public class RSCMClient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rscmclientId;
	private String applikationKey;
	private String clientRSAPublicKey;
	private Date createdOn;
	private Date keyCreationDate;
	private String rscmPassword;
	private String rscmKeypass;
	private String clientKeypass;
	private int clientPort;
	@OneToMany(fetch = FetchType.EAGER, mappedBy="rscmClient",cascade=CascadeType.ALL, orphanRemoval = true)
	private List<RSCMClientConnection> rSCMClientConnections;
	private boolean isActive;
	@ManyToMany(mappedBy = "rscmclients", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<User> users = new HashSet<>();
	
	public boolean getIsActive(){
		return isActive;
	}
	
	public void setIsActive(boolean isActive){
		this.isActive = isActive;
	}
	

	public void addUser(User user) {
		if(users==null) {
			users = new HashSet<User>();
		}
		users.add(user);
		
	}
	
	public void addRSCMClientConnection(RSCMClientConnection rscmClientConnection) {
		if(rSCMClientConnections==null) {
			rSCMClientConnections = new ArrayList<RSCMClientConnection>();
		}
		rSCMClientConnections.add(rscmClientConnection);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Class: " + this.getClass().getName() + "<br/>");
		sb.append("rscmclientId: " + rscmclientId + "<br/>");
		sb.append("applikationKey: " + applikationKey + "<br/>");
		sb.append("clientRSAPublicKey: " + clientRSAPublicKey + "<br/>");
		if(createdOn!=null) {
			sb.append("createdOn: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(createdOn) + "<br/>");
		}else {
			sb.append("createdOn: null<br/>");
		}
		if(keyCreationDate!=null) {
			sb.append("keyCreationDate: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(keyCreationDate) + "<br/>");
		}else {
			sb.append("keyCreationDate: null<br/>");
		}
		sb.append("rscmKeypass: " + rscmKeypass + "<br/>");
		sb.append("clientKeypass: " + clientKeypass + "<br/>");
		sb.append("clientPort: " + clientPort + "<br/>");
		if(rSCMClientConnections!=null) {
			sb.append("rSCMClientConnections number: " + rSCMClientConnections.size() + "<br/>");
		}else {
			sb.append("rSCMClientConnections: null<br/>");
		}
		if(users!=null) {
			if(users.iterator().hasNext()) {
				sb.append("user: " + users.iterator().next().getUserName() + "<br/>");
			}else {
				sb.append("user: <br/>");
			}
		}else {
			sb.append("users: null<br/>");
		}
		
		
		return sb.toString();
	}
	
	
}


