package at.ac.univie.rscm.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "clientkeys")
public class RSAClientPubKey {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int keyID;
	private String applikationKey;
	private String clientRSAPublicKey;
	private Date createdOn;

	@Override
	public String toString() {
		String s = "";
		s =s+ "KeyID: " + keyID + "<BR>";
		s =s+ "APKey: " + applikationKey + "<BR>";
		s =s+ "RSKey: " + clientRSAPublicKey + "<BR>";
		s =s+ "Date : " + createdOn + "<BR>";
		s =s+ "---------------------<BR>";
		return s;
	}
	
}


