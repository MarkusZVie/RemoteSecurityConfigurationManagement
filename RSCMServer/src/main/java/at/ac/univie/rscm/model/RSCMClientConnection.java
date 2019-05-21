package at.ac.univie.rscm.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "rscmclient_connections")
public class RSCMClientConnection {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int connectionId;
		private Date connectionStart;
		private Date connectionEnd;
		private String connectionExitcode;
		private String connectionDescription;
		private String connectionSupplement;
		@ManyToOne
	    @JoinColumn(name="rscmclient_fs", nullable=false)
		private RSCMClient rscmClient;
		@ManyToOne
	    @JoinColumn(name="environment_fs")
		private Environment environment;
		
		public String getEnvironmentIp() {
			if(environment!=null) {
				return environment.getIpRangeBegin();
			}else {
				return "null";
			}
			
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Class: " + this.getClass().getName() + "<br/>");
			sb.append("connectionId: " + connectionId + "<br/>");
			if(connectionStart!=null) {
				sb.append("connectionStart: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(connectionStart) + "<br/>");
			}else {
				sb.append("connectionStart: null<br/>");
			}
			if(connectionEnd!=null) {
				sb.append("connectionEnd: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(connectionEnd) + "<br/>");
			}else {
				sb.append("connectionEnd: null<br/>");
			}
			sb.append("connectionExitCode: " + connectionExitcode + "<br/>");
			sb.append("connectionDescription: " + connectionDescription + "<br/>");
			sb.append("connectionSupplement: " + connectionSupplement + "<br/>");
			if(rscmClient!=null) {
				sb.append("rscmClient: " + rscmClient.getRscmclientId() + "<br/>");
			}else {
				sb.append("rscmClient: null<br/>");
			}
			if(environment!=null) {
				sb.append("environment: " + environment.getIpRangeBegin() + "<br/>");
			}else {
				sb.append("environment: null<br/>");
			}
			
			return sb.toString();
		}
		

}
