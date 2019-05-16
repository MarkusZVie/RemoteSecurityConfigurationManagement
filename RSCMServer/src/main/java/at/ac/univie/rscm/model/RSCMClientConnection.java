package at.ac.univie.rscm.model;

import java.util.Date;

import javax.persistence.Entity;
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
		private String connectionExitCode;
		private String connectionDescription;
		private String connectionSupplement;
		@ManyToOne
	    @JoinColumn(name="rscmclient_fs", nullable=false)
		private RSCMClient rscmClient;
		@ManyToOne
	    @JoinColumn(name="environment_fs", nullable=false)
		private Environment environment;

}
